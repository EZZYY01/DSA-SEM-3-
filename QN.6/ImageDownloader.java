import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;

public class ImageDownloader extends JFrame {

    private final ExecutorService executorService;
    private final JTextField urlField;
    private final JButton startButton;
    private final JButton pauseButton;
    private final JButton cancelButton;
    private final JProgressBar progressBar;
    private DownloadTask currentTask;

    public ImageDownloader() {
        //SWING ( GUI )START
        setTitle("IMAGE DOWNLOADER WITH URL");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setPreferredSize(new Dimension(600,   200));
        setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));
        urlField = new JTextField(30);
        urlField.setFont(new Font("Arial", Font.PLAIN,   14));
        startButton = new JButton("Start Download");
        pauseButton = new JButton("Pause");
        cancelButton = new JButton("Cancel");
        progressBar = new JProgressBar();
        progressBar.setStringPainted(true);
        progressBar.setMinimum(0);
        progressBar.setMaximum(100);
        progressBar.setVisible(false); // TO HIDE THE BAR

        JPanel controlPanel = new JPanel(new GridBagLayout());
        controlPanel.setBorder(BorderFactory.createEmptyBorder(10,   10,   10,   10));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx =   0;
        gbc.gridy =   0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx =   1;
        controlPanel.add(urlField, gbc);

        gbc.gridy++;
        controlPanel.add(startButton, gbc);

        gbc.gridy++;
        controlPanel.add(pauseButton, gbc);

        gbc.gridy++;
        controlPanel.add(cancelButton, gbc);

        gbc.gridy++;
        controlPanel.add(progressBar, gbc);

        add(controlPanel);

        executorService = Executors.newSingleThreadExecutor();

        startButton.addActionListener(e -> startDownload());
        pauseButton.addActionListener(e -> pauseDownload());
        cancelButton.addActionListener(e -> cancelDownload());

        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }
    //SWING ( GUI )END

    //-------------------------------------------------//
    private void startDownload() {
        String urlText = urlField.getText();
        if (!urlText.isEmpty()) {
            URI uri;
            try {
                uri = new URI(urlText);
                URL urlObj = uri.toURL();
                JFileChooser fileChooser = new JFileChooser();
                int returnValue = fileChooser.showSaveDialog(this);
                if (returnValue == JFileChooser.APPROVE_OPTION) {
                    File selectedFile = fileChooser.getSelectedFile();
                    currentTask = new DownloadTask(urlObj, selectedFile, progressBar);
                    progressBar.setVisible(true); 
                    executorService.submit(currentTask);
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Invalid URL: " + ex.getMessage());
            }
        } else {
            JOptionPane.showMessageDialog(this, "Please enter a valid URL.");
        }
    }

    private void pauseDownload() {
        if (currentTask != null) {
            currentTask.pause();
        }
    }

    private void cancelDownload() {
        if (currentTask != null) {
            currentTask.cancel(true);
            executorService.shutdownNow();
            currentTask = null;
            progressBar.setVisible(false); // THIS IS HERE SO PROGRESS BAR IS SHOWN ONLY WHEN DOWNLOADING
        }
    }

    private class DownloadTask extends SwingWorker<Void, Integer> {

        private final URL url;
        private final File destinationFile;
        private final JProgressBar progressBar;
        private volatile boolean paused;
        private volatile boolean cancelled;

        public DownloadTask(URL url, File destinationFile, JProgressBar progressBar) {
            this.url = url;
            this.destinationFile = destinationFile;
            this.progressBar = progressBar;
        }

        public void pause() {
            paused = true;
        }

        @Override
        protected Void doInBackground() throws Exception {
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.connect();

            try (InputStream in = connection.getInputStream();
                 FileOutputStream out = new FileOutputStream(destinationFile)) {

                byte[] buffer = new byte[8192];
                int bytesRead;
                long totalBytesRead =   0;
                int contentLength = connection.getContentLength();

                while (!cancelled && (bytesRead = in.read(buffer)) != -1) {
                    while (paused) {
                        Thread.sleep(100);
                    }
                    totalBytesRead += bytesRead;
                    int progress = (int) ((totalBytesRead *   100L) / contentLength);
                    publish(progress);
                    out.write(buffer,   0, bytesRead);
                }
            }
            return null;
        }
        @Override
        protected void process(List<Integer> progressList) {
            int progress = progressList.get(progressList.size() -   1);
            progressBar.setValue(progress);
        }

        @Override
        protected void done() {
            if (isCancelled()) {
                progressBar.setValue(0);
                JOptionPane.showMessageDialog(ImageDownloader.this, "Download cancelled");
            } else {
                progressBar.setValue(100);
                JOptionPane.showMessageDialog(ImageDownloader.this, "Download completed");
            }
        }
    }
    //-------------------------------------------------//
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new ImageDownloader());
    }
}


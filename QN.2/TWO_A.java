//sewing machines
public class TWO_A {

    public static int minMovesToEqualize(int[] dresses) {
        int totalDresses = 0;
        for (int dressCount : dresses) {
            totalDresses += dressCount;
        }

        int numMachines = dresses.length;
        if (totalDresses % numMachines != 0) {
            return -1;
        }

        int targetDresses = totalDresses / numMachines;
        int moves = 0;
        for (int dressCount : dresses) {
            moves += Math.abs(targetDresses - dressCount);
        }

        // We're counting moves in pairs since each move involves two machines
        return moves / 2;
    }

    public static void main(String[] args) {
        int[] inputDresses = {1, 0, 5};
        System.out.println("Minimum moves required to equalize the dresses is "+ minMovesToEqualize(inputDresses)); 
    }
}

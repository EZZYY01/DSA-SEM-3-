import java.util.*;

class TreeNode {
    int val;
    TreeNode left, right;

    public TreeNode(int val) {
        this.val = val;
    }
}

class DistanceNode {
    double distance;
    int value;

    public DistanceNode(double distance, int value) {
        this.distance = distance;
        this.value = value;
    }
}

public class FOUR_B {
    public static List<Integer> closestValues(TreeNode root, double target, int x) {
        List<Integer> result = new ArrayList<>();
        if (root == null || x == 0) return result;

        PriorityQueue<DistanceNode> pq = new PriorityQueue<>(x, (a, b) -> Double.compare(b.distance, a.distance));

        inOrderTraversal(root, target, x, pq);

        while (!pq.isEmpty()) {
            result.add(pq.poll().value);
        }
        return result;
    }

    private static void inOrderTraversal(TreeNode node, double target, int x, PriorityQueue<DistanceNode> pq) {
        if (node == null) return;

        inOrderTraversal(node.left, target, x, pq);

        double diff = Math.abs(target - node.val);
        pq.offer(new DistanceNode(diff, node.val));

        if (pq.size() > x) {
            pq.poll();
        }

        inOrderTraversal(node.right, target, x, pq);
    }

    public static void main(String[] args) {
        // Construct the BST
        TreeNode root = new TreeNode(4);
        root.left = new TreeNode(2);
        root.right = new TreeNode(5);
        root.left.left = new TreeNode(1);
        root.left.right = new TreeNode(3);

        double target = 3.8;
        int x = 2;
        List<Integer> closest = closestValues(root, target, x);
        System.out.println("Closest values to " + target + " with distance " + x + ": " + closest);
    }
}
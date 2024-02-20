import java.util.*;

class Edge implements Comparable<Edge> {
    int source, destination, weight;

    public Edge(int source, int destination, int weight) {
        this.source = source;
        this.destination = destination;
        this.weight = weight;
    }

    @Override
    public int compareTo(Edge other) {
        return Integer.compare(this.weight, other.weight);
    }
}

class DisjointSet {
    int[] parent, rank;

    public DisjointSet(int n) {
        parent = new int[n];
        rank = new int[n];
        Arrays.fill(rank, 1);
        for (int i = 0; i < n; i++) {
            parent[i] = i;
        }
    }

    public int find(int u) {
        if (parent[u] != u) {
            parent[u] = find(parent[u]);
        }
        return parent[u];
    }

    public void union(int u, int v) {
        int pu = find(u);
        int pv = find(v);
        if (pu != pv) {
            if (rank[pu] > rank[pv]) {
                parent[pv] = pu;
            } else if (rank[pu] < rank[pv]) {
                parent[pu] = pv;
            } else {
                parent[pu] = pv;
                rank[pv]++;
            }
        }
    }
}

class PriorityQueueWithHeap<T extends Comparable<T>> {
    private List<T> heap;

    public PriorityQueueWithHeap() {
        heap = new ArrayList<>();
    }

    public PriorityQueueWithHeap(List<T> items) {
        heap = new ArrayList<>(items);
        buildHeap();
    }

    private void buildHeap() {
        for (int i = (heap.size() / 2) - 1; i >= 0; i--) {
            siftDown(i);
        }
    }

    public void push(T item) {
        heap.add(item);
        siftUp(heap.size() - 1);
    }

    public T pop() {
        if (heap.isEmpty()) return null;
        if (heap.size() == 1) return heap.remove(0);
        T top = heap.get(0);
        heap.set(0, heap.remove(heap.size() - 1));
        siftDown(0);
        return top;
    }

    private void siftUp(int index) {
        while (index > 0) {
            int parentIndex = (index - 1) / 2;
            if (heap.get(parentIndex).compareTo(heap.get(index)) > 0) {
                swap(parentIndex, index);
                index = parentIndex;
            } else {
                break;
            }
        }
    }

    private void siftDown(int index) {
        int size = heap.size();
        while (index < size) {
            int leftChildIndex = 2 * index + 1;
            int rightChildIndex = 2 * index + 2;
            int minIndex = index;

            if (leftChildIndex < size && heap.get(leftChildIndex).compareTo(heap.get(minIndex)) < 0) {
                minIndex = leftChildIndex;
            }
            if (rightChildIndex < size && heap.get(rightChildIndex).compareTo(heap.get(minIndex)) < 0) {
                minIndex = rightChildIndex;
            }

            if (minIndex != index) {
                swap(minIndex, index);
                index = minIndex;
            } else {
                break;
            }
        }
    }

    private void swap(int i, int j) {
        T temp = heap.get(i);
        heap.set(i, heap.get(j));
        heap.set(j, temp);
    }

    public boolean isEmpty() {
        return heap.isEmpty();
    }

    public int size() {
        return heap.size();
    }

    public T peek() {
        return heap.isEmpty() ? null : heap.get(0);
    }
}

public class THREE_B
 {
    public static List<Edge> findMinimumSpanningTree(List<Edge> edges, int numVertices) {
        PriorityQueueWithHeap<Edge> pq = new PriorityQueueWithHeap<>(edges);
        DisjointSet ds = new DisjointSet(numVertices);
        List<Edge> mst = new ArrayList<>();

        while (!pq.isEmpty() && mst.size() < numVertices - 1) {
            Edge edge = pq.pop();
            int sourceParent = ds.find(edge.source);
            int destParent = ds.find(edge.destination);

            if (sourceParent != destParent) {
                mst.add(edge);
                ds.union(sourceParent, destParent);
            }
        }

        return mst;
    }

    public static void main(String[] args) {
        List<Edge> edges = new ArrayList<>();
        edges.add(new Edge(0, 1, 4));
        edges.add(new Edge(0, 2, 3));
        edges.add(new Edge(1, 2, 5));
        edges.add(new Edge(1, 3, 2));
        edges.add(new Edge(1, 4, 4));
        edges.add(new Edge(2, 4, 6));
        edges.add(new Edge(3, 4, 7));

        List<Edge> mst = findMinimumSpanningTree(edges, 5);

        System.out.println("Minimum Spanning Tree:");
        for (Edge edge : mst) {
            System.out.println(edge.source + " - " + edge.destination + ": " + edge.weight);
        }
    }
}

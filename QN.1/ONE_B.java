import java.util.PriorityQueue;

public class ONE_B {

    static class Engine {
        int time;
        int engineerCount;

        public Engine(int time, int engineerCount) {
            this.time = time;
            this.engineerCount = engineerCount;
        }
    }

    public static int minTimeToBuildEngines(int[] engines, int splitCost) {
        int totalTime = 0;
        int availableEngineers = 1;
        PriorityQueue<Engine> heap = new PriorityQueue<>((a, b) -> a.time - b.time); // min-heap

        for (int time : engines) {
            heap.offer(new Engine(time, 1));
        }

        while (!heap.isEmpty()) {
            Engine engine = heap.poll();
            totalTime = Math.max(totalTime, engine.time);

            if (engine.engineerCount > availableEngineers) {
                int splitTime = splitCost * (engine.engineerCount - availableEngineers);
                totalTime += splitTime;
                availableEngineers += engine.engineerCount - availableEngineers;
            }

            availableEngineers -= engine.engineerCount;
            if (availableEngineers < 1) {
                totalTime += splitCost;
                availableEngineers += 1;
            }
        }

        return totalTime;
    }

    public static void main(String[] args) {
        int[] engines = {1, 2, 3};
        int splitCost = 1;
        System.out.println("Minimum Time to Build All Engines is " + minTimeToBuildEngines(engines, splitCost));
    }
}

    
//ant colony
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class FIVE_A {
    private int numOfCities; // Number of cities
    private int[][] distanceMatrix; // Distance matrix between cities
    private double[][] pheromoneMatrix; // Pheromone matrix
    private double alpha; // Pheromone importance factor
    private double beta; // Heuristic importance factor
    private double evaporationRate; // Pheromone evaporation rate
    private int numAnts; // Number of ants
    private int maxIterations; // Maximum number of iterations
    private Random random;

    public FIVE_A(int numOfCities, int[][] distanceMatrix, double alpha, double beta, double evaporationRate, int numAnts, int maxIterations) {
        this.numOfCities = numOfCities;
        this.distanceMatrix = distanceMatrix;
        this.alpha = alpha;
        this.beta = beta;
        this.evaporationRate = evaporationRate;
        this.numAnts = numAnts;
        this.maxIterations = maxIterations;
        this.random = new Random();

        // Initialize pheromone matrix
        this.pheromoneMatrix = new double[numOfCities][numOfCities];
        for (int i = 0; i < numOfCities; i++) {
            Arrays.fill(pheromoneMatrix[i], 1.0);
        }
    }

    public List<Integer> solveTravellingSalesmanProb() {
        List<Integer> bestTour = null;
        double bestTourLength = Double.POSITIVE_INFINITY;

        for (int iter = 0; iter < maxIterations; iter++) {
            // Initialize ant tours
            List<List<Integer>> antTours = new ArrayList<>();
            double[] tourLengths = new double[numAnts];

            // Construct solutions
            for (int ant = 0; ant < numAnts; ant++) {
                List<Integer> tour = constructTour();
                antTours.add(tour);
                tourLengths[ant] = calculateTourLength(tour);
                // Update best tour found so far
                if (tourLengths[ant] < bestTourLength) {
                    bestTourLength = tourLengths[ant];
                    bestTour = new ArrayList<>(tour);
                }
            }

            // Update pheromone trails
            updatePheromoneTrails(antTours, tourLengths);
        }

        return bestTour;
    }

    private List<Integer> constructTour() {
        List<Integer> tour = new ArrayList<>();
        boolean[] visited = new boolean[numOfCities];
        int initialCity = random.nextInt(numOfCities); // Start from a random city
        int currentCity = initialCity;
        tour.add(initialCity);
        visited[initialCity] = true;

        for (int i = 1; i < numOfCities; i++) {
            int nextCity = selectNextCity(currentCity, visited);
            tour.add(nextCity);
            visited[nextCity] = true;
            currentCity = nextCity;
        }

        return tour;
    }

    private int selectNextCity(int currentCity, boolean[] visited) {
        double[] probabilities = new double[numOfCities];
        double totalProbability = 0;

        for (int i = 0; i < numOfCities; i++) {
            if (!visited[i]) {
                double pheromone = Math.pow(pheromoneMatrix[currentCity][i], alpha);
                double visibility = Math.pow(1.0 / distanceMatrix[currentCity][i], beta);
                probabilities[i] = pheromone * visibility;
                totalProbability += probabilities[i];
            }
        }

        // Roulette wheel selection
        double rand = random.nextDouble() * totalProbability;
        double sum = 0;
        for (int i = 0; i < numOfCities; i++) {
            sum += probabilities[i];
            if (sum >= rand) {
                return i;
            }
        }

        // Unreachable
        return -1;
    }

    private void updatePheromoneTrails(List<List<Integer>> antTours, double[] tourLengths) {
        // Evaporate pheromone trails
        for (int i = 0; i < numOfCities; i++) {
            for (int j = 0; j < numOfCities; j++) {
                pheromoneMatrix[i][j] *= (1 - evaporationRate);
            }
        }

        // Update pheromone trails based on ant tours
        for (int ant = 0; ant < numAnts; ant++) {
            List<Integer> tour = antTours.get(ant);
            double tourLength = tourLengths[ant];
            for (int i = 0; i < numOfCities - 1; i++) {
                int city1 = tour.get(i);
                int city2 = tour.get(i + 1);
                pheromoneMatrix[city1][city2] += 1.0 / tourLength;
                pheromoneMatrix[city2][city1] += 1.0 / tourLength;
            }
        }
    }

    private double calculateTourLength(List<Integer> tour) {
        double length = 0;
        for (int i = 0; i < tour.size() - 1; i++) {
            int city1 = tour.get(i);
            int city2 = tour.get(i + 1);
            length += distanceMatrix[city1][city2];
        }
        // Add distance from last city back to the start city
        int initialCity = tour.get(0);
        int lastCity = tour.get(tour.size() - 1);
        length += distanceMatrix[lastCity][initialCity];
        return length;
    }

    public static void main(String[] args) {
        int numOfCities = 5;
        int[][] distanceMatrix = {
                {0, 10, 15, 20, 25},
                {10, 0, 35, 25, 30},
                {15, 35, 0, 30, 50},
                {20, 25, 30, 0, 40},
                {25, 30, 50, 40, 0}
        };
        double alpha = 1.0; // Pheromone importance factor
        double beta = 2.0; // Heuristic importance factor
        double evaporationRate = 0.5; // Pheromone evaporation rate
        int numAnts = 10; // Number of ants
        int maxIterations = 100; // Maximum number of iterations

        FIVE_A antcolony = new FIVE_A(numOfCities, distanceMatrix, alpha, beta, evaporationRate, numAnts, maxIterations);
        List<Integer> bestTour = antcolony.solveTravellingSalesmanProb();

        System.out.println("Best tour: " + bestTour);
        System.out.println("Tour length: " + antcolony.calculateTourLength(bestTour));
    }
}

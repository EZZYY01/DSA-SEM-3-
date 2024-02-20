//the possible secretsharing intervals have occurred
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class TWO_B {

    public static List<Integer> secretPossessors(int n, int[][] intervals, int initialPerson) {
        Set<Integer> knownIndividuals = new HashSet<>();

        // Iterate through each interval
        for (int[] interval : intervals) {
            int start = interval[0];
            int end = interval[1];
            Set<Integer> individualsInRange = new HashSet<>();

            for (int i = start; i <= end; i++) {
                if (i < n) { 
                    individualsInRange.add(i);
                }
            }

            knownIndividuals.addAll(individualsInRange);
        }

        // Add the initialPerson if not already present
        knownIndividuals.add(initialPerson);

        // Convert set to list and sort it
        List<Integer> result = new ArrayList<>(knownIndividuals);
        result.sort(null);
        return result;
    }

    public static void main(String[] args) {
        int n = 5;
        int[][] intervals = {{0, 2}, {1, 3}, {2, 4}};
        int initialPerson = 0;

        List<Integer> result = secretPossessors(n, intervals, initialPerson);
        System.out.println("Set of individuals who know the secret is " + result);
    }
}
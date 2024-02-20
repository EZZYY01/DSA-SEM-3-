public class ONE_A {
    public static int minCostToDecorate(int[][] costs) {
        if (costs == null || costs.length == 0) {
            return 0;
        }

        int n = costs.length;   //no of venues
        int k = costs[0].length;  //no of themes

        // Initialize the dynamic programming table
        int[][] dp = new int[n][k];        
        for (int j = 0; j < k; j++) {
            dp[0][j] = costs[0][j];
        }
        
        for (int i = 1; i < n; i++) {
            for (int j = 0; j < k; j++) {
                dp[i][j] = Integer.MAX_VALUE;
                for (int prevTheme = 0; prevTheme < k; prevTheme++) {
                    
                    if (prevTheme == j) {
                        continue;
                    }
                    dp[i][j] = Math.min(dp[i][j], dp[i - 1][prevTheme] + costs[i][j]);
                }
            }
        }
        
        int minCost = Integer.MAX_VALUE;
        for (int j = 0; j < k; j++) {
            minCost = Math.min(minCost, dp[n - 1][j]);
        }
        
        return minCost;
    }

    public static void main(String[] args) {
        int[][] costs = {{1, 3, 2}, {4, 6, 8}, {3, 1, 5}};
        System.out.println("Minimum cost: " + minCostToDecorate(costs));
    }
}

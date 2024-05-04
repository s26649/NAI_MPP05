import java.util.List;
import java.util.Random;

public class KMeans {
    private static double[][] initializeCentroids(List<Data> data, int k) {
        Random rand = new Random();
        double[][] centroids = new double[k][data.get(0).getAttributes().length];
        for (int i = 0; i < k; i++) {
            centroids[i] = data.get(rand.nextInt(data.size())).getAttributes();
        }
        return centroids;
    }

    private static int closestCentroid(double[] dataPoint, double[][] centroids) {
        double minDistance = Double.MAX_VALUE;
        int cluster = 0;
        for (int i = 0; i < centroids.length; i++) {
            double distance = 0;
            for (int j = 0; j < dataPoint.length; j++) {
                distance += Math.pow(dataPoint[j]-centroids[i][j], 2);
            }
            if (distance < minDistance) {
                minDistance = distance;
                cluster = i;
            }
        }
        return cluster;
    }

    private static double[][] recalculateCentroids(List<Data> data, int[] assignments, int k, int dimensions) {
        double[][] newCentroids = new double[k][dimensions];
        int[] counts = new int[k];
        for (int i = 0; i < assignments.length; i++) {
            int cluster = assignments[i];
            counts[cluster]++;
            for (int j = 0; j < dimensions; j++) {
                newCentroids[cluster][j] += data.get(i).getAttributes()[j];
            }
        }
        for (int i = 0; i < k; i++) {
            for (int j = 0; j < dimensions; j++) {
                if (counts[i] != 0) {
                    newCentroids[i][j] /= counts[i];
                }
            }
        }
        return newCentroids;
    }

    private static double calculateSSE(List<Data> data, int[] assignments, double[][] centroids) {
        double sse = 0;
        for (int i = 0; i < assignments.length; i++) {
            int cluster = assignments[i];
            for (int j = 0; j < data.get(i).getAttributes().length; j++) {
                sse += Math.pow(data.get(i).getAttributes()[j] - centroids[cluster][j], 2);
            }
        }
        return sse;
    }

    public static void runKMeans(List<Data> data, int k, int numberOfTrials) {
        int maxIterations = 100;
        int dimensions = data.get(0).getAttributes().length;
        double bestSSE = Double.MAX_VALUE;

        for (int trial = 0; trial < numberOfTrials; trial++) {
            System.out.println("\nProba " + (trial + 1));
            double[][] centroids = initializeCentroids(data, k);
            int[] assignments = new int[data.size()];
            double sse = Double.MAX_VALUE;

            for (int iteration = 0; iteration < maxIterations; iteration++) {
                for (int i = 0; i < data.size(); i++) {
                    assignments[i] = closestCentroid(data.get(i).getAttributes(), centroids);
                }

                double[][] newCentroids = recalculateCentroids(data, assignments, k, dimensions);
                double newSSE = calculateSSE(data, assignments, newCentroids);

                if (Math.abs(newSSE - sse) < 0.001) {
                    System.out.printf("\tIteracja %d, E: %f\n", iteration + 1, newSSE);
                    break;
                }
                centroids = newCentroids;
                sse = newSSE;
                System.out.printf("\tIteracja %d, E: %f\n", iteration + 1, sse);
            }
            if (sse < bestSSE) {
                bestSSE = sse;
            }
        }
        System.out.println("\nNajlepsze E po wszystkich probach: " + bestSSE);
    }
}
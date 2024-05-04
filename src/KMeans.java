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

    public static void runKMeans(List<Data> data, int k) {
        int maxIterations = 100;
        int dimensions = data.get(0).getAttributes().length;
        double[][] centroids = initializeCentroids(data, k);
        int[] assignments = new int[data.size()];
        double[][] newCentroids;
        double sse;

        for (int iteration = 0; iteration < maxIterations; iteration++) {
            // przydzielanie klasterow
            for (int i = 0; i < data.size(); i++) {
                assignments[i] = closestCentroid(data.get(i).getAttributes(), centroids);
            }

            // obliczanie nowych centroidow
            newCentroids = recalculateCentroids(data, assignments, k, dimensions);

            // w razie czego
            if (Math.abs(calculateSSE(data, assignments, newCentroids) - calculateSSE(data, assignments, centroids)) < 0.001) {
                break;
            }

            centroids = newCentroids;
            sse = calculateSSE(data, assignments, centroids);
            System.out.printf("Iteracja %d, Suma kwadratow odleglosci wewnatrz klastrow (E): %f\n", iteration + 1, sse);
        }
    }
}
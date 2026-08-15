import java.util.ArrayList;
import java.util.HashMap;

/**
 * <b>Class - Agglomerative</b>
 * <p>This class's purpose is to cluster our data using the agglomerative clustering algorithm</p>
 * <p>Specifically - this means that we will begin with singleton clusters (clusters with one data point in them)</p>
 * <p>Then, until we reach the desired number of clusters, we will compare clusters to find the two most similar, and then combine those</p>
 */
public class Agglomerative {

    ArrayList<Cluster> clusterList = new ArrayList<>();
    ArrayList<double[]> clusterSimilarityList = new ArrayList<>();
    //ArrayList<Cluster> expectedClusters = new ArrayList<>();
    Cluster[] expectedClusters;
    ArrayList<Perceptron> trainedPerceptrons = new ArrayList<>();
    int K = 0; // Number of clusters from arguments
    boolean clusteringComplete = false;
    final static double NONCOMPARED = -1000;

    /**
     * <b>Constructor - Agglomerative Default</b>
     * <p>Currently has no functionality</p>
     */
    public Agglomerative() {

    }

    /**
     * <b>Constructor - Agglomerative</b>
     * @param clusterList List of Cluster objects to agglomeratively cluster
     * @param K Number of clusters to end up with
     */
    public Agglomerative(ArrayList<Cluster> clusterList, int K) {
        this.clusterList = clusterList;
        this.K = K;
        this.trainedPerceptrons = clusterList.get(0).clusterPerceptronList;
        this.expectedClusters = new Cluster[K];
        for(int i=0; i<K; i++) {
            this.expectedClusters[i] = new Cluster();
            this.expectedClusters[i].setPerceptrons(trainedPerceptrons);
        }
        if(clusterList.size() == K) {
            for(int i=0; i<clusterList.size(); i++) {
                this.expectedClusters[i].addToCluster(clusterList.get(i));
            }
            clusteringComplete = true;
        }else {
            clusteringComplete = formClusters();
        }
    }
    /**
     * <b>Method - formClusters</b>
     * <p>This method forms clusters into "K" clusters agglomeratively</p>
     * <p>It starts by populating the ArrayList of double arrays that represent each image's similarity to each other image</p>
     * <p>Then, it uses those similarities to find the two most similar images, and then merges their respective clusters</p>
     * @return true iff we have checked all clusters and moved them all to the array version of storage
     */
    private boolean formClusters() {
        while(clusterList.size() > this.K) {
            findClusterSimilarities();
            combineMostSimilar();
        }
        return true;
    }

    /**
     * <b>Method - findClusterSimilarities</b>
     * <p>This method populates the class member clusterSimilaritiesList</p>
     * <p>First, it clears any possible current values in the list</p>
     * <p>Next, it iterates through each value in the clusterList ArrayList, and finds all similarity values between all Clusters</p>
     * <p>These similarity values are stored as an ArrayList of double[]'s (arrays)</p>
     * <p>The permanent value of -1000 is used as a placeholder for the cluster's own spot - therefore, a cluster will not be compared to itself,</p>
     * <p>and -1000 should never be a better similarity match than anything.</p>
     */
    private void findClusterSimilarities() {
        this.clusterSimilarityList.clear();
        for(int i=0; i<this.clusterList.size(); i++) {
            this.clusterSimilarityList.add(compareClusterToList(this.clusterList.get(i), i));
        }
    }

    /**
     * <b>Method - compareClusterToList</b>
     * <p>This method compares the given cluster to the rest of the clusterList</p>
     * <p>It then creates an array of doubles that represents its similarity to all other clusters, and adds it to clusterSimilarityList</p>
     * @param toCompare Cluster to compare to the rest of the list
     * @param index index value of the cluster in the list, so we do not compare a Cluster to itself
     * @return double[] of similarity values to all other clusters
     */
    private double[] compareClusterToList(Cluster toCompare, int index) {
        double[] comparisonArr = new double[this.clusterList.size()];
        for(int i=0; i<this.clusterList.size(); i++) {
            if(i == index) {
                comparisonArr[i] = NONCOMPARED;
                continue;
            }
            Comparitor comparitor = new Comparitor(toCompare, this.clusterList.get(i));
            comparisonArr[i] = comparitor.similarity;
        }
        return comparisonArr;
    }

    /**
     * <b>Method - combineMostSimilar</b>
     * <p>This method combines the most similar two Clusters in the clusterList</p>
     * <p>This is done by perceptron similarity comparison</p>
     * <p>Specifically, here we iterate through the clusterSimilarityList (list of arrays), finding the highest similarity value and its index per array</p>
     * <p>Once this highest similarity value and index are found, we get the two most similar clusters and merge them</p>
     */
    private void combineMostSimilar() {
        int clusterIndex = -1;
        int arrIndex = -1;
        double mostSimilar = 0.0;
        // indexValue[0] = the index of the most similar value in the array
        // indexValue[1] = the similarity score
        for(int i=0; i<this.clusterSimilarityList.size(); i++) {
            double[] indexValue = findMostSimilar(i);
            if(indexValue[1] > mostSimilar) {
                mostSimilar = indexValue[1];
                arrIndex = (int)indexValue[0];
                clusterIndex = i;
            }
        }
        this.clusterList.get(clusterIndex).addToCluster(this.clusterList.remove(arrIndex));
    }

    /**
     * <b>Method - findMostSimilar</b>
     * <p>This method finds the highest similarity value within a given array of similarity values</p>
     * <p>It then returns the index of said similarity and the similarity value itself, to be used by the combineMostSimilar method</p>
     * @param index the index of the double[] array to parse
     * @return a double[] array that contains [0] = index, [1] = similarityValue
     */
    private double[] findMostSimilar(int index) {
        // indexValue[0] = the index of the most similar value in the array
        // indexValue[1] = the similarity score
        double[] indexValue = new double[2];
        for(int i=0; i<this.clusterSimilarityList.get(index).length; i++) {
            if(this.clusterSimilarityList.get(index)[i] > indexValue[1]) {
                indexValue[0] = i;
                indexValue[1] = this.clusterSimilarityList.get(index)[i];
            }
        }
        return indexValue;
    }

}

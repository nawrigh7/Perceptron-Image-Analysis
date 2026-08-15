import java.util.ArrayList;
import java.util.Collections;

/**
 * <b>Class - Cluster</b>
 * <p>This class is used to group Histograms together by similarity.</p>
 */
public class Cluster {

    /*
     * Class members
     */
    static final int MAXIMUM_LENGTH = 64;
    ArrayList<Histogram> histList = new ArrayList<>();
    ArrayList<String> namesList = new ArrayList<>();
    ArrayList<Perceptron> clusterPerceptronList = new ArrayList<>();
    // The clusterHistogram will be stored as the average of all of the Histograms objects in the clusters'
    // normalized histograms.
    double[] clusterHistogram = new double[MAXIMUM_LENGTH];
    int numInCluster = 0;
    //double similarityMeasure = 0; // This equates to the y_nj in the equation from the assignment. Use this in the equation when comparing to new images

    /**
     * <b>Constructor - Cluster Default</b>
     * <p>This constructor does not initialize any values other than its class members</p>
     * <p>This is primarily useful for creating cluster objects that you expect to need, but do not yet have Histograms to add to</p>
     */
    public Cluster() {
    }

    /**
     * <b>Constructor - Cluster(Histogram)</b>
     * <p>This constructor takes in a Histogram object and adds it to the cluster</p>
     * <p>This involves increasing the numInCluster value, and re-averaging the normalized versions of each histogram to create a representative histogram for the cluster.</p>
     * @param h Histogram to add
     */
    public Cluster(Histogram h) {
        addToCluster(h);
    }

    /**
     * <b>Constructor - Cluster(ArrayList(Histogram))</b>
     * <p>This constructor takes an ArrayList of Histogram objects, for ease of creating a large cluster</p>
     * <p>Similar to creating a Cluster with one Histogram, this calls the addToCluster method for each Histogram in the ArrayList</p>
     * @param histList List of Histograms to add to the cluster
     */
    public Cluster(ArrayList<Histogram> histList) {
        for(Histogram h : histList) {
            addToCluster(h);
        }
    }

    //When adding a histogram to the cluster, add it to the ArrayList for filename storage
    //And then add its histogram to the
    /**
     * <b>Method - addToCluster(Histogram)</b>
     * <p>This method will allow one to add any given Histogram object to the cluster by adding it to the ArrayList, re-evaluating the average histogram, and increasing the numInCluster</p>
     * <p>This can be done to any Cluster, no matter how many histograms it already contains</p>
     * @param h Histogram to add
     */
    public void addToCluster(Histogram h) {
        this.numInCluster++;
        this.histList.add(h);
        this.namesList.add(h.fileName);
        createAverageHistogram();
        //calculateSimilarity();
    }

    /**
     * <b>Method - addToCluster(Cluster)</b>
     * <p>This method will allow one to add any given Cluster object to the cluster, essentially merging them</p>
     * <p>This is done in much the same way as adding an individual cluster, however it will iterate through all Histograms contained in a cluster to add them</p>
     * @param c Cluster object to add
     */
    public void addToCluster(Cluster c) {
        for(Histogram h : c.histList) {
            addToCluster(h);
        }
    }

    /**
     * <b>Method - removeFromCluster</b>
     * <p>This method will remove a Histogram from the cluster, and re-configure its comparative histogram</p>
     * @param histName The name of the file to remove from the cluster
     * @return The removed Histogram object
     * @throws ClusterException Thrown if the requested Histogram is not found in the cluster.
     */
    public Histogram removeFromCluster(String histName) throws ClusterException {
        this.numInCluster--;
        for(int i=0; i<histList.size(); i++) {
            if(histList.get(i).fileName.equals(histName)){
                removeName(histName);
                return histList.remove(i);       
            }
        }
        throw new ClusterException("Error - that Histogram is not in this cluster");
    }

    /**
     * <b>Method - removeName</b>
     * <p>Called from removeFromCluster, this method removes the name of a Histogram from the Cluster's namesList</p>
     * @param nameToRemove name of the histogram to remove
     */
    private void removeName(String nameToRemove) {
        for(int i=0; i<this.namesList.size(); i++) {
            if(namesList.get(i).equals(nameToRemove)){
                namesList.remove(i);
            }
        }
    }

    /**
     * <b>Method - createAverageHistogram</b>
     * <p>This method creates an average histogram from all of the histogram objects it contains</p>
     * <p>This average is used to allow comparison to the cluster as a whole</p>
     */
    private void createAverageHistogram() {
        // Ensure our cluster Histogram is cleared
        for(int i=0; i<this.clusterHistogram.length; i++) {
            clusterHistogram[i] = 0;
        }
        // Add the value from the index of each Histogram in the histList, then divide by numInCluster
        for(int i=0; i<this.clusterHistogram.length; i++) {
            clusterHistogram[i] += getAverageValue(i);
        }
    }
    /**
     * <b>Method getAverageValue(int)</b>
     * <p>This method returns the average value from the [index] place of every Histogram object in the Cluster</p>
     * @param index The index of the normalized histogram to access
     * @return Average of all values
     */
    private double getAverageValue(int index) {
        double avg = 0;
        for(int i=0; i<this.histList.size(); i++) {
            avg += histList.get(i).normalized[index];
        }
        return avg / numInCluster;
    }

    /**
     * <b>Method - setPerceptron</b>
     * <p>Each cluster object may have an ArrayList of Perceptron objects associated with it, to allow for similarity measures from Perceptron training to be used for comparison</p>
     * <p>This method simply assigns the class member to the argument</p>
     * @param clusterPerceptronList List of perceptrons to assign to this cluster
     */
    public void setPerceptrons(ArrayList<Perceptron> clusterPerceptronList) {
        this.clusterPerceptronList = clusterPerceptronList;
        //calculateSimilarity();
    }

    /**
     * <b>@Override method - toString</b>
     * <p>This method override's the parent Object toString() method to allow Cluster objects to return a printable String</p>
     */
    @Override
    public String toString() {
        Collections.sort(this.namesList);
        String result = "";
        for(String s : namesList) {
            result = result + " " + s;
        }
        return result.trim();
    }
}

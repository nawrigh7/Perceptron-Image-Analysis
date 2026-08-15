/**
 * <b>Class - Comparitor</b>
 * <p>This class compares normalized histograms, which can be done multiple ways.</p>
 * <p>The value 2 is used as an elimination-from-consideration case.</p>
 */
public class Comparitor{

    /*
     * Class Members
     */
    static final int MAXIMUM_LENGTH = 64;
    /*ArrayList<double[]> histList = new ArrayList<>();
    ArrayList<double[]> normHistList = new ArrayList<>();
    ArrayList<double[]> comparisons = new ArrayList<>();*/
    double[] normOne = new double[MAXIMUM_LENGTH];
    double[] normTwo = new double[MAXIMUM_LENGTH];
    double similarity = 2.0;


    /**
     * <b>Constructor - Comparitor default</b>
     * <p>This currently does not do anything. </p>
     */
    public Comparitor(){

    }

    /**
     * <b>Constructor - Comparitor</b>
     * <p>This creates a comparitor object, which uses two normalized histograms to evaluate their similarity with a bin-to-bin comparison.</p>
     * <p>This similarity will be a value between 0 and 1, where 1 is the same, and 0 is completely different.</p>
     * @param histOne The normalized histogram that corresponds to the first file
     * @param histTwo The normalized histogram that corresponds to the second file
     */
    public Comparitor(double[] histOne, double[] histTwo) {
        this.normOne = histOne;
        this.normTwo = histTwo;
        this.similarity = compare(normOne, normTwo);
    }

    /**
     * <b>Constructor - Comparitor</b>
     * <p>This uses a Perceptron similarity measure, implementing the function sim(i,j) = Σ(n=1->N) (1 / (y_n,i - y_n,j)²)</p>
     * <p>Put plainly, let i = one image, and j = another image, and have number of trained perceptrons N.</p>
     * <p>The similarity between i and j equals the sum of (1 / ((i run through perceptron n) - (j run through perceptron n)²) for all perceptrons n</p>
     * <p>This creates a comparitor object which can compare two clusters, rather than individual histograms</p>
     * @param clusterOne First Cluster to be compared
     * @param clusterTwo Second Cluster to be compared
     */
    public Comparitor(Cluster clusterOne, Cluster clusterTwo) {
        this.similarity = (getSimilarityMeasures(clusterOne, clusterTwo));
    }

    /**
     * <b>Method - getSimilarityMeasures</b>
     * <p>This method finds the similarity measure between the two images</p>
     * <p>It does so image by image, storing the result in double arrays.</p>
     * @param clusterOne image 1, i
     * @param clusterTwo image 2, j
     * @return similarity value between image 1 (i) and 2 (j) based on the equation sim(i,j) = Σ(n=1->N) (1 / (y_n,i - y_n,j)²)
     */
    private double getSimilarityMeasures(Cluster clusterOne, Cluster clusterTwo) {
        double[] y_ni = new double[clusterOne.clusterPerceptronList.size()];
        double[] y_nj = new double[clusterTwo.clusterPerceptronList.size()];
        double similarity = 0;
        // implement function: sim(i,j) = Σ(n=1->N) (1 / (y_n,i - y_n,j)²)
        for(int i=0; i<y_ni.length; i++) {
            y_ni[i] = multiplyWeights(clusterOne.clusterHistogram, clusterOne.clusterPerceptronList.get(i));
            y_nj[i] = multiplyWeights(clusterTwo.clusterHistogram, clusterTwo.clusterPerceptronList.get(i));
            similarity += 1/(Math.pow((y_ni[i] - y_nj[i]), 2));
        }
        return similarity;
    }

    /**
     * <b>Method - multiplyWeights</b>
     * <p>This method finds the similarity measure to the given perceptron, using the same function that the perceptrons are trained with:</p>
     * <p>y = b + Σ(i=0->63) w(i) * h(i)</p>
     * <p>In this, y is the resultant similarity measure, b is the perceptron's bias, then adding the sum of the corresponding perceptron weights * value in the normalized histogram bin</p>
     * @param clusterHistogram The normalized histogram that represents the averages of all histograms in the cluster
     * @param perceptron Given trained perceptron to compare to the cluster
     * @return the result of the afforementioned formula
     */
    private double multiplyWeights(double[] clusterHistogram, Perceptron perceptron) {
        double result = perceptron.bias;
        for(int i=0; i<clusterHistogram.length; i++) {
            result += (perceptron.weightMap.get(i) * clusterHistogram[i]);
        }
        return result;
    }

    /**
     * <b>Method - compare()</b>
     * <p>This method takes in two arrays that represent normalized histograms, and computes the sum of the pair-wise minimums of each bin in the histograms</p>
     * <p>This value should be between 0 and 1, with the closer it gets to 1 being more similar</p>
     * @param histOne The first histogram to compare
     * @param histTwo The second histogram to compare
     * @return The comparison / similarity value
     */
    public static double compare(double[] histOne, double[] histTwo) {
        double sum = 0;
        for(int i=0; i<histOne.length; i++) {
            if(histOne[i] <= histTwo[i]) {
                sum += histOne[i];
            }else if (histTwo[i] < histOne[i]){
                sum += histTwo[i];
            }
        }
        return sum;
    }
    
    
}
import java.util.ArrayList;
import java.util.HashMap;


/**
 * <b>Class - Perceptron</b>
 * <p>The perceptron class is the implementation of a perceptron for basic machine learning</p>
 * <p>The perceptron implements the following equation:</p>
 * <p> y = b + Σ(i=0->63) w(i) * h(i)</p>
 * <p>The y value in this equation is the perceptron value</p>
 * <p>The b value is our bias - in this case, it will be 1 if the image is in the class of interest, and -1 otherwise</p>
 * <p>The w value is the weight of a value being evaluated, and is updated with each epoch</p>
 * <p>The h value is the % of an image contained in a single histogram bin (i.e., the # in the bin / total number of entries in the image file). This is equivalent to the value in the normalized histogram calculated in the Histogram object</p>
 */
public class Perceptron {

    /*
     * Class Members
     */

    double bias, perceptronValue;
    int imageClass;
    final int HISTOGRAM_LENGTH = 64;
    HashMap<Integer, Double> weightMap = new HashMap<>(); // This map corresponds to the "w" value in the equation. There will be 64 values in this.
    ArrayList<Histogram> histList = new ArrayList<>();
    //double[] weights = new double[HISTOGRAM_LENGTH];

    /**
     * <b>Constructor - Perceptron default</b>
     * <p>The default constructor simply initializes all values to 0, where possible</p>
     * <p>The image class value is initialized to -1, as this is not a valid class and we do not want to confuse this object with a ready-to-use Perceptron</p>
     */
    public Perceptron() {
        this.bias = 0;
        this.perceptronValue = 0;
        this.imageClass = -1;
        fillWeightMap();
    }

    /**
     * <b>Constructor - Perceptron(ArrayList, int)</b>
     * <p>This constructor takes in an arrayList of Histogram objects and an integer</p>
     * <p>The integer should be the desired class of image to process, as entered on the command line at runtime</p>
     * <p>The class of an image should be embedded in the file name: classX_#.pgm, where X is the class</p>
     * @param histList List of histogram objects created from the supplied file
     * @param imageClass class of image to process, as indicated from command line.
     */
    public Perceptron(ArrayList<Histogram> histList, int imageClass) {
        this.perceptronValue = 0;
        this.bias = 0;
        this.imageClass = imageClass;
        this.histList = histList;
        fillWeightMap();
        for(Histogram h : this.histList) {
            calculatePerceptronValue(h);
        }
    }

    /**
     * <b>Method - fillWeightMap</b>
     * <p>This method fills the weightMap with keys from 0-63 and values of 0.0 in all places. This is only used upon object creation (by constructors)</p>
     */
    private void fillWeightMap() {
        for(int i=0; i<HISTOGRAM_LENGTH; i++) {
            this.weightMap.put(i, 0.0);
            //this.weights[i] = 0.0;
        }
    }

    /**
     * <b>Method - epoch</b>
     * <p>This method executes the primary formula of the Perceptron: y = b + Σ(i=0->63) w(i) * h(i)</p>
     * <p>The variable perceptronValue equates to y in the equation, and bias equates to b.</p>
     * <p>The following sum is executed in the weightTimesHistogram method</p>
     */
    public void epoch() {
        for(Histogram h : this.histList) {
            calculatePerceptronValue(h);
            updateWeight(h);
        }
    }

    /**
     * <b>Method - epoch(int)</b>
     * <p>This method is an override of epoch that takes in an integer parameter, and calls the epoch-noargs method for "iterations" number of times</p>
     * @param iterations number of epochs to complete
     */
    public void epoch(int iterations) {
        for(int i=0; i<iterations; i++) {
            epoch();
        }
    }

    /**
     * <b>Method - calculatePerceptronValue()</b>
     * <p>calculates the perceptron value, or the "y," from the formula using the provided list of Histogram objects</p>
     */
    private void calculatePerceptronValue(Histogram h) {
        this.perceptronValue = this.bias + weightTimesHistogram(h);
    }
    
    /**
     * <b>Method - weightTimesHistogram</b>
     * <p>This method executes the sum of the Perceptron formula: y = b + Σ(i=0->63) w(i) * h(i)</p>
     * <p>Variable definitions:</p>
     * <ul>
     *      <li>w(i) = weight at given index (i)</li>
     *      <li>h(i) = normalized histogram value at index (i)</li>
     * </ul>
     * @param hist histogram to gather hist values from
     * @return the sum of the products of each indexed weight * normalized histogram value, taken from the weightMap and histMap, respectively.
     */
    private double weightTimesHistogram(Histogram hist) {
        double sum = 0;
        for(int i=0; i<HISTOGRAM_LENGTH; i++) {
            sum += weightMap.get(i) * hist.histMap.get(i);
            //sum += weights[i] * hist.normalized[i];
        }
        return sum;
    }

/**
     * <b>Method - updateWeight</b>
     * <p>This method updates the weightMap according to the equation: w(i) <- w(i) + (d - y)*h(i)</p>
     * <ul>
     *      <li>w(i) = weight at index (i)</li>
     *      <li>d = class association. this is 1 if the image is from the target class, and -1 if not</li>
     *      <li>y = perceptronValue</li>
     *      <li>h(i) = value of the normalized histogram at index (i)</li>
     * </ul>
     * @param trainingSample Histogram to take values from for udpate
     * @param d class association as calculated in updateBias
     */
        private void updateWeight(Histogram trainingSample) {
        int d = 0; // This is only needed if using specific image-class matching (as in pa8) If using this matching, un-comment the if/else just below as well.
        //int d = 1;
            if(Integer.parseInt(trainingSample.imageClass) == this.imageClass) {
                d = 1;
            }else {
                d = -1;
            }
            for(int i=0; i<HISTOGRAM_LENGTH; i++) {
                double mapUpdate = this.weightMap.remove(i) + (d - this.perceptronValue) * trainingSample.histMap.get(i);
                this.weightMap.put(i, mapUpdate);
                //double arrUpdate = this.weights[i] + ((d - this.perceptronValue) * trainingSample.normalized[i]);
                //weights[i] = arrUpdate;
            }
            updateBias(trainingSample, d);
            
        }

    /**
     * <b>Method - updateBias</b>
     * <p>This method updates the bias of our perceptron as outlined in the equation: b <- b + (d - y)</p>
     * <p>Variable Definitions:</p>
     * <ul>
     *      <li>b = bias</li>
     *      <li>d = class association. this is 1 if the image is from the target class, and -1 if not</li>
     *      <li>y = perceptronValue</li>
     * </ul>
     * @param trainingSample Histogram object used to update the bias value
     */
    private void updateBias(Histogram trainingSample, int d) {
        this.bias += (d - this.perceptronValue);
    }

    /**
     * <b>Method - toString</b>
     * <p>Used for easy printing of Perceptron objects, as outlined in pa8</p>
     */
    @Override
    public String toString() {
        String printable = "";
        for(int i=0; i<HISTOGRAM_LENGTH; i++) {
            printable = printable + String.format("%.6f ", this.weightMap.get(i)) /*this.weights[i])*/;
        }
        printable = printable + String.format("%.6f", this.bias);
        return printable.trim();
    }

    /**
     * <b>Setter - imageClass</b>
     * <p>Sets the class member imageClass to the input value</p>
     * @param imageClass image class to set the class member to
     */
    public void setImageClass(int imageClass) {
        this.imageClass = imageClass;
    }
}
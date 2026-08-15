import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

/**
 * <b>Class - Histogram</b>
 * <p>This class creates a histogram from a File object</p>
 * <p>It will create both an integer Histogram, which can easily be printed, and a double Histogram for normalization</p>
 * <p>It will then normalize the histogram, following this normalization process:</p>
 * <ul>
 *  <li>Sum all of the bins in a histogram</li>
 *  <li>Divide each bin by this resulting sum</li>
 * </ul>
 */
public class Histogram {

    /*
     * Class Members
     */
    static final int HISTOGRAM_LENGTH = 64;
    File inputFile;
    String fileName;
    String imageClass; // This value is only used for pa7+ functionality, and should be parsed to an int in that functionality.
    ArrayList<Integer> valueList = new ArrayList<>();
    int[] histogram = new int[HISTOGRAM_LENGTH];
    double[] dblHist = new double[HISTOGRAM_LENGTH];
    double sum = 0.0;
    double[] normalized = new double[HISTOGRAM_LENGTH];
    HashMap<Integer, Double> histMap = new HashMap<>();

    /**
     * <b>Constructor - Histogram Default</b>
     * <p>Not for use, as such it only throws an exception (for testing purposes, it is not private)</p>
     * 
     * @throws InvalidHistogramException throws exception upon use, as it should never be called.
     */
    public Histogram() throws InvalidHistogramException {
        throw new InvalidHistogramException("Error - default constructor not for use");
    }

    /**
     * <b>Constructor - Histogram</b>
     * <p>This constructor takes in a file, and creates a histogram from it</p>
     * <p>The resulting histogram represents how many times a given number in a 4-count range occurs (for example, a number between 0-3, or 4-7, etc.) in the range from 0-255</p>
     * @param inputFile The file to create a histogram from
     * @throws InvalidHistogramException Should not necessarily be thrown - only thrown if file is not found, which should have already been validated by FileValidator
     */
    public Histogram(File inputFile) throws InvalidHistogramException{
        this.inputFile = inputFile;
        this.fileName = inputFile.getName();
        this.imageClass = this.fileName.substring(5,6);
        this.valueList = createListFromFile(this.inputFile);
        this.histogram = rangeCounter(valueList);
        this.dblHist = convertToDouble(histogram);
        this.sum = getSum(dblHist);
        this.normalized = normalize(dblHist, sum);
        createMapFromList();
    }

    /**
     * <b>Method - createListFromFile</b>
     * <p>This method creates an ArrayList of Integers from a .pgm file</p>
     * @param fileToCheck file to check
     * @return ArrayList of Integers
     * @throws InvalidHistogramException throws if the file is not found
     */
    public static ArrayList<Integer> createListFromFile(File fileToCheck) throws InvalidHistogramException{
        ArrayList<Integer> returnList = new ArrayList<>();
        try {
            Scanner scan = new Scanner(fileToCheck);
            scan.next();
            scan.next();
            scan.next();
            scan.next();
            while(scan.hasNextInt()) {
                returnList.add(scan.nextInt());
            }
            scan.close();
        } catch (FileNotFoundException e) {
            throw new InvalidHistogramException("Error - File not found while creating Histogram");
        }
        return returnList;
    }
    /**
     * <b>Method - createMapFromList</b>
     * <p>This method creates a HashMap of the normalized histogram</p>
     * <p>This is used for faster access to the values now that we can know the location of them</p>
     * <p>The key for each value is an integer representing the index of the histogram array, and the value is the normalized value</p>
     */
    private void createMapFromList() {
        for(int i=0; i<normalized.length; i++) {
            histMap.put(i, normalized[i]);
        }
    }
    /**
     * <b>Method - rangeCounter()</b>
     * <p>This method creates our histogram by counting the number of values in sets of fours, and increasing each 0-4 "bucket" by that amount</p>
     * <p>For example, every time it sees the number "100," it calculates 100/4 = 25, and increases the 25th bucket of our histogram object by 1</p>
     * @param fileValues ArrayList created from the pgm file, created in createListFromFile()
     * @return histogram object of ints
     */
    public static int[] rangeCounter(ArrayList<Integer> fileValues) {
        int[] histogram = new int[HISTOGRAM_LENGTH];
        for(Integer i : fileValues) {
            histogram[i/4] += 1;
        }
        return histogram;
    }
    /**
     * <b>Method - convertToDouble()</b>
     * <p>This method converts an int Histogram into a double Histogram object</p>
     * @param histogram int Histogram, as created by rangeCounter()
     * @return double Histogram
     */
    public static double[] convertToDouble(int[] histogram) {
        double[] dblHist = new double[HISTOGRAM_LENGTH];
        for(int i=0; i<histogram.length; i++) {
            dblHist[i] = (double) histogram[i];
        }
        return dblHist;
    }

    /**
     * <b>Method - getSum()</b>
     * <p>This method takes in an array of doubles, and returns the sum of all of its values</p>
     * @param hist double array to evaluate
     * @return sum of values
     */
    public static Double getSum(double[] hist) {
        Double sum = 0.0;
        for(double d : hist) {
            sum += d;
        }
        return sum;
    }
    /**
     * <b>Method - normalize()</b>
     * <p>This method takes in an array of doubles (a histogram) and the sum of its values, and returns a normalized version of it.</p>
     * <p>The normalization process, as described earlier, is simply dividing each value by the sum of all the values</p>
     * @param hist histogram array
     * @param sum sum of values of the histogram array
     * @return normalized array
     */
    public static double[] normalize(double[] hist, Double sum) {
        double[] result = new double[hist.length];
        for(int i=0; i<hist.length; i++) {
            result[i] = hist[i] / sum;
        }
        return result;
    }

    @Override
    public String toString() {
        String toReturn = "";
        for(double d : normalized) {
            toReturn = toReturn + d + " ";
        }
        return toReturn.trim();
    }
}

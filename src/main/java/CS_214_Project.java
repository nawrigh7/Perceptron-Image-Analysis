import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * <b>Class - CS_214_Project</b>
 * <p>Runtime class, used to take in command line args and execute program</p>
 */
public class CS_214_Project {
    /*
     * Class Members
     */
    static final String expectedExtension = ".txt";
    static HashMap<Integer, ArrayList<Histogram>> trainingHistogramsByClass = new HashMap<>();
    static ArrayList<Histogram> allTrainingHistograms = new ArrayList<>();
    static ArrayList<Histogram> testHistogramsList = new ArrayList<>();
    static ArrayList<Perceptron> perceptronList = new ArrayList<>();
    // The following three class members are instantiated during the checkArgs method
    static String trainingSet; //args[0]
    static String imageSet; //args[1]
    static int numClusters; //args[2]


        //  /\_/\  
        // ( o.o ) {Welcome to the Final Countdown}
        //  > ^ < 
        // "The end of a melody is not its goal: but nonetheless, had the melody not reached its end it would not have reached its goal either." --Someone 
    /**
     * <b>Method - main</b>
     * <p>Execution method for the program</p>
     * @param args Command line arguments provided by user
     * @throws ExtensionException Thrown if a supplied file is not a .txt file, in this case
     * @throws PgmException PgmValidator exception handling
     * @throws InvalidFileException File Validator exception handling (PgmValidator parent class)
     * @throws FileNotFoundException If files are nonexistant
     * @throws InvalidHistogramException Error handling for Histogram Objects
     * @throws ArgsException 
     */
    public static void main(String[] args) throws ExtensionException, FileNotFoundException, InvalidFileException, PgmException, InvalidHistogramException, ArgsException {
        if(args.length != 3) {
            if(args.length == 0) {
                throw new ArgsException(" No arguments were provided. Please provide the filenames of a training set.txt, a testing set.txt, and the number of desired clusters");
            }
            throw new ArgsException(" invalid number of arguments. Please provide the names for two text files, and the integer number of clusters you would like created.");
        }
        //long start = System.currentTimeMillis();
        if(checkArgs(args)){
            run(args);
        }
        //long end = System.currentTimeMillis();
        //String printableTime = "Time to run: " + (end-start);
        //System.out.println(printableTime);
    }

    /**
     * <b>Method - run</b>
     * <p>Flow control method - taking responsibilities out of main, this method executes the functionalities of the program as specified</p>
     * @param args The arguments provided at command line
     * @throws ExtensionException Used for extension error handling
     * @throws PgmException PgmValidator exception handling
     * @throws InvalidFileException File Validator exception handling (PgmValidator parent class)
     * @throws FileNotFoundException If files are nonexistant
     * @throws InvalidHistogramException Error handling for Histogram objects
     * @throws ArgsException thrown if arguments are invalid
    */
    private static void run(String[] args) throws ExtensionException, FileNotFoundException, InvalidFileException, PgmException, InvalidHistogramException, ArgsException {
        PgmValidator trainingImages = new PgmValidator(trainingSet);
        trainingImages.validate();
        PgmValidator imagesToCompare = new PgmValidator(imageSet);
        imagesToCompare.validate();
        if(trainingImages.numOfClasses < 2) {
            throw new PgmException("Error - there must be at least 2 unique classes in the training set.");
        }
        if(numClusters > imagesToCompare.imageList.size()) {
            throw new ArgsException("Error - there are not enough images to create that many clusters.");
        }
        if(trainingImages.isValid && imagesToCompare.isValid) {
            createHistograms(trainingImages, imagesToCompare);
        }
        createPerceptrons();
        ArrayList<Cluster> clusterList = createClusters();

        Agglomerative aggloCluster = new Agglomerative(clusterList, numClusters);

        //Finished product
        if(aggloCluster.clusteringComplete) {
            for(int i=0; i<numClusters; i++) {
                System.out.println(aggloCluster.clusterList.get(i));
            }
        }
        
    }

    /**
     * <b>Method - checkArgs</b>
     * <p>This method checks that the provided arguments are valid for use in this program's functionality</p>
     * <p>This would mean that the first and second arguments are filenames for .txt files, and the third is a positive integer</p>
     * @param args arguments provided at command line
     * @return true iff all arguments are valid
     * @throws ExtensionException Used for error handling regarding the file type of the first two arguments
     * @throws ArgsException Used for error handling regarding the third argument (negative, not an integer, etc.)
     */
    private static boolean checkArgs(String[] args) throws ExtensionException, ArgsException {
        trainingSet = args[0];
        imageSet = args[1];
        try{
            numClusters = Integer.parseInt(args[2]);
            if(numClusters < 1) {
                throw new ArgsException("Error - the third argument should be a positive integer value. The entered value of " + args[2] + " is invalid.");
            }
        } catch(NumberFormatException e) {
            throw new ArgsException("Error - the third argument should be a positive integer value. Entered value of " + args[2] + " is invalid.");
        }
        ExtensionChecker txtChecker = new ExtensionChecker(expectedExtension);
        
        if(!txtChecker.checkExtension(trainingSet)) {
            throw new ExtensionException("Error - the file provided for perceptron training was not of the expected .txt type. Please try again with a text file!");
        }
        if(!txtChecker.checkExtension(imageSet)) {
            throw new ExtensionException("Error - the file provided as the depth image set was not of the expected .txt type. Please try again with a text file!");
        }
        return true;
    }

    /**
     * <b>Method - createHistograms</b>
     * <p>This method accepts two PgmValidator objects (created from the provided arguments in the run method)</p>
     * <p>It then populates two data structures:</p>
     * <ul>
     *      <li>1 - The trainingHistogramsByClass HashMap, which will be arranged in (key, value) pairs as (class, ArrayList of Histograms of that class)</li>
     *      <li>2 - The testHistogramsList, which is an ArrayList of Histogram objects created from our test images set, to test our perceptrons against later.</li>
     * </ul>
     * @param trainingImages The set of images used to train our perceptrons, arg[0]
     * @param imagesToCompare The set of images to compare to our perceptrons, arg[1]
     * @throws InvalidHistogramException Histogram object error handling
    */
    private static void createHistograms(PgmValidator trainingImages, PgmValidator imagesToCompare) throws InvalidHistogramException {
        trainingImages.imagesByClass.forEach( (key, value) -> {
            // Within this forEach, the key represents each class, and the value represents an ArrayList of .pgm image File objects.
            // The goal is to create a mirrored HashMap where the key is the class, and the value is an ArrayList of Histogram objects made from those image files.
            // In theory - for each class in trainingImagesByClass, add that class to this hashMap, and populate the ArrayList<Histogram> value to that class
            // Then, we can create a Perceptron for each class from this hashMap easily.
            trainingHistogramsByClass.put(key, new ArrayList<Histogram>());
            for(File f : value) {
                try {
                    trainingHistogramsByClass.get(key).add(new Histogram(f));
                    allTrainingHistograms.add(new Histogram(f));
                } catch (InvalidHistogramException e) {
                    // This SHOULD never occur, at this stage.
                    e.printStackTrace();
                }
            }
        });
        for(File image : imagesToCompare.imageList) {
            testHistogramsList.add(new Histogram(image));
        }
    }
    
    /**
     * <b>Method - createPerceptrons</b>
     * <p>This method populates the ArrayList of Perceptron objects from each of the ArrayList of Histograms in our trainingHistogramsByClass HashMap</p>
     * <p>It then calls trainPerceptrons, which will epoch each perceptron 100 times on their respective dataset</p>
     */
    private static void createPerceptrons() {
        trainingHistogramsByClass.forEach( (key, value) -> {
            //perceptronList.add(new Perceptron(value, key)); // This statement assigns each Perceptron to only train based on the expected class. i.e., class1 => Perceptron 1, class2 => Perceptron 2, etc.
            perceptronList.add(new Perceptron(allTrainingHistograms, key)); // This statement assigns all perceptrons to be assigned, and indicates a "classness value" for each perceptron ("d" in the Perceptron equation)
        });

        trainPerceptrons();
    }

    /**
     * <b>Method - trainPerceptrons</b>
     * <p>This method trains the Perceptrons from the provided training set (args[0])</p>
     * <p>This means it runs the epoch method for the assigned number of times (in this situation, it is 100)</p>
     */
    private static void trainPerceptrons() {
        for(Perceptron ai : perceptronList) {
            ai.epoch(100);
        }
    }

    /**
     * <b>Method - createClusters</b>
     * <p>This method creates and returns an ArrayList of Cluster objects</p>
     * <p>The number of clusters created is equal to the number of images in the test set.</p>
     * <p>These will then be combined using agglomerative clustering based on perceptron similarity measures</p>
     * @return ArrayList of Cluster objects with size = number of test images
     */
    private static ArrayList<Cluster> createClusters() {
        ArrayList<Cluster> clusterList = new ArrayList<>();
        for(Histogram image : testHistogramsList) {
            clusterList.add(new Cluster(image));
        }
        for(Cluster c : clusterList) {
            c.setPerceptrons(perceptronList);
        }
        return clusterList;
    }
}
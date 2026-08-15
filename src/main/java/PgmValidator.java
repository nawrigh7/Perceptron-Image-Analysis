import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

/**
 * <b>Class - PgmValidator</b>
 * <p>Child class of FileValidator</p>
 * <p>This class expands functionality to assess .pgm files to the specifications of the assignment</p>
 */
public class PgmValidator extends FileValidator{

    /*
     * Class Members
     */
    ArrayList<File> imageList = new ArrayList<>();
    HashMap<Integer, Integer> classFrequency = new HashMap<>();
    HashMap<Integer, ArrayList<File>> imagesByClass = new HashMap<>();
    String fileName;
    File inputFile;
    Scanner scan;
    boolean isValid;
    boolean listCreated;
    static final String expectedFormat = "P2";
    String imageExtension = ".pgm";
    int numOfClasses = 0;

    /**
     * <b>PgmValidator - Default constructor</b>
     * <p>Simply sets isValid to false, does nothing else.</p>
     * <p>Requires use of setters to update any values.</p>
     */
    public PgmValidator() {
        isValid = false;
    }

    /**
     * <b>PgmValidator - Constructor</b>
     * <p>When created and provided a string that represents a file name, this object will create an ArrayList of File objects.</p>
     * <p>These file objects will then be checked for validity for the purposes of this program</p>
     * @param fileName A string that represents a .txt file which we will parse through to get our image files
     * @throws FileNotFoundException if the file is not found
     * @throws InvalidFileException if a parent class method fails
     * @throws PgmException if a pgm file is found to be invalid
     * @throws ExtensionException if a file is found to not be a .pgm file
     */
    public PgmValidator(String fileName) throws FileNotFoundException, InvalidFileException, PgmException, ExtensionException {
        this.fileName = fileName;
        //Path filePath = FileSystems.getDefault().getPath(fileName);
        //this.inputFile = filePath.toFile();
        this.inputFile = new File(this.fileName).getAbsoluteFile();
    }

    /**
     * <b>Method - validate</b>
     * <p>Executes the rest of the methods to check if the pgm files are valid or not</p>
    * @throws ExtensionException Thrown by ExtensionChecker object in createList()
    * @throws PgmException Thrown in many cases throughout the validity checking process
    * @throws InvalidFileException Thrown from parent class
    * @throws FileNotFoundException Potentially thrown by scanner object
    */
    public void validate() throws FileNotFoundException, InvalidFileException, PgmException, ExtensionException {
        if(exists(this.inputFile) && notEmpty(this.inputFile)) {
            this.listCreated = createList();
        }
        if(listCreated) {
            isValid = checkImageListContents();
        }
        this.numOfClasses = classFrequency.size();
    }

    /**
     * <b>Method - createList</b>
     * <p>This method fills out our ArrayList of Files from the contents of the .txt file</p>
     * @return true iff the list is successfully populated
     * @throws FileNotFoundException thrown if the file in question is not found
     * @throws PgmException Potentially thrown if a file in the text file is found to not be a .pgm file, or if the file is too short to be a valid file name
     * @throws ExtensionException an ExtensionChecker object is created in this method
     */
    private boolean createList() throws FileNotFoundException, PgmException, ExtensionException {
        scan = new Scanner(this.inputFile);
        ExtensionChecker pgmChecker = new ExtensionChecker(imageExtension);
        while(scan.hasNextLine()) {
            String lineFromFile = scan.nextLine().trim();
            //Path filePath = FileSystems.getDefault().getPath(lineFromFile);
            if(lineFromFile.length() == 0) {
                continue;
            }
            try{
                if(pgmChecker.checkExtension(lineFromFile)){
                    //File outerFilePath = new File(inputFile.getParent());
                    //this.imageList.add(new File(outerFilePath.getParent() + "/" + lineFromFile).getAbsoluteFile());
                    this.imageList.add(new File(lineFromFile).getAbsoluteFile());
                    //this.imageList.add(filePath.toFile());
                }else {
                    scan.close();
                    throw new PgmException("Error - the filename " + lineFromFile + " is not a .pgm file. Please supply a text file containing only images of the file type .pgm");
                }
            } catch(IndexOutOfBoundsException e) {
                scan.close();
                throw new PgmException("Error - the file name " + lineFromFile + " is not long enough to be a valid .pgm image file");
            }
        }
        scan.close();
        return true;
    }

    /**
     * <b>method - checkImageListContents</b>
     * <p>This is a flow control method - this will call all of the validation methods for our image files on each file</p>
     * @return true iff all files contained in the .txt are valid pgm files. Validity is explained at each of the "check" methods
     * @throws PgmException potentially thrown from each "check" method
     * @throws FileNotFoundException 
     * @throws InvalidFileException 
     */
    private boolean checkImageListContents() throws PgmException, FileNotFoundException, InvalidFileException {
        for(File image : imageList) {
            if(exists(image) && notEmpty(image)) {
                checkName(image);
                checkFormat(image);
                checkOnlyContainsInts(image);
                checkDims(image);
            }
        }
        return true;
    }

    @Override
    protected boolean exists(File image) throws InvalidFileException, FileNotFoundException {
        return super.exists(image);
    }
    @Override
    protected boolean notEmpty(File image) throws InvalidFileException, FileNotFoundException {
        return super.notEmpty(image);
    }
    /**
     * <b>Method - checkName</b>
     * <p>A Valid image file has a particular naming convention: "classX_#.pgm",</p>
     * <p>where X is an integer that determines what class the image belongs to</p>
     * <p>and # is an integer that allows for creating unique file names</p>
     * @param image File to check for validity
     * @throws PgmException thrown if the file is found to be invalid in name, format, or content
     */
    private void checkName(File image) throws PgmException {
        String imageName = image.getName();
        String expectedName = "class";
        final int minimum_length_without_extension = 8;
        if(imageName.length()-4 < minimum_length_without_extension){
            throw new PgmException("Error - the file name " + imageName + " is not named appropriately (the name is too short). Please name .pgm files in the format: \"classX_#.pgm\"");
        }
        if(!imageName.substring(0,5).equals(expectedName)){
            throw new PgmException("Error - the file name " + imageName + " is not formatted correctly - it should begin with \"class\". Please name .pgm files in the format: \"classX_#.pgm\"");
        }
        String classRemoved = imageName.substring(5);
        String classAndExtensionRemoved = classRemoved.substring(0,classRemoved.length()-4);
        String[] classAndUniqueValues = classAndExtensionRemoved.split("_");
        if(classAndUniqueValues.length != 2) {
            throw new PgmException("Error - the file name " + imageName + " is not formatted correctly, and either has too many or too few underscores. Please name .pgm files in the format: \"classX_#.pgm\"");
        }
        verifyIntsInName(classAndUniqueValues, image);
    }

    /**
     * <b>Method - verifyIntsInName</b>
     * <p>This method verifies that the values that should be integers (X and # out of the format classX_#.pgm)</p>
     * <p>are, and are within the range (generally speaking, >0)</p>
     * @param classAndUniqueValues An array of Strings which should correspond to two the two numbers in a correctly formatted filename
     * @param imageName The name of the file being processed. Used for error reporting
     * @throws PgmException Thrown if either value in classAndUniqueValues is not an integer greater than 0.
     */
    private void verifyIntsInName(String[] classAndUniqueValues, File image) throws PgmException {
        try{
            int classValue = Integer.parseInt(classAndUniqueValues[0]);
            int uniqueValue = Integer.parseInt(classAndUniqueValues[1]);
            if(classValue < 0) {
                throw new PgmException("Error - the class value for the file " + image.getName() + " should be a positive integer, but was found to be " + classValue);
            }
            if(uniqueValue < 0) {
                throw new PgmException("Error - the unique file identifier value for the file " + image.getName() + " should be a positive integer, but was found to be " + uniqueValue);
            }
            if(classFrequency.containsKey(classValue)){
                classFrequency.put(classValue, classFrequency.get(classValue)+1);
            }else{
                classFrequency.put(classValue, 1);
            }
            if(imagesByClass.containsKey(classValue)) {
                imagesByClass.get(classValue).add(image);
            }else {
                imagesByClass.put(classValue, new ArrayList<File>());
                imagesByClass.get(classValue).add(image);
            }
        } catch(NumberFormatException e) {
            throw new PgmException("Error - the file name " + image.getName() + " is not formatted correctly, and does not contain appropriate integer values for its class or unique identifier. Please name .pgm files in the format: \"classX_#.pgm\"");
        }
    }

    /**
     * <b>Method - checkFormat</b>
     * <p>This method checks that the format value (the first value in the file) is P2, as expected</p>
     * @param image The image file to check
     * @throws PgmException thrown if the value is not P2
     * @throws FileNotFoundException potentially thrown by scanner object
     */
    private void checkFormat(File image) throws PgmException, FileNotFoundException{
        scan = new Scanner(image);
        if(!scan.next().equals(expectedFormat)){
            scan.close();
            throw new PgmException("Error - the expected format value of P2 was not found in file: " + image.getName());
        }
    }

    /**
     * <b>Method - checkOnlyContainsInts</b>
     * <p>This method checks a .pgm file and verifies that after its "P2" Header value, it only contains integer values from 0-255</p>
     * @param image The file to check
     * @throws FileNotFoundException Thrown if the file does not exist.
     * @throws PgmException Thrown if a non-integer value is found, or a value outside of the 0-255 acceptable range is found.
     */
    private void checkOnlyContainsInts(File image) throws FileNotFoundException, PgmException {
        scan = new Scanner(image);
        scan.next();
        int x;
        while(scan.hasNext()) {
            try{
                x = Integer.parseInt(scan.next());
                if(x < 0 || x > 255) {
                    scan.close();
                    throw new PgmException("Error - an integer value is outside of the 0-255 range in " + image.getName());
                }
            } catch (NumberFormatException e) {
                scan.close();
                throw new PgmException("Error - the file" + image.getName() + " contains a non-integer value");
            }
        }
        scan.close();
    }

    /**
     * <b>Method - checkDims</b>
     * <p>This method checks all of the values after the "P2" header value</p>
     * <p>The second value should be the width: an integer from 0-255</p>
     * <p>The third value should be the height: an integer from 0-255</p>
     * <p>The third value should be the maximum pixel value: an integer from 0-255 (in our case, this will be 255)</p>
     * <p>This method also checks that the number of entries after this header section is equal to width x height</p>
     * @param image The file to check
     * @throws FileNotFoundException potentially thrown by Scanner object
     * @throws PgmException Thrown if a value is outside of the correct range, or otherwise unacceptable
     */
    private void checkDims(File image) throws FileNotFoundException, PgmException {
        scan = new Scanner(image);
        scan.next();
        int width = scan.nextInt();
        if(width < 0 || width > 255) {
            throw new PgmException("Error - the width in the file " + image.getName() + " of " + width + " is not within the 0-255 range.");
        }
        int height = scan.nextInt();
        if(height < 0 || height > 255) {
            throw new PgmException("Error - the height in the file " + image.getName() + " of " + height + " is not within the 0-255 range.");
        }
        int dimensions = width * height;
        int pixel = scan.nextInt();
        if(pixel < 0 || pixel > 255) {
            throw new PgmException("Error - the Pixel value in the file " + image.getName() + " of " + pixel + " is not within the 0-255 range.");
        }
        int count = 0;
        while(scan.hasNext()) {
            scan.next();
            count++;
        }
        if(count != dimensions) {
            throw new PgmException("Error - the file " + image.getName() + " does not have the expected dimensions of " + dimensions);
        }
    }

    /**
     * <b>Getter Method - getClass</b>
     * <p>This method gets the "class" indicator from the name of a file and returns it</p>
     * @param image the file to extract the class from
     * @return class of the given file
     */
    public Integer getClass(File image) {
        String classValue = image.getName();
        classValue = classValue.substring(5, classValue.length()-4);
        String[] numbersInClassValue = classValue.split("_");
        return Integer.parseInt(numbersInClassValue[0]);
    }

}

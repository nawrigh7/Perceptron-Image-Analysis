import java.io.*;
import java.util.Scanner;
/**
 * <b>Class - FileValidator</b>
 * <p>This class is used to validate files for use in this program</p>
 * <p>This is a parent class. The child classes are:</p>
 *  <ul>
 *      <li>TxtValidator</li>
 *      <li>PgmValidator</li>
 *  </ul>
 */
public class FileValidator {

    /*
     * Class Members
     */
    String fileName;
    File inputFile;
    Scanner scan;
    boolean isValid;

    /**
     * <b>Constructor - Default</b>
     * <p>This is the default constructor, and does not do much</p>
     */
    public FileValidator() {
        this.fileName = "";
        this.isValid = false;
    }

    /**
     * <b>Constructor - FileValidator</b>
     * <p>This constructor creates a generic FileValidator</p>
     * <p>This object can be used to store a text file, and automatically checks that it exists and is non-empty</p>
     * @param fileName Name of the file to store
     * @throws FileNotFoundException if the file does not exist in the given path
     * @throws InvalidFileException thrown if the file does not exist or is empty
     */
    public FileValidator(String fileName) throws FileNotFoundException, InvalidFileException {
        this.fileName = fileName;
        this.inputFile = new File(this.fileName).getAbsoluteFile();
        if(exists(this.inputFile) && notEmpty(this.inputFile)) {
            this.isValid = true;
        }

    }

    /**
     * <b>Method - exists()</b>
     * <p>This method determines if a supplied file exists or not, and returns true if so.</p>
     * <p>Otherwise, it throws an exception</p>
     * 
     * @return boolean value, true if file exists
     * @param fileToTest the file we are checking if exists or not
     * @throws InvalidFileException error to throw if the file does not exist
     * @throws FileNotFoundException if a file is not found
     */
    protected boolean exists(File fileToTest) throws InvalidFileException, FileNotFoundException{
        if(fileToTest.exists()) {
            return true;
        }else {
            throw new InvalidFileException("Error - the filename (" + fileToTest.getPath() + ") supplied at the command line does not exist.");
        }
    }

    /**
     * <b>Method - empty()</b>
     * <p>This method determines if a file is empty or not, and returns true if it is not empty.</p>
     * <p>Otherwise, it throws an exception</p>
     * 
     * @return true iff the file is not empty
     * @param fileToTest the file we are checking if it is empty or not
     * @throws InvalidFileException Exception to throw
     * @throws FileNotFoundException Thrown if file is not found
     */
    protected boolean notEmpty(File fileToTest) throws InvalidFileException, FileNotFoundException{
        scan = new Scanner(fileToTest);
        if(scan.hasNext()) {
            scan.close();
            return true;
        }else {
            scan.close();
            throw new InvalidFileException("Error - the supplied file was found to be empty");
        }
    }
}

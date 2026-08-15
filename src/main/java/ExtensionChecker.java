
/**
 * <b>Class - ExtensionChecker</b>
 * <p>This class is used to check file extensions against a provided expected extension</p>
 */
public class ExtensionChecker {

    /*
     * Class members
     */
    final String expectedExtension;

    /**
     * <b>Constructor - ExtensionChecker</b>
     * <p>Takes a String representing the expected extension to match</p>
     * @param expectedExtension Expected extension. If you want to check for .txt files, this would be ".txt"
     */
    public ExtensionChecker(String expectedExtension) {
        this.expectedExtension = expectedExtension;
    }

    /**
     * <b>Method - checkExtension</b>
     * <p>For ease of use, this takes an entire string, and only checks the last 4 values of the string (i.e., the extension)</p>
     * <p>If the string is less than 4 values, it throws an error. Otherwise, it returns false to allow for use in decision statements</p>
     * @param extensionToCheck String to check the extension of. This does <b>not</b> need to be in ".xxx" form - a full filename is acceptable
     * @return True iff the extension of the supplied String matches what is expected
     * @throws ExtensionException Thrown if the String supplied is too short to be a valid file type.
     */
    public boolean checkExtension(String extensionToCheck) throws ExtensionException {
        try{
            if(extensionToCheck.substring(extensionToCheck.length()-4, extensionToCheck.length()).compareTo(expectedExtension) == 0){
                return true;
            }else {
                return false;
            }
        }catch(IndexOutOfBoundsException e) {
            throw new ExtensionException("Error - the supplied string (" + extensionToCheck + ") is too short to possibly be a valid file.");
        }
    }

}

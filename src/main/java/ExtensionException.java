/**
 * <b>Exception - Used for error handling regarding file Extensions</b>
 */
public class ExtensionException extends Exception{
    /**
     * <b>Exception - ExtensionException</b>
     * <p>This is thrown if a provided file is not of the correct type (i.e., the file extensions are not correct)</p>
     * @param message Message to print to the user
     */
    public ExtensionException(String message) {System.err.println(message);}
}

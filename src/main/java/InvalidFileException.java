
/**
 * <b>Exception - FileValidator</b>
 * <p>This exception is used for error handling in the FileValidator class</p>
 */
public class InvalidFileException extends Exception{
    /**
     * <p>Calling this method will simply print the supplied message to System.err</p>
     * @param message The Message to print
     */
    public InvalidFileException(String message) {
        System.err.println(message);
    }
}

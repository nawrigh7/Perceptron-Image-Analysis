/**
 * <b>Exception - PgmValidator</b>
 * <p>This exception is used for error handling in the PgmValidator class</p>
 */
public class PgmException extends Exception{
    /**
     * <p>Calling this method will simply print the supplied message to System.err</p>
     * @param message The Message to print
     */
    public PgmException(String message) {
        System.err.println(message);
    }
}
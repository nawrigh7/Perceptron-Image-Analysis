
/**
 * Exception for Histogram class Error Handling
 */
public class InvalidHistogramException extends Exception{
    /**
     * Exception for Histogram class Error Handling
     * @param message Message to print to the user
     */
    public InvalidHistogramException(String message) {
        System.err.println(message);
    }
}
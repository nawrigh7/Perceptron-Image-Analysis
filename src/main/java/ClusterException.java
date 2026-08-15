/**
 * <b>Exception - Used for Cluster exception handling</b>
 */
public class ClusterException extends Exception{
    /**
     * <b>Exception - ClusterException</b>
     * <p>Used for error handling in the Cluster class</p>
     * @param message Message to print to the user
     */
    public ClusterException(String message) {System.err.println(message);}
}
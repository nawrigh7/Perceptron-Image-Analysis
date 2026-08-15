public class ArgsException extends Exception{
    public ArgsException(String message) {
        System.err.println("Error: " + message);
    }
}

package ben;

/**
 * Custom exception class for Ben chatbot-specific errors.
 */
public class BenException extends Exception{

    /**
     * Constructs a new BenException with the specified error message.
     *
     * @param message the detail message explaining the error condition
     */
    public BenException(String message) {
        super(message);
    }
}

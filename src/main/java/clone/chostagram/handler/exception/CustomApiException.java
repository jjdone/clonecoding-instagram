package clone.chostagram.handler.exception;

public class CustomApiException extends RuntimeException{

    public CustomApiException(String message) {
        super(message);
    }
}

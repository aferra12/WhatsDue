//THIS CODE WAS TAKEN FROM WORK WE DID IN LECTURE
package exceptions;

public class CrudException extends RuntimeException {
    public CrudException(String message, Throwable cause) {
        super(message, cause);
    }
}

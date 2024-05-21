package server.exceptions;

public class UserNotOwnerOfObject extends RuntimeException {
    public UserNotOwnerOfObject(String message) {
        super(message);
    }
}
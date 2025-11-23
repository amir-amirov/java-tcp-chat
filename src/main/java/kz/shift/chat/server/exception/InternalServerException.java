package kz.shift.chat.server.exception;

public class InternalServerException extends ChatException {
    public InternalServerException() {
        super("Internal server error");
    }
}

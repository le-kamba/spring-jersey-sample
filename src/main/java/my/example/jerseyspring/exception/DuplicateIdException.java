package my.example.jerseyspring.exception;

public class DuplicateIdException extends RuntimeException {
    public DuplicateIdException() {
        super("そのIDのEmployeeはすでに登録されています。");
    }
}

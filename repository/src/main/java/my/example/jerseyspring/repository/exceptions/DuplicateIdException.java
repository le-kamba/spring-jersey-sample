package my.example.jerseyspring.repository.exceptions;

public class DuplicateIdException extends RuntimeException {
    public DuplicateIdException() {
        super("そのIDのEmployeeはすでに登録されています。");
    }
}

package my.example.jerseyspring.repository.exceptions;

public class EmployeeNotFoundException extends RuntimeException {
    public EmployeeNotFoundException() {
        super("そのIDのEmployeeは見つかりません。");
    }
}

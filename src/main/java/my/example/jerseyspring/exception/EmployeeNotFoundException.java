package my.example.jerseyspring.exception;

public class EmployeeNotFoundException extends RuntimeException {
    public EmployeeNotFoundException() {
        super("そのIDのEmployeeは見つかりません。");
    }
}

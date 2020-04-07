package my.example.jerseyspring.repository.exceptions;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class EmployeeNotFoundExceptionTest {

    @Test
    void getMessage() {
        EmployeeNotFoundException e = new EmployeeNotFoundException();
        assertThat(e.getMessage()).isEqualTo("そのIDのEmployeeは見つかりません。");
    }
}
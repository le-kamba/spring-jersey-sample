package my.example.jerseyspring.repository.exceptions;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class EmployeeNameNotFoundExceptionTest {

    @Test
    void getMessage() {
        EmployeeNameNotFoundException e = new EmployeeNameNotFoundException("name");
        assertThat(e.getMessage()).isEqualTo("文字列{name}を含む名前のEmployeeは存在しません。");
    }
}
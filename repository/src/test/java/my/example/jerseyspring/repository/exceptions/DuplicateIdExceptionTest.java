package my.example.jerseyspring.repository.exceptions;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class DuplicateIdExceptionTest {

    @Test
    public void getMessage() {
        DuplicateIdException e = new DuplicateIdException();
        assertThat(e.getMessage()).isEqualTo("そのIDのEmployeeはすでに登録されています。");
    }
}
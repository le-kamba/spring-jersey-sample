package my.example.jerseyspring.rest;

import my.example.jerseyspring.repository.EmployeeRepository;
import my.example.jerseyspring.repository.models.Employee;
import my.example.jerseyspring.rest.handler.DuplicateExceptionHandler;
import my.example.jerseyspring.rest.handler.NameNotFoundExceptionHandler;
import my.example.jerseyspring.rest.handler.NotFoundExceptionHandler;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.test.JerseyTest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@ContextConfiguration("classpath:/applicationContext.xml")
public class EmployeeServiceTest extends JerseyTest {

    @Override
    protected Application configure() {
        return new ResourceConfig(EmployeeService.class)
                .register(DuplicateExceptionHandler.class)
                .register(NameNotFoundExceptionHandler.class)
                .register(NotFoundExceptionHandler.class);
    }

    @BeforeEach
    @Override
    public void setUp() throws Exception {
        super.setUp();
    }

    @AfterEach
    @Override
    public void tearDown() throws Exception {
        super.tearDown();
    }

    @ParameterizedTest
    @ValueSource(strings = {MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public void getAll(String mediaType) {
        final Response response = target("/employees/all").request().accept(mediaType).get();
        assertThat(response.getHeaderString("Content-Type"))
                .isEqualTo(mediaType);

        List<Employee> content = response.readEntity(new GenericType<>() {
        });
        assertThat(content.size()).isEqualTo(5);
        assertThat(content.get(0)).isEqualToComparingFieldByField(new Employee(3, "Cupcake"));
        assertThat(content.get(1)).isEqualToComparingFieldByField(new Employee(4, "Donuts"));
        assertThat(content.get(2)).isEqualToComparingFieldByField(new Employee(5, "Eclair"));
        assertThat(content.get(3)).isEqualToComparingFieldByField(new Employee(8, "Froyo"));
        assertThat(content.get(4)).isEqualToComparingFieldByField(new Employee(9, "Gingerbread"));
    }

    @ParameterizedTest
    @MethodSource("getParamProvider")
    public void getEmployee(int id, String mediaType) {
        String urlPath = String.format("/employees/%d", id);
        final Response response = target(urlPath).request().accept(mediaType).get();
        assertThat(response.getHeaderString("Content-Type"))
                .isEqualTo(mediaType);

        Employee employee = response.readEntity(Employee.class);
        Employee expect = EmployeeRepository.getInstance().select(id);
        assertThat(employee).isEqualToComparingFieldByField(expect);
    }

    static Stream<Arguments> getParamProvider() {
        return Stream.of(
                Arguments.of(3, MediaType.APPLICATION_JSON),
                Arguments.of(4, MediaType.APPLICATION_JSON),
                Arguments.of(5, MediaType.APPLICATION_JSON),
                Arguments.of(8, MediaType.APPLICATION_JSON),
                Arguments.of(9, MediaType.APPLICATION_JSON),
                Arguments.of(3, MediaType.APPLICATION_XML),
                Arguments.of(4, MediaType.APPLICATION_XML),
                Arguments.of(5, MediaType.APPLICATION_XML),
                Arguments.of(8, MediaType.APPLICATION_XML),
                Arguments.of(9, MediaType.APPLICATION_XML)
        );
    }

    @ParameterizedTest
    @ValueSource(strings = {MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public void searchEmployee(String mediaType) {
        final Response response = target("/employees/search")
                .queryParam("name", "a")
                .request()
                .accept(mediaType)
                .get();
        assertThat(response.getHeaderString("Content-Type"))
                .isEqualTo(mediaType);

        List<Employee> content = response.readEntity(new GenericType<>() {
        });
        assertThat(content.size()).isEqualTo(3);
        assertThat(content.get(0)).isEqualToComparingFieldByField(new Employee(3, "Cupcake"));
        assertThat(content.get(1)).isEqualToComparingFieldByField(new Employee(5, "Eclair"));
        assertThat(content.get(2)).isEqualToComparingFieldByField(new Employee(9, "Gingerbread"));
    }

    @ParameterizedTest
    @MethodSource("postRawProvider")
    public void addEmployee(int id, String bodyRaw, String mediaType) {

        final Response response = target("/employees").request()
                .post(Entity.entity(bodyRaw, mediaType));
        assertThat(response.getStatus()).isEqualTo(201);
        assertThat(response.getHeaderString("Location"))
                .isEqualTo("http://localhost:9998/employees/" + id);
    }

    static Stream<Arguments> postRawProvider() {
        final String json = "{\"firstName\":\"Honeycomb\",\"id\":11}";
        final String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" +
                "<employee><firstName>KitKat</firstName><id>19</id></employee>";
        return Stream.of(
                Arguments.of(11, json, MediaType.APPLICATION_JSON),
                Arguments.of(19, xml, MediaType.APPLICATION_XML)
        );
    }

    @ParameterizedTest
    @MethodSource("putRawProvider")
    public void updateEmployee(int id, String bodyRaw, String mediaType) {
        final Response response = target("/employees").request()
                .put(Entity.entity(bodyRaw, mediaType));
        assertThat(response.getStatus()).isEqualTo(200);

        Employee employee = target("/employees/" + id).request().get(Employee.class);
        Employee expected = EmployeeRepository.getInstance().select(id);
        assertThat(employee).isEqualToComparingFieldByField(expected);
    }

    static Stream<Arguments> putRawProvider() {
        final String json = "{\"firstName\":\"Frozen yogurt\",\"id\":8}";
        final String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" +
                "<employee><firstName>Cup Cake</firstName><id>3</id></employee>";
        return Stream.of(
                Arguments.of(8, json, MediaType.APPLICATION_JSON),
                Arguments.of(3, xml, MediaType.APPLICATION_XML)
        );
    }

    @Test
    public void deleteEmployee() {
        final Response response = target("/employees/9")
                .request().delete();
        assertThat(response.getStatus()).isEqualTo(204);
    }

    @Test
    public void exception_selectEmployee() {
        final Response response = target("/employees/1").request().get();
        assertThat(response.getStatus()).isEqualTo(404);
    }

    @Test
    public void exception_searchEmployee() {
        final Response response = target("/employees/search?name=android").request().get();
        assertThat(response.getStatus()).isEqualTo(404);
    }

    @ParameterizedTest
    @MethodSource("putRawProvider")
    public void exception_addEmployee(int id, String bodyRaw, String mediaType) {
        final Response response = target("/employees").request()
                .post(Entity.entity(bodyRaw, mediaType));
        assertThat(response.getStatus()).isEqualTo(409);
    }

    @ParameterizedTest
    @MethodSource("putExceptionProvider")
    public void exception_updateEmployee(int id, String bodyRaw, String mediaType) {
        final Response response = target("/employees").request()
                .put(Entity.entity(bodyRaw, mediaType));
        assertThat(response.getStatus()).isEqualTo(404);
    }

    static Stream<Arguments> putExceptionProvider() {
        final String json = "{\"firstName\":\"Lollipop\",\"id\":21}";
        final String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" +
                "<employee><firstName>Jelly Bean</firstName><id>17</id></employee>";
        return Stream.of(
                Arguments.of(21, json, MediaType.APPLICATION_JSON),
                Arguments.of(3, xml, MediaType.APPLICATION_XML)
        );
    }

    @Test
    public void exception_deleteEmployee() {
        final Response response = target("/employees/1").request().get();
        assertThat(response.getStatus()).isEqualTo(404);
    }
}
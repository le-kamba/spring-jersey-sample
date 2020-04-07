package my.example.jerseyspring.rest;

import my.example.jerseyspring.repository.EmployeeRepository;
import my.example.jerseyspring.repository.models.Employee;
import org.springframework.stereotype.Service;

import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.util.List;

@Service
@Path("/employees")
public class EmployeeService {

    final EmployeeRepository employeeRepository;

    public EmployeeService(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    @Context
    UriInfo uriInfo;

    @GET
    @Path("/all")
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public List<Employee> getAll() {
        return employeeRepository.selectAll();
    }

    @GET
    @Path("/{id}")
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Employee getEmployee(@PathParam("id") int id) {
        return employeeRepository.select(id);
    }

    @GET
    @Path("/search")
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public List<Employee> searchEmployee(@QueryParam("name") String name) {
        return employeeRepository.search(name);
    }

    @POST
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response addEmployee(Employee employee) {
        employeeRepository.insert(employee.getId(), employee.getFirstName());

        UriBuilder builder = uriInfo.getAbsolutePathBuilder();
        builder.path(String.valueOf(employee.getId()));
        return Response.created(builder.build()).build();
    }

    @PUT
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response updateEmployee(Employee employee) {
        employeeRepository.update(employee.getId(), employee.getFirstName());
        // 新規作成した場合はcreatedを返す必要があるが、このサンプルではエラーとするため、常にokを返す
        return Response.ok().build();
    }

    @DELETE
    @Path("/{id}")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response deleteEmployee(@PathParam("id") int id) {
        employeeRepository.delete(id);
        // Entityの状態を返す場合はokを返す。
        // 受け付けたが処理が終わっていない場合は(キューに乗っただけなど)acceptedを返す
        // このサンプルでは削除が完了して該当コンテントがなくなったことだけ返す
        return Response.noContent().build();
    }
}

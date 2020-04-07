package my.example.jerseyspring.rest.handler;


import my.example.jerseyspring.repository.exceptions.EmployeeNameNotFoundException;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class NameNotFoundExceptionHandler implements ExceptionMapper<EmployeeNameNotFoundException> {

    public Response toResponse(EmployeeNameNotFoundException ex) {
        return Response.status(Response.Status.NOT_FOUND).build();
    }
}


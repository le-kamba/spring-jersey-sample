package my.example.jerseyspring.rest;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

import org.springframework.stereotype.Component;
import my.example.jerseyspring.transaction.TransactionBo;

@Component
@Path("/payment")
public class PaymentService {

    final TransactionBo transactionBo;

    public PaymentService(TransactionBo transactionBo) {
        this.transactionBo = transactionBo;
    }

    @GET
    @Path("/mkyong")
    public Response savePayment() {
        String result = transactionBo.save();
        return Response.status(200).entity(result).build();
    }
}
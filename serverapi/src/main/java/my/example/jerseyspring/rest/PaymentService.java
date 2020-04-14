package my.example.jerseyspring.rest;

import my.example.jerseyspring.repository.transaction.TransactionBo;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

@Service
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
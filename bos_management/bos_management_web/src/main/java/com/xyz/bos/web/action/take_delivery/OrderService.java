package com.xyz.bos.web.action.take_delivery;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import com.xyz.bos.domain.take_delivery.Order;

@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public interface OrderService {

    @POST//增
    @Path("/saveOrder")
    void saveOrder(Order order);//保存的接口
}

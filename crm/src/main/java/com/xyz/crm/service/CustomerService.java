package com.xyz.crm.service;
import java.util.List;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import com.xyz.crm.domain.Customer;

@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public interface CustomerService {
    @GET
    @Path("/findAll")
    List<Customer> findAll();
 // 注册用户接口
    @POST//@POST增 @DELETE删 @PUT 改@GET查
    @Path("/save")//访问路径
    void save(Customer customer);//接口方法和参数
    // 改type为0为删除标识
    @PUT// @POST增 @DELETE删 @PUT改 @GET查
    @Path("/active")//访问路径
    void active(@QueryParam("telephone")String telephone);//接口方法和参数
 // 检查用户是否激活
    @GET
    @Path("/isActived")
    Customer isActived(@QueryParam("telephone") String telephone);

    // 登录
    @GET
    @Path("/login")
    Customer login(@QueryParam("telephone") String telephone,@QueryParam("password") String password);
    
    // 查询未关联定区的客户 的接口    
    @GET//@POST增 @DELETE删 @PUT改 @GET查
    @Path("/findCustomersUnAssociated")//访问地址
    List<Customer> findCustomersUnAssociated();//返回的是个list

    // 查询已关联到指定定区的客户
    @GET
    @Path("/findCustomersAssociated2FixedArea")
    List<Customer> findCustomersAssociated2FixedArea(@QueryParam("fixedAreaId") String fixedAreaId);

    // 定区ID,要关联的数据
    // 根据定区ID,把关联到这个定区的所有客户全部解绑
    // 要关联的数据和定区Id进行绑定
    @PUT//改
    @Path("/assignCustomers2FixedArea")
    void assignCustomers2FixedArea(//传两参数
	    @QueryParam("customerIds")Long[]customerIds,
	    @QueryParam("fixedAreaId")String fixedAreaId);
    
    //根据地址查询定区ID
    @GET    
    @Path("/findFixedAreaIdByAdddress")
    String findFixedAreaIdByAdddress(@QueryParam("address") String address);//传客户里填的地址过去
    
    
    
}

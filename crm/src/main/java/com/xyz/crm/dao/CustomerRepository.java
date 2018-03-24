package com.xyz.crm.dao;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import com.xyz.crm.domain.Customer;

public interface CustomerRepository extends JpaRepository<Customer, Long> {
    // 标准格式查询 未关联定区的客户
    List<Customer> findByFixedAreaIdIsNull();

    // 查询已关联到指定定区的客户
    List<Customer> findByFixedAreaId(String fixedAreaId);
    

    // 把关联到指定定区的客户id 进行解绑操作
    @Query("update Customer set fixedAreaId = null where fixedAreaId = ?")
    @Modifying
    void unbindCustomerByFixedArea(String fixedAreaId);

    // 把客户绑定到指定的定区
    @Query("update Customer set fixedAreaId = ?2 where id = ?1")//写数字确定顺序
    @Modifying
    void bindCustomer2FixedArea(Long customerId, String fixedAreaId);
    

    // 激活
    @Query("update Customer set type = 1 where telephone = ?")
    @Modifying
    void active(String telephone);
     
    //根据地址查询 定区ID
    @Query("select fixedAreaId from Customer where address=?")//自定义语句
    String findFixedAreaIdByAdddress(String address);
    
  
    
    // 查看用户是否激活
    Customer findByTelephone(String telephone);

    // 登录
    Customer findByTelephoneAndPassword(String telephone, String password);
    
    
}


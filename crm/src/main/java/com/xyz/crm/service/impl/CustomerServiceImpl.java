package com.xyz.crm.service.impl;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.xyz.crm.dao.CustomerRepository;
import com.xyz.crm.domain.Customer;
import com.xyz.crm.service.CustomerService;

@Service
@Transactional
public class CustomerServiceImpl implements CustomerService {

    @Autowired
    private CustomerRepository customerRepository;

    @Override
    public List<Customer> findAll() {

        return customerRepository.findAll();
    }

    // 查询未关联定区的客户
    @Override
    public List<Customer> findCustomersUnAssociated() {
        return customerRepository.findByFixedAreaIdIsNull();
    }

    // 查询已关联到指定定区的客户
    @Override
    public List<Customer> findCustomersAssociated2FixedArea(String fixedAreaId) {
        return customerRepository.findByFixedAreaId(fixedAreaId);
    }

  

	@Override
	public void save(Customer customer) {
		customerRepository.save(customer);
	}

	@Override
	public void active(String telephone) {
		customerRepository.active(telephone);
	}

    @Override
    public Customer isActived(String telephone) {

        return customerRepository.findByTelephone(telephone);
    }

    @Override
    public Customer login(String telephone, String password) {

        return customerRepository.findByTelephoneAndPassword(telephone,
                password);
    }

	@Override//先全部解绑 再把右边的进行关联
	public void assignCustomers2FixedArea(Long[] customerIds, String fixedAreaId) {
		if (StringUtils.isNotEmpty(fixedAreaId) ) {//如果传过来的定区id不为空
			customerRepository.unbindCustomerByFixedArea(fixedAreaId);//就解绑所有关联这个定区的所有 客户
		}
		if (customerIds!=null&&fixedAreaId.length()>0) {//如果客户数组不为Null并且长度大于0(有数据)
			for (Long customerId : customerIds) {//遍历客户
				customerRepository.bindCustomer2FixedArea(customerId, fixedAreaId);//要关联客户和定区关联
			}
		}
	}
}
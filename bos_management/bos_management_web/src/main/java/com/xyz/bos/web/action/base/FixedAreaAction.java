package com.xyz.bos.web.action.base;

import java.io.IOException;
import java.util.List;

import javax.ws.rs.core.MediaType;

import org.apache.cxf.jaxrs.client.WebClient;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;

import com.xyz.bos.domain.base.FixedArea;
import com.xyz.bos.service.base.FixedAreaService;
import com.xyz.bos.web.action.CommonAction;
import com.xyz.crm.domain.Customer;

import net.sf.json.JsonConfig;
@Namespace("/")
@ParentPackage("struts-default")
@Controller
@Scope("prototype")
public class FixedAreaAction extends CommonAction<FixedArea> {
	public FixedAreaAction() {
		super(FixedArea.class);
	}
	//增加定区
	@Autowired
private FixedAreaService fixedAreaService;
	@Action(value="fixedAreaAction_save",results={
			@Result(location="/pages/base/fixed_area.html",name="success",type="redirect")
	})
	public String save(){
		fixedAreaService.save(getModel());
		return SUCCESS;
	}
	//分页展示
	@Action(value="fixedAreaAction_pageQuery")
	public String pageQuery() throws IOException{
		Pageable pageable = new PageRequest(page-1, rows);
		Page<FixedArea>page=fixedAreaService.findAll(pageable);
		JsonConfig jsonConfig = new JsonConfig();
		jsonConfig.setExcludes(new String[]{"subareas","couriers"});
		page2json(page, jsonConfig);
		return NONE;
	}
	
	
    // 向CRM系统发起请求,查询未关联定区的客户
    @Action(value = "fixedAreaAction_findUnAssociatedCustomers")
    public String findUnAssociatedCustomers() throws IOException {
        List<Customer> list = (List<Customer>) WebClient.create(//调用跨域方法 返回的用list接收
            "http://localhost:8082/crm/webservice/customerService/findCustomersUnAssociated")
            .type(MediaType.APPLICATION_JSON)//指定json
            .accept(MediaType.APPLICATION_JSON)
            .getCollection(Customer.class);//传客户实体字节码文件过去
        list2json(list, null);//list转json
        return NONE;
    }

    
    // 向CRM系统发起请求,查询已关联指定定区的客户
    @Action(value = "fixedAreaAction_findAssociatedCustomers")
    public String findAssociatedCustomers() throws IOException {

        List<Customer> list = (List<Customer>) WebClient.create(
                "http://localhost:8082/crm/webservice/customerService/findCustomersAssociated2FixedArea")
                .query("fixedAreaId", getModel().getId())
                .type(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .getCollection(Customer.class);
        list2json(list, null);
        return NONE;
    }
    
    
    private Long[]customerIds;//属性驱动获取要关联定区 的客户id数组
    public void setCustomerIds(Long[] customerIds) {
		this.customerIds = customerIds;	}
    
    // 向CRM系统发起请求,关联客户
    @Action(value = "fixedAreaAction_assignCustomers2FixedArea",results={
			@Result(location="/pages/base/fixed_area.html",name="success",type="redirect")
	})
    public String assignCustomers2FixedArea() throws IOException {
    	WebClient.create(
    	"http://localhost:8082/crm/webservice/customerService/assignCustomers2FixedArea")
        .query("fixedAreaId", getModel().getId())//传过去fixedAreaId从模型驱动获得的的id
        .query("customerIds",customerIds)//传过去属性驱动customerIds数组id
        .type(MediaType.APPLICATION_JSON)
        .accept(MediaType.APPLICATION_JSON)
        .put(null);
    	return SUCCESS;
    }
    
    private long[]subAreaIds;//使用属性驱动获得前端分区的隐藏ID
    public void setSubAreaIds(long[] subAreaIds) {
		this.subAreaIds = subAreaIds;	}
    // 关联分区 方法
    @Action(value = "fixedAreaAction_assignSubAreas2FixedArea",results={
    		@Result(location="/pages/base/fixed_area.html",name="success",type="redirect")
    })
    public String assignSubAreas2FixedArea() throws IOException {
     fixedAreaService.assignSubAreas2FixedArea(getModel().getId(),subAreaIds);
    	return SUCCESS;
    }
    
    private Long courierId;//属性驱动获取前端name=的值 快速员id
    private Long takeTimeId;//时段id
    public void setCourierId(Long courierId) {
		this.courierId = courierId;	}
    public void setTakeTimeId(Long takeTimeId) {
		this.takeTimeId = takeTimeId;	}
    
    // 关联快递员
    @Action(value = "fixedAreaAction_associationCourierToFixedArea",results={
    		@Result(location="/pages/base/fixed_area.html",name="success",type="redirect")
    })
    public String associationCourierToFixedArea() throws IOException {
    	fixedAreaService.associationCourierToFixedArea(getModel().getId(),courierId,takeTimeId);
    	
    	return SUCCESS;
    }

    

}

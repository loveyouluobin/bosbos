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


}

package com.xyz.bos.fore.web.action; //包名后一定加.action

import javax.ws.rs.core.MediaType;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.cxf.jaxrs.client.WebClient;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.xyz.bos.utils.SmsUtils;
import com.xyz.crm.domain.Customer;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;
@Namespace("/")
@ParentPackage("struts-default")
@Controller
@Scope("prototype")
public class CustomerAction extends ActionSupport implements ModelDriven<Customer> {
private Customer model=new Customer();
    @Override
    public Customer getModel() {
        return model;    }
    
    @Action(value="customerAction_sendSMS")//发短信
    public String sendSMS() throws Exception{
       String code = RandomStringUtils.randomNumeric(6);//随机生成6位验证码
       ServletActionContext.getRequest().getSession().setAttribute("serverCode", code);//存到session
       SmsUtils.sendSms(model.getTelephone(), code);//调utils类发短信
       System.out.println(code);
        return NONE;
    }
    
    
    public String regist() throws Exception{
        String serverCode = (String) ServletActionContext.getRequest().getSession().getAttribute("serverCode");//取session
        if (StringUtils.isNotEmpty(checkcode)&&StringUtils.isNotEmpty(serverCode)&&serverCode.equals(checkcode)) {//不为空并相等
            WebClient.create("http://localhost:81/crm/webService/customerService/save")
            .type(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON).post(model);//调crm的方法 传model过去 
            return SUCCESS;
        }
        return ERROR;
    }
}
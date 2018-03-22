package com.xyz.bos.fore.web.action; //包名后一定加.action

import java.util.concurrent.TimeUnit;

import javax.ws.rs.core.MediaType;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.cxf.jaxrs.client.WebClient;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;

import com.aliyuncs.exceptions.ClientException;



import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;
import com.xyz.bos.utils.MailUtils;
import com.xyz.bos.utils.SmsUtils;
import com.xyz.crm.domain.Customer;
@Namespace("/")
@ParentPackage("struts-default")
@Controller
@Scope("prototype")
public class CustomerAction extends ActionSupport implements ModelDriven<Customer> {
private Customer model=new Customer();//模型驱动Customer客户实体
    @Override
    public Customer getModel() {
        return model;    }
    
    @Action(value="customerAction_sendSMS")//发短信的方法
    public String sendSMS() throws Exception{
       String code = RandomStringUtils.randomNumeric(6);//随机生成6位验证码//org.apache.commons.lang3.RandomStringUtils
       ServletActionContext.getRequest().getSession().setAttribute("serverCode", code);//存到session为验证用
       SmsUtils.sendSms(model.getTelephone(), code);//调utils类发短信 传参数 模型驱动里从前端获得的手机号和 生成的验证码
       System.out.println(code);
        return NONE;
    }
    
    private String checkcode;//属性驱动获取前端用户输入的验证码 (实体无此字段就用属性驱动提供set)
    public void setCheckcode(String checkcode) {
		this.checkcode = checkcode;	}
    
    @Autowired//注入redisTemplate
	private RedisTemplate<String, String> redisTemplate;
    
    @Action(value = "customerAction_regist",results = {
    		@Result(name = "success", location = "/signup-success.html",type = "redirect"),
    		@Result(name = "error", location = "/signup-fail.html",type = "redirect")
        })
    public String regist() throws Exception{//注册的方法
        String serverCode = (String) ServletActionContext.getRequest().getSession().getAttribute("serverCode");//取session发送验证码时存的验证码
		if (StringUtils.isNotEmpty(checkcode)&&StringUtils.isNotEmpty(serverCode)&&serverCode.equals(checkcode)) {//输出的和发出的验证码不为空并相等
            WebClient.create("http://localhost:8082/crm/webservice/customerService/save")//调跨域crm里的save方法:主机名:端口号/项目名/web.xml文件中指定的路径/applicationContext.xml文件中指定的路径/接口上使用@Path注解指定的地址/方法上使用@Path注解指定的地址
            .type(MediaType.APPLICATION_JSON)//是json数据
            .accept(MediaType.APPLICATION_JSON).post(model);//调crm的方法 传model过去 注册
            
            String activeCode = RandomStringUtils.randomNumeric(30);//生成邮箱验证码
            redisTemplate.opsForValue().set(model.getTelephone(), activeCode, 1,TimeUnit.DAYS);//存到redis 有效期一天
String emailBody = 
"感谢您注册本网站的帐号，请在24小时之内点击<a href='http://localhost:8280/portal/customerAction_active.action?activeCode="+ activeCode + "&telephone=" + model.getTelephone()+ "'>本链接</a>激活您的帐号";
			MailUtils.sendMail(model.getEmail(), "请激活您的帐号", emailBody);//发送邮件
            return SUCCESS;//成功就返回SUCCESS
        }
        return ERROR;
    }
    
 
    private String activeCode;// 使用属性驱动获取链接里的 激活码
    public void setActiveCode(String activeCode) {
        this.activeCode = activeCode;    }
    @Action(value = "customerAction_active",//激活帐号的方法
            results = {
                    @Result(name = "success", location = "/login.html",
                            type = "redirect"),
                    @Result(name = "error", location = "/signup-fail.html",
                            type = "redirect")})
    public String active() {
  String serverCode =redisTemplate.opsForValue().get(model.getTelephone());//从redis拿到name为手机号的随机数
        if (StringUtils.isNotEmpty(serverCode)&& StringUtils.isNotEmpty(activeCode)&& serverCode.equals(activeCode)) {//如果激活码和存在redis一样
         WebClient.create(//就调激活的方法accept
                    "http://http://localhost:8082/crm/webService/customerService/active")
                    .type(MediaType.APPLICATION_JSON)
                    .query("telephone", model.getTelephone())
                    .accept(MediaType.APPLICATION_JSON).put(null);
            return SUCCESS;
        }
        return ERROR;
    }
    
   
	
}
package com.xyz.bos.fore.web.action; //包名后一定加.action

import java.util.concurrent.TimeUnit;

import javax.jms.JMSException;
import javax.jms.MapMessage;
import javax.jms.Message;
import javax.jms.Session;
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
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
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
    
    @Autowired //注入jms消息模板
    private JmsTemplate jmsTemplate;
    
    @Action(value="customerAction_sendSMS")//发短信的方法
    public String sendSMS() throws Exception{
       final String code = RandomStringUtils.randomNumeric(6);//随机生成6位验证码//org.apache.commons.lang3.RandomStringUtils
       ServletActionContext.getRequest().getSession().setAttribute("serverCode", code);//存到session为验证用
       //由消息中间件来发送手机验证码
       jmsTemplate.send("sms",new MessageCreator() {
		@Override
		public Message createMessage(Session session) throws JMSException {
			MapMessage message=session.createMapMessage();//消息map对像集合
			message.setString("tel",model.getTelephone());//手机号
			message.setString("code", code);//生成的验证码
			return message;//返回消息对象
		}
	});
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
"感谢您注册本网站的帐号，请在24小时之内点击<a href='http://localhost:8083/portal/customerAction_active.action?activeCode="+ activeCode + "&telephone=" + model.getTelephone()+ "'>本链接</a>激活您的帐号";
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
                    "http://localhost:8082/crm/webservice/customerService/active")
                    .type(MediaType.APPLICATION_JSON)
                    .query("telephone", model.getTelephone())
                    .accept(MediaType.APPLICATION_JSON).put(null);
            return SUCCESS;
        }
        return ERROR;
    }
    
@Action(value = "customerAction_login",results = {//登陆方法
@Result(name = "success", location = "/index.html",type = "redirect"),
@Result(name = "error", location = "/login.html",type = "redirect"),
@Result(name = "unactived", location = "/login.html",type = "redirect")})
    public String login() {
   String serverCode = (String) ServletActionContext.getRequest().getSession().getAttribute("validateCode");//拿到session
if (StringUtils.isNotEmpty(serverCode)&& StringUtils.isNotEmpty(checkcode)&& serverCode.equals(checkcode)) {//session里验证码和登陆页面属性驱动activeCode获得的一致
  Customer customer = WebClient.create(// 校验用户是否激活 发查询请求
        "http://localhost:8082/crm/webservice/customerService/isActived")
        .type(MediaType.APPLICATION_JSON)
        .query("telephone", model.getTelephone())
        .accept(MediaType.APPLICATION_JSON).get(Customer.class);
if (customer != null && customer.getType() != null) {//对象不为空 对象的删除状态也不为空
    if (customer.getType() == 1) {// 这个用户对象的 type==1就是激活了
        Customer c = WebClient.create(// 就请求登录
        "http://localhost:8082/crm/webservice/customerService/login")
        .type(MediaType.APPLICATION_JSON)
        .query("telephone", model.getTelephone())
        .query("password", model.getPassword())
        .accept(MediaType.APPLICATION_JSON)
        .get(Customer.class);
        if (c != null) {//请求的用户对象不为空 就把对象存到session
       ServletActionContext.getRequest().getSession().setAttribute("user", c);
            return SUCCESS;//成功
        } else {
          return ERROR;//失败
        }
    } else {// 用户已经注册成功，但是没有激活
        return "unactived";
      }
   }
 }
    return ERROR;
}

   
	
}
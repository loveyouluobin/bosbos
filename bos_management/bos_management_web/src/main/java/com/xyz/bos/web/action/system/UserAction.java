package com.xyz.bos.web.action.system;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.xyz.bos.domain.system.User;
import com.xyz.bos.service.system.UserService;
import com.xyz.bos.web.action.CommonAction;
@Namespace("/") // 访问这个Action路径
@ParentPackage("struts-default") //继承struts属性
@Controller // spring 的注解,控制层代码
@Scope("prototype")//多实现
public class UserAction extends CommonAction<User> {
	public UserAction() {
		super(User.class);	}

private String checkcode;//用户输入的验证码
public void setCheckcode(String checkcode) {
	this.checkcode = checkcode;}

@Action(value="userAction_login",results={
		@Result(name="success",location="/index.html",type="redirect"),
		@Result(name="login",location="/login.html",type="redirect")
})
public String login(){
	String serverCode = (String) ServletActionContext.getRequest().getSession().getAttribute("key");//服务器生成的验证码
	if (StringUtils.isNotEmpty(serverCode)&&StringUtils.isNotEmpty(checkcode)&&serverCode.equals(checkcode)) {//如果两者都存在并相等
		Subject subject = SecurityUtils.getSubject();//shiro静态方法获得 当前用户权限主体
		AuthenticationToken token= new UsernamePasswordToken(getModel().getUsername(),getModel().getPassword());//授权令牌
		try {
			subject.login(token);//执行登陆 传给token
			User user = (User) subject.getPrincipal();//返回的user对象
			ServletActionContext.getRequest().getSession().setAttribute("user", user);//存到session
			return SUCCESS;//成功则跳转到后台首页
		} catch (UnknownAccountException e) {
			e.printStackTrace();
			System.out.println("用户名写错");
		}catch (IncorrectCredentialsException e) {
			e.printStackTrace();
			System.out.println("密码错误");
		}catch (Exception e) {
			e.printStackTrace();
			System.out.println("其它错误");
		}
	}
	return LOGIN;//失败返回重新登陆
}
}

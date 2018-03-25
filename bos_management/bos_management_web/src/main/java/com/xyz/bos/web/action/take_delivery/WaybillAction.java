package com.xyz.bos.web.action.take_delivery;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.opensymphony.xwork2.ActionSupport;
import com.xyz.bos.domain.take_delivery.WayBill;
import com.xyz.bos.service.take_delivery.WaybillService;
import com.xyz.bos.web.action.CommonAction;

@Namespace("/") // 访问这个Action目录
@ParentPackage("struts-default") //继承struts属性
@Controller // spring 的注解,控制层代码
@Scope("prototype")//多实现
public class WaybillAction extends CommonAction<WayBill>  {
	public WaybillAction() {
		super(WayBill.class);	}
	@Autowired
private WaybillService waybillService;
	
	@Action(value="waybillAction_save")//保存快速运单录入到数据库
	public String save() throws IOException {
		String msg="0";//标记位为0 给前台判断
		try {
			waybillService.save(getModel());
		} catch (Exception e) {
			e.printStackTrace();
			 msg="1";//有异常设为1
		}
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setContentType("text/html;charset=UTF-8");
		response.getWriter().write(msg);//输出msg流到前台 显示
		return NONE;
	}
	
	
}

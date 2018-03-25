package com.xyz.bos.fore.web.action;

import javax.ws.rs.core.MediaType;

import org.apache.commons.lang3.StringUtils;
import org.apache.cxf.jaxrs.client.WebClient;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;
import com.xyz.bos.domain.base.Area;
import com.xyz.bos.domain.take_delivery.Order;
import com.xyz.crm.domain.Customer;
@Namespace("/")
@ParentPackage("struts-default")
@Controller
@Scope("prototype")
public class OrderAction extends ActionSupport implements ModelDriven<Order> {
private Order model=new Order();
	@Override
	public Order getModel() {
		return model;
	}
	
private String recAreaInfo;//模型驱动接收前台 发收人name="省/市/区"
private String sendAreaInfo;//模型驱动接收前台收件人name="省/市/区的"
public void setRecAreaInfo(String recAreaInfo) {
	this.recAreaInfo = recAreaInfo;}
public void setSendAreaInfo(String sendAreaInfo) {
	this.sendAreaInfo = sendAreaInfo;}
	
@Action(value = "orderAction_saveOrder",results = {//提交订单的方法
	@Result(name = "success", location = "/index.html",type = "redirect"),})
 public String saveOrder() {
	if (StringUtils.isNotEmpty(sendAreaInfo)) {//判断接收的不为空 北京市/北京市/西城区
		String[] split = sendAreaInfo.split("/");//以斜杆切割成数组
		String province = split[0];//取第一个元素得到省 北京市
		String city = split[1];//只取第2元素得到市 北京市
		String district = split[2];//只保留3元素得到区 西城区
		 province = province.substring(0, province.length()-1);//去掉后面的省字
		city=city.substring(0,city.length()-1);//去掉后面的市字
		district=district.substring(0,district.length()-1);//去掉后面的区字
		Area area = new Area();//封闭成一个区域对象Area把这省市区设置进去
		area.setProvince(province);
		area.setCity(city);
		area.setDistrict(district);
		model.setSendArea(area);//设置到model是Order对象里的SendArea发件人地址
	}
	if (StringUtils.isNotEmpty(recAreaInfo)) {
        String[] split = recAreaInfo.split("/");
        String province = split[0];
        String city = split[1];
        String district = split[2];
        province = province.substring(0, province.length() - 1);
        city = city.substring(0, city.length() - 1);
        district = district.substring(0, district.length() - 1);
        Area area = new Area();
        area.setProvince(province);
        area.setCity(city);
        area.setDistrict(district);
        model.setRecArea(area);//设置到model是Order对象里的RecArea收件人地址
    }
	WebClient.create("http://localhost:8080/bos_management_web/webService/orderService/saveOrder")
	.type(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON).post(model);
	return SUCCESS;//带Order订单对象过去
}
	

}

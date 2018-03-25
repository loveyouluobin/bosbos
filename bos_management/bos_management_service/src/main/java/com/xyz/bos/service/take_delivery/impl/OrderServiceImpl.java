package com.xyz.bos.service.take_delivery.impl;
import java.io.IOException;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import javax.ws.rs.core.MediaType;

import org.apache.commons.lang3.StringUtils;
import org.apache.cxf.jaxrs.client.WebClient;
import org.apache.struts2.convention.annotation.Action;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ctc.wstx.util.StringUtil;
import com.xyz.bos.dao.base.AreaRepository;
import com.xyz.bos.dao.base.FixedAreaRepository;
import com.xyz.bos.dao.take_delivery.OrderRepsitory;
import com.xyz.bos.dao.take_delivery.WorkBillRepository;
import com.xyz.bos.domain.base.Area;
import com.xyz.bos.domain.base.Courier;
import com.xyz.bos.domain.base.FixedArea;
import com.xyz.bos.domain.base.SubArea;
import com.xyz.bos.domain.take_delivery.Order;
import com.xyz.bos.domain.take_delivery.WorkBill;
import com.xyz.bos.service.take_delivery.OrderService;
import com.xyz.crm.domain.Customer;
@Transactional
@Service
public class OrderServiceImpl implements OrderService {
	@Autowired//注入订单dao接口
	private OrderRepsitory orderRepsitory;
	@Autowired//注入区域dao接口
	private AreaRepository areaRepository;
	@Autowired//注入定区dao接口
	private FixedAreaRepository fixedAreaRepository;
	@Autowired//注入工单dao接口
	private WorkBillRepository workBillRepository;
	@Override
	
	public void saveOrder(Order order) {//保存订单
		// 把瞬时态的Area转换为持久态的Area
		Area sendArea = order.getSendArea();//获得订单实体里的发送人地址 
		if (sendArea!=null) {//如果获得对象 成功
			//查询数据库中区域 成持久化对象findByProvinceAndCityAndDistrict
		Area sendAreaDB=areaRepository.findByProvinceAndCityAndDistrict(sendArea.getProvince(),sendArea.getCity(),sendArea.getDistrict());
		order.setSendArea(sendAreaDB);//把发送人地址设置进去
		}
		Area recArearea=order.getRecArea();//获得订单实体里的收送人地址 
		if (recArearea!=null) {
		Area recAreaDB=areaRepository.findByProvinceAndCityAndDistrict(recArearea.getProvince(), recArearea.getCity(), recArearea.getDistrict());
			order.setRecArea(recAreaDB);//把发送人地址设置进去
		}
		
		order.setOrderNum(UUID.randomUUID().toString().replace("-", ""));//设置订单号并把-替换掉
		order.setOrderTime(new Date());//设置日期 
		orderRepsitory.save(order);//保存订单
		
		//保存后,根据发件地址完全匹配:让crm系统根据发件地址查询定区ID根据定区ID查询定区并查询出快递员,安排快递员上门
	  String sendAddress = order.getSendAddress();//从订单里获得客户输入的地址
	  if (StringUtils.isNotEmpty(sendAddress)) {//如果不为空让crm去查
		 String fixedAreaId=WebClient.create(//调用跨域方法 返回定区id
	            "http://localhost:8082/crm/webservice/customerService/findFixedAreaIdByAdddress")
	            .type(MediaType.APPLICATION_JSON)//指定json
	            .query("address", sendAddress)//传订单里客户输入的地址过去
	            .accept(MediaType.APPLICATION_JSON).get(String.class);//要查的类型字节码
		 if (StringUtils.isNotEmpty(fixedAreaId)) {//如果查询出的定区id不为空
		FixedArea fixedArea=fixedAreaRepository.findOne(Long.parseLong(fixedAreaId));//就根据定区ID查询定区
		if (fixedArea!=null) {//定区对象不为空
			  Set<Courier>couriers = fixedArea.getCouriers();//查询出这个定区所有快递员
		if (!couriers.isEmpty()) {//如果快递员不为空
			Iterator<Courier> iterator = couriers.iterator();//迭代集合
			Courier courier = iterator.next();//迭代到下一个//可根据快递员的上班时间/收派能力/忙闲程度
			order.setCourier(courier);//把快递员设置进订单 指派快递员
			WorkBill workBill = new WorkBill();//生成工单
			workBill.setAttachbilltimes(0);
            workBill.setBuildtime(new Date());
            workBill.setCourier(courier);
            workBill.setOrder(order);
            workBill.setPickstate("新单");
            workBill.setRemark(order.getRemark());
            workBill.setSmsNumber("111");
            workBill.setType("新");
            
            workBillRepository.save(workBill);//保存个工单
            // 发送短信,推送一个通知
             order.setOrderType("自动分单");//设置订单分单方式 为自动分单
		     return;// 中断代码的执行
				}	  
			}
		}else {// 定区关联分区,在页面上填写的发件地址,必须是对应的分区的关键字或者辅助关键字包含输入的
			 Area sendArea2 = order.getSendArea();
             if (sendArea2 != null) {
                 Set<SubArea> subareas = sendArea2.getSubareas();
                 for (SubArea subArea : subareas) {
                     String keyWords = subArea.getKeyWords();
                     String assistKeyWords = subArea.getAssistKeyWords();
                     if (sendAddress.contains(keyWords)|| sendAddress.contains(assistKeyWords)) {
                         FixedArea fixedArea2 = subArea.getFixedArea();

                         if (fixedArea2 != null) {
                             // 查询快递员
                             Set<Courier> couriers =
                                     fixedArea2.getCouriers();
                             if (!couriers.isEmpty()) {
                                 Iterator<Courier> iterator =
                                         couriers.iterator();
                                 Courier courier = iterator.next();
                                 // 指派快递员
                                 order.setCourier(courier);
                                 // 生成工单
                                 WorkBill workBill = new WorkBill();
                                 workBill.setAttachbilltimes(0);
                                 workBill.setBuildtime(new Date());
                                 workBill.setCourier(courier);
                                 workBill.setOrder(order);
                                 workBill.setPickstate("新单");
                                 workBill.setRemark(order.getRemark());
                                 workBill.setSmsNumber("111");
                                 workBill.setType("新");

                                 workBillRepository.save(workBill);
                                 // 发送短信,推送一个通知
                                 // 中断代码的执行
                                 order.setOrderType("自动分单");
				                    return;
				                    }
				                   }
				               }
							}
				        }
				  }
				}
                 order.setOrderType("人工分单");//其它情况为人工
			}
		}

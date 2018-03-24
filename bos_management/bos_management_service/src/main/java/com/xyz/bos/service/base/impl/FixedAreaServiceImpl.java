package com.xyz.bos.service.base.impl;

import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.xyz.bos.dao.base.CourierRepository;
import com.xyz.bos.dao.base.FixedAreaRepository;
import com.xyz.bos.dao.base.SubAreaRepository;
import com.xyz.bos.dao.base.TakeTimeRepository;
import com.xyz.bos.domain.base.Courier;
import com.xyz.bos.domain.base.FixedArea;
import com.xyz.bos.domain.base.SubArea;
import com.xyz.bos.domain.base.TakeTime;
import com.xyz.bos.service.base.FixedAreaService;
@Service
@Transactional
public class FixedAreaServiceImpl implements FixedAreaService {
	@Autowired
 private FixedAreaRepository fixedAreaRepository;
	@Override
	public void save(FixedArea model) {
	fixedAreaRepository.save(model);
		
	}
	@Override
	public Page<FixedArea> findAll(Pageable pageable) {
		return fixedAreaRepository.findAll(pageable);
	}
	@Autowired //把客户和时间的 的接口注入进来
	private CourierRepository CourierRepository;
	@Autowired
	private TakeTimeRepository takeTimeRepository;
	@Override
	public void associationCourierToFixedArea(Long fixedAreaId, Long courierId, Long takeTimeId) {
		//代码执行成功 快递员会发生update 快递员和定区多对多关系的中间表会发生insert
		FixedArea fixedArea = fixedAreaRepository.findOne(fixedAreaId);//把定区id根据模型驱动把id查出来
		Courier courier = CourierRepository.findOne(courierId);//把客户id根据属性驱动从数据库中查出来
		TakeTime takeTime = takeTimeRepository.findOne(takeTimeId);//把时段id根据属性驱动从数据库中查出来
		courier.setTakeTime(takeTime);//建立快递员和时间关联 把快递员里面的时间字段设置进去
		//courier.getFixedAreas().add(fixedArea);//把快递员中的定区增加到 定区 courier实体类这个字段放弃维护
		fixedArea.getCouriers().add(courier);//用定区里的快递员 加到快递员里
	}
	
	@Autowired//注入分区的dao接口
	private SubAreaRepository subAreaRepository;
	@Override//关联分区到指定 的定区
	public void assignSubAreas2FixedArea(Long fixedAreaId, long[] subAreaIds) {
		FixedArea fixedArea = fixedAreaRepository.findOne(fixedAreaId);//以id查这个定区
		Set<SubArea> subareas = fixedArea.getSubareas();//获得当前定区所有分区 
		for (SubArea subArea : subareas) {//遍历
			subArea.setFixedArea(null);//全部解绑
		}
		for (Long subAreaId : subAreaIds) {//遍历选择右边的分区 绑定到定区
			SubArea subArea = subAreaRepository.findOne(subAreaId);//以id查这个定区
			subArea.setFixedArea(fixedArea);//以这个定区 设置进右边所有分区 
		}
	}

}

package com.xyz.bos.service.take_delivery.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.xyz.bos.dao.take_delivery.WaybillRepository;
import com.xyz.bos.domain.take_delivery.WayBill;
import com.xyz.bos.service.take_delivery.WaybillService;
@Service
@Transactional
public class WaybillServiceImpl implements WaybillService {
	@Autowired
 private WaybillRepository waybillRepository;

	@Override
	public void save(WayBill model) {
waybillRepository.save(model);		
	}
	

}

package com.xyz.bos.service.base.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.xyz.bos.dao.base.TakeTimeRepository;
import com.xyz.bos.domain.base.TakeTime;
import com.xyz.bos.service.base.TakeTimeService;
@Transactional
@Service
public class TakeTimeServiceImpl implements TakeTimeService {

	@Autowired
	private TakeTimeRepository takeTimeRepository;
	@Override
	public List<TakeTime> findAll() {
		return takeTimeRepository.findAll();
	}

}

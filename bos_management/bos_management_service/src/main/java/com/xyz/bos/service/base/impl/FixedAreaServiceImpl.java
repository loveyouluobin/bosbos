package com.xyz.bos.service.base.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.xyz.bos.dao.base.FixedAreaRepository;
import com.xyz.bos.domain.base.FixedArea;
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

}

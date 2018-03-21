package com.xyz.bos.service.base.impl;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.xyz.bos.dao.base.StandardRepository;
import com.xyz.bos.domain.base.Standard;
import com.xyz.bos.service.base.StandardService;

@Transactional
@Service
public class StandardServiceImpl implements StandardService {
@Autowired
	private StandardRepository standrdRepository;
	@Override
	public void save(Standard model) {
		standrdRepository.save(model);
	}
	@Override
	public Page<Standard> findAll(Pageable pageable) {
		// TODO Auto-generated method stub
		return standrdRepository.findAll(pageable);
	}
	

}

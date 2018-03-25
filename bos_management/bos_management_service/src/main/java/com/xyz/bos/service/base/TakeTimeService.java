package com.xyz.bos.service.base;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.xyz.bos.dao.base.StandardRepository;
import com.xyz.bos.dao.base.TakeTimeRepository;
import com.xyz.bos.domain.base.TakeTime;


public interface TakeTimeService {

	List<TakeTime> findAll();
	
}

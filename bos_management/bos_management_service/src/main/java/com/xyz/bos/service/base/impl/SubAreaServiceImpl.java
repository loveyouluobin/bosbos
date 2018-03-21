package com.xyz.bos.service.base.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.xyz.bos.dao.base.SubAreaRepository;
import com.xyz.bos.domain.base.FixedArea;
import com.xyz.bos.domain.base.SubArchive;
import com.xyz.bos.domain.base.SubArea;
import com.xyz.bos.service.base.SubAreaService;

/**
 * ClassName:SubAreaServiceImpl <br/>
 * Function: <br/>
 * Date: 2018年3月16日 上午9:40:39 <br/>
 */
@Service
@Transactional

public class SubAreaServiceImpl implements SubAreaService {

    @Autowired
    private SubAreaRepository subAreaRepository;


    @Override
    public Page<SubArea> findAll(Pageable pageable) {

        return subAreaRepository.findAll(pageable);
    }


	@Override
	public void save(SubArea model) {
		subAreaRepository.save(model);
	}









   
}

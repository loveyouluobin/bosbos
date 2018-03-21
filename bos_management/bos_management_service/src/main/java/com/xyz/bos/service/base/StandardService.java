package com.xyz.bos.service.base;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.xyz.bos.domain.base.Standard;

public interface StandardService {

	void save(Standard model);

	Page<Standard> findAll(Pageable pageable);


}

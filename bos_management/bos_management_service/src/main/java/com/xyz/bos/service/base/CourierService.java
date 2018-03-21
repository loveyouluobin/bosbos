package com.xyz.bos.service.base;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import com.xyz.bos.domain.base.Courier;

public interface CourierService {

	void save(Courier model);

	Page<Courier> findAll(Specification<Courier> specification, Pageable pageable);

	void batchDel(String ids);

	List<Courier> findAvaible();

	Page<Courier> findAll(Pageable pageable);

}

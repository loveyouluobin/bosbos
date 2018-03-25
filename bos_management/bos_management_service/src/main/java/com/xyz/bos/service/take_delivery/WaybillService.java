package com.xyz.bos.service.take_delivery;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import com.xyz.bos.domain.base.Courier;
import com.xyz.bos.domain.take_delivery.WayBill;

public interface WaybillService {

	void save(WayBill model);

	

}

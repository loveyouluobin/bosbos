package com.xyz.bos.dao.take_delivery;

import org.springframework.data.jpa.repository.JpaRepository;

import com.xyz.bos.domain.take_delivery.Order;
import com.xyz.bos.domain.take_delivery.WayBill;

public interface WaybillRepository extends JpaRepository<WayBill, Long> {

}

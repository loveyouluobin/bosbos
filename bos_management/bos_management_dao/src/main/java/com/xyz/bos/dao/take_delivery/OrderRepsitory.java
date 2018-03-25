package com.xyz.bos.dao.take_delivery;

import org.springframework.data.jpa.repository.JpaRepository;

import com.xyz.bos.domain.take_delivery.Order;

public interface OrderRepsitory extends JpaRepository<Order, Long> {

}

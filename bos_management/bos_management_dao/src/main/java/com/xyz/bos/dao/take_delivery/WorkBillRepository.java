package com.xyz.bos.dao.take_delivery;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.xyz.bos.domain.base.Area;
import com.xyz.bos.domain.take_delivery.WorkBill;

/**
 * ClassName:AreaRepository <br/>
 * Function: <br/>
 * Date: 2018年3月15日 上午11:31:03 <br/>
 */
public interface WorkBillRepository extends JpaRepository<WorkBill, Long> {

}

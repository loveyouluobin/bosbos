package com.xyz.bos.dao.base;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.xyz.bos.domain.base.Area;
import com.xyz.bos.domain.base.TakeTime;

/**
 * ClassName:AreaRepository <br/>
 * Function: <br/>
 * Date: 2018年3月15日 上午11:31:03 <br/>
 */
public interface TakeTimeRepository extends JpaRepository<TakeTime, Long> {

}

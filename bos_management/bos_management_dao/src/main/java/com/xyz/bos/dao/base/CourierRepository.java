package com.xyz.bos.dao.base;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.xyz.bos.domain.base.Courier;
import com.xyz.bos.domain.base.Standard;

public interface CourierRepository extends JpaRepository<Courier, Long>,JpaSpecificationExecutor<Courier> {
	 // 根据ID更改删除的标志位
    @Modifying
    @Query("update Courier set deltag = 1 where id = ?")
    void updateDelTagById(Long id);

    List<Courier> findByDeltagIsNull();
}

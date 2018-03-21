package com.xyz.bos.service.base.impl;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.xyz.bos.dao.base.CourierRepository;
import com.xyz.bos.domain.base.Courier;
import com.xyz.bos.service.base.CourierService;
@Service
@Transactional
public class CourierServiceImpl implements CourierService {

    @Autowired
    private CourierRepository courierRepository;

    // 保存
    @Override
    public void save(Courier courier) {

        courierRepository.save(courier);
    }

    // 无条件的分页查询
    @Override
    public Page<Courier> findAll(Pageable pageable) {
        return courierRepository.findAll(pageable);
    }

    // 批量删除
    @Override
    public void batchDel(String ids) {
        // 真实开发中只有逻辑删除
        // null " "
        // 判断数据是否为空
        if (StringUtils.isNotEmpty(ids)) {
            // 切割数据
            String[] split = ids.split(",");
            for (String id : split) {
                courierRepository.updateDelTagById(Long.parseLong(id));
            }
        }

    }

    // 带有条件的分页查询
    @Override
    public Page<Courier> findAll(Specification<Courier> specification,
            Pageable pageable) {
        return courierRepository.findAll(specification, pageable);
    }

    @Override
    public List<Courier> findAvaible() {

        return courierRepository.findByDeltagIsNull();
    }
}
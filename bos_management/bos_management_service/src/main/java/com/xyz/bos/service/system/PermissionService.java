package com.xyz.bos.service.system;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.xyz.bos.domain.system.Permission;

public interface PermissionService {

	Page<Permission> findAll(Pageable pageable);

	void save(Permission model);

}

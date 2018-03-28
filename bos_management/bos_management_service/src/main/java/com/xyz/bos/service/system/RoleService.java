package com.xyz.bos.service.system;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.xyz.bos.domain.system.Permission;
import com.xyz.bos.domain.system.Role;

public interface RoleService {

	Page<Role> findAll(Pageable pageable);

	void save(Role model, String menuTds, Long[] permissionIds);

}

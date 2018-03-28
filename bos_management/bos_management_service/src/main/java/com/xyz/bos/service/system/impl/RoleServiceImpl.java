package com.xyz.bos.service.system.impl;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.xyz.bos.dao.system.MenuRepository;
import com.xyz.bos.dao.system.PermissionRepository;
import com.xyz.bos.dao.system.RoleRepository;
import com.xyz.bos.domain.system.Menu;
import com.xyz.bos.domain.system.Permission;
import com.xyz.bos.domain.system.Role;
import com.xyz.bos.service.system.RoleService;

import groovyjarjarasm.asm.commons.Method;
@Service
@Transactional
public class RoleServiceImpl implements RoleService {
	
 @Autowired
 private RoleRepository roleRepository;
 
	@Override
	public Page<Role> findAll(Pageable pageable) {
		return roleRepository.findAll(pageable);
	}

	@Autowired
	private MenuRepository menuRepository;//菜单dao接口
	@Autowired
	private PermissionRepository permissionRepository;//权限dao接口
	
	@Override
	public void save(Role role, String menuTds, Long[] permissionIds) {
		roleRepository.save(role);//保存角色
		Method(role,menuTds,permissionIds);//调用方法用角色 去关联菜单和权限
	}

	private void Method(Role role, String menuTds, Long[] permissionIds) {//使用脱管态关联
		if (StringUtils.isNotEmpty(menuTds)) {
			String[] split = menuTds.split(",");
			for (String menuId : split) {
				Menu menu = new Menu();
				menu.setId(Long.parseLong(menuId));//菜单设置id
				role.getMenus().add(menu);//把角色里的菜单加入菜单
			}
		}
		if (permissionIds!=null&&permissionIds.length>0) {
			for (Long permissionId : permissionIds) {
				Permission permission = new Permission();
				permission.setId(permissionId);
				role.getPermissions().add(permission);
			}
		}
	}
	private void method1(Role role, String menuIds, Long[] permissionIds) {//查询持久态对象并关联
		if (StringUtils.isNotEmpty(menuIds)) {
			String[] split = menuIds.split(",");
			for (String menuId : split) {
				// 持久态的菜单
				Menu menu = menuRepository.findOne(Long.parseLong(menuId));
				role.getMenus().add(menu);
			}
		}
		if (permissionIds != null && permissionIds.length > 0) {
			for (Long permissionId : permissionIds) {
				// 持久态的权限
				Permission permission = permissionRepository.findOne(permissionId);
				role.getPermissions().add(permission);
			}
		}
	}



}

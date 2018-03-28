package com.xyz.bos.service.system.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.xyz.bos.dao.system.UserRepository;
import com.xyz.bos.domain.system.Role;
import com.xyz.bos.domain.system.User;
import com.xyz.bos.service.system.UserService;
@Transactional
@Service
public class UserServiceImpl implements UserService {
	@Autowired
private UserRepository userRepository;
	
	@Override
	public void save(User user, Long[] roleIds) {
		userRepository.save(user);
		if (roleIds!=null&&roleIds.length>0) {
			for (Long roleId : roleIds) {
				Role role = new Role();
				role.setId(roleId);
				user.getRoles().add(role);
			}
		}
	}

	@Override
	public Page<User> findAll(Pageable pageable) {
		return	userRepository.findAll(pageable);
	}

}

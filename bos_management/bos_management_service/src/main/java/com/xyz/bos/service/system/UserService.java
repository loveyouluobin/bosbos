package com.xyz.bos.service.system;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.xyz.bos.domain.system.User;

public interface UserService {

	void save(User model, Long[] roleIds);

	Page<User> findAll(Pageable pageable);

}

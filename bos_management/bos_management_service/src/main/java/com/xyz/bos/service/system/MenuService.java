package com.xyz.bos.service.system;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.xyz.bos.domain.system.Menu;
import com.xyz.bos.domain.system.User;

public interface MenuService {

	Page<Menu> findAll(Pageable pageable);

	List<Menu> findLevelOne();

	void save(Menu model);

	List<Menu> findbyUser(User user);



}

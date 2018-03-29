package com.xyz.bos.service.system.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.xyz.bos.dao.system.MenuRepository;
import com.xyz.bos.domain.system.Menu;
import com.xyz.bos.domain.system.User;
import com.xyz.bos.service.system.MenuService;
@Service
@Transactional
public class MenuServiceImpl implements MenuService {
@Autowired
	private MenuRepository menuRepository;

@Override
public Page<Menu> findAll(Pageable pageable) {
	return menuRepository.findAll(pageable);
}

@Override
public List<Menu> findLevelOne() {
	return menuRepository.findByParentMenuIsNull();
}

public void save(Menu model) {
	Menu parentMenu = model.getParentMenu();
	if (parentMenu!=null&&parentMenu.getId()==null) {//先判断用户是否要添加一级菜单,并且父菜单是id为null
		model.setParentMenu(null);
	}
	menuRepository.save(model);
	
}

@Override
public List<Menu> findbyUser(User user) {
	if ("admin".equals(user.getUsername())) {//如果登陆的是admin
		return menuRepository.findAll();//返回全部权限
	}
	return menuRepository.findbyUser(user.getId());//是其它用户根据id查
}



	

}

package com.xyz.bos.dao.system;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.xyz.bos.domain.system.Menu;
import com.xyz.bos.domain.system.User;

public interface MenuRepository extends JpaRepository<Menu, Long> {

	List<Menu> findByParentMenuIsNull();


}

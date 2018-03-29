package com.xyz.bos.dao.system;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.xyz.bos.domain.system.Menu;
import com.xyz.bos.domain.system.User;

public interface MenuRepository extends JpaRepository<Menu, Long> {

	List<Menu> findByParentMenuIsNull();
	 @Query("select m from Menu m inner join m.roles r inner join r.users u where u.id = ?")
	    List<Menu> findbyUser(Long id);

}

package com.xyz.bos.dao.system;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.xyz.bos.domain.system.Menu;
import com.xyz.bos.domain.system.Permission;
import com.xyz.bos.domain.system.Role;
import com.xyz.bos.domain.system.User;

public interface RoleRepository extends JpaRepository<Role, Long> {
@Query("select r from Role r inner join r.users u where u.id=?")
	List<Role> findbyUid(Long uid);



}

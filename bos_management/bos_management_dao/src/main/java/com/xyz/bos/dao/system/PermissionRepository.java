package com.xyz.bos.dao.system;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.xyz.bos.domain.system.Menu;
import com.xyz.bos.domain.system.Permission;
import com.xyz.bos.domain.system.User;

public interface PermissionRepository extends JpaRepository<Permission, Long> {
//已知用户id 查用户权限sql语句转成GQL hql语句 都转成对象:
	/*select*from T_PERMISSION p 
	inner join T_ROLE_PERMISSION rp on rp.c_permission_id=p.c_id
	inner join t_role r on r.c_id=rp.C_ROLE_ID
	inner join t_user_role ur on ur.c_role_id=r.c_id
	inner join t_user u on u.c_id=ur.c_user_id
	where u.c_id=3501*/
	@Query("select p from Permission p inner join p.roles r inner join r.users u where u.id=?")
	List<Permission> findbyUid(Long uid);



}

package com.xyz.bos.dao.system;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.xyz.bos.domain.system.Menu;
import com.xyz.bos.domain.system.Permission;
import com.xyz.bos.domain.system.User;

public interface PermissionRepository extends JpaRepository<Permission, Long> {



}

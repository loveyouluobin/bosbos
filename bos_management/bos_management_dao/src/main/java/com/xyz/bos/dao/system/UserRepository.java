package com.xyz.bos.dao.system;

import org.springframework.data.jpa.repository.JpaRepository;

import com.xyz.bos.domain.system.User;

public interface UserRepository extends JpaRepository<User, Long> {

	User findByUsername(String username);

}

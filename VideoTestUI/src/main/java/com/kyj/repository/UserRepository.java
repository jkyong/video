package com.kyj.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.kyj.domain.User;

public interface UserRepository extends JpaRepository<User, Long> {
	public User findByName(String name);
}

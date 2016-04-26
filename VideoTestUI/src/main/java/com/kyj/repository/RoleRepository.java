package com.kyj.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.kyj.domain.Role;

public interface RoleRepository extends JpaRepository<Role, Integer>{

	Role findByName(String name); 
}

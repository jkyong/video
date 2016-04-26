package com.kyj.persistence;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.kyj.domain.Role;
import com.kyj.repository.RoleRepository;

@Repository
public class RoleDao {

	@Autowired
	RoleRepository roleRepository;
	
	public Role findByName(String name) {
		return roleRepository.findByName(name);
	}
}

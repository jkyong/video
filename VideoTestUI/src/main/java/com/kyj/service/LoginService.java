package com.kyj.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.kyj.domain.Role;
import com.kyj.domain.User;
import com.kyj.persistence.RoleDao;
import com.kyj.persistence.UserDao;

@Service
public class LoginService {

	@Autowired
	UserDao userDao;
	
	@Autowired
	RoleDao roleDao;
	
	public void save(User user) {
		List<Role> roles = new ArrayList<Role>();		
		roles.add(roleDao.findByName("ROLE_USER"));
		
		user.setEnabled(true);
		user.setRoles(roles);
		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
		user.setPassword(encoder.encode(user.getPassword()));
		userDao.save(user);
	}	
}

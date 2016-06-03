package com.kyj.persistence;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.kyj.domain.User;
import com.kyj.repository.UserRepository;

@Repository
public class UserDao {

	@Autowired
	UserRepository userRepository;
	
	public void save(User user) {		
		userRepository.save(user);
	}

	public User findOne(Long id) {
		User user = userRepository.findOne(id);
		
		return user;
	}
	
	public User findByName(String name) {
		User user = userRepository.findByName(name);
		
		return user;
	}
}

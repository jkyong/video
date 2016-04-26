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
	
}

package com.kyj.persistence;

import java.util.UUID;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.kyj.domain.ExternalFile;
import com.kyj.domain.FileInfo;
import com.kyj.domain.User;
import com.kyj.repository.ExternalRepository;
import com.kyj.repository.UserRepository;

@Repository
public class ExternalFileDAO {
	
	@PersistenceContext
	private EntityManager em;
	
	@Autowired
	private ExternalRepository exRepository;
	
	@Autowired
	private UserRepository userRepository;
	
	@Transactional
	public ExternalFile save(long fileInfoId, ExternalFile externalFile) {
		FileInfo fileInfo = em.find(FileInfo.class, fileInfoId);
		
		String externalUuid = UUID.randomUUID().toString();
		String external = "";
		String[] split = externalUuid.split("-");
		
		String randomUri = RandomStringUtils.randomAlphanumeric(11);
		
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		String authName = auth.getName(); // get logged in username
		
		User user = userRepository.findByName(authName);
		
		if ( externalFile.getAccess().equals("public")) {
			// public
		}
		else if ( externalFile.getAccess().equals("private")){
			// private
			// 12ÀÚ¸®
			for ( int i =0; i < 2; i++) {
				external = external + split[i];
			}
			externalFile.setExternal(external);
		}
		
		externalFile.setFileInfo(fileInfo);
		externalFile.setUri(randomUri);
		externalFile.setUser(user);
		fileInfo.getExternalFiles().add(externalFile);
		user.getExternalFiles().add(externalFile);
		
		em.persist(externalFile);
		
		return externalFile;
	}
	
	public ExternalFile findByExternal(String external) {
		return exRepository.findByExternal(external);
	}
	
	public ExternalFile findByUri(String uri) {
		return exRepository.findByUri(uri);
	}
}

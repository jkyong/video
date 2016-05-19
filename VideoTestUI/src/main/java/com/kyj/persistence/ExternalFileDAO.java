package com.kyj.persistence;

import java.util.UUID;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.kyj.domain.ExternalFile;
import com.kyj.domain.FileInfo;
import com.kyj.repository.ExternalRepository;

@Repository
public class ExternalFileDAO {
	
	@PersistenceContext
	private EntityManager em;
	
	@Autowired
	private ExternalRepository exRepository;
	
	@Transactional
	public ExternalFile save(long fileInfoId, ExternalFile externalFile) {
		
		
		FileInfo fileInfo = em.find(FileInfo.class, fileInfoId);
		
		String externalUuid = UUID.randomUUID().toString();
		String external = "";
		String[] split = externalUuid.split("-");
		
		String random = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
		String resultUri = "";
		
		if ( externalFile.getAccess().equals("public")) {
			// public
			
		}
		else if ( externalFile.getAccess().equals("private")){
			// private
			// 12자리
			for ( int i =0; i < 2; i++) {
				external = external + split[i];
			}
			externalFile.setExternal(external);
		}
				
		// 11자리 uri
		for ( int i = 0; i < 11; i++) {
			int r = ((int) (Math.random() * random.length()));
			
			resultUri = resultUri + random.charAt(r);
		}
		
		externalFile.setFileInfo(fileInfo);
		externalFile.setUri(resultUri);
		fileInfo.getExternalFiles().add(externalFile);
		
		em.persist(fileInfo);
		
		return externalFile;
	}
	
	public ExternalFile findByExternal(String external) {
		return exRepository.findByExternal(external);
	}
	
	public ExternalFile findByUri(String uri) {
		return exRepository.findByUri(uri);
	}
}

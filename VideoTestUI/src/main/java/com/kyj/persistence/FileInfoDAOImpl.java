package com.kyj.persistence;

import java.util.List;
import java.util.UUID;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceContextType;
import javax.persistence.TypedQuery;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.kyj.domain.FileInfo;
import com.kyj.domain.Structure;

@Repository
public class FileInfoDAOImpl implements FileInfoDAO {

	@PersistenceContext
	private EntityManager em;
	
	@Transactional
	public long save(java.util.Calendar createDate, String path, String nameOnly, long size, String extension, long structure_id) {
		Structure structure = em.find(Structure.class, structure_id);
		
		FileInfo fileInfo= new FileInfo();
		fileInfo.setCreateDate(createDate);
		fileInfo.setPath(path);
		fileInfo.setName(nameOnly);
		fileInfo.setSize(size);
		fileInfo.setExtension(extension);
		fileInfo.setStructure(structure);
		
	/*	if ( extension == "mp4") {
			String externalUuid = UUID.randomUUID().toString();
			String external = "";
			String[] split = external.split("-");
			
			// 12자리
			for ( int i =0; i < 2; i++) {
				external = external + split[i];
			}
			fileInfo.setExternal(external);
		}*/
		
		em.persist(fileInfo);
		
		return fileInfo.getId();
	}

	@Transactional
	public void remove(long id) {
		FileInfo files= em.find(FileInfo.class, id);
		List<FileInfo> fileInfo = files.getStructure().getFileInfo();
		
		for ( int i = 0; i < fileInfo.size(); i++) {
			if ( fileInfo.get(i).getName().equals(files.getName()))
				fileInfo.remove(i);
		}
	}
	
	@Transactional
	public void move(long id, long moveId) {
		FileInfo fileInfo= em.find(FileInfo.class, id);
		
		Structure structure= em.find(Structure.class, moveId);
		
		fileInfo.setStructure(structure);
	}
	
	@Transactional
	public List<FileInfo> partialSelect(long structure_id) {
		Structure structure = em.find(Structure.class, structure_id);
		
		return structure.getFileInfo();
	}
	
	@Transactional
	public void update(long id, String name) {
		FileInfo fileInfo = em.find(FileInfo.class, id);
		fileInfo.setName(name);
	}

	public FileInfo find(long id) {
		FileInfo fileInfo = em.find(FileInfo.class, id);
		
		return fileInfo;
	}
	
	public List<FileInfo> findAll() {
		TypedQuery<FileInfo> query = em.createQuery("select f from FileInfo f", FileInfo.class);
		
		return query.getResultList();
	}
	
	public FileInfo selectExternal(String external) {
		TypedQuery<FileInfo> query = em.createQuery("select f from FileInfo f where f.external = :external", FileInfo.class);
		query.setParameter("external", external);
		
		List<FileInfo> result = query.getResultList();
		
		if ( result.isEmpty())
			return null;
		else
			return result.get(0);
	}
	
	public List<FileInfo> selectVideoFormat() {
		TypedQuery<FileInfo> query = em.createQuery("select f from FileInfo f where f.extension = :extension", FileInfo.class);
		query.setParameter("extension", "mp4");
		
		List<FileInfo> result = query.getResultList();
		
		if ( result.isEmpty())
			return null;
		else
			return result;
	}

}

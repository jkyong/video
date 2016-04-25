package com.kyj.persistence;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.kyj.domain.FileInfo;

@Repository
public class FileInfoDAOImpl implements FileInfoDAO {

	@PersistenceContext
	private EntityManager em;
	
	@Transactional
	public long save(java.util.Calendar createDate, String path, String nameOnly, long size, String extension, long structure_id) {
		FileInfo fileInfo= new FileInfo();
//		fileInfo.setUuid(uuid);
		fileInfo.setCreateDate(createDate);
		fileInfo.setPath(path);
		fileInfo.setName(nameOnly);
		fileInfo.setSize(size);
		fileInfo.setExtension(extension);
		fileInfo.setStructure_id(structure_id);
		
		em.persist(fileInfo);
		
		return fileInfo.getId();
	}

	@Transactional
	public FileInfo remove(long id) {
		FileInfo files= em.find(FileInfo.class, id);
		
		em.remove(files);
		
		return files;
	}
	
	@Transactional
	public void move(long id, long moveId) {
		FileInfo fileInfo= em.find(FileInfo.class, id);
		
		fileInfo.setStructure_id(moveId);		
	}
	
	
	public List<FileInfo> partialSelect(long structure_id) {
		TypedQuery<FileInfo> query = em.createQuery("select f from FileInfo f where f.structure_id = :structure_id", FileInfo.class);
		query.setParameter("structure_id", structure_id);
		
		return query.getResultList();
	}

	public FileInfo find(long id) {
		// TODO Auto-generated method stub
		FileInfo fileInfo = em.find(FileInfo.class, id);
		
		return fileInfo;
	}
	
	public List<FileInfo> findAll() {
		TypedQuery<FileInfo> query = em.createQuery("select f from FileInfo f", FileInfo.class);
		
		return query.getResultList();
	}

}

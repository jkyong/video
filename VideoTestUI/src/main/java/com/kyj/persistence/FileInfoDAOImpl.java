package com.kyj.persistence;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
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
		
		em.persist(fileInfo);
		
		return fileInfo.getId();
	}

	@Transactional
	public void remove(long id) {
		FileInfo files= em.find(FileInfo.class, id);
		
		if ( files != null)
			em.remove(files);
	}
	
	@Transactional
	public void move(long id, long moveId) {
		FileInfo fileInfo= em.find(FileInfo.class, id);
		
		Structure structure= em.find(Structure.class, moveId);
		
		fileInfo.setStructure(structure);
	}
	
	
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

}

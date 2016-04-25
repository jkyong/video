package com.kyj.persistence;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.kyj.domain.FileInfo;
import com.kyj.domain.Structure;

@Repository
public class StructureDAOImpl implements StructureDAO{

	@PersistenceContext
	private EntityManager em;
	
	@Override
	public List<Structure> findAll() {
		TypedQuery<Structure> query = em.createQuery("select s from Structure s order by s.pid", Structure.class);
		return query.getResultList();
	}
	
	public List<Structure> findChildren(long id) {
		TypedQuery<Structure> query = em.createQuery("select s from Structure s where s.pid = :id", Structure.class);
		query.setParameter("id", id);
		
		return query.getResultList();
	}

	@Transactional
	public long save(String title, long pid) {
		Structure structure = new Structure();
		structure.setTitle(title);
		structure.setPid(pid);
		
		em.persist(structure);
		System.out.println(em);
		return structure.getKey();
	}
	
	@Transactional
	public List<FileInfo> remove(long id) {
		Structure structure = em.find(Structure.class, id);
		List<FileInfo> fileInfo = structure.getFileInfo();		
		Query query = em.createQuery("delete from Structure s where s.id = :id or s.pid = :id");
		
		query.setParameter("id", id);
		query.executeUpdate();
		
		return fileInfo;
	}

	@Transactional
	public void update(long id, String title) {
		// TODO Auto-generated method stub
		Structure structure = em.find(Structure.class, id);
		structure.setTitle(title);
	}
	
	@Transactional
	public void move(long id, long moveId) {
		Structure structure = em.find(Structure.class, id);
		
		structure.setPid(moveId);
	}

	@Override
	public Structure find(long id) {
		Structure structure = em.find(Structure.class, id);
		
		return structure;
	}
	
	
	
}

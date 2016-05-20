package com.kyj.persistence;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.kyj.domain.Structure;
import com.kyj.domain.StructureForTree;
import com.kyj.repository.StructureRepository;

@Repository
public class StructureDAOImpl implements StructureDAO{

	@PersistenceContext
	private EntityManager em;
	
	@Autowired
	private StructureRepository structureRepository;
	
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
	
	public List<StructureForTree> getAll() {
		TypedQuery<StructureForTree> query = em.createQuery("select s from StructureForTree s order by s.pid", StructureForTree.class);
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
	public void remove(long id) {
		Structure structure = em.find(Structure.class, id);
		if ( structure != null) {
			em.remove(structure);
		}
	}

	@Transactional
	public void update(long id, String title) {
		Structure structure = em.find(Structure.class, id);
		structure.setTitle(title);
		System.out.println("id " + id + ", title" + title);
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

	@Override
	public List<Structure> findByPid(long id) {
		
		return structureRepository.findByPid(id);
	}
	
}

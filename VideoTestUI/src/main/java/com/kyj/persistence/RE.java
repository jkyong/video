package com.kyj.persistence;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.kyj.domain.Area;
import com.kyj.domain.Blade;

@Repository
public class RE {
	@PersistenceContext
	private EntityManager em;
	
	@Transactional
	public void show() {
		
		Area area = em.find(Area.class, 1L);
		List<Blade> blades = area.getBlades();
		
		for ( int i =0; i < blades.size(); i++) {
			System.out.println(blades.get(i).getName());
		}
	}
}

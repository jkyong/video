package com.kyj.persistence;

import java.util.List;

import com.kyj.domain.FileInfo;
import com.kyj.domain.Structure;

public interface StructureDAO {
	public List<Structure> findAll();
	
	public List<Structure> findChildren(long id);
	
	public long save(String title, long pid);	
	
	public void remove(long id);
	
	public void update(long id, String title);
	
	public void move(long id, long moveId);
	
	public Structure find(long id);
	
}

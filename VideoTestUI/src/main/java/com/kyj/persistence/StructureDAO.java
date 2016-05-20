package com.kyj.persistence;

import java.util.List;

import com.kyj.domain.FileInfo;
import com.kyj.domain.Structure;
import com.kyj.domain.StructureForTree;

public interface StructureDAO {
	public List<Structure> findAll();
	
	public List<Structure> findChildren(long id);
	
	public List<StructureForTree> getAll();
	
	public long save(String title, long pid);	
	
	public void remove(long id);
	
	public void update(long id, String title);
	
	public void move(long id, long moveId);
	
	public Structure find(long id);
	
	public List<Structure> findByPid(long id);
	
}

package com.kyj.persistence;

import java.util.List;

import com.kyj.domain.FileInfo;

public interface FileInfoDAO {
	public long save(java.util.Calendar createDate, String path, String nameOnly, long size, String extension, long structure_id);
	
	public void remove(long id);
	
	public void move(long id, long moveId);
	
	public List<FileInfo> partialSelect(long structure_id);
	
	public void update(long id, String name);
	
	public FileInfo find(long id);
	
	public List<FileInfo> findAll();
	
	public FileInfo selectExternal(String external);
	
	public List<FileInfo> selectVideoFormat();
}

package com.kyj.persistence;

import java.util.List;

import com.kyj.domain.FileInfo;

public interface FileInfoDAO {
	public long save(java.util.Calendar createDate, String path, String nameOnly, long size, String extension, long structure_id);
	
	public FileInfo remove(long id);
	
	public void move(long id, long moveId);
	
	public List<FileInfo> partialSelect(long structure_id);
	
	public FileInfo find(long id);
	
	public List<FileInfo> findAll();
}
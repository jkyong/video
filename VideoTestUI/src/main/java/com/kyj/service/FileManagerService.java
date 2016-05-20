package com.kyj.service;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kyj.domain.FileInfo;
import com.kyj.domain.Structure;
import com.kyj.persistence.FileInfoDAO;
import com.kyj.persistence.StructureDAO;

@Service
public class FileManagerService {
	@Autowired
	private StructureDAO structureDAO;
	
	@Autowired
	private FileInfoDAO fileInfoDAO;
	
	public String renameFolderValid(long pid, long folderId, String renameInput) {
		List<Structure> children = structureDAO.findChildren(pid);
		
		String title = renameInput;
		String validTitle;
		
		for ( int i = 0; i < children.size(); i++) {
			validTitle = children.get(i).getTitle();
			
			if ( title.equals(validTitle)) {
				return "";
			}
		}
		
		return title;
	}
	
	public void renameFolder(long folderId, String title) {
		structureDAO.update(folderId, title);
	}
	
	public String renameFileValid(long pid, long fileId, String renameInput) {
		List<FileInfo> children = fileInfoDAO.partialSelect(pid);
		
		String name = renameInput;
		String validTitle;
		
		for ( int i = 0; i < children.size(); i++) {
			validTitle = children.get(i).getName();
			
			if ( name.equals(validTitle)) {
				return "";
			}
		}
		
		return name;
	}
	
	public void renameFile(long fileId, String name) {
		fileInfoDAO.update(fileId, name);
	}
	
	@Transactional
	public void remove(long[] folderIds, long[] fileIds, String uploadDefaultPath) {
		if ( fileIds != null) {
			for ( int i =0; i < fileIds.length; i++) {
				FileInfo removeFile = fileInfoDAO.find(fileIds[i]);
				StringBuffer fullPath = new StringBuffer();
				
				try {
					Calendar datePath = removeFile.getCreateDate();
					String year = String.valueOf(datePath.get(Calendar.YEAR));
					String month = String.valueOf(datePath.get(Calendar.MONTH) + 1); // 0 ~ 11
					String date = String.valueOf(datePath.get(Calendar.DATE));
					
					fullPath.append(uploadDefaultPath);
					fullPath.append("/");
					fullPath.append(year);
					fullPath.append("/");
					fullPath.append(month);
					fullPath.append("/");
					fullPath.append(date);
					fullPath.append("/");
					fullPath.append(removeFile.getId());
					fullPath.append("_");
					fullPath.append(removeFile.getName());
					fullPath.append(".");
					fullPath.append(removeFile.getExtension());
					
					File file = new File(fullPath.toString());
					if (file.delete()) {
						System.out.println(fullPath + " is deleted.");
						fileInfoDAO.remove(fileIds[i]);
					}
					else {
						System.out.println(fullPath);
						System.out.println("file delete fail.");
						fileInfoDAO.remove(fileIds[i]);

					}
					
				} catch (Exception e) {
					e.printStackTrace();
				}
				
			}
			fileIds = null;
		}
		
		if ( folderIds != null) {
			List<Structure> strChild = new ArrayList<>();
			List<FileInfo> fileInfos = new ArrayList<>();
			for ( int i =0; i < folderIds.length; i++) {
				strChild = structureDAO.findChildren(folderIds[i]);
//				fileInfos = structure.find(folderIds[i]).getFileInfo();
//				fileInfos = structure.remove(folderIds[i]);
				if ( structureDAO.find(folderIds[i]) != null) {
					fileInfos = structureDAO.find(folderIds[i]).getFileInfo();
				}
				
				if ( fileInfos != null) {
					
					for ( int k = 0; k < fileInfos.size(); k++) {
						StringBuffer fullPath = new StringBuffer();
						try {						
							Calendar datePath = fileInfos.get(k).getCreateDate();
							String year = String.valueOf(datePath.get(Calendar.YEAR));
							String month = String.valueOf(datePath.get(Calendar.MONTH) + 1); // 0 ~ 11
							String date = String.valueOf(datePath.get(Calendar.DATE));
							
							fullPath.append(uploadDefaultPath);
							fullPath.append("/");
							fullPath.append(year);
							fullPath.append("/");
							fullPath.append(month);
							fullPath.append("/");
							fullPath.append(date);
							fullPath.append("/");
							fullPath.append(fileInfos.get(k).getId());
							fullPath.append("_");
							fullPath.append(fileInfos.get(k).getName());
							fullPath.append(".");
							fullPath.append(fileInfos.get(k).getExtension());
							
							File file = new File(fullPath.toString());
							if ( file.delete()) {
								System.out.println(fullPath + " is deleted.");
								fileInfoDAO.remove(fileInfos.get(k).getId());
							}
							else {
								System.out.println("file delete fail.");
							}
						} catch (Exception e) {
							
						}
					}
				}
				
				if ( strChild.size() != 0) {
					long[] newFolderIds = new long[strChild.size()];
					for ( int j =0; j < strChild.size(); j++)
						newFolderIds[j] = strChild.get(j).getKey();
					if ( newFolderIds.length != 0)
						remove(newFolderIds, fileIds, uploadDefaultPath);
				}

				structureDAO.remove(folderIds[i]);
			}
		}
	}
	
	public List<Structure> moveFolderNameValid(long id) {
		List<Structure> folders = structureDAO.findChildren(id);
		
		return folders;
	}
	
	@Transactional
	public List<FileInfo> moveFileNameValid(long id) {
		Structure structure = structureDAO.find(id);
		List<FileInfo> files = structure.getFileInfo();
		
		return files;
	}
	
	public void move(long[] folderId, long[] fileId, long moveId) {
		if (folderId != null) {
			for (int i = 0; i < folderId.length; i++) {
				structureDAO.move(folderId[i], moveId);
			}
		}
		if (fileId != null) {
			for (int k = 0; k < fileId.length; k++) {
				fileInfoDAO.move(fileId[k], moveId);
			}
		}
	}
	
	public HashMap<String, Object> folderCreate(String title, long pid) {
		HashMap<String, Object> map = new HashMap<>(); 
		
		long saveKey = structureDAO.save(title, pid);
		map.put("id", saveKey);
		
		return map;
	}
}

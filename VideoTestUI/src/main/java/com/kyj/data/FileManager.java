package com.kyj.data;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import com.kyj.domain.FileInfo;
import com.kyj.domain.Structure;
import com.kyj.persistence.FileInfoDAO;
import com.kyj.persistence.StructureDAO;

public class FileManager {
	public String renameFolderValid(List<Structure> children, String renameInput) {
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
	
	public String renameFileValid(List<FileInfo> children, String renameInput) {
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
	
	@Transactional
	public void remove(StructureDAO structure, FileInfoDAO fileInfo, long[] folderIds, long[] fileIds, String uploadDefaultPath) {
		if ( fileIds != null) {
			for ( int i =0; i < fileIds.length; i++) {
				FileInfo removeFile = fileInfo.find(fileIds[i]);
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
						fileInfo.remove(fileIds[i]);
					}
					else {
						System.out.println(fullPath);
						System.out.println("file delete fail.");
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
				strChild = structure.findChildren(folderIds[i]);
//				fileInfos = structure.find(folderIds[i]).getFileInfo();
//				fileInfos = structure.remove(folderIds[i]);
				if ( structure.find(folderIds[i]) != null) {
					fileInfos = structure.find(folderIds[i]).getFileInfo();
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
								fileInfo.remove(fileInfos.get(k).getId());
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
						remove(structure, fileInfo, newFolderIds, fileIds, uploadDefaultPath);
				}

				structure.remove(folderIds[i]);
			}
		}
	}
}

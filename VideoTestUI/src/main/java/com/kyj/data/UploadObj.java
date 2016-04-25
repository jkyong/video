package com.kyj.data;

import java.io.File;
import java.io.IOException;
import java.util.Calendar;

import org.springframework.util.FileCopyUtils;

public class UploadObj {
	private String name;
	
	private String extension;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getExtension() {
		return extension;
	}

	public void setExtension(String extension) {
		this.extension = extension;
	}
	
	public String getFileExt(String fileName) {
		String[] split = fileName.split("[.]");
				
		String extension = split[split.length - 1];
		
		return extension;
	}
	
	public String getNameOnly(String fileName) {
		String[] split = fileName.split("[.]");
		
		String extension = split[split.length - 1];
		
		String nameOnly = fileName.substring(0, fileName.length() - extension.length() - 1);
		
		return nameOnly;
	}
	
	public String uploadFile(String uploadPath, long id, String originalName, byte[] fileData) throws IOException {
		
		File directory = new File(uploadPath);
		
		if ( !directory.exists()) {
			if ( directory.mkdirs()) {
				System.out.println("create directory");
			}
			else {
				System.out.println("alreay");
			}
		}
		
	    String savedName = id + "_" + originalName;

	    File target = new File(uploadPath, savedName);

	    FileCopyUtils.copy(fileData, target);

	    return uploadPath;

	  }
}

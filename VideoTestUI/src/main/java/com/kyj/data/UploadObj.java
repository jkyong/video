package com.kyj.data;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
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
		
		try {
			nameOnly = new String(nameOnly.getBytes("8859_1"), "UTF-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
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
		
		originalName = new String(originalName.getBytes("8859_1"), "UTF-8");
		
	    String savedName = id + "_" + originalName;

	    File target = new File(uploadPath, savedName);

	    FileCopyUtils.copy(fileData, target);

	    return uploadPath;

	  }
}

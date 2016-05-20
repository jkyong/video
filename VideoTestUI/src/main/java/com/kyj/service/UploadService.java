package com.kyj.service;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.multipart.MultipartFile;

import com.kyj.domain.FileInfo;
import com.kyj.persistence.FileInfoDAO;

@Service
public class UploadService {

	@Autowired
	private FileInfoDAO fileInfoDAO;
	
	@Resource
	private String uploadDefaultPath;
	
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

		if (!directory.exists()) {
			if (directory.mkdirs()) {
				System.out.println("create directory");
			} else {
				System.out.println("alreay");
			}
		}

		originalName = new String(originalName.getBytes("8859_1"), "UTF-8");

		String savedName = id + "_" + originalName;

		File target = new File(uploadPath, savedName);

		FileCopyUtils.copy(fileData, target);

		return uploadPath;

	}
	
	public void uploadForm(MultipartFile file, long selectedId) {
		UploadService uploadObj = new UploadService();
		
		String originalName = file.getOriginalFilename();
		
		String nameOnly = uploadObj.getNameOnly(originalName);
		long size = file.getSize();
		String extension = uploadObj.getFileExt(originalName);
		long structure_id = selectedId;
		
		Calendar createDate = Calendar.getInstance();
		
		String year = String.valueOf(createDate.get(Calendar.YEAR));
		String month = String.valueOf(createDate.get(Calendar.MONTH) + 1);	// 0 ~ 11
		String day = String.valueOf(createDate.get(Calendar.DATE));
		
		String uploadPath = uploadDefaultPath + "/" + year + "/" + month + "/" + day;

		long id = fileInfoDAO.save(createDate, uploadPath, nameOnly, size, extension, structure_id);
		
	    try {
			uploadFile(uploadPath, id, file.getOriginalFilename(), file.getBytes());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public List<String> uploadFileValid(String[] selectedFiles, long uploadedId) {
		List<FileInfo> existFile = fileInfoDAO.partialSelect(uploadedId);
		String validName;
		List<String> duplicate = new ArrayList<>();
		System.out.println("selected files : " + selectedFiles[0]);
		for (int i = 0; i < existFile.size(); i++) {
			validName = existFile.get(i).getName() + "." + existFile.get(i).getExtension();
			for ( int k = 0; k < selectedFiles.length; k++) {
				if ( validName.equals( selectedFiles[k] )) {
					duplicate.add(validName);
				}
			}
		}
		System.out.println(duplicate.size());
		
		return duplicate;
	}
	
}

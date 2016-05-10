package com.kyj.controller;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.config.http.FilterChainMapBeanDefinitionDecorator;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.kyj.data.DownloadObj;
import com.kyj.data.FileManager;
import com.kyj.data.UploadObj;
import com.kyj.domain.FileInfo;
import com.kyj.domain.Structure;
import com.kyj.persistence.FileInfoDAO;
import com.kyj.persistence.StructureDAO;

@Controller
public class FileManagerController {
	@Autowired
	private StructureDAO structure;
	
	@Autowired
	private FileInfoDAO fileInfo;
	
	/*@Resource(name = "uploadPath")
	private String uploadPath;*/

//	private Children children;
	
	static String uploadDefaultPath = "C:/zzz/upload";

	@RequestMapping(value = "folderCreate", method = RequestMethod.GET)
	public synchronized @ResponseBody HashMap<String, Object> fileInsert(@RequestParam(value = "title") String title, @RequestParam(value = "pid") long pid) {
		HashMap<String, Object> map = new HashMap<>(); 
		
		long saveKey = structure.save(title, pid);
		map.put("id", saveKey);
		
		return map;
	}
	
	@RequestMapping(value = "remove", method = RequestMethod.GET) 
	public @ResponseBody void remove(@RequestParam(value= "folderIds[]", required = false) long[] folderIds, @RequestParam(value = "fileIds[]", required = false) long[] fileIds) {
		
		FileManager fileManager = new FileManager();
		fileManager.remove(structure, fileInfo, folderIds, fileIds, uploadDefaultPath);
	}
	
	@RequestMapping(value = "renameFolder", method = RequestMethod.GET)
	public @ResponseBody void renameFolder(@RequestParam long folderId, @RequestParam String title) {
		structure.update(folderId, title);
	}
	
	@RequestMapping(value = "renameFolderValid", method = RequestMethod.GET)
	public @ResponseBody String renameFolderValid(@RequestParam long pid, @RequestParam long folderId, @RequestParam String renameInput) {
		List<Structure> children = structure.findChildren(pid);
		FileManager fileManager = new FileManager();
		
		String rename = fileManager.renameFolderValid(children, renameInput);
		return rename;
	}
	
	@RequestMapping(value = "renameFile", method = RequestMethod.GET)
	public @ResponseBody void renameFile(@RequestParam long fileId, @RequestParam String name) {
		fileInfo.update(fileId, name);
	}
	
	@RequestMapping(value = "renameFileValid", method = RequestMethod.GET)
	public @ResponseBody String renameFileValid(@RequestParam long pid, @RequestParam long fileId, @RequestParam String renameInput) {
		List<FileInfo> children = fileInfo.partialSelect(pid);
		FileManager fileManager = new FileManager();
		
		String rename = fileManager.renameFileValid(children, renameInput);
		return rename;
	}
	
	private static final Logger logger = LoggerFactory
			.getLogger(FileManagerController.class);
	
	@RequestMapping(value = "/uploadForm", method = RequestMethod.POST)
	public @ResponseBody void uploadForm(MultipartFile file, @RequestParam long selectedId) throws ServletException, IOException {
		UploadObj uploadObj = new UploadObj();
		
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

		long id = fileInfo.save(createDate, uploadPath, nameOnly, size, extension, structure_id);
		
	    uploadObj.uploadFile(uploadPath, id, file.getOriginalFilename(), file.getBytes());
	    
	}
	
	@RequestMapping(value = "/uploadFileValid", method = RequestMethod.GET)
	public @ResponseBody List<String> getUploadFileValid(@RequestParam(value = "selectedFiles[]", required = false) String[] selectedFiles, @RequestParam long uploadedId) {
		List<FileInfo> existFile = fileInfo.partialSelect(uploadedId);
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
	
	@RequestMapping(value = "move", method = RequestMethod.GET)
	public @ResponseBody void move(@RequestParam(value = "folderId[]", required = false) long[] folderId, @RequestParam(value = "fileId[]", required = false) long[] fileId, @RequestParam long moveId) {
		
		if (folderId != null) {
			for (int i = 0; i < folderId.length; i++) {
				structure.move(folderId[i], moveId);
			}
		}
		if (fileId != null) {
			for (int k = 0; k < fileId.length; k++) {
				fileInfo.move(fileId[k], moveId);
			}
		}
	}
	
	@RequestMapping(value = "moveFileNameValid", method = RequestMethod.GET)
	public @ResponseBody List<FileInfo> moveFileNameValid(@RequestParam(value = "moveId") long id) {
		Structure struc = structure.find(id);
		List<FileInfo> files = struc.getFileInfo();
		
		return files;
	}
	
	@RequestMapping(value = "moveFolderNameValid", method = RequestMethod.GET)
	public @ResponseBody List<Structure> moveFolderNameValid(@RequestParam(value = "moveId") long id) {
		List<Structure> folders = structure.findChildren(id);	
		
		return folders;
	}
	
	@RequestMapping(value = "downloadOnlyOneFile/{fileId}", method = RequestMethod.GET)
	public @ResponseBody void downloadOnlyOneFile(@PathVariable("fileId") long fileId, HttpServletResponse response) throws IOException {
		FileInfo fi = fileInfo.find(fileId);
		
		DownloadObj downObj = new DownloadObj();
		downObj.downOnlyOneFile(fi, response);
	}
	
	@RequestMapping(value = "downloadOnlyOneFolder/{folderId}", method = RequestMethod.GET)
	public @ResponseBody HashMap<String, Object> downloadOnlyOneFolder(@PathVariable("folderId") long folderId, HttpServletResponse response) throws IOException {
		
		List<Structure> structureAll = structure.findAll();
		List<FileInfo> fileInfoAll = fileInfo.findAll();
		
		Structure folderObj = structure.find(folderId);
		String folderName = folderObj.getTitle();
		
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
	    String authName = auth.getName(); //get logged in username
		
		DownloadObj downObj = new DownloadObj();
		downObj = downObj.appendFolderPath(folderId, structureAll);
		
		String dumpDirPath = downObj.createDumpDir(downObj.getFullPathList(), authName, folderName, "");
		
		downObj.fileCopy(downObj.getMapList(), fileInfoAll, authName, folderObj, null, "");
		
		String zipPath = downObj.createZip(dumpDirPath, authName, folderName, response);
		
		String delDumpTarget = downObj.delDumpTarget(dumpDirPath);
		
		downObj.delDumpDir(delDumpTarget);
		
		System.out.println("down full path list : " + downObj.getFullPathList());
		System.out.println("zip path : " + zipPath);
		System.out.println("del dump dir path : " + delDumpTarget);
		
		HashMap<String, Object> map = new HashMap<>();
		
		map.put("zipPath", zipPath);
		map.put("zipName", folderName);
		
		return map;
	}
	
	@RequestMapping(value = "startDownloadOneFolder", method = RequestMethod.GET)
	public void startDownloadOneFolder(@RequestParam String zipPath, @RequestParam String zipName, HttpServletResponse response) throws IOException {
		DownloadObj downObj = new DownloadObj();
		
		downObj.downloadZip(zipPath, zipName, response);
	}
	
	@RequestMapping(value = "downloadMultiple", method = RequestMethod.GET)
	public @ResponseBody HashMap<String, Object> downloadMultiple(@RequestParam long parentFolderId, @RequestParam String parentFolderTitle, @RequestParam(value = "folderId[]", required = false) long[] folderId,
			@RequestParam(value = "fileId[]", required = false) long[] fileId, HttpServletResponse response) {
		List<Structure> structureAll = structure.findAll();
		List<FileInfo> fileInfoAll = fileInfo.findAll();
		
		Structure folderObj;
		
		String folderName = null;

		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		String authName = auth.getName(); // get logged in username

		DownloadObj downObj = null;

		String dumpDirPath = null;
		String zipPath = null;
		String delDumpTarget = null;
		
		// 파일만 선택 시
		if (fileId != null && folderId == null) {
			List<FileInfo> fileInfos = fileInfo.partialSelect(parentFolderId);
			List<FileInfo> downFiles = new ArrayList<>();
			for (int i = 0; i < fileInfos.size(); i++) {
				downObj = new DownloadObj();

				for (int j = 0; j < fileId.length; j++) {

					if (fileInfos.get(i).getId() == fileId[j]) {
						downFiles.add(fileInfos.get(i));
					}
				}
			}
			dumpDirPath = downObj.upperFilesCopyOnly(authName, downFiles, parentFolderTitle);
		}

		else {
			// 폴더 선택 시 또는 파일포함 선택 시
			for (int i = 0; i < folderId.length; i++) {
				folderObj = structure.find(folderId[i]);
				folderName = folderObj.getTitle();
	
				downObj = new DownloadObj();
				downObj = downObj.appendFolderPath(folderId[i], structureAll);
	
				dumpDirPath = downObj.createDumpDir(downObj.getFullPathList(), authName, folderName, parentFolderTitle);
	
				// 폴더만 선택 시
				if ( fileId == null) {
					downObj.fileCopy(downObj.getMapList(), fileInfoAll, authName, folderObj, null, parentFolderTitle);
				}
				// 폴더, 파일 선택 시
				else {
					List<FileInfo> fileInfos = fileInfo.partialSelect(parentFolderId);
					List<FileInfo> downFiles = new ArrayList<>();
					for ( int j = 0; j < fileInfos.size(); j++) {
						for ( int k = 0; k < fileId.length; k++) {
							if ( fileInfos.get(j).getId() == fileId[k] ) {
								downFiles.add(fileInfos.get(j));
							}
						}
					}
					downObj.fileCopy(downObj.getMapList(), fileInfoAll, authName, folderObj, downFiles, parentFolderTitle);
				}
			}
		}
		
		String[] split = dumpDirPath.split("/");
		int lastSplitSize = split[split.length - 1].length();
		dumpDirPath = dumpDirPath.substring(0, dumpDirPath.length() - lastSplitSize - 1);
				
		zipPath = downObj.createZip(dumpDirPath, authName, folderName, response);
		
		delDumpTarget = downObj.delDumpTarget(dumpDirPath);
		downObj.delDumpDir(delDumpTarget);

		System.out.println("down full path list : " + downObj.getFullPathList());
		System.out.println("map list : " + downObj.getMapList());
		System.out.println("zip path : " + zipPath);
		System.out.println("del dump dir path : " + delDumpTarget);
		HashMap<String, Object> map = new HashMap<>();
		map.put("zipPath", zipPath);
		map.put("zipName", parentFolderTitle);
		
		return map;
	}
	
	@RequestMapping(value = "startDownloadMultiple", method = RequestMethod.GET)
	public void startDownloadMultiple(@RequestParam String zipPath, @RequestParam String zipName, HttpServletResponse response) throws IOException {
		DownloadObj downObj = new DownloadObj();
		
		downObj.downloadZip(zipPath, zipName, response);
	}
	
}
	
	
	

	
	

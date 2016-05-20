package com.kyj.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.kyj.domain.FileInfo;
import com.kyj.domain.Structure;
import com.kyj.persistence.FileInfoDAO;
import com.kyj.persistence.StructureDAO;
import com.kyj.service.DownloadService;
import com.kyj.service.FileManagerService;
import com.kyj.service.UploadService;

@Controller
public class FileManagerController {
	
	private static final Logger logger = LoggerFactory
			.getLogger(FileManagerController.class);
	
	@Autowired
	private DownloadService downloadService;
	
	@Autowired
	private FileManagerService fileManagerService;
	
	@Autowired
	private UploadService uploadService;
	
	@Resource
	private String uploadDefaultPath;

	@RequestMapping(value = "folderCreate", method = RequestMethod.GET)
	public synchronized @ResponseBody HashMap<String, Object> folderCreate(@RequestParam(value = "title") String title, @RequestParam(value = "pid") long pid) {
		return fileManagerService.folderCreate(title, pid);
	}
	
	@RequestMapping(value = "remove", method = RequestMethod.GET) 
	public @ResponseBody void remove(@RequestParam(value= "folderIds[]", required = false) long[] folderIds, @RequestParam(value = "fileIds[]", required = false) long[] fileIds) {
		fileManagerService.remove(folderIds, fileIds, uploadDefaultPath);
	}
	
	@RequestMapping(value = "renameFolder", method = RequestMethod.GET)
	public @ResponseBody void renameFolder(@RequestParam long folderId, @RequestParam String title) {
		fileManagerService.renameFolder(folderId, title);
	}
	
	@RequestMapping(value = "renameFolderValid", method = RequestMethod.GET)
	public @ResponseBody String renameFolderValid(@RequestParam long pid, @RequestParam long folderId, @RequestParam String renameInput) {
		return fileManagerService.renameFolderValid(pid, folderId, renameInput);
	}
	
	@RequestMapping(value = "renameFile", method = RequestMethod.GET)
	public @ResponseBody void renameFile(@RequestParam long fileId, @RequestParam String name) {
		fileManagerService.renameFile(fileId, name);
	}
	
	@RequestMapping(value = "renameFileValid", method = RequestMethod.GET)
	public @ResponseBody String renameFileValid(@RequestParam long pid, @RequestParam long fileId, @RequestParam String renameInput) {
		return fileManagerService.renameFileValid(pid, fileId, renameInput);
	}
	
	@RequestMapping(value = "/uploadForm", method = RequestMethod.POST)
	public @ResponseBody void uploadForm(MultipartFile file, @RequestParam long selectedId) throws ServletException, IOException {
	    uploadService.uploadForm(file, selectedId);
	}
	
	@RequestMapping(value = "/uploadFileValid", method = RequestMethod.GET)
	public @ResponseBody List<String> getUploadFileValid(@RequestParam(value = "selectedFiles[]", required = false) String[] selectedFiles, @RequestParam long uploadedId) {
		return uploadService.uploadFileValid(selectedFiles, uploadedId);
	}
	
	@RequestMapping(value = "move", method = RequestMethod.GET)
	public @ResponseBody void move(@RequestParam(value = "folderId[]", required = false) long[] folderId, @RequestParam(value = "fileId[]", required = false) long[] fileId, @RequestParam long moveId) {
		fileManagerService.move(folderId, fileId, moveId);
	}
	
	@RequestMapping(value = "moveFileNameValid", method = RequestMethod.GET)
	public @ResponseBody List<FileInfo> moveFileNameValid(@RequestParam(value = "moveId") long id) {
		return fileManagerService.moveFileNameValid(id);
	}
	
	@RequestMapping(value = "moveFolderNameValid", method = RequestMethod.GET)
	public @ResponseBody List<Structure> moveFolderNameValid(@RequestParam(value = "moveId") long id) {
		return fileManagerService.moveFolderNameValid(id);
	}
	
	@RequestMapping(value = "downloadOnlyOneFile/{fileId}", method = RequestMethod.GET)
	public @ResponseBody void downloadOnlyOneFile(@PathVariable("fileId") long fileId, HttpServletResponse response) throws IOException {
		downloadService.downloadOnlyOneFile(fileId, response);
	}
	
	@RequestMapping(value = "downloadOnlyOneFolder/{folderId}", method = RequestMethod.GET)
	public @ResponseBody HashMap<String, Object> downloadOnlyOneFolder(@PathVariable("folderId") long folderId, HttpServletResponse response) throws IOException {
		return downloadService.downloadOnlyOneFolder(folderId, response);
	}
	
	@RequestMapping(value = "startDownloadOneFolder", method = RequestMethod.GET)
	public void startDownloadOneFolder(@RequestParam String zipPath, @RequestParam String zipName, HttpServletResponse response) throws IOException {
		downloadService.downloadZip(zipPath, zipName, response);
	}
	
	@RequestMapping(value = "downloadMultiple", method = RequestMethod.GET)
	public @ResponseBody HashMap<String, Object> downloadMultiple(@RequestParam long parentFolderId, @RequestParam String parentFolderTitle, @RequestParam(value = "folderId[]", required = false) long[] folderId,
			@RequestParam(value = "fileId[]", required = false) long[] fileId, HttpServletResponse response) {
		return downloadService.downloadMultiple(parentFolderId, parentFolderTitle, folderId, fileId, response);
	}
	
	@RequestMapping(value = "startDownloadMultiple", method = RequestMethod.GET)
	public void startDownloadMultiple(@RequestParam String zipPath, @RequestParam String zipName, HttpServletResponse response) throws IOException {
		downloadService.downloadZip(zipPath, zipName, response);
	}
	
}
	
	
	

	
	

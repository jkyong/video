package com.kyj.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
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

import com.kyj.data.UploadObj;
import com.kyj.domain.FileInfo;
import com.kyj.domain.Structure;
import com.kyj.persistence.FileInfoDAO;
import com.kyj.persistence.StructureDAO;
import com.kyj.tree.Children;

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

		List<Structure> structureList = structure.findAll();
//		List<Structure> childrenList = new ArrayList<>();
		System.out.println("     pid :" + pid);
//		children = new Children();
		
		HashMap<String, Object> map = new HashMap<>(); 

		System.out.println("folder create structure list size : " + structureList.size());
//		childrenList = children.getChildrenObj(structureList, pid);

//		for (int i = 0; i < childrenList.size(); i++) {
//			System.out.println("list : " + childrenList.get(i).getKey());
//			
//		}
		
		long saveKey = structure.save(title, pid);
		map.put("id", saveKey);
		
		return map;
		
	}
	
	@RequestMapping(value = "remove", method = RequestMethod.GET) 
	public @ResponseBody void remove(@RequestParam(value= "folderIds[]", required = false) long[] folderIds, @RequestParam(value = "fileIds[]", required = false) long[] fileIds) {
		
		if ( folderIds != null) {
			for ( int i =0; i < folderIds.length; i++) {
				List<FileInfo> fileInfo = structure.remove(folderIds[i]);
				
				for ( int k = 0; k < fileInfo.size(); k++) {
					StringBuffer fullPath = new StringBuffer();
					try {						
						Calendar datePath = fileInfo.get(k).getCreateDate();
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
						fullPath.append(fileInfo.get(k).getId());
						fullPath.append("_");
						fullPath.append(fileInfo.get(k).getName());
						fullPath.append(".");
						fullPath.append(fileInfo.get(k).getExtension());
						
						File file = new File(fullPath.toString());
						if ( file.delete()) {
							System.out.println(fullPath + " is deleted.");
						}
						else {
							System.out.println("file delete fail.");
						}
					} catch (Exception e) {
						
					}
				}
			}
		}
		
		if ( fileIds != null) {
			for ( int i =0; i < fileIds.length; i++) {
				FileInfo removeFile = fileInfo.remove(fileIds[i]);
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
					if (file.delete())
						System.out.println(fullPath + " is deleted.");
					else
						System.out.println("file delete fail.");
					
				} catch (Exception e) {
					
				}
				
			}
		}
	}
	
	@RequestMapping(value = "rename", method = RequestMethod.GET)
	public @ResponseBody void rename(@RequestParam(value = "id") int id, @RequestParam(value = "title") String title) {
		System.out.println("rename id : " + id + ", title : " + title);
		int key = id;
		structure.update(key, title);
	}
	
	private static final Logger logger = LoggerFactory
			.getLogger(FileManagerController.class);

	/**
	 * Upload single file using Spring Controller
	 */
	/*@RequestMapping(value = "/upload", method = RequestMethod.POST)
	public @ResponseBody
	String uploadFileHandler(
			@RequestParam("file") MultipartFile file) {

		if (!file.isEmpty()) {
			try {
				byte[] bytes = file.getBytes();

				// Creating the directory to store file
				String rootPath = System.getProperty("catalina.home");
				File dir = new File(rootPath + File.separator + "tmpFiles");
				if (!dir.exists())
					dir.mkdirs();

				// Create the file on server
				File serverFile = new File(dir.getAbsolutePath()
						+ File.separator + "klklkl");
				BufferedOutputStream stream = new BufferedOutputStream(
						new FileOutputStream(serverFile));
				stream.write(bytes);
				stream.close();

				logger.info("Server File Location="
						+ serverFile.getAbsolutePath());

				return "You successfully uploaded file=" + "klklkl";
			} catch (Exception e) {
				return "You failed to upload " + "klklkl" + " => " + e.getMessage();
			}
		} else {
			return "You failed to upload " + "klklkl"
					+ " because the file was empty.";
		}
	}*/
	
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
		HashMap<String, Object> map = new HashMap<>();
		
		System.out.println(fi.getName());
		System.out.println(fi.getPath());
		System.out.println(fi.getSize());
		
		String id = String.valueOf(fi.getId());
		String path = fi.getPath();
		String fileName = fi.getName();
		String extension = fi.getExtension();
		
		StringBuffer fullPath = new StringBuffer();
		fullPath.append(path);
		fullPath.append("/");
		fullPath.append(id);
		fullPath.append("_");
		fullPath.append(fileName);
		fullPath.append(".");
		fullPath.append(extension);
		
		
		File file = new File(fullPath.toString());
        InputStream is = new FileInputStream(file);
 
        // MIME type of the file
        response.setContentType("application/download");
        // Response header
        response.setHeader("Content-Disposition", "attachment; filename=\""
                + file.getName() + "\"");
        // Read from the file and write into the response
        ServletOutputStream os = response.getOutputStream();
//        byte[] buffer = new byte[1024];
        byte[] buffer = org.apache.commons.io.IOUtils.toByteArray(is);
        int len;
        while ((len = is.read(buffer)) != -1) {
            os.write(buffer, 0, len);
        }
        os.flush();
        os.close();
        is.close();
		
	}
	
	@RequestMapping(value = "downloadOnlyOneFolder", method = RequestMethod.GET)
	public @ResponseBody void downloadOnlyOneFolder(@RequestParam long folderId) {
		List<Structure> structureAll = structure.findAll();
		List<FileInfo> fileInfoAll = fileInfo.findAll();
		
		Structure struc = structure.find(folderId);
		
	//	List<Structure> children = structure.findChildren(folderId);
		
		List result = new ArrayList();
		
		String defaultPath = "C:/";
		String path;
/*		for ( int i = 0; i < children.size(); i++) {
			path = defaultPath + "/" +children.get(i).getTitle();
		}*/
		
		List<String> fullPathList = new ArrayList<>();
		String appendPath = "";

	/*	for ( int i =0; i < children.size(); i++) {
			System.out.println(children.get(i).getTitle());
			System.out.println(children.get(i).getChildren().size());
			
			if ( !children.get(i).getChildren().isEmpty()) {
				System.out.println(children.get(i).getChildren().get(0).getTitle());
			}
		}*/
		List<Structure> list = new ArrayList<>();
		Children c = new Children();
		list = c.getChildrenObj(structureAll, folderId);
		
		re(list, structureAll, fullPathList, appendPath, "");
		for ( int i = 0 ; i < fullPathList.size(); i++)
			System.out.println(fullPathList.get(i));
	}
	
	public void re(List<Structure> list, List<Structure> structureAll, List<String> fullPathList, String appendPath, String upperPath) {
		String twoUpperPath = "";
		
		for ( int i =0; i < list.size(); i++) {
			long id = list.get(i).getKey();
			
			Children c = new Children();
			
			List<Structure> lst = c.getChildrenObj(structureAll, id);
			
			// 하위 없음
			if ( lst.size() == 0) {
				
				appendPath = upperPath + "/" + list.get(i).getTitle();
				fullPathList.add(appendPath);
//				appendPath = beforePath;
				
			}
			else {
				// 재귀
				twoUpperPath = upperPath;
				upperPath = appendPath;
				if ( twoUpperPath == "")
					appendPath = upperPath + "/" + list.get(i).getTitle();
				else
					appendPath = twoUpperPath + "/" + list.get(i).getTitle();
				
				re(lst, structureAll, fullPathList, appendPath, upperPath);
				appendPath = "";
			}
			
		}
		
	}
	
	/*public void re(List<Structure> list, List<Structure> structureAll, List<FileInfo> fileInfoAll, List<String> fullPathList, String appendPath) {
		for ( int i =0; i < list.size(); i++) {
			long id = list.get(i).getKey();
			
			Children c = new Children();
			
			List<Structure> l = c.getChildrenObj(structureAll, id);

			if ( l.size() == 0) {
				// 파일만 확인
				for ( int k = 0; k < fileInfoAll.size(); k++) {
					if ( id == fileInfoAll.get(k).getStructure_id())
						fullPathList.add(new String(fileInfoAll.get(k).getName() + "." + fileInfoAll.get(k).getExtension()));
					
					if ( k == fileInfoAll.size() - 1 ) {
						appendPath = appendPath + "/" + list.get(i).getTitle();
						fullPathList.add(new String(appendPath));
					}
				}
	//			appendPath = "";
			}
			else {
				// 재귀
				for ( int k = 0; k < l.size(); k++) {
					if ( !appendPath.contains(l.get(k).getTitle())) {
						if ( k == 0)
							appendPath = appendPath + "/" + list.get(i).getTitle();
						
						for ( int j = 0; j < fileInfoAll.size(); j++) {
							if ( id == fileInfoAll.get(j).getStructure_id()) {
							
								fullPathList.add(new String(appendPath + "/" + fileInfoAll.get(j).getName() + "." + fileInfoAll.get(j).getExtension()));
							}
						}
					}
				}
	//			fullPathList.add(new String(appendPath));
				re(l, structureAll, fileInfoAll, fullPathList, appendPath);
				if ( i + 1 > list.size() - 1) {
					
				}
				else {
					appendPath = "";
				}
			}
		}
		
	}*/
	
}

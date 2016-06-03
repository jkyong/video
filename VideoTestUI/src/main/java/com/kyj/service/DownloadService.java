package com.kyj.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kyj.domain.FileInfo;
import com.kyj.domain.Structure;
import com.kyj.persistence.FileInfoDAO;
import com.kyj.persistence.StructureDAO;

@Service
public class DownloadService {

	@Autowired
	private StructureDAO structureDAO;
	
	@Autowired
	private FileInfoDAO fileInfoDAO;
	
	@Resource
	private String createZipPath;
	
	@Resource
	private String tempFilesPath;

	public String createDumpDir(List<String> fullPathList, String authName, String folderName, String parentFolderTitle) {
		String upperPath = null;
		
		if ( parentFolderTitle == "")
			upperPath = tempFilesPath + "/" + authName + "/" + folderName;
		else
			upperPath = tempFilesPath + "/" + authName + "/" + parentFolderTitle + "/" + folderName;
		
		
		File upperFolder = new File(upperPath);
		
		if (!upperFolder.exists()) {
			if (upperFolder.mkdirs()) {
				System.out.println("create directory");
			} else {
				System.out.println("alreay");
			}
		}
		
		for (int k = 0; k < fullPathList.size(); k++) {

			String path = upperPath + "/" + fullPathList.get(k);
			File createFolder = new File(path);

			if (!createFolder.exists()) {
				if (createFolder.mkdirs()) {
					System.out.println("create directory");
				} else {
					System.out.println("alreay");
				}
			}
		}
		return upperPath;
	}

	public void downloadZip(String zipPath, String zipName, HttpServletResponse response) throws IOException {
		
		File file = new File(zipPath);
        InputStream is = new FileInputStream(file);
        
        response.setContentType("application/zip");
        response.setHeader("Content-Disposition", "attachment; filename=\"" + zipName + ".zip" + "\"");
        
        // Read from the file and write into the response
        ServletOutputStream os = response.getOutputStream();
        byte[] buffer = new byte[2048];
//        byte[] buffer = org.apache.commons.io.IOUtils.toByteArray(is);
        int len;
        while ((len = is.read(buffer)) != -1) {
            os.write(buffer, 0, len);
        }
        os.flush();
        os.close();
        is.close();
	}
	

	public String createZip(String dumpDirPath, String authName, String folderName, HttpServletResponse response) {
		List<String> fileList = new ArrayList<>();
		DownloadService down = new DownloadService();
		
		UUID uuid = UUID.randomUUID();
		
		String createdZipPath = createZipPath + "/" + authName;
		String createFullZipPath = createdZipPath + "/" + uuid.toString() + ".zip";
		
		File createZipFolder = new File(createdZipPath);
		
		if (!createZipFolder.exists()) {
			if (createZipFolder.mkdirs()) {
				System.out.println("create directory");
			} else {
				System.out.println("alreay");
			}
		}
		
		down.generateFileList(new File(dumpDirPath), fileList, dumpDirPath);
		down.zipIt(createFullZipPath, fileList, dumpDirPath, response);
		
		return createFullZipPath;
	}
	
	public String delDumpTarget(String dumpDirPath) {
		String[] split = dumpDirPath.split("/");
		int splitSize = split.length;
		int dumpDirSize = dumpDirPath.length();
		
		dumpDirPath = dumpDirPath.substring(0, (dumpDirSize - split[splitSize - 1].length() - 1));
		
		return dumpDirPath;
	}
	
	public void delDumpDir(String dumpDirPath) {
		
		File[] listFile = new File(dumpDirPath).listFiles();
		try {
			if (listFile.length > 0) {
				for (int i = 0; i < listFile.length; i++) {
					if (listFile[i].isFile()) {
						listFile[i].delete();
					} else {
						delDumpDir(listFile[i].getPath());
					}
					listFile[i].delete();
				}
			}
		} catch (Exception e) {
			System.err.println(System.err);
		}
	}
	
	public String upperFilesCopyOnly( String authName, List<FileInfo> downFiles, String parentFolderTitle) {
		String upperDumpCopyPath = tempFilesPath + "/" + authName + "/" + parentFolderTitle;
		
		File upperFolder = new File(upperDumpCopyPath);
		
		if (!upperFolder.exists()) {
			if (upperFolder.mkdirs()) {
				System.out.println("create directory");
			} else {
				System.out.println("alreay");
			}
		}
		
		for (int i = 0; i < downFiles.size(); i++) {
			String realPath = downFiles.get(i).getPath();
			long realId = downFiles.get(i).getId();
			String name = downFiles.get(i).getName();
			String extension = downFiles.get(i).getExtension();

			String sourcePath = realPath + "/" + realId + "_" + name + "." + extension;
			String destinationPath = upperDumpCopyPath + "/" + name + "." + extension;
			System.out.println("s : " + sourcePath);
			System.out.println("d : " + destinationPath);
			Path source = Paths.get(sourcePath);
			Path destination = Paths.get(destinationPath);

			try {
				Files.copy(source, destination);
			} catch (Exception e) {

			}

		}
		
		return upperDumpCopyPath;
	}

	@Transactional
	public void fileCopy(List<HashMap<Object, Object>> mapList, String authName, Structure folderObj,
			List<FileInfo> downFiles, String parentFolderTitle) {
		String id = null;
		String path = null;

		String dumpCopyPath = null;

		if (parentFolderTitle == "")
			dumpCopyPath = tempFilesPath + "/" + authName + "/";
		else {
			dumpCopyPath = tempFilesPath + "/" + authName + "/" + parentFolderTitle + "/";

			if (downFiles != null) {
				for (int i = 0; i < downFiles.size(); i++) {
					String realPath = downFiles.get(i).getPath();
					long realId = downFiles.get(i).getId();
					String name = downFiles.get(i).getName();
					String extension = downFiles.get(i).getExtension();

					String sourcePath = realPath + "/" + realId + "_" + name + "." + extension;
					String destinationPath = dumpCopyPath + name + "." + extension;
					System.out.println("s : " + sourcePath);
					System.out.println("d : " + destinationPath);
					Path source = Paths.get(sourcePath);
					Path destination = Paths.get(destinationPath);

					try {
						Files.copy(source, destination);
					} catch (Exception e) {

					}

				}
			}
		}

		long folderId = folderObj.getKey();

		List<FileInfo> fileInfo = structureDAO.find(folderId).getFileInfo();

		for (int i = 0; i < fileInfo.size(); i++) {
			String realPath = fileInfo.get(i).getPath();
			long realId = fileInfo.get(i).getId();
			String name = fileInfo.get(i).getName();
			String extension = fileInfo.get(i).getExtension();

			String sourcePath = realPath + "/" + realId + "_" + name + "." + extension;
			String destinationPath = dumpCopyPath + folderObj.getTitle() + "/" + name + "." + extension;
			System.out.println("s : " + sourcePath);
			System.out.println("d : " + destinationPath);
			Path source = Paths.get(sourcePath);
			Path destination = Paths.get(destinationPath);

			try {
				Files.copy(source, destination);
			} catch (Exception e) {

			}

		}

		for (int i = 0; i < mapList.size(); i++) {

			for (Map.Entry<Object, Object> map : mapList.get(i).entrySet()) {
				id = new String(map.getKey() + "");
				path = new String(map.getValue() + "");
			}

			List<FileInfo> fileInfos = structureDAO.find(Long.parseLong(id)).getFileInfo();

			for (int k = 0; k < fileInfos.size(); k++) {
				String realPath = fileInfos.get(k).getPath();
				long realId = fileInfos.get(k).getId();
				String name = fileInfos.get(k).getName();
				String extension = fileInfos.get(k).getExtension();

				String sourcePath = realPath + "/" + realId + "_" + name + "." + extension;
				String destinationPath = dumpCopyPath + folderObj.getTitle() + "/" + path + "/" + name + "."
						+ extension;
				System.out.println("s : " + sourcePath);
				System.out.println("d : " + destinationPath);
				Path source = Paths.get(sourcePath);
				Path destination = Paths.get(destinationPath);

				try {
					Files.copy(source, destination);
				} catch (Exception e) {

				}

			}

		}
	}

	public HashMap<String, Object> appendFolderPath(long folderId) {

		List<String> fullPathList = new ArrayList<>();
		String appendPath = "";

		List<HashMap<Object, Object>> mapList = new ArrayList<HashMap<Object, Object>>();
		List<Structure> list = new ArrayList<>();
		list = structureDAO.findByPid(folderId);
		
		subFolders(list, fullPathList, appendPath, mapList);
		
		HashMap<String, Object> returnMap = new HashMap<>();
		returnMap.put("mapList", mapList);
		returnMap.put("fullPathList", fullPathList);
		
		return returnMap;
	}

	public void subFolders(List<Structure> list, List<String> fullPathList,
			String appendPath, List<HashMap<Object, Object>> mapList) {

		for (int i = 0; i < list.size(); i++) {
			long id = list.get(i).getKey();

			List<Structure> lst = structureDAO.findByPid(id);

			// 하위 없음
			if (lst.size() == 0) {
				appendPath = appendPath + "/" + list.get(i).getTitle();
				fullPathList.add(appendPath);
				HashMap<Object, Object> map = new HashMap<>();

				map.put(list.get(i).getKey(), appendPath);
				mapList.add(map);
				appendPath = "";
			} else {
				// 재귀
				appendPath = appendPath + "/" + list.get(i).getTitle();
				// fullPathList.add(appendPath); // 최하위폴더까지 add 한다면 이것을 주석
				HashMap<Object, Object> map = new HashMap<>();

				map.put(list.get(i).getKey(), appendPath);
				mapList.add(map);
				subFoldersRecur(lst, fullPathList, appendPath, mapList);

				appendPath = "";
			}
		}
	}

	public void subFoldersRecur(List<Structure> list, List<String> fullPathList,
			String appendPath, List<HashMap<Object, Object>> mapList) {

		for (int i = 0; i < list.size(); i++) {
			long id = list.get(i).getKey();

			List<Structure> lst = structureDAO.findByPid(id);

			// 하위 없음
			if (lst.size() == 0) {
				appendPath = appendPath + "/" + list.get(i).getTitle();
				fullPathList.add(appendPath);
				HashMap<Object, Object> map = new HashMap<>();

				map.put(list.get(i).getKey(), appendPath);
				mapList.add(map);
				appendPath = appendPath.substring(0, (appendPath.length() - 2 - list.get(i).getTitle().length() + 1));

			} else {
				// 재귀
				appendPath = appendPath + "/" + list.get(i).getTitle();
				// fullPathList.add(appendPath); // 최하위폴더까지 add 한다면 이것을 주석
				HashMap<Object, Object> map = new HashMap<>();

				map.put(list.get(i).getKey(), appendPath);
				mapList.add(map);
				subFoldersRecur(lst, fullPathList, appendPath, mapList);

				String[] split = appendPath.split("/");
				int splitSize = split.length;
//				appendPath = appendPath.substring(0, (appendPath.length() - split[1].length() - 1));
				
				appendPath = appendPath.substring(0, (appendPath.length() - split[splitSize-1].length() - 1));

			}
		}
	}

	public void zipIt(String zipFile, List<String> fileList, String sourcePath, HttpServletResponse response) {

		byte[] buffer = new byte[1024];

		try {

			FileOutputStream fos = new FileOutputStream(zipFile);
			ZipOutputStream zos = new ZipOutputStream(fos);

			System.out.println("Output to Zip : " + zipFile);

			for (String file : fileList) {

				System.out.println("File Added : " + file);
				ZipEntry ze = new ZipEntry(file);
				zos.putNextEntry(ze);

				FileInputStream in = new FileInputStream(sourcePath + File.separator + file);

				int len;
				while ((len = in.read(buffer)) > 0) {
					zos.write(buffer, 0, len);
				}

				in.close();
			}

			zos.closeEntry();
			// remember close it
			zos.close();

			System.out.println("Done");
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}

	public void generateFileList(File node, List<String> fileList, String sourcePath) {

		// add file only
		if (node.isFile()) {
			fileList.add(generateZipEntry(node.getAbsoluteFile().toString(), sourcePath));
		}

		if (node.isDirectory()) {
			String[] subNote = node.list();
			for (String filename : subNote) {
				generateFileList(new File(node, filename), fileList, sourcePath);
			}
		}
	}

	private String generateZipEntry(String file, String sourcePath) {
		return file.substring(sourcePath.length() + 1, file.length());
	}
	
	public void downloadOnlyOneFile(long fileId, HttpServletResponse response) throws IOException {
		FileInfo fi = fileInfoDAO.find(fileId);
		
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
                + fileName + "." +extension + "\"");
        // Read from the file and write into the response
        ServletOutputStream os = response.getOutputStream();
        byte[] buffer = new byte[1024];
        int len;
        while ((len = is.read(buffer)) != -1) {
            os.write(buffer, 0, len);
        }
        os.flush();
        os.close();
        is.close();
	}
	
	public HashMap<String, Object> downloadMultiple(long parentFolderId, String parentFolderTitle, long[] folderId, long[] fileId, HttpServletResponse response) {
		Structure folderObj;
		
		String folderName = null;

		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		String authName = auth.getName(); // get logged in username

		String dumpDirPath = null;
		String zipPath = null;
		String delDumpTarget = null;
		
		HashMap<String, Object> hm = new HashMap<>();
		// 파일만 선택 시
		if (fileId != null && folderId == null) {
			List<FileInfo> fileInfos = fileInfoDAO.partialSelect(parentFolderId);
			List<FileInfo> downFiles = new ArrayList<>();
			for (int i = 0; i < fileInfos.size(); i++) {
				for (int j = 0; j < fileId.length; j++) {

					if (fileInfos.get(i).getId() == fileId[j]) {
						downFiles.add(fileInfos.get(i));
					}
				}
			}
			dumpDirPath = upperFilesCopyOnly(authName, downFiles, parentFolderTitle);
		}

		else {
			// 폴더 선택 시 또는 파일포함 선택 시
			for (int i = 0; i < folderId.length; i++) {
				folderObj = structureDAO.find(folderId[i]);
				folderName = folderObj.getTitle();
				
				hm = appendFolderPath(folderId[i]);
	
				dumpDirPath = createDumpDir(((List<String>) hm.get("fullPathList")), authName, folderName, parentFolderTitle);

				// 폴더만 선택 시
				if ( fileId == null) {
					fileCopy(((List<HashMap<Object, Object>>) hm.get("mapList")), authName, folderObj, null, parentFolderTitle);
				}
				// 폴더, 파일 선택 시
				else {
					List<FileInfo> fileInfos = fileInfoDAO.partialSelect(parentFolderId);
					List<FileInfo> downFiles = new ArrayList<>();
					for ( int j = 0; j < fileInfos.size(); j++) {
						for ( int k = 0; k < fileId.length; k++) {
							if ( fileInfos.get(j).getId() == fileId[k] ) {
								downFiles.add(fileInfos.get(j));
							}
						}
					}
					fileCopy(((List<HashMap<Object, Object>>) hm.get("mapList")), authName, folderObj, downFiles, parentFolderTitle);
				}
			}
		}
		
		String[] split = dumpDirPath.split("/");
		int lastSplitSize = split[split.length - 1].length();
		dumpDirPath = dumpDirPath.substring(0, dumpDirPath.length() - lastSplitSize - 1);
				
		zipPath = createZip(dumpDirPath, authName, folderName, response);
		
		delDumpTarget = delDumpTarget(dumpDirPath);
		delDumpDir(delDumpTarget);

		System.out.println("down full path list : " + ((List<String>) hm.get("fullPathList")));
		System.out.println("map list : " + ((List<HashMap<Object, Object>>) hm.get("mapList")));
		System.out.println("zip path : " + zipPath);
		System.out.println("del dump dir path : " + delDumpTarget);
		HashMap<String, Object> map = new HashMap<>();
		map.put("zipPath", zipPath);
		map.put("zipName", parentFolderTitle);
		
		return map;
	}
	
	public HashMap<String, Object> downloadOnlyOneFolder(long folderId, HttpServletResponse response) {
		Structure folderObj = structureDAO.find(folderId);
		String folderName = folderObj.getTitle();
		
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
	    String authName = auth.getName(); //get logged in username
		
	    HashMap<String, Object> hm = new HashMap<>();
	    
	    hm = appendFolderPath(folderId);
		
		String dumpDirPath = createDumpDir(((List<String>) hm.get("fullPathList")), authName, folderName, "");
		
		fileCopy(((List<HashMap<Object, Object>>) hm.get("mapList")), authName, folderObj, null, "");
		
		String zipPath = createZip(dumpDirPath, authName, folderName, response);
		
		String delDumpTarget = delDumpTarget(dumpDirPath);
		
		delDumpDir(delDumpTarget);
		
		System.out.println("down full path list : " + ((List<String>) hm.get("fullPathList")));
		System.out.println("zip path : " + zipPath);
		System.out.println("del dump dir path : " + delDumpTarget);
		
		HashMap<String, Object> map = new HashMap<>();
		
		map.put("zipPath", zipPath);
		map.put("zipName", folderName);
		
		return map;
	}
}

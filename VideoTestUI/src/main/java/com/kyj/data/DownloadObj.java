package com.kyj.data;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
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

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import com.kyj.domain.FileInfo;
import com.kyj.domain.Structure;
import com.kyj.tree.Children;

public class DownloadObj {

	final static String CREATE_ZIP_PATH = "C:/zzz/download";

	final static String TEMP_FILES = "C:/zzz/temp_files";

	private List<HashMap<Object, Object>> mapList;

	private List<String> fullPathList;

	public DownloadObj() {
	}

	public DownloadObj(List<HashMap<Object, Object>> mapList, List<String> fullPathList) {
		this.mapList = mapList;
		this.fullPathList = fullPathList;
	}

	public List<HashMap<Object, Object>> getMapList() {
		return mapList;
	}

	public List<String> getFullPathList() {
		return fullPathList;
	}

	public String createDumpDir(List<String> fullPathList, String authName, String folderName, String parentFolderTitle) {
		String upperPath = null;
		
		if ( parentFolderTitle == "")
			upperPath = TEMP_FILES + "/" + authName + "/" + folderName;
		else
			upperPath = TEMP_FILES + "/" + authName + "/" + parentFolderTitle + "/" + folderName;
		
		
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
		DownloadObj down = new DownloadObj();
		
		UUID uuid = UUID.randomUUID();
		
		String createZipPath = CREATE_ZIP_PATH + "/" + authName;
		String createFullZipPath = createZipPath + "/" + uuid.toString() + ".zip";
		
		File createZipFolder = new File(createZipPath);
		
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
		String upperDumpCopyPath = TEMP_FILES + "/" + authName + "/" + parentFolderTitle;
		
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

	public void fileCopy(List<HashMap<Object, Object>> mapList, List<FileInfo> fileInfoAll, String authName, Structure folderObj, List<FileInfo> downFiles, String parentFolderTitle) {
		String id = null;
		String path = null;

		String dumpCopyPath = null;
		
		if ( parentFolderTitle == "")
			dumpCopyPath = TEMP_FILES + "/" + authName + "/";
		else {
			dumpCopyPath = TEMP_FILES + "/" + authName + "/" + parentFolderTitle + "/";
			
			if ( downFiles != null) {
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
		
		// 최상위 파일이 있으면 넣기
		for ( int j = 0; j < fileInfoAll.size(); j++) {
			long structureId = fileInfoAll.get(j).getStructure().getKey();
			if ( folderId == structureId) {
				String realPath = fileInfoAll.get(j).getPath();
				long realId = fileInfoAll.get(j).getId();
				String name = fileInfoAll.get(j).getName();
				String extension = fileInfoAll.get(j).getExtension();

				if (folderId == structureId) {
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
			}
		}
		
		for (int i = 0; i < mapList.size(); i++) {

			for (Map.Entry<Object, Object> map : mapList.get(i).entrySet()) {
				id = new String(map.getKey() + "");
				path = new String(map.getValue() + "");
			}

			for (int k = 0; k < fileInfoAll.size(); k++) {
				long structureId = Long.parseLong(id);
				long fileInfoStrId = fileInfoAll.get(k).getStructure().getKey();

				String realPath = fileInfoAll.get(k).getPath();
				long realId = fileInfoAll.get(k).getId();
				String name = fileInfoAll.get(k).getName();
				String extension = fileInfoAll.get(k).getExtension();

				if (fileInfoStrId == structureId) {
					String sourcePath = realPath + "/" + realId + "_" + name + "." + extension;
					String destinationPath = dumpCopyPath + folderObj.getTitle() + "/" + path + "/" + name + "." + extension;
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
	}

	public DownloadObj appendFolderPath(long folderId, List<Structure> structureAll) {

		List<String> fullPathList = new ArrayList<>();
		String appendPath = "";

		List<HashMap<Object, Object>> mapList = new ArrayList<HashMap<Object, Object>>();
		List<Structure> list = new ArrayList<>();
		Children c = new Children();
		list = c.getChildrenObj(structureAll, folderId);

		subFolders(list, structureAll, fullPathList, appendPath, mapList);

		return new DownloadObj(mapList, fullPathList);
	}

	public void subFolders(List<Structure> list, List<Structure> structureAll, List<String> fullPathList,
			String appendPath, List<HashMap<Object, Object>> mapList) {

		for (int i = 0; i < list.size(); i++) {
			long id = list.get(i).getKey();

			Children c = new Children();

			List<Structure> lst = c.getChildrenObj(structureAll, id);

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
				subFoldersRecur(lst, structureAll, fullPathList, appendPath, mapList);

				appendPath = "";
			}
		}
	}

	public void subFoldersRecur(List<Structure> list, List<Structure> structureAll, List<String> fullPathList,
			String appendPath, List<HashMap<Object, Object>> mapList) {

		for (int i = 0; i < list.size(); i++) {
			long id = list.get(i).getKey();

			Children c = new Children();

			List<Structure> lst = c.getChildrenObj(structureAll, id);

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
				subFoldersRecur(lst, structureAll, fullPathList, appendPath, mapList);

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
	
	public void downOnlyOneFile(FileInfo fi, HttpServletResponse response) throws IOException {
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
}

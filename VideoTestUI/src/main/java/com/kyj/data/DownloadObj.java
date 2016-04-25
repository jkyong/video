package com.kyj.data;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.springframework.beans.factory.annotation.Autowired;

import com.kyj.domain.FileInfo;
import com.kyj.domain.Structure;
import com.kyj.persistence.FileInfoDAO;
import com.kyj.persistence.StructureDAO;

public class DownloadObj {
	@Autowired
	private StructureDAO structure;
	
	@Autowired
	private FileInfoDAO fileInfo;
	
	public void dd(long folderId) {
		Structure parentId = structure.find(folderId);
		
	
		System.out.println(parentId.getChildren());
	
	}
	
	public void multiDownload() {
		try {
			FileOutputStream fos = new FileOutputStream("E://atest.zip");
			ZipOutputStream zos = new ZipOutputStream(fos);

			String file1Name = "E://file1.txt";
			String file2Name = "E://file2.txt";
			String file3Name = "E://folder/file3.txt";
			String file4Name = "E://folder/file4.txt";
			String file5Name = "E://f1/f2/f3/file5.txt";

			addToZipFile(file1Name, zos);
			addToZipFile(file2Name, zos);
			addToZipFile(file3Name, zos);
			addToZipFile(file4Name, zos);
			addToZipFile(file5Name, zos);

			zos.close();
			fos.close();

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void addToZipFile(String fileName, ZipOutputStream zos) throws FileNotFoundException, IOException {

		System.out.println("Writing '" + fileName + "' to zip file");

		File file = new File(fileName);
		FileInputStream fis = new FileInputStream(file);
		ZipEntry zipEntry = new ZipEntry(fileName);
		zos.putNextEntry(zipEntry);

		byte[] bytes = new byte[1024];
		int length;
		while ((length = fis.read(bytes)) >= 0) {
			zos.write(bytes, 0, length);
		}

		zos.closeEntry();
		fis.close();
	}
	

}

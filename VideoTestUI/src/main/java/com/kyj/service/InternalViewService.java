package com.kyj.service;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kyj.domain.FileInfo;
import com.kyj.persistence.FileInfoDAO;

@Service
@Transactional
public class InternalViewService {
	
	@Autowired
	private FileInfoDAO fileInfoDAO;
	
	public void playInternalView(long id, HttpServletResponse response) {
		FileInfo fileInfo = fileInfoDAO.find(id);
		
		String path = fileInfo.getPath();
		String key = String.valueOf(fileInfo.getId());
		String name = fileInfo.getName();
		String extension = fileInfo.getExtension();
		
		StringBuffer sb = new StringBuffer();
		
		sb.append(path);
		sb.append("/");
		sb.append(key);
		sb.append("_");
		sb.append(name);
		sb.append(".");
		sb.append(extension);

		try {
			response.setContentType("video/mp4");

			InputStream is = new FileInputStream(sb.toString());

/*			byte[] b = org.apache.commons.io.IOUtils.toByteArray(is);
//			byte[] b = new byte[1024];
			
			ServletOutputStream sos = response.getOutputStream();
			sos.write(b);
			sos.flush();
			sos.close();*/
			
//	        ServletOutputStream os = response.getOutputStream();
	        OutputStream os = response.getOutputStream();
	        byte[] buffer = new byte[1024];
	        int len;
	        while ((len = is.read(buffer)) != -1) {
	            os.write(buffer, 0, len);
	        }
	        os.flush();
	        os.close();
	        is.close();

		} catch (IOException e) {
		
		}
		System.out.println("video ready");
	}
}

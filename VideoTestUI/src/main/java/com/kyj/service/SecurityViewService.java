package com.kyj.service;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.kyj.domain.Authority;
import com.kyj.domain.ExternalFile;
import com.kyj.domain.FileInfo;
import com.kyj.persistence.ExternalFileDAO;
import com.kyj.persistence.FileInfoDAO;

@Service
public class SecurityViewService {
	@Autowired
	private FileInfoDAO fileInfo;

	@Autowired
	private ExternalFileDAO externalFile;
	
	public ExternalFile findByExternal(String external) {
		return externalFile.findByExternal(external);
	}
	
	public ExternalFile findByUri(String uri) {
		return externalFile.findByUri(uri);
	}
	
	@Transactional
	public void playVideo(String uri, String external, HttpServletResponse response) {
		ExternalFile exFile = null;
		System.out.println("play video uri : " + uri);
		System.out.println("play video external : " + external);
		
		// public
		if ( external.equals("null")) {
			exFile = externalFile.findByUri(uri);
		}
		// private
		else {
			exFile = externalFile.findByExternal(external);
			System.out.println("in");
		}
		
		System.out.println(exFile);
//		ExternalFile exFile = externalFile.findByExternal(external);
		FileInfo parentFile = exFile.getFileInfo();
		
		String path = parentFile.getPath();
		String key = String.valueOf(parentFile.getId());
		String name = parentFile.getName();
		String extension = parentFile.getExtension();
		
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

			byte[] b = org.apache.commons.io.IOUtils.toByteArray(is);
			ServletOutputStream sos = response.getOutputStream();
			sos.write(b);
			sos.flush();

		} catch (IOException e) {
			
		}
		System.out.println("video ready");
	}
	
	public String validUri(String uri, Model model, RedirectAttributes rttr) {
		ExternalFile extern = externalFile.findByUri(uri);
		
		if ( extern != null) {
			if ( extern.getAccess().equals("private") ) {
				model.addAttribute("uri", uri);
				model.addAttribute("external", extern.getExternal());
				
				return "securityPage/authorityPage";
			}
			else if ( extern.getAccess().equals("public")) {
				model.addAttribute("uri", uri);
				model.addAttribute("external", "null");
				
				return "securityPage/securityView";
			}
		}
		else {
			return "securityPage/accessDenied";
		}
		
		return null;
	}
	
	public boolean validVideoFormat(long id) {
		FileInfo f = fileInfo.find(id);
		
		if ( f.getExtension().equals("mp4")) 
			return true;
		else 
			return false;
	}
	
	public String guestUriPost(Authority authority, String uri, SecurityViewService service, Model model) {
		if ( authority != null) {
			if ( authority.getPassword() != null) {
				ExternalFile ex = service.findByExternal(authority.getPassword());
				
				if ( ex == null)
					return "redirect:/guest" + "/" + uri;
				else {
					model.addAttribute("uri", uri);
					model.addAttribute("external", ex.getExternal());
					
					return "securityPage/securityView";
				}
			}
			else 
				return "redirect:/access/denied";			
		}
		else
			return "redirect:/access/denied";
	}
	
	// 초 분 시 일 월 요일 년
	// 0 0 * * * * * ??????????
	@Scheduled(cron = "0 17 11 * * ?")
	public void updateExternal() {
		List<FileInfo> videoList = fileInfo.selectVideoFormat();
		
		for ( int i = 0; i < videoList.size(); i++) {
			long externalId = videoList.get(i).getId();
			
		//	fileInfo.updateExternal(externalId);
		}
	}
	
}

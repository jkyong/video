package com.kyj.controller;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.kyj.domain.Authority;
import com.kyj.domain.ExternalFile;
import com.kyj.domain.FileInfo;
import com.kyj.persistence.ExternalFileDAO;
import com.kyj.persistence.FileInfoDAO;
import com.kyj.service.SecurityViewService;

@Controller
public class SecurityViewController {
	
	@Autowired
	private SecurityViewService service;
	
	@Autowired
	private FileInfoDAO fileInfo;
	
	@Autowired
	private ExternalFileDAO externalFile;
	
	@RequestMapping(value = "guest/private", method = RequestMethod.GET)
	public String guestViews() {
		
		return "securityPage/authorityPage";
	}
	
	@RequestMapping(value = "access/denied", method = RequestMethod.GET)
	public String accessDenied() {
		return "securityPage/accessDenied";
	}
	
	@RequestMapping(value = "security/view", method = RequestMethod.GET)
	public String securityView() {
		return "securityPage/securityView";
	}
	
	@RequestMapping(value = "guest/view/{uri}/{external}", method = { RequestMethod.GET, RequestMethod.POST})
	public String authorityPage(@Valid Authority authority, @PathVariable("uri") String uri, @PathVariable("external") String external, Model model) {
		String externalPW = null;

		if ( authority != null) {
			if ( authority.getPassword() != null) {
				externalPW = authority.getPassword();
				
				ExternalFile ex = service.findByExternal(externalPW);
				FileInfo fileInfo = ex.getFileInfo();
				
				if ( fileInfo != null) {
			//		String privateURI = "guest/private/" + externalPW;
					String privateURI = "guest" + "/" + uri;
					
//					model.addAttribute("accessURI", privateURI);
//					model.addAttribute("access", "private");
					model.addAttribute("uri", uri);
					model.addAttribute("external", external);
					System.out.println("success vvvvvvv");	
					return "redirect:/security/view";
				}
				else {
					return "redirect:/access/denied";
				}
			}
			else {
				return "redirect:/access/denied";
			}
		}
		else
			return "redirect:/access/denied";
	}
	
	
	@RequestMapping(value = "validExtension", method = RequestMethod.GET) 
	public @ResponseBody boolean validExtension(@RequestParam long id) {
		FileInfo f = fileInfo.find(id);
		
		if ( f.getExtension().equals("mp4")) {
			return true;
		}
		else {
			return false;
		}
		
	}
	
/*	@RequestMapping(value = "external/{external}", method = RequestMethod.GET)
	public void external(@PathVariable("external") String external, HttpServletResponse response) {
		service.playVideo(external, response);
	}*/
	
	@RequestMapping(value = "externalSave", method = RequestMethod.GET)
	public @ResponseBody ExternalFile externalSave(@Valid ExternalFile externFile, @RequestParam long fileId) {
		System.out.println("access : " + externFile.getAccess());
		System.out.println("start date : " + externFile.getStartDate());
		System.out.println("end date : " + externFile.getEndDate());
		System.out.println("memo : " + externFile.getMemo());
		System.out.println("uri : " + externFile.getUri());
		System.out.println("file id : " + fileId);
		
		return externalFile.save(fileId, externFile);
	}
	
	/*@RequestMapping(value = "guest/public/{external}", method = RequestMethod.GET)
	public String guestPublic(@PathVariable("external") String external, Model model, HttpServletResponse response) {
		String publicURI = "guest/public/" + external;
		
		model.addAttribute("accessURI", publicURI);
		model.addAttribute("access", "public");
		
		return "securityPage/securityView";
	}*/
	
/*	@RequestMapping(value = "guest/public/{external}/public", method = RequestMethod.GET)
	public void guestPublicView(@PathVariable("external") String external, HttpServletResponse response) {
		service.playVideo(external, response);
	}
	
	@RequestMapping(value = "guest/private/{external}/private", method = RequestMethod.GET)
	public void guestPrivate(@PathVariable("external") String external, HttpServletResponse response) {
		service.playVideo(external, response);
	}*/
	
	@RequestMapping(value = "guest/{uri}", method = RequestMethod.GET)
	public String guestUri(@PathVariable("uri") String uri, Model model, RedirectAttributes rttr) {
		return service.validUri(uri, model, rttr);
	}
	
	@RequestMapping(value = "guest/{uri}", method = RequestMethod.POST)
	public String guestUriPost(@Valid Authority authority, @PathVariable("uri") String uri, Model model) {
		String externalPW = null;

		if ( authority != null) {
			if ( authority.getPassword() != null) {
				externalPW = authority.getPassword();
				
				ExternalFile ex = service.findByExternal(externalPW);
				FileInfo fileInfo = ex.getFileInfo();
				
				if ( fileInfo != null) {
			//		String privateURI = "guest/private/" + externalPW;
					String privateURI = "guest" + "/" + uri;
					
//					model.addAttribute("accessURI", privateURI);
//					model.addAttribute("access", "private");
					model.addAttribute("uri", uri);
					model.addAttribute("external", ex.getExternal());
					System.out.println("success vvvvvvv");	
					return "securityPage/securityView";
				}
				else {
					return "redirect:/access/denied";
				}
			}
			else {
				return "redirect:/access/denied";
			}
		}
		else
			return "redirect:/access/denied";
	}
	
	@RequestMapping(value = "play/video/{uri}/{external}", method = RequestMethod.GET)
	public void playVideo(@PathVariable("uri") String uri, @PathVariable("external") String external, HttpServletResponse response) {
	
		System.out.println("uri : " + uri);
		System.out.println("external : " + external);
		service.playVideo(uri, external, response);
	}
}

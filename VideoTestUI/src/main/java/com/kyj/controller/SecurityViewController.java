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
import com.kyj.persistence.ExternalFileDAO;
import com.kyj.service.SecurityViewService;

@Controller
public class SecurityViewController {
	
	@Autowired
	private SecurityViewService service;
	
	@Autowired
	private ExternalFileDAO externalFile;
	
	@RequestMapping(value = "validExtension", method = RequestMethod.GET) 
	public @ResponseBody boolean validExtension(@RequestParam long id) {
		return service.validVideoFormat(id);
	}
	
	@RequestMapping(value = "externalSave", method = RequestMethod.GET)
	public @ResponseBody ExternalFile externalSave(@Valid ExternalFile externFile, @RequestParam long fileId) {
		return externalFile.save(fileId, externFile);
	}
	
	@RequestMapping(value = "guest/{uri}", method = RequestMethod.GET)
	public String guestUri(@PathVariable("uri") String uri, Model model, RedirectAttributes rttr) {
		return service.validUri(uri, model, rttr);
	}
	
	@RequestMapping(value = "guest/{uri}", method = RequestMethod.POST)
	public String guestUriPost(@Valid Authority authority, @PathVariable("uri") String uri, Model model) {
		return service.guestUriPost(authority, uri, service, model);
	}
	
	@RequestMapping(value = "play/video/{uri}/{external}", method = RequestMethod.GET)
	public void playVideo(@PathVariable("uri") String uri, @PathVariable("external") String external, HttpServletResponse response) {
		service.playVideo(uri, external, response);
	}
}

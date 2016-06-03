package com.kyj.controller;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.kyj.service.InternalViewService;

@Controller
public class InternalViewController {
	
	@Autowired
	private InternalViewService internalViewService;
	
	@RequestMapping(value = "play/internal/view", method = RequestMethod.GET)
	public String playView(@RequestParam long id, Model model) {
		model.addAttribute("id", id);
		
		return "control/internalVideoPlay";
	}
	
	@RequestMapping(value = "play/internal/view/{id}", method = RequestMethod.GET)
	public void playViewGo(@PathVariable("id") long id, HttpServletResponse response) {
		internalViewService.playInternalView(id, response);
	}
}

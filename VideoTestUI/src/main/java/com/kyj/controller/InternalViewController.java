package com.kyj.controller;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.View;

import com.kyj.service.InternalViewService;

@Controller
public class InternalViewController {
	
	@Resource(name="streamView")
	View streamView;
	
	@RequestMapping(value = "play/internal/view", method = RequestMethod.GET)
	public String playView(@RequestParam long id, Model model) {
		model.addAttribute("id", id);
		
		return "control/internalVideoPlay";
	}
	
	@RequestMapping(value = "play/internal/view/{id}", method = RequestMethod.GET)
	public ModelAndView playViewGo(@PathVariable("id") Long id, HttpServletRequest request,  HttpServletResponse response) throws Exception {
		
		return new ModelAndView(this.streamView, "id", id);
	}
}

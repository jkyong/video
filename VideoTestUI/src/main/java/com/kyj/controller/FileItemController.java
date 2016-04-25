package com.kyj.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class FileItemController {
	@RequestMapping(value="/fileItem", method = RequestMethod.GET)
	public String fileItem() {
		return "control/fileItem";
	}
}

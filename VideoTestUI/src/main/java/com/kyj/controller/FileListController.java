package com.kyj.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.kyj.domain.FileInfo;
import com.kyj.domain.Structure;
import com.kyj.persistence.StructureDAO;
import com.kyj.repository.StructureRepository;

@Controller
public class FileListController {
	
	@Autowired
	private StructureDAO structure;
	
	@Autowired
	private StructureRepository structureRepository; 
	
	@RequestMapping(value = "/fileList", method = RequestMethod.GET)
	public @ResponseBody List<Structure> fileList(@RequestParam("id") long id) {
		return structureRepository.findByPid(id); 
	}
	
	@RequestMapping(value = "/uploadFileAppend", method = RequestMethod.GET)
	public @ResponseBody List<FileInfo> uploadFileAppend(@RequestParam long selectedId) {
		Structure s = structure.find(selectedId);
		List<FileInfo> fi = new ArrayList<>();
		
		if ( s != null)
			fi = s.getFileInfo();
		
	/*	for(FileInfo f : s.getFileInfo()) {
			System.out.println(f);
		}*/
		
//		System.out.println("file info size : " + fi.size());
		
		return fi;		
	}
		
}

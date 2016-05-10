package com.kyj.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.kyj.domain.FileInfo;
import com.kyj.domain.Structure;
import com.kyj.persistence.StructureDAO;
import com.kyj.tree.Children;

@Controller
public class FileListController {
	
	@Autowired
	private StructureDAO structure;
	
	private Children children;
	
	@RequestMapping(value = "/fileList", method = RequestMethod.GET)
	public @ResponseBody List<Structure> fileList(@RequestParam("id") long id) {
		List<Structure> structureList = structure.findAll();
		List<Structure> childrenList = new ArrayList<>();
		
		long selectedId = id;
		
		children = new Children();
		
		System.out.println("selectedId : " + selectedId);
		
		System.out.println("structureList size : " + structureList.size());
		System.out.println("selected children : " + children.getChildrenObj(structureList, selectedId).size());
		
		childrenList = children.getChildrenObj(structureList, selectedId);
		
		return childrenList;
	}
	
	@RequestMapping(value = "/uploadFileAppend", method = RequestMethod.GET)
	public @ResponseBody List<FileInfo> uploadFileAppend(@RequestParam long selectedId) {
		Structure s = structure.find(selectedId);
		List<FileInfo> fi = new ArrayList<>();
		
		if ( s != null)
			fi = s.getFileInfo();
		
		/*for(FileInfo fi : s.getFileInfo()) {
			System.out.println(fi);
		}*/
		
//		System.out.println("file info size : " + fi.size());
		
		return fi;		
	}
		
}

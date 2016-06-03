package com.kyj.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.kyj.domain.StructureForTree;
import com.kyj.persistence.StructureDAO;
import com.kyj.tree.Recursion;

@Controller
public class TreeController {

	@Autowired 
	private StructureDAO file;
		
	private Recursion recursion;
	
	@RequestMapping(value="/db", method= RequestMethod.GET)
	public @ResponseBody List<StructureForTree> db() {
//		List<Structure> dataList = file.findAll();
		List<StructureForTree> dataList = file.getAll();
//		List<Structure> result = new ArrayList<>();
		List<StructureForTree> result = new ArrayList<>();
		recursion = new Recursion();

		result = recursion.addRootTree(result, dataList);
		
//		System.out.println("dataList : " + dataList + "result : " + result);
		return result;		
	}
}

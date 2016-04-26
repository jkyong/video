package com.kyj.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.kyj.domain.Structure;
import com.kyj.persistence.StructureDAO;
import com.kyj.tree.Recursion;


/**
 * Handles requests for the application home page.
 */
@Controller
public class HomeController {
	
	@Autowired 
	private StructureDAO file;
		
	private Recursion recursion;
	
	private static final Logger logger = LoggerFactory.getLogger(HomeController.class);
	
	/**
	 * Simply selects the home view to render by returning its name.
	 */
	
	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String login() {
	
		return "login";
	}
	
	@RequestMapping(value = "/home", method = RequestMethod.GET)
	public String home() {
	
		return "home";
	}
	
/*	@RequestMapping(value = "/test", method = RequestMethod.GET)
	public String test() {
			
		System.out.println(file.getAllFiles());
		List<File> list = file.getAllFiles();
		System.out.println(list.get(0).getName());
		return "test";
	}*/
	
	@RequestMapping(value = "/zzz", method = RequestMethod.GET)
	public String zzz() {
		return "zzz";
	}
	
	@RequestMapping(value = "/json", method = RequestMethod.GET)
	public @ResponseBody List<HashMap<Integer, Object>> json() {
		List<Structure> list = file.findAll();
		List<HashMap<Integer, Object>> test= new ArrayList();
		HashMap<Integer, Object> hm = new HashMap<Integer, Object>();
//		hm.put(list.get(0).getId(), list.get(0).getName().toString());
		System.out.println("json ");
		test.add(hm);
		return test;		
	}
	
	@RequestMapping(value = "/books", method = RequestMethod.GET)
	public @ResponseBody List<HashMap<String, Object>> books() {
	//	List<File> list = file.getAllFiles();
		List<HashMap<String, Object>> test= new ArrayList();
		HashMap<String, String> hm = new HashMap<String, String>();
		HashMap<String, Object> hm2 = new HashMap<String, Object>();
		
		List<HashMap<String, String>> test2= new ArrayList();
		
		hm.put("title", "Electronics & Computers");
		hm.put("expanded", "true");
		hm.put("folder", "true");
		test2.add(hm);
		hm2.put("title", "Books & Audible");
		hm2.put("expanded", "true");
		hm2.put("folder", "true");
		hm2.put("children", test2);
		
	/*	hm.put(11, "Books");
		hm.put(111, "Books");
		hm.put(12, "Movies, TV, Music, Games");
		hm.put(121, "Music");
		hm.put(122, "MP3 Downloads");
		hm.put(13, "Electronicss & Computers");
		hm.put(131, "Electronics");
		hm.put(1311, "Camera & Photo");
		hm.put(1312, "TV & Home Cinema");*/
	//	hm.put(list.get(0).getId(), list.get(0).getName().toString());
		System.out.println("book ");
		test.add(hm2);
		return test;		
	}
	
/*	@RequestMapping(value = "/db", method = RequestMethod.GET)
	public @ResponseBody List<Structure> db() {
		List<Structure> dataList = file.getAllFiles();
		List<Structure> result = new ArrayList<Structure>();
		
		result = recursion.addTree(result, dataList);
		
		return result;		
	}*/
	
	

	
	
}

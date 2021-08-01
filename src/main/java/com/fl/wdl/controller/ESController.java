package com.fl.wdl.controller;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class ESController {
	
	@Autowired
	com.fl.wdl.service.ESService ESService;
	
	@GetMapping({"/","/index"})
	public String index() {
		return "index";
	}
	
	@GetMapping("/search/{keyword}/{page}")
	@ResponseBody
	public List<Map<String,Object>> search(@PathVariable("keyword")String keyword, @PathVariable("page")int page) throws IOException{
		return ESService.search(keyword, page);
	}
}

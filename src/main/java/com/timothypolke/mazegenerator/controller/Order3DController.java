package com.timothypolke.mazegenerator.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.timothypolke.mazegenerator.entity.Order3D;
import com.timothypolke.mazegenerator.entity.Size3D;
import com.timothypolke.mazegenerator.service.IOrder3DService;
import com.timothypolke.mazegenerator.service.ISize3DService;

@Controller
@RequestMapping("orders3D/")
public class Order3DController {
	@Autowired
	private IOrder3DService orderService;
	@Autowired
	private ISize3DService sizeService;
	
	@RequestMapping(value="add")
	public ModelAndView add(){
		Map<String, Object> mapModel = new HashMap<String, Object>();
		List<Size3D> sizes = sizeService.readAll();
		mapModel.put("sizes", sizes);
		mapModel.put("order", new Order3D());
		return new ModelAndView("register3DOrder",mapModel);
	}
	
	@RequestMapping(value="save",method=RequestMethod.POST)
	public ModelAndView save(Model model,@ModelAttribute("order") Order3D order){
		orderService.createOrUpdate(order);
		model.addAttribute("order",order);
		return new ModelAndView("redirect:../orders3D/");
	}
	
	@RequestMapping(value="")
	public ModelAndView list(Model model){
		model.addAttribute("listorders", orderService.readAll());
		return new ModelAndView("list3DOrders","",model);
	}
	
	@RequestMapping(value="delete/{id}")
	public ModelAndView delete(@PathVariable("id") UUID id, Model model){
		orderService.delete(orderService.read(id));
		return new ModelAndView("redirect:..");
	}
	
	@RequestMapping(value="send/{id}")
	public ModelAndView send(@PathVariable("id") UUID id, Model model){
		orderService.deliver(orderService.read(id));
		return new ModelAndView("redirect:..");
	}
	
	@RequestMapping(value="downloadPuzzles/{id}")
	public ResponseEntity downloadPuzzles(@PathVariable("id") UUID id, Model model){
		return ResponseEntity.ok()
			.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"puzzle3D.zip\"")
			.contentType(MediaType.APPLICATION_OCTET_STREAM)
			.body(orderService.downloadPuzzles(orderService.read(id)));
	}
	
	@RequestMapping(value="downloadSolutions/{id}")
	public ResponseEntity downloadSolutions(@PathVariable("id") UUID id, Model model){
		return ResponseEntity.ok()
			.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"solution3D.zip\"")
			.contentType(MediaType.APPLICATION_OCTET_STREAM)
			.body(orderService.downloadSolutions(orderService.read(id)));
	}
}
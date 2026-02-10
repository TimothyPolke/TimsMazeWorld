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

import com.timothypolke.mazegenerator.entity.Order2D;
import com.timothypolke.mazegenerator.service.IOrder2DService;

@Controller
@RequestMapping("orders2D/")
public class Order2DController {
	@Autowired
	private IOrder2DService orderService;
	
	@RequestMapping(value="add")
	public ModelAndView add(Model model){
		Map<String, Object> mapModel = new HashMap<String, Object>();
		mapModel.put("order", new Order2D());
		return new ModelAndView("register2DOrder",mapModel);
	}
	
	@RequestMapping(value="save",method=RequestMethod.POST)
	public ModelAndView save(Model model,@ModelAttribute("order") Order2D order){
		orderService.createOrUpdate(order);
		model.addAttribute("order",order);
		return new ModelAndView("redirect:../orders2D/");
	}
	
	@RequestMapping(value="")
	public ModelAndView list(Model model){
		model.addAttribute("listorders", orderService.readAll());
		return new ModelAndView("list2DOrders","",model);
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
			.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"2DPuzzles.zip\"")
			.contentType(MediaType.APPLICATION_OCTET_STREAM)
			.body(orderService.downloadPuzzles(orderService.read(id)));
	}
	
	@RequestMapping(value="downloadSolutions/{id}")
	public ResponseEntity downloadSolutions(@PathVariable("id") UUID id, Model model){
		return ResponseEntity.ok()
			.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"2DSolutions.zip\"")
			.contentType(MediaType.APPLICATION_OCTET_STREAM)
			.body(orderService.downloadSolutions(orderService.read(id)));
	}
}
package com.timothypolke.mazegenerator.controller;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.timothypolke.mazegenerator.entity.Size2D;
import com.timothypolke.mazegenerator.service.ISize2DService;

@Controller
@RequestMapping("orders2D/sizes/")
public class Size2DController {
	@Autowired
	ISize2DService sizeService;
	
	@RequestMapping(value="add")
	public ModelAndView add(Model model){
		model.addAttribute("size",new Size2D());
		return new ModelAndView("register2DSize","",model);
	}
	
	@RequestMapping(value="save",method=RequestMethod.POST)
	public ModelAndView save(Model model,@ModelAttribute("size") Size2D size){
		sizeService.createOrUpdate(size);
		model.addAttribute("size",size);
		return new ModelAndView("redirect:../sizes/");
	}
	
	@RequestMapping(value="")
	public ModelAndView list(Model model){
		model.addAttribute("listSizes", sizeService.readAll());
		return new ModelAndView("list2DSizes","",model);
	}
	
	@RequestMapping(value="edit/{id}")
	public ModelAndView edit(@PathVariable("id") UUID id, Model model){
		model.addAttribute(sizeService.read(id));
		return new ModelAndView("register2DSize","",model);
	}
	
	@RequestMapping(value="delete/{id}")
	public ModelAndView delete(@PathVariable("id") UUID id, Model model){
		sizeService.delete(sizeService.read(id));
		return new ModelAndView("redirect:..");
	}
}
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

import com.timothypolke.mazegenerator.entity.Theme2D;
import com.timothypolke.mazegenerator.service.ITheme2DService;

@Controller
@RequestMapping("orders2D/themes/")
public class Theme2DController {
	@Autowired
	private ITheme2DService themeService;
	
	@RequestMapping(value="add")
	public ModelAndView add(Model model){
		model.addAttribute("theme",new Theme2D());
		return new ModelAndView("register2DTheme","",model);
	}
	
	@RequestMapping(value="save",method=RequestMethod.POST)
	public ModelAndView save(Model model,@ModelAttribute("theme") Theme2D theme){
		themeService.createOrUpdate(theme);
		model.addAttribute("theme",theme);
		return new ModelAndView("redirect:../themes/");
	}
	
	@RequestMapping(value="")
	public ModelAndView list(Model model){
		model.addAttribute("listThemes", themeService.readAll());
		return new ModelAndView("list2DThemes","",model);
	}
	
	@RequestMapping(value="edit/{id}")
	public ModelAndView edit(@PathVariable("id") UUID id, Model model){
		model.addAttribute("theme", themeService.read(id));
		return new ModelAndView("register2DTheme","",model);
	}
	
	@RequestMapping(value="delete/{id}")
	public ModelAndView delete(@PathVariable("id") UUID id, Model model){
		themeService.delete(themeService.read(id));
		return new ModelAndView("redirect:..");
	}
}
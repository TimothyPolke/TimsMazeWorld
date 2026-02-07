package com.timothypolke.mazegenerator.service;

import java.util.UUID;
import java.util.List;
import java.util.ArrayList;

import com.timothypolke.mazegenerator.misc.Downloader;
import com.timothypolke.mazegenerator.misc.Mailer;

import jakarta.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.timothypolke.mazegenerator.entity.Order2D;
import com.timothypolke.mazegenerator.dao.Order2DDAO;
import com.timothypolke.mazegenerator.entity.Size2D;
import com.timothypolke.mazegenerator.entity.Theme2D;
import com.timothypolke.mazegenerator.misc.Puzzle2D;
import com.timothypolke.mazegenerator.misc.HexToColorConverter;

@Service
@Transactional
public class Order2DService implements IOrder2DService {
	
	@Autowired
	Size2DService size2DService;
	@Autowired
	Theme2DService themeService;
	
	@Autowired
	Order2DDAO orderDAO;

	@Override
	public void createOrUpdate(Order2D order) {

		HexToColorConverter converter = new HexToColorConverter();

		List<Size2D> sizes = size2DService.readAll();
        for (Size2D size : sizes) {
            if (size.getSizeID().equals(order.getSize().getSizeID())) {
                order.setSize(size);
            }
        }
		List<Theme2D> themes = themeService.readAll();
        for (Theme2D theme : themes) {
            if (theme.getThemeID().equals(order.getTheme().getThemeID())) {
                order.setTheme(theme);
            }
        }

		ArrayList<String> puzzles2DSolved = new ArrayList<>();
		ArrayList<String> puzzles2DUnsolved = new ArrayList<>();
		Puzzle2D puzzle2D;
		for (int i=0;i<order.getOrderQuantity();i++){
			puzzle2D = new Puzzle2D(Integer.parseInt(order.getSize().getColumnCount()), Integer.parseInt(order.getSize().getRowCount()), Integer.parseInt(order.getSize().getWallSize()), Integer.parseInt(order.getSize().getCellSize()), converter.hexToColor(order.getTheme().getForeground()), converter.hexToColor(order.getTheme().getBackground()), converter.hexToColor(order.getTheme().getHighlight()));
			puzzles2DSolved.add(puzzle2D.getSolved());
			puzzles2DUnsolved.add(puzzle2D.getUnsolved());
		}
		order.setSolvedImages(puzzles2DSolved);
		order.setUnsolvedImages(puzzles2DUnsolved);

		orderDAO.save(order);
	}

	@Override
	public void delete(Order2D order) {
		orderDAO.delete(order);
	}

	@Override
	public Order2D read(UUID id) {
		return orderDAO.getReferenceById(id);
	}	
	
	@Override
	public List<Order2D> readAll() {
		return orderDAO.findAll();
	}

	@Override
	public void deliver(Order2D order) {
		Downloader downloader = new Downloader();
		
		ArrayList<String> recipients = new ArrayList<>();
		recipients.add(order.getOrderEmailAddress());
		
		Mailer mailer = new Mailer("tim.mazegenerator@gmail.com","vmnu jyat wudp tvej","smtp.gmail.com","465",recipients,String.format("2D Maze Delivery: %s",order.getOrderID()),"I hope you enjoy! -TIM",downloader.serialize(order.getUnsolvedImages(), "2DPuzzles", "png"),downloader.serialize(order.getSolvedImages(), "2DSolutions", "png"));
		mailer.send();
		
		fullfill(order,true);
	}
	
	@Override
	public byte[] downloadPuzzles(Order2D order) {
		Downloader downloader = new Downloader();		
		return downloader.serialize(order.getUnsolvedImages(), "Puzzles", "png");
	}
	
	@Override
	public byte[] downloadSolutions(Order2D order) {
		Downloader downloader = new Downloader();
		return downloader.serialize(order.getSolvedImages(), "Solutions", "png");
	}
	
	@Override
	public void fullfill(Order2D order, boolean result) {
		order.setFullfilled(result);
	}

}
package com.timothypolke.mazegenerator.service;

import java.io.File;
import java.io.InputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;
import java.util.UUID;
import java.util.List;
import java.util.ArrayList;

import com.timothypolke.mazegenerator.misc.Downloader;
import com.timothypolke.mazegenerator.misc.Mailer;

import jakarta.transaction.Transactional;

import org.springframework.core.io.InputStreamResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.timothypolke.mazegenerator.entity.Order2D;
import com.timothypolke.mazegenerator.dao.Order2DDAO;
import com.timothypolke.mazegenerator.misc.Puzzle2D;
import com.timothypolke.mazegenerator.misc.HexToColorConverter;

@Service
@Transactional
public class Order2DService implements IOrder2DService {
	@Autowired
	Order2DDAO orderDAO;
	
	@Override
	public void createOrUpdate(Order2D order) {
		InputStream inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream("config.properties");
		Properties properties = new Properties();
		try{
			properties.load(inputStream);
				}
		catch(IOException e){
			e.printStackTrace();
		}
		
		orderDAO.save(order);
		
		HexToColorConverter converter = new HexToColorConverter();
		ArrayList<String> puzzles2DSolved = new ArrayList<>();
		ArrayList<String> puzzles2DUnsolved = new ArrayList<>();
		Puzzle2D puzzle2D;
		for (int i=0;i<order.getOrderQuantity();i++){
			puzzle2D = new Puzzle2D(Integer.parseInt(order.getColumnCount()), Integer.parseInt(order.getRowCount()), Integer.parseInt(order.getWallSize()), Integer.parseInt(order.getCellSize()), converter.hexToColor(order.getForeground()), converter.hexToColor(order.getBackground()), converter.hexToColor(order.getHighlight()));
			puzzles2DSolved.add(puzzle2D.getSolved());
			puzzles2DUnsolved.add(puzzle2D.getUnsolved());
		}
		
		Downloader downloader = new Downloader();
		FileOutputStream  fileOutputStream = null;
		File orderDirectory = new File(properties.getProperty("2DoutputPath")+"/"+order.getOrderID().toString());
		orderDirectory.mkdirs();
		
		try {
			fileOutputStream = new FileOutputStream(orderDirectory+"/"+"2DPuzzles.zip");
			fileOutputStream.write(downloader.serialize(puzzles2DUnsolved, "2DPuzzles", "png"));
			fileOutputStream.flush();
			fileOutputStream.close();
		}
		catch(IOException e){
			e.printStackTrace();
		}
		order.setUnsolvedImages(orderDirectory.getPath()+"/"+"2DPuzzles.zip");
		
		try {
			fileOutputStream = new FileOutputStream(orderDirectory+"/"+"2DSolutions.zip");
			fileOutputStream.write(downloader.serialize(puzzles2DSolved, "2DSolutions", "png"));
			fileOutputStream.flush();
			fileOutputStream.close();
		}
		catch(IOException e){
			e.printStackTrace();
		}
		order.setSolvedImages(orderDirectory.getPath()+"/"+"2DSolutions.zip");
		
		orderDAO.save(order);
	}

	@Override
	public void delete(Order2D order) {
		InputStream inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream("config.properties");
		Properties properties = new Properties();
		try{
			properties.load(inputStream);
				}
		catch(IOException e){
			e.printStackTrace();
		}
		
		File directory = new File(properties.getProperty("2DoutputPath")+"/"+order.getOrderID().toString());
		File[] files = directory.listFiles();
		if (files != null) {
			for (File file : files) {
				file.delete();
			}
		}
		directory.delete();
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
		ArrayList<String> recipients = new ArrayList();
		recipients.add(order.getOrderEmailAddress());
		
		Mailer mailer = new Mailer("tim.mazegenerator@gmail.com","vmnu jyat wudp tvej","smtp.gmail.com","465",recipients,String.format("2D Maze Delivery: %s",order.getOrderID()),"I hope you enjoy! -TIM",new File(order.getUnsolvedImages()),new File(order.getSolvedImages()));
		mailer.send();
	
		fullfill(order,true);
	}

	@Override
	public InputStreamResource downloadPuzzles(Order2D order) {
		InputStreamResource puzzles = null;
		try{
			puzzles = new InputStreamResource(new FileInputStream(new File(order.getUnsolvedImages())));
		}
		catch (FileNotFoundException e){
			e.printStackTrace();
		}
		return puzzles;
	}

	@Override
	public InputStreamResource downloadSolutions(Order2D order) {
		InputStreamResource solutions = null;
		try{
			solutions = new InputStreamResource(new FileInputStream(new File(order.getSolvedImages())));
		}
		catch (FileNotFoundException e){
			e.printStackTrace();
		}
		return solutions;
	}

	@Override
	public void fullfill(Order2D order, boolean result) {
		order.setFullfilled(result);
	}

}
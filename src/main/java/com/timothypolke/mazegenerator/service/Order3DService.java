package com.timothypolke.mazegenerator.service;

import java.io.File;
import java.io.InputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.UUID;
import java.util.List;
import java.util.ArrayList;

import com.timothypolke.mazegenerator.misc.Mailer;
import com.timothypolke.mazegenerator.misc.Downloader;

import jakarta.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.timothypolke.mazegenerator.dao.Order3DDAO;
import com.timothypolke.mazegenerator.entity.Order3D;
import com.timothypolke.mazegenerator.entity.Size3D;
import com.timothypolke.mazegenerator.misc.Puzzle3D;

@Service
@Transactional
public class Order3DService implements IOrder3DService {
	@Autowired
	Size3DService sizeService;

	@Autowired
	Order3DDAO orderDAO;

	@Override
	public void createOrUpdate(Order3D order) {
		InputStream inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream("config.properties");
		Properties properties = new Properties();
		try{
			properties.load(inputStream);
				}
		catch(IOException e){
			e.printStackTrace();
		}
		
		List<Size3D> sizes = sizeService.readAll();
		for (Size3D size : sizes) {
			if (size.getSizeID().equals(order.getSize().getSizeID())) {
				order.setSize(size);
			}
		}
		
		orderDAO.save(order);
		
		ArrayList<String> puzzles3DSolved = new ArrayList<>();
		ArrayList<String> puzzles3DUnsolved = new ArrayList<>();
		Puzzle3D puzzle3D;
		for (int i=0;i<order.getOrderQuantity();i++){
			puzzle3D = new Puzzle3D(Integer.parseInt(order.getSize().getColumnCount()), Integer.parseInt(order.getSize().getRowCount()), Integer.parseInt(order.getSize().getLayerCount()), Integer.parseInt(order.getSize().getWallSize()), Integer.parseInt(order.getSize().getCellSize()));
			puzzles3DSolved.add(puzzle3D.getSolved());
			puzzles3DUnsolved.add(puzzle3D.getUnsolved());
		}
		
		Downloader downloader = new Downloader();
		FileOutputStream  fileOutputStream = null;
		File orderDirectory = new File(properties.getProperty("3DoutputPath")+"/"+order.getOrderID().toString());
		orderDirectory.mkdirs();
		
		try {
			fileOutputStream = new FileOutputStream(orderDirectory+"/"+"3DPuzzles.zip");
			fileOutputStream.write(downloader.serialize(puzzles3DUnsolved, "3DPuzzles", "obj"));
			fileOutputStream.flush();
			fileOutputStream.close();
		}
		catch(IOException e){
			e.printStackTrace();
		}
		order.setUnsolvedImages(orderDirectory.getPath()+"/"+"3DPuzzles.zip");
		
		try {
			fileOutputStream = new FileOutputStream(orderDirectory+"/"+"3DSolutions.zip");
			fileOutputStream.write(downloader.serialize(puzzles3DSolved, "3DSolutions", "obj"));
			fileOutputStream.flush();
			fileOutputStream.close();
		}
		catch(IOException e){
			e.printStackTrace();
		}
		order.setSolvedImages(orderDirectory.getPath()+"/"+"3DSolutions.zip");
		
		orderDAO.save(order);
	}

	@Override
	public Order3D read(UUID id) {
		return orderDAO.getReferenceById(id);
	}	

	@Override
	public List<Order3D> readAll() {
		return orderDAO.findAll();
	}

	@Override
	public void delete(Order3D order) {
		InputStream inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream("config.properties");
		Properties properties = new Properties();
		try{
			properties.load(inputStream);
				}
		catch(IOException e){
			e.printStackTrace();
		}
		
		File directory = new File(properties.getProperty("3DoutputPath")+"/"+order.getOrderID().toString());
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
	public void deliver(Order3D order) {		
		ArrayList<String> recipients = new ArrayList<>();
		recipients.add(order.getOrderEmailAddress());
		
		Mailer mailer = new Mailer("tim.mazegenerator@gmail.com","vmnu jyat wudp tvej","smtp.gmail.com","465",recipients,String.format("3D Maze Delivery: %s",order.getOrderID()),"I hope you enjoy! -TIM",new File(order.getUnsolvedImages()),new File(order.getSolvedImages()));
		mailer.send();
		
		fullfill(order,true);
	}

	@Override
	public byte[] downloadPuzzles(Order3D order) {
		Downloader downloader = new Downloader();
		return order.getUnsolvedImages().getBytes();
	}

	@Override
	public byte[] downloadSolutions(Order3D order) {
		Downloader downloader = new Downloader();
		return order.getSolvedImages().getBytes();
	}

	@Override
	public void fullfill(Order3D order, boolean result) {
		order.setFullfilled(result);
	}
}
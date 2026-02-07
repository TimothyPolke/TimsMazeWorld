package com.timothypolke.mazegenerator.service;

import java.util.UUID;
import java.util.List;
import java.util.ArrayList;
import java.io.ObjectInputStream;
import java.io.ByteArrayInputStream;
import java.io.IOException;

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
		List<Size3D> sizes = sizeService.readAll();
        for (Size3D size : sizes) {
			if (size.getSizeID().equals(order.getSize().getSizeID())) {
				order.setSize(size);
			}
		}

		ArrayList<String> puzzles3DSolved = new ArrayList<>();
		ArrayList<String> puzzles3DUnsolved = new ArrayList<>();
		Puzzle3D puzzle3D;
		for (int i=0;i<order.getOrderQuantity();i++){
			puzzle3D = new Puzzle3D(Integer.parseInt(order.getSize().getColumnCount()), Integer.parseInt(order.getSize().getRowCount()), Integer.parseInt(order.getSize().getLayerCount()), Integer.parseInt(order.getSize().getWallSize()), Integer.parseInt(order.getSize().getCellSize()));
			puzzles3DSolved.add(puzzle3D.getSolved());
			puzzles3DUnsolved.add(puzzle3D.getUnsolved());
		}
		order.setSolvedImages(puzzles3DSolved);
		order.setUnsolvedImages(puzzles3DUnsolved);

		orderDAO.save(order);
	}

	@Override
	public void delete(Order3D order) {
		orderDAO.delete(order);
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
	public void deliver(Order3D order) {
		Downloader downloader = new Downloader();
		
		ArrayList<String> recipients = new ArrayList<>();
		recipients.add(order.getOrderEmailAddress());
		
		Mailer mailer = new Mailer("tim.mazegenerator@gmail.com","vmnu jyat wudp tvej","smtp.gmail.com","465",recipients,String.format("3D Maze Delivery: %s",order.getOrderID()),"I hope you enjoy! -TIM",downloader.serialize(order.getUnsolvedImages(), "3DPuzzles", "obj"),downloader.serialize(order.getSolvedImages(), "3DSolutions", "obj"));
		mailer.send();
		
		fullfill(order,true);
	}
	
	@Override
	public byte[] downloadPuzzles(Order3D order) {
		Downloader downloader = new Downloader();
		return downloader.serialize(order.getUnsolvedImages(), "Puzzles", "obj");
	}
	
	@Override
	public byte[] downloadSolutions(Order3D order) {
		Downloader downloader = new Downloader();
		return downloader.serialize(order.getSolvedImages(), "Solutions", "obj");
	}
	
	@Override
	public void fullfill(Order3D order, boolean result) {
		order.setFullfilled(result);
	}
}
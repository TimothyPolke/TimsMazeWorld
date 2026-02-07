package com.timothypolke.mazegenerator.service;

import java.util.UUID;
import java.util.List;

import com.timothypolke.mazegenerator.entity.Order2D;

public interface IOrder2DService {
	void createOrUpdate(Order2D order);
	void delete(Order2D order);
	Order2D read(UUID id);
	List<Order2D> readAll();
	void deliver(Order2D order);
	byte[] downloadPuzzles(Order2D order);
	byte[] downloadSolutions(Order2D order);
	void fullfill(Order2D order, boolean value);
}
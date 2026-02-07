package com.timothypolke.mazegenerator.service;

import java.util.UUID;
import java.util.List;

import com.timothypolke.mazegenerator.entity.Order3D;

public interface IOrder3DService {
	void createOrUpdate(Order3D order);
	void delete(Order3D order);
	Order3D read(UUID id);
	List<Order3D> readAll();
	void deliver(Order3D order);
	byte[] downloadPuzzles(Order3D order);
	byte[] downloadSolutions(Order3D order);
	void fullfill(Order3D order, boolean value);
}
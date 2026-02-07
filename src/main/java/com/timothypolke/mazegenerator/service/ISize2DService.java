package com.timothypolke.mazegenerator.service;

import java.util.UUID;
import java.util.List;

import com.timothypolke.mazegenerator.entity.Size2D;

public interface ISize2DService {
	void createOrUpdate(Size2D size);
	void delete(Size2D size);
	Size2D read(UUID id);
	List<Size2D> readAll();
}
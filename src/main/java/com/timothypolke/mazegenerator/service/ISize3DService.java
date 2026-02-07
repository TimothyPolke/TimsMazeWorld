package com.timothypolke.mazegenerator.service;

import java.util.UUID;
import java.util.List;

import com.timothypolke.mazegenerator.entity.Size3D;

public interface ISize3DService {
	void createOrUpdate(Size3D size);
	void delete(Size3D size);
	Size3D read(UUID id);
	List<Size3D> readAll();
}
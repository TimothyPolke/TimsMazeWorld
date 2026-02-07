package com.timothypolke.mazegenerator.service;

import java.util.UUID;
import java.util.List;

import com.timothypolke.mazegenerator.entity.Theme2D;

public interface ITheme2DService {
	void createOrUpdate(Theme2D theme);
	void delete(Theme2D theme);
	Theme2D read(UUID id);
	List<Theme2D> readAll();
}
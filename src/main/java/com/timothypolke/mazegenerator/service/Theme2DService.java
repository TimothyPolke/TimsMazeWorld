package com.timothypolke.mazegenerator.service;

import java.util.UUID;
import java.util.List;
import java.util.ArrayList;

import jakarta.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.timothypolke.mazegenerator.dao.Theme2DDAO;
import com.timothypolke.mazegenerator.entity.Theme2D;

@Service
@Transactional
public class Theme2DService implements ITheme2DService {

	@Autowired
	Theme2DDAO theme2DDAO;

	@Override
	public void createOrUpdate(Theme2D theme) {
		theme2DDAO.save(theme);
	}

	@Override
	public void delete(Theme2D theme) {
		theme2DDAO.delete(theme);
	}

	@Override
	public Theme2D read(UUID id) {
		return theme2DDAO.getReferenceById(id);
	}	

	@Override
	public List<Theme2D> readAll() {
		return theme2DDAO.findAll();
	}

}
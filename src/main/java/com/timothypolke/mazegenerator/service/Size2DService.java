package com.timothypolke.mazegenerator.service;

import java.util.UUID;
import java.util.List;
import java.util.ArrayList;

import jakarta.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.timothypolke.mazegenerator.dao.Size2DDAO;
import com.timothypolke.mazegenerator.entity.Size2D;

@Service
@Transactional
public class Size2DService implements ISize2DService {
	@Autowired
	Size2DDAO size2DDAO;

	@Override
	public void createOrUpdate(Size2D size) {
		size2DDAO.save(size);
	}

	@Override
	public void delete(Size2D size) {
		size2DDAO.delete(size);
	}

	@Override
	public Size2D read(UUID id) {
		return size2DDAO.getReferenceById(id);
	}	

	@Override
	public List<Size2D> readAll() {
		return size2DDAO.findAll();
	}
}
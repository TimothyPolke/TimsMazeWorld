package com.timothypolke.mazegenerator.service;

import java.util.UUID;
import java.util.List;
import java.util.ArrayList;

import jakarta.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.timothypolke.mazegenerator.dao.Size3DDAO;
import com.timothypolke.mazegenerator.entity.Size3D;

@Service
@Transactional
public class Size3DService implements ISize3DService {
	@Autowired
	Size3DDAO sizeDAO;
	
	@Override
	public void createOrUpdate(Size3D size) {
		sizeDAO.save(size);
	}

	@Override
	public void delete(Size3D size) {
		sizeDAO.delete(size);
	}
	
	@Override
	public Size3D read(UUID id) {
		return sizeDAO.getReferenceById(id);
	}	
	
	@Override
	public List<Size3D> readAll() {
		return sizeDAO.findAll();
	}
}
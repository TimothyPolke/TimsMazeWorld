package com.timothypolke.mazegenerator.dao;

import java.util.UUID;

import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;

import com.timothypolke.mazegenerator.entity.Size3D;

@Repository
public interface Size3DDAO extends JpaRepository<Size3D, UUID> {

	
}
package com.timothypolke.mazegenerator.dao;

import java.util.UUID;

import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;

import com.timothypolke.mazegenerator.entity.Size2D;

@Repository
public interface Size2DDAO extends JpaRepository<Size2D, UUID> {

	
}
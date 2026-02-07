package com.timothypolke.mazegenerator.dao;

import java.util.UUID;

import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;

import com.timothypolke.mazegenerator.entity.Order2D;

@Repository
public interface Order2DDAO extends JpaRepository<Order2D,UUID> {

	
}
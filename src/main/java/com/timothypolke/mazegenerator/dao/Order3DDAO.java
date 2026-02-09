package com.timothypolke.mazegenerator.dao;

import java.util.UUID;

import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;

import com.timothypolke.mazegenerator.entity.Order3D;

@Repository
public interface Order3DDAO extends JpaRepository<Order3D,UUID> {
}
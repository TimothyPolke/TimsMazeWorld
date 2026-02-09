package com.timothypolke.mazegenerator.dao;

import java.util.UUID;

import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;

import com.timothypolke.mazegenerator.entity.Theme2D;

@Repository
public interface Theme2DDAO extends JpaRepository<Theme2D, UUID> {
}
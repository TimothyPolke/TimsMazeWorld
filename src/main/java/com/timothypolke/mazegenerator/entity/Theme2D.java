package com.timothypolke.mazegenerator.entity;

import java.io.Serializable;
import java.util.UUID;

import org.hibernate.annotations.GenericGenerator;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="table_Theme2D")
public class Theme2D implements Serializable {
	@Id
	@GeneratedValue(generator = "uuid")
	@GenericGenerator(name = "uuid", strategy = "uuid2")
	@Column(name="themeID")
	private UUID themeID;
	
	@Column(name="themeAlias")
	private String themeAlias;
	
	@Column(name="themeDate")
	private String themeDate;
	
	@Column(name="foreground")
	private String foreground;
	
	@Column(name="background")
	private String background;
	
	@Column(name="highlight")
	private String highlight;
}
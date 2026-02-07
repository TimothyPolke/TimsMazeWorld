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
@Table(name="table_Size3D")
public class Size3D implements Serializable {	
	@Id
	@GeneratedValue(generator = "uuid")
	@GenericGenerator(name = "uuid", strategy = "uuid2")
	@Column(name="sizeID")
	private UUID sizeID;
	
	@Column(name="sizeAlias")
	private String sizeAlias;
	
	@Column(name="sizeDate")
	private String sizeDate;
	
	@Column(name="columnCount",length=2)
	private String columnCount;
	@Column(name="rowCount",length=2)
	private String rowCount;
	@Column(name="layerCount",length=2)
	private String layerCount;
	
	@Column(name="wallSize",length=3)
	private String wallSize;
	@Column(name="cellSize",length=3)
	private String cellSize;
}
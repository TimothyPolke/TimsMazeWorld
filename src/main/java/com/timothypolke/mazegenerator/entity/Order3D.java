package com.timothypolke.mazegenerator.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.UUID;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
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
@Table(name="table_Order3D")
public class Order3D implements Serializable {
	@Id
	@GeneratedValue(generator = "uuid")
	@GenericGenerator(name = "uuid", strategy = "uuid2")
	@Column(name="orderID")
	private UUID orderID;
	
	@Column(name="orderDate")
	private String orderDate;
	
	@Column(name="orderEmailAddress")
	private String orderEmailAddress;
	
	@Column(name="orderQuantity")
	private int orderQuantity;
	
	@Column(name="columnCount",length=2)
	private String columnCount;
	@Column(name="rowCount",length=2)
	private String rowCount;
	@Column(name="layerCount",length=2)
	private String layerCount;
	@Column(name="wallSize",length=2)
	private String wallSize;
	@Column(name="cellSize",length=2)
	private String cellSize;
	
	@Column(name = "solvedImages")
	private String solvedImages;
	@Column(name = "unsolvedImages")
	private String unsolvedImages;
	
	@Column(name="fullfilled")
	private boolean fullfilled = false;
}
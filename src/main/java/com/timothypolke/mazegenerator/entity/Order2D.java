package com.timothypolke.mazegenerator.entity;

import java.io.Serializable;
import java.util.UUID;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
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
@Table(name="table_Order2D")
public class Order2D implements Serializable {
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
	
	@ManyToOne
	@OnDelete(action = OnDeleteAction.CASCADE)
	@JoinColumn(name = "sizeID")
	private Size2D size;
	
	@ManyToOne
	@OnDelete(action = OnDeleteAction.CASCADE)
	@JoinColumn(name = "themeID")
	private Theme2D theme;
	
	@Column(name = "solvedImages")
	private String solvedImages;
	
	@Column(name = "unsolvedImages")
	private String unsolvedImages;
	
	@Column(name="fullfilled")
	private boolean fullfilled = false;
}
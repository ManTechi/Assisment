package com.jspider.RoxilerApp.pojo;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

@Entity
@Data
public class Product {

	@Id
	private int id;
	
	private String title;
	
	private long price;
	
	private String description;
	
	private String category;
	
	private String image;
	
	private boolean sold;
	
	private String dateOfSale;
	
	
}

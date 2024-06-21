package com.jspider.RoxilerApp.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.jspider.RoxilerApp.pojo.Product;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long>{

	@Query(value = "SELECT product from Product product WHERE product.title LIKE %?1% OR product.description LIKE %?1%")
	List<Product> fetchProducts(String text);
	List<Product> findProductByPrice(long price);

	
	
}

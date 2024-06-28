package com.jspider.RoxilerApp.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.jspider.RoxilerApp.pojo.Product;
import com.jspider.RoxilerApp.repository.ProductRepository;
import com.jspider.RoxilerApp.response.ResponseStructure;

@RestController
@CrossOrigin(origins = "*")
public class ProductController {
//	@Autowired
//	private ContactService contactService;
	@Autowired
	private ProductRepository productRepository;
	
	@PostMapping("/products")
	public List<Product> addProduct(){
		
		RestTemplate restTemplate=new RestTemplate();
		Product[] products=restTemplate.getForObject("https://s3.amazonaws.com/roxiler.com/product_transaction.json", Product[].class);
		List<Product> list=new ArrayList<>();
		
		for (Product product : products) {
			list.add(product);
		}
		
		return productRepository.saveAll(list);
		
	}
	
	@GetMapping("/products")
	public ResponseEntity<ResponseStructure<List<Product>>> fetchProducts(@RequestParam(name = "text") String text){
		ResponseStructure<List<Product>> responseStructure = new ResponseStructure<>();
		try {
			System.out.println(text);
			Long price=Long.parseLong(text);
			List<Product> products= productRepository.findProductByPrice(price);
			responseStructure.setMessage("messa");
			responseStructure.setData(products);
			responseStructure.setStatus(HttpStatus.OK.value());
			return new ResponseEntity<ResponseStructure<List<Product>>>(responseStructure, HttpStatus.OK);
		} catch (Exception e) {
			// TODO: handle exception
//			return 
			List<Product> products= productRepository.fetchProducts(text);
			responseStructure.setMessage("messa");
			responseStructure.setData(products);
			responseStructure.setStatus(HttpStatus.OK.value());
			return new ResponseEntity<ResponseStructure<List<Product>>>(responseStructure, HttpStatus.OK);
			
		}
		
	}
	
	@GetMapping("/statistics{selectedMonth}")
	public ResponseEntity<ResponseStructure<long[]>> fetchStatistics(@PathVariable String selectedMonth){
		ResponseStructure<long[]> responseStructure = new ResponseStructure<>();
		List<Product> products=productRepository.findAll();
		long amount = 0;
		int sold=0;
		int unsold=0;
		
		for (Product product : products) {
			if (product.getDateOfSale().contains("-"+selectedMonth+"-")) {
				amount +=product.getPrice();
				if (product.isSold()) {
					sold++;
				} else {
					unsold++;
				}	
			}
		}
		long[] data=new long[3];
		data[0]=amount;
		data[1]=sold;
		data[2]=unsold;
		responseStructure.setMessage("Stats");
		responseStructure.setStatus(HttpStatus.OK.value());

		responseStructure.setData(data);
		return new ResponseEntity<ResponseStructure<long[]>>(responseStructure, HttpStatus.OK);
		
	}
	
	@GetMapping("/barchart")
	public HashMap<String, Integer> fetchBarChart(@RequestParam(name = "m") String m){
		List<Product> products=productRepository.findAll();
		int[] a=new int[10];
		for (Product product : products) {
			
			if (product.getDateOfSale().contains("-"+m+"-")) {
				long price=product.getPrice();
				if (price>=0 && price<=100) {
					a[0]=++a[0];
				}else if (price>=101 && price<=200) {
					a[1]=++a[1];
				}else if (price>=201 && price<=300) {
					a[2]=++a[2];
				}else if (price>=301 && price<=400) {
					a[3]=++a[3];
				}else if (price>=401 && price<=500) {
					a[4]=++a[4];
				}else if (price>=501 && price<=600) {
					a[5]=++a[5];
				}else if (price>=601 && price<=700) {
					a[6]=++a[6];
				}else if (price>=701 && price<=800) {
					a[7]=++a[7];
				}else if (price>=801 && price<=900) {
					a[8]=++a[8];
				}else {
					a[9]=++a[9];
				}
			}
		}
		
		HashMap<String, Integer> hashMap=new HashMap<>();
		hashMap.put("0-100", a[0]);
		hashMap.put("101-200", a[1]);
		hashMap.put("201-300", a[2]);
		hashMap.put("301-400", a[3]);
		hashMap.put("401-500", a[4]);
		hashMap.put("501-600", a[5]);
		hashMap.put("601-700", a[6]);
		hashMap.put("701-800", a[7]);
		hashMap.put("801-900", a[8]);
		hashMap.put("901-1000", a[9]);
		return hashMap;
		
	}
	
	@GetMapping("/Allproducts")
	public ResponseEntity<ResponseStructure<List<Product>>> fetchAllProducts(){
		ResponseStructure<List<Product>> responseStructure = new ResponseStructure<>();
			
			List<Product> products= productRepository.findAll();
			responseStructure.setData(products);
			return new ResponseEntity<ResponseStructure<List<Product>>>(responseStructure, HttpStatus.OK);
		
		
	}
//
//	@PostMapping("/contact")
//	public ResponseEntity<ResponseStructure<Product>> addContact(@RequestBody Product contact) {
//		contactService.addContact(contact);
//		ResponseStructure<Product> responseStructure = new ResponseStructure<>();
//		responseStructure.setMessage("Contact Added");
//		responseStructure.setData(contact);
//		responseStructure.setStatus(HttpStatus.OK.value());
//		return new ResponseEntity<ResponseStructure<Product>>(responseStructure, HttpStatus.OK);
//	}
//
//	@GetMapping("/contacts")
//	public ResponseEntity<ResponseStructure<List<Product>>> getAllContact() {
//		ResponseStructure<List<Product>> responseStructure = new ResponseStructure<>();
//		List<Product> contacts = contactService.getAllContact();
//		responseStructure.setMessage("Contacts");
//		responseStructure.setData(contacts);
//		responseStructure.setStatus(HttpStatus.OK.value());
//
//		return new ResponseEntity<ResponseStructure<List<Product>>>(responseStructure, HttpStatus.OK);
//
//	}
//
//	@GetMapping("/contactId{id}")
//	public ResponseEntity<ResponseStructure<Product>> getContactById(@PathVariable int id) {
//		Product contact1 = contactService.findContactById(id);
//		ResponseStructure<Product> responseStructure = new ResponseStructure<>();
//		responseStructure.setMessage("Contact");
//		responseStructure.setData(contact1);
//		responseStructure.setStatus(HttpStatus.OK.value());
//
//		return new ResponseEntity<ResponseStructure<Product>>(responseStructure, HttpStatus.OK);
//	}



//	@PutMapping("/contact")
//	public ResponseEntity<ResponseStructure<Product>> updateContact(@RequestBody Product contact) {
//		contactService.updateContact(contact);
//		ResponseStructure<Product> responseStructure = new ResponseStructure<>();
//		responseStructure.setMessage("Contact Updated");
//		responseStructure.setData(contact);
//		responseStructure.setStatus(HttpStatus.OK.value());
//		return new ResponseEntity<ResponseStructure<Product>>(responseStructure, HttpStatus.OK);
//	}
//	
//	@DeleteMapping("/contact{id}")
//	public ResponseEntity<ResponseStructure<Product>> deleteContact(@PathVariable int id) {
//		Product contact=contactService.deleteContact(id);
//		ResponseStructure<Product> responseStructure = new ResponseStructure<>();
//		responseStructure.setMessage("Contact deleted");
//		responseStructure.setData(contact);
//		responseStructure.setStatus(HttpStatus.OK.value());
//		return new ResponseEntity<ResponseStructure<Product>>(responseStructure, HttpStatus.OK);
//	}
	
	
}

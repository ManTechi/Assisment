package com.jspider.RoxilerApp.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jspider.RoxilerApp.pojo.Product;
import com.jspider.RoxilerApp.repository.ProductRepository;

@Service
public class ContactService {
	@Autowired
	private ProductRepository productRepository;

	public Product addContact(Product contact) {
		// TODO Auto-generated method stub
		return productRepository.save(contact);
		
	}

	public List<Product> getAllContact() {
		return productRepository.findAll();
		
	}

	public Product  findContactById(int id) {
		
		Optional<Product> contact= productRepository.findById(id);	
		
		if (contact.isPresent()) {
			return contact.get();
		} else {
			return null;
		}
	}

	public Product updateContact(Product contact) {
		Product UpdatedContact=productRepository.findById(contact.getId()).get();
		UpdatedContact.setName(contact.getName());
		UpdatedContact.setPhoto(contact.getPhoto());
		
		UpdatedContact.setContact(contact.getContact());
		UpdatedContact.setEmail(contact.getEmail());
		UpdatedContact.setTitle(contact.getTitle());
		UpdatedContact.setCompanyName(contact.getCompanyName());
		UpdatedContact.setGroupId(contact.getGroupId());
		
		 productRepository.save(UpdatedContact);	
		 
		 return UpdatedContact;
	}

	public Product deleteContact(int id) {
		Product contact=productRepository.findById(id).get();
		productRepository.delete(contact);
		
		return contact;
		
		
		
	}
}

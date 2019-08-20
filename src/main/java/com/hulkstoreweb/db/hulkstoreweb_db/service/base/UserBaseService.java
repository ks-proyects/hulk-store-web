package com.hulkstoreweb.db.hulkstoreweb_db.service.base;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import com.hulkstoreweb.db.hulkstoreweb_db.service.RolesService;
import com.hulkstoreweb.db.hulkstoreweb_db.repositories.RolesRepository;
import com.hulkstoreweb.db.hulkstoreweb_db.entity.Roles;

import com.hulkstoreweb.db.hulkstoreweb_db.entity.User;

import com.hulkstoreweb.db.hulkstoreweb_db.repositories.UserRepository;

@Service
@Transactional
public class UserBaseService {

	
	@Autowired
	UserRepository userRepository;
	@Autowired
	RolesRepository rolesRepository;
	private static RolesService rolesService = new RolesService();

	public User login(String username, String password) {
		if (userRepository.count() == 0) {
			User newUser = new User();
			newUser.setUsername("admin");
			newUser.addRoles(new Roles("ADMIN", newUser));
			newUser.setPassword("62f264d7ad826f02a8af714c0a54b197935b717656b80461686d450f7b3abde4c553541515de2052b9af70f710f0cd8a1a2d3f4d60aa72608d71a63a9a93c0f5");
			rolesRepository.save(newUser.getRoles().get(0));
			userRepository.save(newUser);
		}
		return userRepository.findByUsernameAndPassword(username, password);
	}


    // CRUD METHODS
    
    // CRUD - CREATE
    
	public User insert(User user) {
		return userRepository.save(user);
	}
	
	// CRUD - REMOVE
    
	public void delete(Long id) {
		userRepository.delete(id);
	}

	// CRUD - GET ONE
    	
	public User getOne(Long id) {
		return userRepository.findOne(id);
	}

    	
    // CRUD - GET LIST
    	
	public List<User> getAll() {
		List<User> list = new ArrayList<>();
		userRepository.findAll().forEach(list::add);
		return list;
	}
	

}

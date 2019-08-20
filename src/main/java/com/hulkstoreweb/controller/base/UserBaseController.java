package com.hulkstoreweb.controller.base;

import java.util.List;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.http.ResponseEntity;
import org.modelmapper.ModelMapper;
import java.util.stream.Collectors;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import org.springframework.security.access.annotation.Secured;
import org.springframework.beans.factory.annotation.Autowired;
import com.hulkstoreweb.db.hulkstoreweb_db.service.UserService;
import com.hulkstoreweb.db.hulkstoreweb_db.entity.User;
import com.hulkstoreweb.db.hulkstoreweb_db.dtos.UserDto;
import com.hulkstoreweb.db.hulkstoreweb_db.entity.Roles;

//IMPORT RELATIONS

import com.hulkstoreweb.db.hulkstoreweb_db.service.RolesService;

public class UserBaseController {
    
    @Autowired
	UserService userService;

	@Autowired
	RolesService rolesService;
	

	@Autowired
	private ModelMapper modelMapper;



//CRUD METHODS


    //CRUD - CREATE
    @Secured({ "ROLE_PRIVATE_USER" })
	@PostMapping("/user")
	public ResponseEntity<UserDto> insert(@RequestBody User obj) {
		if(obj.getRoles() != null && !obj.getRoles().isEmpty()) {
			obj.getRoles().forEach(role -> {
				role = rolesService.findByRoleAndUser(role.getRole(), obj) != null ? rolesService.findByRoleAndUser(role.getRole(), obj) : role;
				role.setUser(obj);
				rolesService.insert(role);
			});
		}
				
		
		return ResponseEntity.ok().body(convertToDto(userService.insert(obj)));
	}

	
    //CRUD - REMOVE
    @Secured({ "ROLE_PRIVATE_USER" })
	@DeleteMapping("/user/{id}")
	public ResponseEntity<Void> delete(@PathVariable("id") Long id) {
		User userSelected = userService.getOne(id);
		
		userService.delete(id);
		return ResponseEntity.ok().build();
	}
	
	
    //CRUD - GET ONE
    @Secured({ "ROLE_PRIVATE_USER" })
	@GetMapping("/user/{id}")
	public ResponseEntity<UserDto> get(@PathVariable("id") Long id) {
		User userSelected = userService.getOne(id);
		return ResponseEntity.ok().body(convertToDto(userSelected));
	}
	
	
    //CRUD - GET LIST
    @Secured({ "ROLE_PRIVATE_USER" })
	@GetMapping("/user")
	public ResponseEntity<List<UserDto>> getList() {
		List<User> list = userService.getAll();
		List<UserDto> listDto = list.stream()
				.map(user -> convertToDto(user))
				.collect(Collectors.toList());
		return ResponseEntity.ok().body(listDto);
	}


    //CRUD - EDIT
    @Secured({ "ROLE_PRIVATE_USER" })
	@PostMapping("/user/{id}")
	public ResponseEntity<UserDto> update(@RequestBody User obj, @PathVariable("id") Long id) {
		if(obj.getRoles() != null && !obj.getRoles().isEmpty()) {
			List<Roles> rolesOld = rolesService.findByUser(obj);
				if (rolesOld.size() > 0) {
					List<String> rolesToRemove = rolesOld.stream()
						.map(Roles::getRole)
						.filter(el -> !obj.hasRole(el))
						.collect(Collectors.toList());
				if(rolesToRemove.size() > 0) {
					rolesOld.forEach(role -> {
						if(rolesToRemove.contains(role.getRole()))
								rolesService.delete(role.get_id());
					});
				}	
			}
			obj.getRoles().forEach(role -> {
				role = rolesService.findByRoleAndUser(role.getRole(), obj) != null ? rolesService.findByRoleAndUser(role.getRole(), obj) : role;
				role.setUser(obj);
				rolesService.insert(role);
			});
		}
	    
		
		return ResponseEntity.ok().body(convertToDto(userService.insert(obj)));
	}
	

//CHANGE PASSWORD
	@Secured({"ROLE_ADMIN"})
	@PostMapping("/{id}/changePassword")
	public Map<String, String> changePassword(@PathVariable("id") Long id,@RequestBody Map<String, String> passwords) throws Exception {
		User userUpdated = userService.insert(userService.getOne(id));
		if(userUpdated != null)
			return new HashMap<String,String>();
		else
			return null;
	}

/*
 * CUSTOM SERVICES
 * 
 *	These services will be overwritten and implemented in  Custom.js
 */


    /*
    Name: changePassword
    Description: Change password of user from admin
    Params: 
    
    @Secured({"ROLE_PRIVATE_USER", "ROLE_ADMIN" })
    @RequestMapping(value = "/user/{id}/changePassword", method = RequestMethod.POST, headers = "Accept=application/json")
    public Object changePassword() {
		return new HashMap<String, String>();
    }
    */
    		

	private UserDto convertToDto(User user) {
		UserDto userDto = modelMapper.map(user, UserDto.class);
		return userDto;
	}
}

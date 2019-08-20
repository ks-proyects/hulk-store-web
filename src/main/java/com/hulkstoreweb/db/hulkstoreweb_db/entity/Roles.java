package com.hulkstoreweb.db.hulkstoreweb_db.entity;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.FetchType;
import com.hulkstoreweb.db.hulkstoreweb_db.entity.User;

@Entity
public class Roles{

	@Id
	@GeneratedValue
    private Long _id;
	@ManyToOne(fetch = FetchType.LAZY)
	private User user;
	private String role;

	public Roles() {}
	
	public Roles(String role, User user) {
		this.role = role;
		this.user = user;
	}

	public Roles(String role) {
		this.role = role;
	}
	
	public Long get_id() {
		return _id;
	}
	public void set_id(Long id) {
		this._id = id;
	}

	public User getUser() {
		return user;
	}


	public void setUser(User user) {
		this.user = user;
	}

	public String getRole() {
		return role;
	}


	public void setRole(String role) {
		this.role = role;
	}

}

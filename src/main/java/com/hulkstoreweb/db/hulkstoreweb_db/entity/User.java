package com.hulkstoreweb.db.hulkstoreweb_db.entity;

import javax.persistence.Entity;
import com.hulkstoreweb.db.hulkstoreweb_db.entity.base.UserBase;

@Entity(name="user_app")
public class User extends UserBase {

    public User() {}

    public User(String id) {
        this.set_id(Long.valueOf(id));
    }

	//OVERRIDE HERE YOUR CUSTOM MAPPER
	
	
}

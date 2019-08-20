package com.hulkstoreweb.db.hulkstoreweb_db.dtos;

import com.hulkstoreweb.db.hulkstoreweb_db.dtos.infos.UserInfo;

public class RolesDto {

    private Long _id;
    private UserInfo user;
    private String role;

    public Long get_id() {
        return _id;
    }

    public void set_id(Long id) {
        this._id = id;
    }

    public UserInfo getUser() {
        return user;
    }

    public void setUser(UserInfo user) {
        this.user = user;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}

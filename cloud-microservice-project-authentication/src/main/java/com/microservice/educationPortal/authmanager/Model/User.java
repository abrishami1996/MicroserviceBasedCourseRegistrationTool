package com.microservice.educationPortal.authmanager.Model;


import java.io.Serializable;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.persistence.*;

@Entity
@Table(name = "user")
public class User implements Serializable{


    private Long id;
    private String username;
    private String Password;
    private int FK;
    private String TypeOfUser;



    @Column(nullable = true)
    public int getFK() {
        return FK;
    }

    public void setFK(int FK) {
        this.FK = FK;
    }

    private Set<Role> roles;

    public User(String username, String password, int FK, Set<Role> roles) {
        this.username = username;
        Password = password;
        this.FK = FK;
        this.roles = roles;
    }

    public User() {
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long getId() {

        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Column(unique = true)
    public String getUsername() {

        return username;
    }

    public void setUsername(String username) {

        this.username = username;
    }

    public String getPassword() {

        return Password;
    }

    public void setPassword(String password) {

        this.Password = password;
    }

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }


    public String getTypeOfUser() {
        return TypeOfUser;
    }

    public void setTypeOfUser(String typeOfUser) {
        TypeOfUser = typeOfUser;
    }
}
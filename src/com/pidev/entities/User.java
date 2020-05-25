/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pidev.entities;

import java.util.Date;

/**
 *
 * @author ghada
 */
public class User {
    private int id;
    private String username;
    private String usernameCanonical;
    private String email;
    private String emailCanonical;
    public  static String Roles;
    private int enabled = 1;
    private String salt;
    private String password;
    private Date lastLogin;
    private String NumeroTelephone;
    private String cin;
    private String Adresse;

    public User() {
    }

    public User(int id) {
        this.id = id;
    }

    public User(int id, String username, String usernameCanonical, String email, String emailCanonical, String Roles, String salt, String password, Date lastLogin, String NumeroTelephone, String cin, String Adresse) {
        this.id = id;
        this.username = username;
        this.usernameCanonical = usernameCanonical;
        this.email = email;
        this.emailCanonical = emailCanonical;
        this.Roles = Roles;
        this.salt = salt;
        this.password = password;
        this.lastLogin = lastLogin;
        this.NumeroTelephone = NumeroTelephone;
        this.cin = cin;
        this.Adresse = Adresse;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUsernameCanonical() {
        return usernameCanonical;
    }

    public void setUsernameCanonical(String usernameCanonical) {
        this.usernameCanonical = usernameCanonical;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getEmailCanonical() {
        return emailCanonical;
    }

    public void setEmailCanonical(String emailCanonical) {
        this.emailCanonical = emailCanonical;
    }

    public String getRoles() {
        return Roles;
    }

    public void setRoles(String Roles) {
        this.Roles = Roles;
    }

    public int getEnabled() {
        return enabled;
    }

    public void setEnabled(int enabled) {
        this.enabled = enabled;
    }

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Date getLastLogin() {
        return lastLogin;
    }

    public void setLastLogin(Date lastLogin) {
        this.lastLogin = lastLogin;
    }

    public String getNumeroTelephone() {
        return NumeroTelephone;
    }

    public void setNumeroTelephone(String NumeroTelephone) {
        this.NumeroTelephone = NumeroTelephone;
    }

    public String getCin() {
        return cin;
    }

    public void setCin(String cin) {
        this.cin = cin;
    }

    public String getAdresse() {
        return Adresse;
    }

    public void setAdresse(String Adresse) {
        this.Adresse = Adresse;
    }

    @Override
    public String toString() {
        return "User{" + "id=" + id + ", username=" + username + '}';
    }

    
}

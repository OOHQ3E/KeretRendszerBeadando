package com.example.oohq3ebeadando.model;

import javax.persistence.*;

@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    long id;
    @Column(name= "uname")
    String uname;

    @Column(name="passwd")
    String passwd;

    @Column(name = "auth")
    String auth;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getUname() {
        return uname;
    }

    public void setUname(String uname) {
        this.uname = uname;
    }

    public String getPasswd() {
        return passwd;
    }

    public void setPasswd(String passwd) {
        this.passwd = passwd;
    }

    public String getAuth() {
        return auth;
    }

    public void setAuth(String auth) {
        this.auth = auth;
    }

    public User(){}
    public User(long id, String uname, String passwd, String auth) {
        this.id = id;
        this.uname = uname;
        this.passwd = passwd;
        this.auth = auth;
    }
}

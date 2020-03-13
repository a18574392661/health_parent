package com.itheima.pojo;

import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by 我自己 on 2020-03-13.
 */
public class Muser implements Serializable {

    private  Integer id;
    private  String username;
    private  String password;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date createDate;

    private  Integer rid;
    private Mrole mrole;


    public Integer getRid() {
        return rid;
    }

    public void setRid(Integer rid) {
        this.rid = rid;
    }

    public Mrole getMrole() {
        return mrole;
    }

    public void setMrole(Mrole mrole) {
        this.mrole = mrole;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }
}

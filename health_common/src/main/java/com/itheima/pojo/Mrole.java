package com.itheima.pojo;

import java.io.Serializable;
import java.util.List;

/**
 * Created by 我自己 on 2020-03-13.
 */
public class Mrole implements Serializable{

    private  Integer id;
    private  String name;
    private List<Muser> musers;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Muser> getMusers() {
        return musers;
    }

    public void setMusers(List<Muser> musers) {
        this.musers = musers;
    }
}

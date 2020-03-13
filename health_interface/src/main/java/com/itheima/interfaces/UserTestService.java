package com.itheima.interfaces;

import com.itheima.pojo.Mrole;
import com.itheima.pojo.Muser;

import java.util.List;

/**
 * Created by 我自己 on 2020-03-13.
 */
public interface UserTestService {


    public List<Muser> muserAll();
    public Muser muserById(String id);
    public void delMuser(String id);
    public void muserAdd(Muser muser);
    public void muserEdit(Muser muser);
    public  List<Mrole> mroleAll();




}

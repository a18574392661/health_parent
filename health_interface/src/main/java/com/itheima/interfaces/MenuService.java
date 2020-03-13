package com.itheima.interfaces;

import java.util.List;

import com.itheima.pojo.Menu;

public interface MenuService {

	List<Menu> queryUserMenu(String username);

}

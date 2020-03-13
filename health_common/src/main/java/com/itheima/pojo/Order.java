package com.itheima.pojo;

import java.io.Serializable;
import java.util.Date;

/**
 * 体检预约信息
 */
public class Order implements Serializable{
    public static final String ORDERTYPE_TELEPHONE = "电话预约";
    public static final String ORDERTYPE_WEIXIN = "微信预约";
    public static final String ORDERSTATUS_YES = "已到诊";
    public static final String ORDERSTATUS_NO = "未到诊";
    private Integer id;
    private Integer memberId;//会员id
    private Date orderDate;//预约日期
    private String orderType;//预约类型 电话预约/微信预约 0 1
    private String orderStatus;//预约状态（是否到诊）0 1 
    private Integer setmealId;//体检套餐id
    private Integer addressId; //地址id
    
    private Member member; //会员对象
    private Address address;			//地址对象
    private Setmeal setmeal; //套餐对象
    
    //游离态
    private String code;
    
    public Order() {
    }

    
    
    
    
    public String getCode() {
		return code;
	}





	public void setCode(String code) {
		this.code = code;
	}





	public Integer getAddressId() {
		return addressId;
	}




	public void setAddressId(Integer addressId) {
		this.addressId = addressId;
	}




	public Member getMember() {
		return member;
	}




	public void setMember(Member member) {
		this.member = member;
	}




	public Address getAddress() {
		return address;
	}




	public void setAddress(Address address) {
		this.address = address;
	}




	public Setmeal getSetmeal() {
		return setmeal;
	}




	public void setSetmeal(Setmeal setmeal) {
		this.setmeal = setmeal;
	}




	public Order(Integer id) {
        this.id = id;
    }

    public Order(Integer memberId, Date orderDate, String orderType, String orderStatus, Integer setmealId) {
        this.memberId = memberId;
        this.orderDate = orderDate;
        this.orderType = orderType;
        this.orderStatus = orderStatus;
        this.setmealId = setmealId;
    }

    public Order(Integer id, Integer memberId, Date orderDate, String orderType, String orderStatus, Integer setmealId) {
        this.id = id;
        this.memberId = memberId;
        this.orderDate = orderDate;
        this.orderType = orderType;
        this.orderStatus = orderStatus;
        this.setmealId = setmealId;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getMemberId() {
        return memberId;
    }

    public void setMemberId(Integer memberId) {
        this.memberId = memberId;
    }

    public Date getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(Date orderDate) {
        this.orderDate = orderDate;
    }

    public String getOrderType() {
        return orderType;
    }

    public void setOrderType(String orderType) {
        this.orderType = orderType;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public Integer getSetmealId() {
        return setmealId;
    }

    public void setSetmealId(Integer setmealId) {
        this.setmealId = setmealId;
    }
}

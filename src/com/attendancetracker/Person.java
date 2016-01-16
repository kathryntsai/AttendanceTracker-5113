// Author: Anthony Ricco 12/29/2015

package com.attendancetracker;

public class Person {

	/**
	 * @param name
	 * @param phoneNum
	 */
	public Person(String name, String phoneNum) {
		super();
		this.name = name;
		this.phoneNum = phoneNum;
	}
	
	public Person() {
		// TODO Auto-generated constructor stub
	}

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPhoneNum() {
		return phoneNum;
	}
	public void setPhoneNum(String phoneNum) {
		this.phoneNum = phoneNum;
	}
	
	private String name;
	private String phoneNum;
	
}

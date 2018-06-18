package com.example.mrc.attendencesystem.entity;

import java.sql.ResultSet;
import java.sql.SQLException;

public class User implements java.io.Serializable{
	private static final long serialVersionUID = 1L;
	private int id;
	private String phoneNumber;
	private String password;
	private String userName;
	private int gender;
	private String createTime;
	private String updateTime;
	private int age;
	private String studentId;
	private String operation;
	public User(ResultSet resultSet) throws SQLException{
		setId(resultSet.getInt("id"));
		setPhoneNumber(resultSet.getString("phonenumber"));
		setPassword(resultSet.getString("password"));
		setUserName(resultSet.getString("username"));
		setGender(resultSet.getInt("gender"));
		setAge(resultSet.getInt("age"));
		setStudentId(resultSet.getString("student_id"));
	}
	public String getOperation() {
		return operation;
	}
	public void setOperation(String operation) {
		this.operation = operation;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getPhoneNumber() {
		return phoneNumber;
	}
	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public int getGender() {
		return gender;
	}
	public void setGender(int gender) {
		this.gender = gender;
	}
	public String getCreateTime() {
		return createTime;
	}
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
	public String getUpdateTime() {
		return updateTime;
	}
	public void setUpdateTime(String updateTime) {
		this.updateTime = updateTime;
	}
	public int getAge() {
		return age;
	}
	public void setAge(int age) {
		this.age = age;
	}
	public String getStudentId() {
		return studentId;
	}
	public void setStudentId(String studentId) {
		this.studentId = studentId;
	}

	@Override
	public String toString() {
		return "User{" +
				"id=" + id +
				", phoneNumber='" + phoneNumber + '\'' +
				", password='" + password + '\'' +
				", userName='" + userName + '\'' +
				", gender=" + gender +
				", createTime='" + createTime + '\'' +
				", updateTime='" + updateTime + '\'' +
				", age=" + age +
				", studentId='" + studentId + '\'' +
				", operation='" + operation + '\'' +
				'}';
	}
}

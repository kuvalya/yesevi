package com.umut.yesevi.main;

public class Person {
	private String name;
	private String lastname;
	private String address;
	private String city;
	
	public Person(String name, String lastname, String address, String city) {
		super();
		this.name = name;
		this.lastname = lastname;
		this.address = address;
		this.city = city;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getLastname() {
		return lastname;
	}
	public void setLastname(String lastname) {
		this.lastname = lastname;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}

	@Override
	public String toString() {
		return "Person [name=" + name + ", lastname=" + lastname + ", address=" + address + ", city=" + city + "]";
	}
}

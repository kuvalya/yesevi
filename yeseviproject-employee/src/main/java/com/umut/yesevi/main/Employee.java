package com.umut.yesevi.main;

public class Employee {

	private double recid;
	private float share;
	private int jobcode;
	private String title;
	
	public Employee(double recid, float share, int jobcode, String title) {
		super();
		this.recid = recid;
		this.share = share;
		this.jobcode = jobcode;
		this.title = title;
	}
	
	public double getRecid() {
		return recid;
	}
	public void setRecid(double recid) {
		this.recid = recid;
	}
	public float getShare() {
		return share;
	}
	public void setShare(float share) {
		this.share = share;
	}
	public int getJobcode() {
		return jobcode;
	}
	public void setJobcode(int jobcode) {
		this.jobcode = jobcode;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	
	@Override
	public String toString() {
		return "Employee [recid=" + recid + ", share=" + share + ", jobcode=" + jobcode + ", title=" + title + "]";
	}
	
}

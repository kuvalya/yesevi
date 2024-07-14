package com.umut.yesevi.main;


public class Asset {
	private double id;
	private float rationum;
	private int barcode;
	private int itemnum;

	public Asset(double id, float rationum, int barcode, int itemnum) {
		super();
		this.id = id;
		this.rationum = rationum;
		this.barcode = barcode;
		this.itemnum = itemnum;
	}
	
	public double getId() {
		return id;
	}
	public void setId(double id) {
		this.id = id;
	}
	public float getRationum() {
		return rationum;
	}
	public void setRationum(float rationum) {
		this.rationum = rationum;
	}
	public int getBarcode() {
		return barcode;
	}
	public void setBarcode(int barcode) {
		this.barcode = barcode;
	}
	public int getItemnum() {
		return itemnum;
	}
	public void setItemnum(int itemnum) {
		this.itemnum = itemnum;
	}
	
	@Override
	public String toString() {
		return "Asset [id=" + id + ", rationum=" + rationum + ", barcode=" + barcode + ", itemnum=" + itemnum + "]";
	}



}

package com.anurag.spring.dynamic.data;

import java.math.BigInteger;
import java.time.LocalDateTime;

public class Message {
	
	private String text;
	private String country;
	private LocalDateTime time;
	private BigInteger id;
	public String getText() {
		return text;
	}
	public LocalDateTime getTime() {
		return time;
	}
	public BigInteger getId() {
		return id;
	}
	public void setText(String text) {
		this.text = text;
	}
	public void setTime(LocalDateTime time) {
		this.time = time;
	}
	public void setId(BigInteger id) {
		this.id = id;
	}
	
	public String getCountry() {
		return country;
	}
	public void setCountry(String country) {
		this.country = country;
	}

	@Override
	public String toString() {
		return "[" + text+":"+ time.toString()+":"+ id+"]";
	}
}

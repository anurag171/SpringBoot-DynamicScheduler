package com.anurag.spring.dynamic.schedule.db;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection ="countryproductcutoff_master")
@CompoundIndex(def = "{'country': 1, 'product': 1}")
public class CountryProductCutOff {
	@Id
	private String key;
	private String country;
	private String product;
	private String startTime;
	private String endTime;
	private String destinationMq;
	private String schedulerExpression;
	private String frequency;
	private String timeZone;
	private String topic;
	
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
	public String getCountry() {
		return country;
	}
	public void setCountry(String country) {
		this.country = country;
	}
	public String getProduct() {
		return product;
	}
	public void setProduct(String product) {
		this.product = product;
	}
	public String getStartTime() {
		return startTime;
	}
	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}
	public String getEndTime() {
		return endTime;
	}
	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}
	public String getDestinationMq() {
		return destinationMq;
	}
	public void setDestinationMq(String destinationMq) {
		this.destinationMq = destinationMq;
	}
	public String getSchedulerExpression() {
		return schedulerExpression;
	}
	public void setSchedulerExpression(String schedulerExpression) {
		this.schedulerExpression = schedulerExpression;
	}
	public String getFrequency() {
		return frequency;
	}
	public void setFrequency(String frequency) {
		this.frequency = frequency;
	}
	public String getTimeZone() {
		return timeZone;
	}
	public void setTimeZone(String timeZone) {
		this.timeZone = timeZone;
	}
	
	@Override
	public String toString() {
		
		return "[" + timeZone+":"+ country+":"+ product+":"+key+ "]";
	}
	public String getTopic() {
		return topic;
	}
	public void setTopic(String topic) {
		this.topic = topic;
	}

}

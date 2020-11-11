package com.anurag.spring.dynamic.schedule.runnable;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

import com.anurag.spring.dynamic.schedule.db.CountryProductCutOff;

public class Job implements Runnable {
	
	private final CountryProductCutOff cutOffData;
	
	public Job(CountryProductCutOff cutOffData) {
		this.cutOffData = cutOffData;
	}

	@Override
	public void run() {
		System.out.println("**********");
		System.out.println("Start Time " + LocalDateTime.now(ZoneId.of(getCutOffData().getTimeZone())).format(DateTimeFormatter.ISO_DATE_TIME));
	   System.out.println(getCutOffData().toString());
	   System.out.println("End Time " + LocalDateTime.now(ZoneId.of(getCutOffData().getTimeZone())).format(DateTimeFormatter.ISO_DATE_TIME));
	   System.out.println("**********");

	}

	public CountryProductCutOff getCutOffData() {
		return cutOffData;
	}	

}

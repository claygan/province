package com.province.platform.pojos.dto;

public class ActivityCalendarDto {
	/** 
	 * @Description:活动id
	 */  
	private long id;
	/** 
	 * @Description:活动开始时间年份
	 */  
	private int year;
	/** 
	 * @Description:活动开始时间月份
	 */  
	private int month;
	/** 
	 * @Description:活动开始时间日
	 */  
	private int day;
	
	/** 
	 * @Description:活动名称
	 */  
	private String name;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public int getYear() {
		return year;
	}

	public void setYear(int year) {
		this.year = year;
	}

	public int getMonth() {
		return month;
	}

	public void setMonth(int month) {
		this.month = month;
	}

	public int getDay() {
		return day;
	}

	public void setDay(int day) {
		this.day = day;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	
}

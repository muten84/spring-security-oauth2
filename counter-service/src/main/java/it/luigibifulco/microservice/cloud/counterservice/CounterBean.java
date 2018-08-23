package it.luigibifulco.microservice.cloud.counterservice;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class CounterBean {

	@Id
	private Integer currentCount;

	@Column
	private Date countTime;

	protected CounterBean() {
		super();
		// TODO Auto-generated constructor stub
	}

	public CounterBean(Integer currentCount) {
		super();
		this.currentCount = currentCount;
	}

	public Integer getCurrentCount() {
		return currentCount;
	}

	public void setCurrentCount(Integer currentCount) {
		this.currentCount = currentCount;
	}

	public Date getCountTime() {
		return countTime;
	}

	public void setCountTime(Date countTime) {
		this.countTime = countTime;
	}

}

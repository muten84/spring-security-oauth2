package it.luigibifulco.oauth2.poc.server.controller.proxy;

public class CounterBean {

	private Integer currentCount;

	public CounterBean(Integer currentCount) {
		super();
		this.currentCount = currentCount;
	}

	protected CounterBean() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Integer getCurrentCount() {
		return currentCount;
	}

}

package it.luigibifulco.oauth2.poc.server.controller;

import it.luigibifulco.oauth2.poc.server.controller.proxy.CounterBean;

public class CountResponse<Content> {

	private Content content;

	private CounterBean counter;

	public Content getContent() {
		return content;
	}

	public void setContent(Content content) {
		this.content = content;
	}

	public CounterBean getCounter() {
		return counter;
	}

	public void setCounter(CounterBean counter) {
		this.counter = counter;
	}

	public static <Content> CountResponse<Content> wrap(Content c, int count) {
		CountResponse<Content> response = new CountResponse<>();
		response.setContent(c);
		response.setCounter(new CounterBean(count));
		return response;
	}
}

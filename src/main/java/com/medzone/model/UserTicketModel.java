package com.medzone.model;

public class UserTicketModel {
	private String username;
	private int ticketId;
	
	public UserTicketModel() {};
	
	public UserTicketModel(String username, int ticketId) {
		super();
		this.username = username;
		this.ticketId = ticketId;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public int getTicketId() {
		return ticketId;
	}

	public void setTicketId(int ticketId) {
		this.ticketId = ticketId;
	}
}

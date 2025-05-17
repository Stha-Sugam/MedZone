package com.medzone.model;

import java.time.LocalDateTime;

public class TicketModel {
	private String username;
	private int ticketId;
	private String subject;
	private String message;
	private LocalDateTime createdDate;
	private String status;
	
	public TicketModel() {}

	public TicketModel(int ticketId, String subject, String message, LocalDateTime createdDate, String status) {
		super();
		this.ticketId = ticketId;
		this.subject = subject;
		this.message = message;
		this.createdDate = createdDate;
		this.status = status;
	}
	
	public TicketModel(String username, int ticketId, String subject, String message, LocalDateTime createdDate, String status) {
		super();
		this.username = username;
		this.ticketId = ticketId;
		this.subject = subject;
		this.message = message;
		this.createdDate = createdDate;
		this.status = status;
	}
	
	public TicketModel(String username, int ticketId, String status) {
		super();
		this.username = username;
		this.ticketId = ticketId;
		this.status = status;
	}
	
	public TicketModel(String subject, String message, LocalDateTime createdDate, String status) {
		super();
		this.subject = subject;
		this.message = message;
		this.createdDate = createdDate;
		this.status = status;
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

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public LocalDateTime getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(LocalDateTime createdDate) {
		this.createdDate = createdDate;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}	
}

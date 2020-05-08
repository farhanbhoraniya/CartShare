package com.cmpe275.CartShare.model;

import java.sql.Date;
import java.util.UUID;

import javax.persistence.*;

@Entity
@Table(name = "confirmation_token")
public class ConfirmationToken {

	@Id
	@Column(name = "token_id")
	private int id;
	
	private String confirmationtoken;
	private Date created;

	@OneToOne(targetEntity = User.class, fetch = FetchType.EAGER)
	@JoinColumn(nullable = false, name = "user", referencedColumnName = "id")
	private User user;
	
	public ConfirmationToken() {}
	
	public ConfirmationToken(User user) {
		this.user = user;
		setCreated(new Date(new java.util.Date().getTime()));
		setConfirmationtoken(UUID.randomUUID().toString());
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}
	
	public String getConfirmationtoken() {
		return confirmationtoken;
	}

	public void setConfirmationtoken(String confirmationtoken) {
		this.confirmationtoken = confirmationtoken;
	}

	public Date getCreated() {
		return created;
	}

	public void setCreated(Date created) {
		this.created = created;
	}
}

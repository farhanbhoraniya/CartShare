package com.cmpe275.CartShare.model;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "pool_membership")
public class PoolMembership {

	// Table does not have id but JPA needs and id annotation
	@Id
	private String pool;
	
	private int user;
	
	private int reference;
	private boolean verified;
	
	public PoolMembership() {}
	
	public PoolMembership(String pool, int user, int reference, boolean verified) {
		this.pool = pool;
		this.user = user;
		this.reference = reference;
		this.verified = verified;
	}
	
	public String getPool() {
		return pool;
	}
	
	public void setPool(String pool) {
		this.pool = pool;
	}
	
	public int getUser() {
		return user;
	}
	
	public void setUser(int user) {
		this.user = user;
	}
	
	public int getReference() {
		return reference;
	}
	
	public void setReference(int reference) {
		this.reference = reference;
	}
	
	public boolean isVerified() {
		return verified;
	}
	
	public void setVerified(boolean verified) {
		this.verified = verified;
	}
}

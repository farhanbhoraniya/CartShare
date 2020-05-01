package com.cmpe275.CartShare.model;

import java.io.Serializable;

public class ProductPK implements Serializable {

	private static final long serialVersionUID = 1L;
	protected String sku;
	protected int storeid;
	
	public ProductPK() {}
	
	public ProductPK(String sku, int storeid) {
		this.sku = sku;
		this.storeid = storeid;
	}
}

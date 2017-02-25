package org.ciwise.blackhole.service.dto;

import java.io.Serializable;

public class SnapshotAccount implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1432490132110037987L;

	private Long id;

    private String name;

    private String type;

    private String dc;

    private String cno;

    private String balance;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getDc() {
		return dc;
	}

	public void setDc(String dc) {
		this.dc = dc;
	}

	public String getCno() {
		return cno;
	}

	public void setCno(String cno) {
		this.cno = cno;
	}

	public String getBalance() {
		return balance;
	}

	public void setBalance(String balance) {
		this.balance = balance;
	}
}

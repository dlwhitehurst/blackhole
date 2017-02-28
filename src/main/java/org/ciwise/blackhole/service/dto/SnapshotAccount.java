package org.ciwise.blackhole.service.dto;

import java.io.Serializable;
import java.util.Objects;

public class SnapshotAccount implements Serializable {

	/**
	 * unique serial identifier
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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        SnapshotAccount snapAccount = (SnapshotAccount) o;
        if(snapAccount.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, snapAccount.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "SnapshotAccount{" +
            "id=" + id +
            ", name='" + name + "'" +
            ", type='" + type + "'" +
            ", dc='" + dc + "'" +
            ", cno='" + cno + "'" +
            ", balance='" + balance + "'" +
            '}';
    }
	
}

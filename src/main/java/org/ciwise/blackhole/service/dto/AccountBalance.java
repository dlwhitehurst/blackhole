/**
 * Copyright (c) CI Wise Inc.  All rights reserved.  http://www.ciwise.com
 * The software in this package is published under the terms of the Apache
 * version 2.0 license, a copy of which has been included with this distribution 
 * in the LICENSE.md file.
 * 
 */ 

package org.ciwise.blackhole.service.dto;

import java.io.Serializable;
import java.util.Objects;

import org.ciwise.blackhole.domain.GenAccount;

/**
 *  @author <a href="mailto:david@ciwise.com">David L. Whitehurst</a>
 *
 */
public class AccountBalance implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    
    private String accountName;
    
    private String cno;
    
    private String balance;
    
    private String type;
    
    public String getAccountName() {
        return accountName;
    }
    public void setAccountName(String accountName) {
        this.accountName = accountName;
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
    public String getType() {
        return type;
    }
    public void setType(String type) {
        this.type = type;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        AccountBalance balance = (AccountBalance) o;
        if(balance.cno == null || cno == null) {
            return false;
        }
        return Objects.equals(cno, balance.cno);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(cno);
    }

    @Override
    public String toString() {
        return "AccountBalance{" +
            //"id=" + id +
            "name='" + accountName + "'" +
            ", type='" + type + "'" +
            ", cno='" + cno + "'" +
            ", balance='" + balance + "'" +
            '}';
    }
    
}

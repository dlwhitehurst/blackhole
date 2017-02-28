/**
 * Copyright (c) CI Wise Inc.  All rights reserved.  http://www.ciwise.com
 * The software in this package is published under the terms of the Apache
 * version 2.0 license, a copy of which has been included with this distribution 
 * in the LICENSE.txt file.
 * 
 */ 

package org.ciwise.blackhole.service;

import java.util.List;

import org.ciwise.blackhole.service.dto.AccountBalance;

/**
 *  @author <a href="mailto:david@ciwise.com">David L. Whitehurst</a>
 *
 */
public interface IncomeStatementService {
	
	/**
	 * 
	 * @return
	 */
	List<AccountBalance> getRevenueAccountBalances();

	/**
	 * 
	 * @return
	 */
	List<AccountBalance> getExpenseAccountBalances();

}

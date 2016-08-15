/**
 * Copyright (c) CI Wise Inc.  All rights reserved.  http://www.ciwise.com
 * The software in this package is published under the terms of the Apache
 * version 2.0 license, a copy of which has been included with this distribution 
 * in the LICENSE.md file.
 * 
 */ 

package org.ciwise.blackhole.service.util;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.Locale;

/**
 *  @author <a href="mailto:david@ciwise.com">David L. Whitehurst</a>
 *
 */
public final class CurrencyUtil {

    /**
     * An Integer representing US cents.
     */
    public static final int US_DECIMAL_PLACES = 2;

    // NOTE: This is US Currency now to begin. This will be refactored later to support
    // International currency.
    /**
     * This is for default construction prevention.
     */
    private CurrencyUtil() {
    }

    /**
     * This static method returns a (textual) number format for currency.
     *
     * @return textual number format for US currency representation when
     *         converting numerical objects to strings
     */
    public static NumberFormat getCurrencyFormat() {
        NumberFormat n = NumberFormat.getCurrencyInstance(Locale.US);
        return n;
    }

    /**
     * This method returns a string representation for money stored as
     * BigDecimal.
     *
     * @param bd
     *            the numeric currency value
     * @return string representation of big decimal object
     */
    public static String getBigDecimalCurrencyString(final BigDecimal bd) {
        BigDecimal value = bd;

        value = value.setScale(US_DECIMAL_PLACES,
                BigDecimal.ROUND_HALF_UP);

        NumberFormat nf = getCurrencyFormat();
        double money = value.doubleValue();
        String bdcs = nf.format(money);
        
        // DO NOT FORGET THIS IS HERE FOR THE REMOVAL OF THE DOLLAR SIGN FOR US CURRENCY
        bdcs = bdcs.substring(1, bdcs.length());
        return bdcs;
    }

    /**
     * This method determines if a String is acceptable for the creation of a
     * BigDecimal currency object.
     *
     * @param aPrice the string currency designation
     * @return true/false indication of string designation
     */
    public static boolean acceptableStringCurrencyFormat(final String aPrice) {
        boolean retVal = true;

        if (!aPrice.matches("^\\d+(\\.\\d{2})?$")) {
            retVal = false;
        }
        return retVal;
    }

    /**
     * 
     * @param a
     * @param b
     * @return
     */
    public static String addCurrency(String a, String b) {
        BigDecimal aBDec = new BigDecimal(a);
        BigDecimal bBDec = new BigDecimal(b);
        BigDecimal resultNumeric = aBDec.add(bBDec);
        
        return getBigDecimalCurrencyString(resultNumeric);
    }

    /**
     * 
     * @param a
     * @param b
     * @return
     */
    public static String subtractCurrency(String a, String b) {
        BigDecimal aBDec = new BigDecimal(a);
        BigDecimal bBDec = new BigDecimal(b);
        BigDecimal resultNumeric = aBDec.subtract(bBDec);
        
        return getBigDecimalCurrencyString(resultNumeric);
    }
    
//    public static void main(String[] args) {
// 
//        System.out.println(subtractCurrency("145.23","45.237857"));
//       
//    }
}


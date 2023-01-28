package com.sixthradix.econetsigner.utils;

public class BillUtils {

    public static final int INVOICE_NUM_LENGTH = 10;

    public static String shortenInvoiceNumber(String invoiceNumber){
        if(invoiceNumber.length()<=INVOICE_NUM_LENGTH){
            return invoiceNumber;
        }
        return invoiceNumber.substring(invoiceNumber.length()-INVOICE_NUM_LENGTH);
    }
}

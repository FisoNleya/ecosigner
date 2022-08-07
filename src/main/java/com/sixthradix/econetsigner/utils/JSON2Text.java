package com.sixthradix.econetsigner.utils;

import com.sixthradix.econetsigner.controllers.ApplicationController;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.stringtemplate.v4.ST;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class JSON2Text {

    public static String BPN = "CustomerBPN";
    public static String VAT = "CustomerVatNumber";
    public static String INVOICE_NUMBER = "InvoiceNumber";
    public static String INVOICE_AMOUNT = "InvoiceAmount";
    public static String TAX_AMOUNT = "InvoiceTaxAmount";
    public static String CURRENCY = "Currency";
    public static String SIGNATURE = "signature";
    public static String CALLBACK_URL = "callback_url";
    Logger logger = LoggerFactory.getLogger(JSON2Text.class);

    public List<String> convert(JSONObject jsonObject){
        List<String> data = new ArrayList<>();

        String currency = jsonObject.getString(CURRENCY);
        String bpn = jsonObject.getString(BPN);
        String VATNumber = jsonObject.getString(VAT);
        String invoiceNumber = jsonObject.getString(INVOICE_NUMBER);
        String invoiceAmount = jsonObject.getString(INVOICE_AMOUNT);
        String invoiceTaxAmount = jsonObject.getString(TAX_AMOUNT);

        ST currency_ = new ST("Currency\t<currency>");
        currency_.add("currency", currency);

        ST bpn_ = new ST("CustomerBPN\t<BPN>");
        bpn_.add("BPN", bpn);

        ST vat = new ST("CustomerVatNumber\t<VATNumber>");
        vat.add("VATNumber", VATNumber);

        ST invNum = new ST("InvoiceNumber\t<InvoiceNumber>");
        invNum.add("InvoiceNumber", invoiceNumber);

        ST amt = new ST("InvoiceAmount\t<InvoiceAmount>");
        amt.add("InvoiceAmount", invoiceAmount);

        ST taxAMT = new ST("InvoiceTaxAmount\t<invoiceTaxAmount>");
        taxAMT.add("invoiceTaxAmount", invoiceTaxAmount);



        data.add(currency_.render());
        data.add(bpn_.render());
        data.add(vat.render());
        data.add(invNum.render());
        data.add(amt.render());
        data.add(taxAMT.render());

        try {
            String callback_url = jsonObject.getString(CALLBACK_URL);
            ST callback = new ST("CallbackUrl\t<callback_url>");
            callback.add("callback_url", callback_url);
            data.add(callback.render());
        } catch (JSONException e) {
           logger.info("No callback url supplied");
        }

        // invoice items
        List<String> items = new ArrayList<>();
        for(Object object: jsonObject.getJSONArray("ItemsXml")){
            JSONObject json = (JSONObject) object;

            String itemName = json.getString("ITEMNAME1");
            String qty = json.getString("QTY");
            String price = json.getString("PRICE");
            String amount = json.getString("AMT");
            String tax = json.getString("TAX");

            ST sale = new ST("INV_SLE '<itemName>'\t<qty>*<price>=<amount>\tT=<tax>");
            sale.add("itemName", itemName);
            sale.add("qty", qty);
            sale.add("price", price);
            sale.add("amount", amount);
            sale.add("tax", tax);

            items.add(sale.render());
        }
        data.addAll(items);
        return data;
    }

    public JSONObject toJSON(List<String> invoiceData){

        String line0 = invoiceData.get(0).trim();
        String currency = line0.substring(line0.indexOf("\t") + 1).trim();

        String line1 = invoiceData.get(1).trim();
        String bpn = line1.substring(line1.indexOf("\t") + 1).trim();

        String line2 = invoiceData.get(2).trim();
        String VATNumber = line2.substring(line2.indexOf("\t") + 1).trim();

        String line3 = invoiceData.get(3).trim();
        String invoiceNumber = line3.substring(line3.indexOf("\t") + 1).trim();

        String line4 = invoiceData.get(4).trim();
        String invoiceAmount = line4.substring(line4.indexOf("\t") + 1).trim();

        String line5 = invoiceData.get(5).trim();
        String invoiceTaxAmount = line5.substring(line5.indexOf("\t") + 1).trim();

        String line6 = invoiceData.get(6).trim();
        String callbackUrl = line6.substring(line6.indexOf("\t") + 1).trim();

        String lastLine = invoiceData.get(invoiceData.size()-1).trim();
        String signature = lastLine.trim();

        JSONObject jsonObject = new JSONObject();
        jsonObject.put(SIGNATURE, signature);
        jsonObject.put(CURRENCY, currency);
        jsonObject.put(BPN, bpn);
        jsonObject.put(VAT, VATNumber);
        jsonObject.put(INVOICE_NUMBER, invoiceNumber);
        jsonObject.put(INVOICE_AMOUNT, invoiceAmount);
        jsonObject.put(TAX_AMOUNT, invoiceTaxAmount);
        jsonObject.put(CALLBACK_URL, callbackUrl);
        return jsonObject;
    }
    public JSONObject toJSON2(List<String> invoiceData){

        JSONObject jsonObject = new JSONObject();

        String lastLine = invoiceData.get(invoiceData.size()-1).trim();
        String signature = lastLine.trim();
        jsonObject.put(SIGNATURE, signature);

        invoiceData.parallelStream().forEach(line ->{
            String trimLine = line.substring(line.indexOf("\t") + 1).trim();
            if (line.trim().contains(CURRENCY))
                jsonObject.put(CURRENCY, trimLine);
            if (line.trim().contains(BPN))
                jsonObject.put(BPN, trimLine);
            if (line.trim().contains(VAT))
                jsonObject.put(VAT, trimLine);
            if (line.trim().contains(INVOICE_NUMBER))
                jsonObject.put(INVOICE_NUMBER, trimLine);
            if (line.trim().contains(INVOICE_AMOUNT))
                jsonObject.put(INVOICE_AMOUNT, trimLine);
            if (line.trim().contains(TAX_AMOUNT))
                jsonObject.put(TAX_AMOUNT, trimLine);

        });

        return jsonObject;
    }


}

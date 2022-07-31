package com.sixthradix.econetsigner.utils;

import org.json.JSONObject;
import org.stringtemplate.v4.ST;

import java.util.ArrayList;
import java.util.List;

public class JSON2Text {

    public List<String> convert(JSONObject jsonObject){
        List<String> data = new ArrayList<>();

        String BPN = jsonObject.getString("CustomerBPN");
        String VATNumber = jsonObject.getString("CustomerVatNumber");
        String invoiceNumber = jsonObject.getString("InvoiceNumber");
        String invoiceAmount = jsonObject.getString("InvoiceAmount");
        String invoiceTaxAmount = jsonObject.getString("InvoiceTaxAmount");

        ST bpn = new ST("CustomerBPN\t<BPN>");
        bpn.add("BPN", BPN);

        ST vat = new ST("CustomerVatNumber\t<VATNumber>");
        vat.add("VATNumber", VATNumber);

        ST invNum = new ST("InvoiceNumber\t<InvoiceNumber>");
        invNum.add("InvoiceNumber", invoiceNumber);

        ST amt = new ST("InvoiceAmount\t<InvoiceAmount>");
        amt.add("InvoiceAmount", invoiceAmount);

        ST taxAMT = new ST("InvoiceTaxAmount\t<invoiceTaxAmount>");
        taxAMT.add("invoiceTaxAmount", invoiceTaxAmount);

        data.add(bpn.render());
        data.add(vat.render());
        data.add(invNum.render());
        data.add(amt.render());
        data.add(taxAMT.render());

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

    public void toJSON(String jsonStr){

    }
}

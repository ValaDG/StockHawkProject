package com.sam_chordas.android.stockhawk.models;

/**
 * Created by Valerio on 14/03/2016.
 */
public class StockObject {

    public String stockSymbol;
    public String bidPrice;
    public String percentChange;

    public StockObject(String stockSymbol, String bidPrice, String percentChange) {
        this.stockSymbol = stockSymbol;
        this.bidPrice = bidPrice;
        this.percentChange = percentChange;
    }
}

package com.sam_chordas.android.stockhawk.ui;

import android.content.CursorLoader;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.db.chart.model.LineSet;
import com.db.chart.model.Point;
import com.db.chart.view.LineChartView;
import com.sam_chordas.android.stockhawk.R;
import com.sam_chordas.android.stockhawk.data.QuoteColumns;
import com.sam_chordas.android.stockhawk.data.QuoteProvider;


public class MyStocksLineActivity extends AppCompatActivity implements android.app.LoaderManager.LoaderCallbacks<Cursor> {

    LineChartView lineView;
    public static final int LOADER_ID = 1;
    String symbol;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_line_graph);

        Intent intent = getIntent();
        symbol = intent.getStringExtra("symbol");
        lineView = (LineChartView) findViewById(R.id.linechart);
        getLoaderManager().initLoader(LOADER_ID,null, this);
    }


    @Override
    public android.content.Loader<Cursor> onCreateLoader(int id, Bundle args) {

        return new CursorLoader(getApplicationContext(), QuoteProvider.Quotes.CONTENT_URI,
                new String[] {QuoteColumns.SYMBOL, QuoteColumns.BIDPRICE,QuoteColumns.PERCENT_CHANGE,QuoteColumns.CREATED},
                QuoteColumns.SYMBOL +" =?", new String[]{symbol}, QuoteColumns._ID + " ASC");
    }

    @Override
    public void onLoadFinished(android.content.Loader<Cursor> loader, Cursor data) {
        double stockValue;

       LineSet dataset = new LineSet();

        if (data.moveToFirst()) {
             stockValue = Double.parseDouble(data.getString(1).replace(",","."));

            while (data.moveToNext()) {
                String bidprice = data.getString(1).replace(",",".");
                double doubleBidprice = Double.parseDouble(bidprice);
                stockValue = stockValue + doubleBidprice;
                dataset.addPoint(new Point("",Float.parseFloat(bidprice)));
            }

            double averageStock = stockValue/data.getCount();

            lineView.dismiss();
            lineView.addData(dataset);
            lineView.setAxisBorderValues((int) averageStock -10 , (int) averageStock +10);
            lineView.show();

        }
    }

    @Override
    public void onLoaderReset(android.content.Loader<Cursor> loader) {



    }
}

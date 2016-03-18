package com.sam_chordas.android.stockhawk.ui;

import android.content.CursorLoader;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.db.chart.model.LineSet;
import com.db.chart.model.Point;
import com.db.chart.view.ChartView;
import com.db.chart.view.LineChartView;
import com.sam_chordas.android.stockhawk.R;
import com.sam_chordas.android.stockhawk.data.QuoteColumns;
import com.sam_chordas.android.stockhawk.data.QuoteProvider;


public class MyStocksLineActivity extends AppCompatActivity implements android.app.LoaderManager.LoaderCallbacks<Cursor> {

    public static final int LOADER_ID = 1;
    LineChartView lineView;
    String symbol;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_line_graph);

        Intent intent = getIntent();
        symbol = intent.getStringExtra("symbol");
        lineView = (LineChartView) findViewById(R.id.linechart);
        getLoaderManager().initLoader(LOADER_ID, null, this);
    }


    @Override
    public android.content.Loader<Cursor> onCreateLoader(int id, Bundle args) {

        return new CursorLoader(getApplicationContext(), QuoteProvider.Quotes.CONTENT_URI,
                new String[]{QuoteColumns.SYMBOL, QuoteColumns.BIDPRICE, QuoteColumns.PERCENT_CHANGE, QuoteColumns.CREATED},
                QuoteColumns.SYMBOL + " =?", new String[]{symbol}, QuoteColumns._ID + " ASC");
    }

    @Override
    public void onLoadFinished(android.content.Loader<Cursor> loader, Cursor data) {
        double stockValue;

        LineSet dataset = new LineSet();
        double minValue = 0;
        double maxValue = 0;
        int calls = 1;

        if (data.moveToFirst()) {

            String bidprice = data.getString(1).replace(",", ".");
            stockValue = Double.parseDouble(bidprice);
            minValue = stockValue;
            maxValue = stockValue;
            dataset.addPoint(new Point(String.valueOf(calls), Float.parseFloat(bidprice)));
            calls++;

            while (data.moveToNext()) {

                bidprice = data.getString(1).replace(",", ".");
                double doubleBidprice = Double.parseDouble(bidprice);
                dataset.addPoint(new Point(String.valueOf(calls), Float.parseFloat(bidprice)));

                if (minValue > doubleBidprice) {
                    minValue = doubleBidprice;
                }
                if (maxValue < doubleBidprice) {
                    maxValue = doubleBidprice;
                }
                calls++;
            }

            Paint paint = new Paint();
            paint.setColor(Color.WHITE);

            dataset.setDotsColor(getResources().getColor(R.color.material_red_700));
            dataset.setColor(getResources().getColor(R.color.material_green_700));

            lineView.dismiss();
            lineView.addData(dataset);
            lineView.setAxisBorderValues((int) minValue - 2, (int) maxValue + 2);
            lineView.setAxisColor(Color.WHITE);
            lineView.setLabelsColor(Color.WHITE);
            lineView.setStep(1);
            lineView.setGrid(ChartView.GridType.FULL, paint);
            lineView.show();

        }


    }

    @Override
    public void onLoaderReset(android.content.Loader<Cursor> loader) {


    }
}

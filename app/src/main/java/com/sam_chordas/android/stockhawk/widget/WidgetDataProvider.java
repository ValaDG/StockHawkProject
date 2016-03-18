package com.sam_chordas.android.stockhawk.widget;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.sam_chordas.android.stockhawk.R;
import com.sam_chordas.android.stockhawk.data.QuoteColumns;
import com.sam_chordas.android.stockhawk.data.QuoteProvider;
import com.sam_chordas.android.stockhawk.models.StockObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Valerio on 14/03/2016.
 */
public class WidgetDataProvider implements RemoteViewsService.RemoteViewsFactory {

    Context mContext = null;
    Intent intent;
    Cursor mCursor;
    List<StockObject> stockList = new ArrayList<>();
    StockObject object;

    public WidgetDataProvider(Context mContext, Intent intent) {
        this.mContext = mContext;
        this.intent = intent;
    }

    private void initData() {

        mCursor = mContext.getContentResolver().query(QuoteProvider.Quotes.CONTENT_URI,
                new String[]{
                        QuoteColumns.SYMBOL,
                        QuoteColumns.BIDPRICE,
                        QuoteColumns.PERCENT_CHANGE,
                        QuoteColumns.ISCURRENT},
                QuoteColumns.ISCURRENT + " =?",
                new String[]{Integer.toString(1)},
                null);

        stockList.clear();

        if (mCursor.moveToFirst()) {

            if (mCursor.getInt(3) == 1) {
                object = new StockObject(mCursor.getString(0), mCursor.getString(1), mCursor.getString(2));
                stockList.add(object);
            }
            while (mCursor.moveToNext()) {

                if (mCursor.getInt(3) == 1) {
                    object = new StockObject(mCursor.getString(0), mCursor.getString(1), mCursor.getString(2));
                    stockList.add(object);
                }

            }

        }

        mCursor.close();
    }

    @Override
    public void onCreate() {
        initData();

    }

    @Override
    public void onDataSetChanged() {
        initData();
    }

    @Override
    public void onDestroy() {

        mCursor.close();
        stockList.clear();
    }

    @Override
    public int getCount() {
        return stockList.size();
    }

    @Override
    public RemoteViews getViewAt(int position) {

        RemoteViews view = new RemoteViews(mContext.getPackageName(), R.layout.widget_collection_item);

        StockObject obj = stockList.get(position);

        view.setTextViewText(R.id.stock_symbol, obj.stockSymbol);
        view.setTextViewText(R.id.change, obj.percentChange);


        return view;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }
}

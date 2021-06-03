package com.xpl.android.inventorymanager.record;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.xpl.android.inventorymanager.R;
import com.xpl.android.inventorymanager.goods.GoodsActivity;
import com.xpl.android.inventorymanager.goods.GoodsAdapter;
import com.xpl.android.inventorymanager.goods.GoodsPO;
import com.xpl.android.inventorymanager.utils.database.MyDatabaseHelper;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by 破晓 on 2021/5/31.
 */

public class RecordActivity extends AppCompatActivity {

    private MyDatabaseHelper dbHelper;
    private List<RecordPO> recordPOs = new ArrayList<RecordPO>();
    RecordPO recordPO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.record_layout);

        dbHelper = new MyDatabaseHelper(this, "inventoryManager.db", null, 2);

        initRecords();
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_record);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        RecordAdapter adapter = new RecordAdapter(recordPOs, RecordActivity.this);
        recyclerView.setAdapter(adapter);

    }

    private void initRecords() {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Cursor cursor = db.query("tRecord", null, null, null, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                recordPO = new RecordPO();
                recordPO.setId(cursor.getInt(cursor.getColumnIndex("id")));
                recordPO.setRecordSn(cursor.getString(cursor.getColumnIndex("recordSn")));
                recordPO.setGoodsId(cursor.getInt(cursor.getColumnIndex("goodsId")));
                recordPO.setGoodsCount(cursor.getInt(cursor.getColumnIndex("goodsCount")));
                recordPO.setIoType(cursor.getInt(cursor.getColumnIndex("ioType")));
                recordPO.setCreateTime(cursor.getString(cursor.getColumnIndex("createTime")));
                recordPOs.add(recordPO);
            } while (cursor.moveToNext());
        }
        cursor.close();
    }
}


//        tableLayout= (TableLayout) findViewById(R.id.table_record);
//        for (int i = 0; i < 5 ; i++){
//
//            TableRow row = new TableRow(getApplicationContext());
//            TextView number= new TextView(getApplicationContext());
//            TextView name = new TextView(getApplicationContext());
//            TextView phone = new TextView(getApplicationContext());
//            Button button = new Button(getApplicationContext());
//
//            row.setBackgroundResource(R.color.colorPrimary);
//
//            number.setText("编号");
//            number.setGravity(Gravity.CENTER);
//            number.setBackgroundResource(R.drawable.tv_shape);
//            row.addView(number);
//
//            name.setText("名称");
//            name.setGravity(Gravity.CENTER);
//            name.setBackgroundResource(R.drawable.tv_shape);
//            row.addView(name);
//
//            phone.setText("电话");
//            phone.setGravity(Gravity.CENTER);
//            phone.setBackgroundResource(R.color.colorWhite);
//            row.addView(phone);
//
//            row.addView(button);
////            TableLayout.LayoutParams lp= new TableLayout.LayoutParams
////                    (TableLayout.LayoutParams.MATCH_PARENT,TableLayout.LayoutParams.WRAP_CONTENT);
////
////            int leftMargin=10;
////            int topMargin=2;
////            int rightMargin=10;
////            int bottomMargin=2;
////            lp.setMargins(leftMargin, 0, rightMargin, 0);
//            //row.setLayoutParams(lp);
//
//
//            tableLayout.addView(row);
//        }
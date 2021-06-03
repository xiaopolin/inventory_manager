package com.xpl.android.inventorymanager.utils.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

public class MyDatabaseHelper extends SQLiteOpenHelper {

    public static final String CREATE_T_GOODS = "create table tGoods ("
            + "id integer primary key autoincrement, "
            + "goodsName text, "
            + "goodsCount integer, "
            + "url text, "
            + "imgId integer, "
            + "goodsState integer, "
            + "createTime text)";

    public static final String CREATE_T_WAREHOUSE = "create table tWarehouse ("
            + "id integer primary key autoincrement, "
            + "warehouseName text, "
            + "address text, "
            + "capacity text, "
            + "remark text, "
            + "directorName text, "
            + "directorPhone text, "
            + "createTime text)";

    public static final String INSERT_WAREHOUSE = "INSERT INTO tWarehouse " +
            "(id,warehouseName,address,capacity,remark,directorName,directorPhone,createTime)" +
            "VALUES (1, '', '', '', '', '', '', '');";

    public static final String CREATE_T_RECORD = "create table tRecord ("
            + "id integer primary key autoincrement, "
            + "recordSn text, "
            + "goodsId integer, "
            + "goodsCount integer, "
            + "ioType integer, "
            + "createTime text)";

    private Context mContext;

    public MyDatabaseHelper(Context context, String name,
                            SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
        mContext = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_T_GOODS);
        db.execSQL(CREATE_T_WAREHOUSE);
        db.execSQL(CREATE_T_RECORD);
        Toast.makeText(mContext, "Create succeeded", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists tGoods");
        db.execSQL("drop table if exists tWarehouse");
        db.execSQL("drop table if exists tRecord");
        onCreate(db);
    }

}

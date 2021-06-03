package com.xpl.android.inventorymanager.goods;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.xpl.android.inventorymanager.R;
import com.xpl.android.inventorymanager.utils.database.MyDatabaseHelper;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

/**
 * Created by 破晓 on 2021/5/31.
 */

public class GoodsActivity extends AppCompatActivity {

    private MyDatabaseHelper dbHelper;
    private List<GoodsPO> goodsPOs = new ArrayList<GoodsPO>();
    private GoodsAdapter adapter;
    private SimpleDateFormat sdfF = new SimpleDateFormat("yyyy-MM-dd HH:ss:mm");
    private GoodsPO goodsPO;
    private Button buttonAddGoods;
    private EditText editGoodsName;
    private ImageView imageGoodsImgId;
    private Button buttonExchangeImgId;
    private int[] fruits = {
            R.drawable.apple_pic,
            R.drawable.banana_pic,
            R.drawable.cherry_pic,
            R.drawable.grape_pic,
            R.drawable.mango_pic,
            R.drawable.orange_pic,
            R.drawable.pear_pic,
            R.drawable.pineapple_pic,
            R.drawable.strawberry_pic,
            R.drawable.watermelon_pic
    };
    private int fruitChoice = 0;
    private int activeGoodsState = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.goods_layout);

        dbHelper = new MyDatabaseHelper(this, "inventoryManager.db", null, 2);

        initGoodss();
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_goods);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new GoodsAdapter(goodsPOs, GoodsActivity.this);
        recyclerView.setAdapter(adapter);

        // 点击添加按钮
        buttonAddGoods = (Button) findViewById(R.id.button_add_goods);
        buttonAddGoods.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAddDialog();
            }
        });

    }

    // 加载商品列表
    private void initGoodss() {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Cursor cursor = db.query("tGoods", null, "goodsState=?", new String[]{String.valueOf(activeGoodsState)}, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                goodsPO = new GoodsPO();
                goodsPO.setId(cursor.getInt(cursor.getColumnIndex("id")));
                goodsPO.setGoodsName(cursor.getString(cursor.getColumnIndex("goodsName")));
                goodsPO.setImgId(cursor.getInt(cursor.getColumnIndex("imgId")));
                goodsPO.setGoodsCount(cursor.getInt(cursor.getColumnIndex("goodsCount")));
                goodsPOs.add(goodsPO);
            } while (cursor.moveToNext());
        }
        cursor.close();
    }

//    private String getRandomLengthName(String name) {
//        Random random = new Random();
//        int length = random.nextInt(20) + 1;
//        StringBuilder builder = new StringBuilder();
//        for (int i = 0; i < length; i++) {
//            builder.append(name);
//        }
//        return builder.toString();
//    }

    // 生成随机数，设置为5，是[0,5)，包含0而不包含5。0,1,2,3,4 五个数
    private int getRandomFruit() {
        Random random = new Random();
        int index = random.nextInt(9);
        return fruits[index];
    }

    // 添加对话框
    private void showAddDialog() {
        int id = 0;
        /* @setIcon 设置对话框图标
         * @setTitle 设置对话框标题
         * @setMessage 设置对话框消息提示
         * setXXX方法返回Dialog对象，因此可以链式设置属性
         */
        final AlertDialog.Builder addDialog = new AlertDialog.Builder(GoodsActivity.this);
        final View dialogView = LayoutInflater.from(GoodsActivity.this).inflate(R.layout.goods_add_dialog,null);
        addDialog.setView(dialogView);

        buttonExchangeImgId = (Button) dialogView.findViewById(R.id.button_exchange_imgId);
        editGoodsName = (EditText) dialogView.findViewById(R.id.edit_goods_name);
        imageGoodsImgId = (ImageView) dialogView.findViewById(R.id.image_goods_imgId);

        addDialog.setPositiveButton("确定",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // 不允许添加重复的商品
                        if (0 != getGoodsCountByGoodsName()) {
                            Toast.makeText(GoodsActivity.this,  "商品名称不允许重复", Toast.LENGTH_SHORT).show();
                        } else {
                            addGoods();
                        }
                    }
                });
        addDialog.setNegativeButton("返回", null);

        // 切换图片
        buttonExchangeImgId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageGoodsImgId.setImageResource(getRandomFruit());
            }
        });

        // 显示
        addDialog.show();
    }

    // 添加商品
    private void addGoods() {

        if (null == imageGoodsImgId.getDrawable()){
            Toast.makeText(GoodsActivity.this,  "请为商品选择一幅图片", Toast.LENGTH_SHORT).show();
            return;
        }

        // 获取图片
        for (int fruit : fruits){
            Drawable.ConstantState drawableCs = getResources().getDrawable(fruit).getConstantState();
            if( imageGoodsImgId.getDrawable().getConstantState().equals(drawableCs) ){
                fruitChoice = fruit;
            }
        }

        // 数据库添加
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("goodsName", editGoodsName.getText().toString());
        values.put("goodsCount", 0);
        values.put("url", "");
        values.put("imgId", fruitChoice);
        values.put("goodsState", 1);
        values.put("createTime", sdfF.format(new Date()));
        db.insert("tGoods", null, values);

        // 列表刷新
        goodsPOs.clear();
        Cursor cursor = db.query("tGoods", null, "goodsState=?", new String[]{String.valueOf(activeGoodsState)}, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                goodsPO = new GoodsPO();
                goodsPO.setId(cursor.getInt(cursor.getColumnIndex("id")));
                goodsPO.setGoodsName(cursor.getString(cursor.getColumnIndex("goodsName")));
                goodsPO.setImgId(cursor.getInt(cursor.getColumnIndex("imgId")));
                goodsPO.setGoodsCount(cursor.getInt(cursor.getColumnIndex("goodsCount")));
                goodsPOs.add(goodsPO);
            } while (cursor.moveToNext());
        }
        cursor.close();
        adapter.notifyItemInserted(goodsPOs.size());

        Toast.makeText(GoodsActivity.this,  "添加成功", Toast.LENGTH_SHORT).show();
    }

    private int getGoodsCountByGoodsName () {
        int result = -1;
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Cursor cursor = db.query("tGoods", null,
                "goodsState=? and goodsName=?",
                new String[]{String.valueOf(activeGoodsState), editGoodsName.getText().toString()},
                null, null, null);
        cursor.moveToFirst();
        result = cursor.getCount();
        cursor.close();
        return result;
    }
}

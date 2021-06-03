package com.xpl.android.inventorymanager.warehouse;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.xpl.android.inventorymanager.ManageActivity;
import com.xpl.android.inventorymanager.R;
import com.xpl.android.inventorymanager.goods.GoodsActivity;
import com.xpl.android.inventorymanager.utils.database.MyDatabaseHelper;

import java.util.Date;

/**
 * Created by 破晓 on 2021/5/31.
 */

public class WarehouseActivity extends AppCompatActivity {

    private MyDatabaseHelper dbHelper;
    private EditText editWarehouseName;
    private EditText editAddress;
    private EditText editCapacity;
    private EditText editRemark;
    private EditText editDirectorName;
    private EditText editDirectorPhone;
    private EditText editCreateTime;
    WarehousePO warehousePO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.warehouse_layout);

        editWarehouseName = (EditText) findViewById(R.id.edit_warehouse_name);
        editAddress = (EditText) findViewById(R.id.edit_address);
        editCapacity = (EditText) findViewById(R.id.edit_capacity);
        editRemark = (EditText) findViewById(R.id.edit_remark);
        editDirectorName = (EditText) findViewById(R.id.edit_director_name);
        editDirectorPhone = (EditText) findViewById(R.id.edit_director_phone);

        // 加载仓库数据
        getWarehouse();

        // 切换编辑
        final Button buttonUpdateWarehouse = (Button) findViewById(R.id.button_update_warehouse);
        buttonUpdateWarehouse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ("编辑".equals(buttonUpdateWarehouse.getText())){
                    // 启动编辑
                    buttonUpdateWarehouse.setText("保存");
                    updateEditTextEnable(true);
                } else {
                    // 修改仓库信息
                    updateWarehouseById(1);
                    // 保存仓库信息
                    buttonUpdateWarehouse.setText("编辑");
                    updateEditTextEnable(false);

                    Toast.makeText(WarehouseActivity.this, "保存仓库信息完成", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // 返回
        Button buttonToManage = (Button) findViewById(R.id.button_to_manage);
        buttonToManage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    private void getWarehouse() {
        // 查询数据
        warehousePO = new WarehousePO();
        dbHelper = new MyDatabaseHelper(this, "inventoryManager.db", null, 2);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Cursor cursor = db.query("TWarehouse", null, null, null, null, null, null);
        if (cursor.moveToFirst()) {
            editWarehouseName.setText(cursor.getString(cursor.getColumnIndex("warehouseName")));
            editAddress.setText(cursor.getString(cursor.getColumnIndex("address")));
            editCapacity.setText(cursor.getString(cursor.getColumnIndex("capacity")));
            editRemark.setText(cursor.getString(cursor.getColumnIndex("remark")));
            editDirectorName.setText(cursor.getString(cursor.getColumnIndex("directorName")));
            editDirectorPhone.setText(cursor.getString(cursor.getColumnIndex("directorPhone")));
        }
        cursor.close();
    }

    private void updateEditTextEnable(boolean enable){
        EditText editWarehouseName = (EditText) findViewById(R.id.edit_warehouse_name);
        editWarehouseName.setEnabled(enable);
        EditText editWarehouseAddress = (EditText) findViewById(R.id.edit_address);
        editWarehouseAddress.setEnabled(enable);
        EditText editWarehouseCapacity = (EditText) findViewById(R.id.edit_capacity);
        editWarehouseCapacity.setEnabled(enable);
        EditText editWarehouseRemark = (EditText) findViewById(R.id.edit_remark);
        editWarehouseRemark.setEnabled(enable);
        EditText editWarehouseDirector = (EditText) findViewById(R.id.edit_director_name);
        editWarehouseDirector.setEnabled(enable);
        EditText editWarehousePhone = (EditText) findViewById(R.id.edit_director_phone);
        editWarehousePhone.setEnabled(enable);
    }

    private void updateWarehouseById(int id) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("warehouseName", editWarehouseName.getText().toString());
        values.put("address", editAddress.getText().toString());
        values.put("capacity", editCapacity.getText().toString());
        values.put("remark", editRemark.getText().toString());
        values.put("directorName", editDirectorName.getText().toString());
        values.put("directorPhone", editDirectorPhone.getText().toString());
        values.put("createTime", new Date().toString());
        db.update("TWarehouse", values, "id = ?", new String[] { "1" });

    }

}

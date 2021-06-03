package com.xpl.android.inventorymanager;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.xpl.android.inventorymanager.goods.GoodsActivity;
import com.xpl.android.inventorymanager.record.RecordActivity;
import com.xpl.android.inventorymanager.warehouse.WarehouseActivity;

public class ManageActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.manage_layout);

        Button buttonToWarehouse = (Button) findViewById(R.id.button_to_warehouse);
        buttonToWarehouse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(ManageActivity.this, WarehouseActivity.class);
                startActivity(intent);
            }
        });

        Button buttonToGoods = (Button) findViewById(R.id.button_to_goods);
        buttonToGoods.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(ManageActivity.this, GoodsActivity.class);
                startActivity(intent);
            }
        });

        Button buttonToRecord = (Button) findViewById(R.id.button_to_record);
        buttonToRecord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(ManageActivity.this, RecordActivity.class);
                startActivity(intent);
            }
        });

        Button buttonMore = (Button) findViewById(R.id.button_more);
        buttonMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(ManageActivity.this,  "更多功能敬请期待", Toast.LENGTH_SHORT).show();
            }
        });
    }
}

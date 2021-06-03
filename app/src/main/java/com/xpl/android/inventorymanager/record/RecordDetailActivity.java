package com.xpl.android.inventorymanager.record;

import android.content.Intent;
import android.icu.text.DecimalFormat;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.xpl.android.inventorymanager.ManageActivity;
import com.xpl.android.inventorymanager.R;
import com.xpl.android.inventorymanager.warehouse.WarehouseActivity;

/**
 * Created by 破晓 on 2021/6/1.
 */

public class RecordDetailActivity extends AppCompatActivity {

    private RecordPO recordPO;
    private TextView textRecordSn;
    private TextView textGoodsName;
    private TextView textIoTypeName;
    private TextView textCreateTime;
    private TextView textGoodsCount;
    private TextView textRemark;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.record_detail_layout);

        textRecordSn = (TextView) findViewById(R.id.text_record_sn);
        textGoodsName = (TextView) findViewById(R.id.text_goods_name);
        textIoTypeName = (TextView) findViewById(R.id.text_io_type_name);
        textCreateTime = (TextView) findViewById(R.id.text_create_time);
        textGoodsCount = (TextView) findViewById(R.id.text_goods_count);
        textRemark = (TextView) findViewById(R.id.text_remark);

        recordPO = new RecordPO();
        Intent intent = getIntent();
        if (null != intent){
            recordPO.setId(intent.getIntExtra("id", 0));
            recordPO.setRecordSn(intent.getStringExtra("recordSn"));
            recordPO.setGoodsId(intent.getIntExtra("goodsId", 0));
            recordPO.setGoodsName(intent.getStringExtra("goodsName"));
            recordPO.setGoodsCount(intent.getIntExtra("goodsCount", 0));
            recordPO.setIoType(intent.getIntExtra("ioType", 0));
            recordPO.setCreateTime(intent.getStringExtra("createTime"));
        }

        textRecordSn.setText(recordPO.getRecordSn() + new DecimalFormat("0000").format(recordPO.getId()));
        textGoodsName.setText(recordPO.getGoodsName());
        textIoTypeName.setText(1 == recordPO.getIoType() ? "入库" : "出库");
        textCreateTime.setText(recordPO.getCreateTime());
        textGoodsCount.setText(String.valueOf(recordPO.getGoodsCount()));
        textRemark.setText("备注");


        // 删除
        Button buttonDeleteRecord = (Button) findViewById(R.id.button_delete_record);
        buttonDeleteRecord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(RecordDetailActivity.this, "当前功能尚未上线", Toast.LENGTH_SHORT).show();
            }
        });

        // 返回
        Button buttonToManage = (Button) findViewById(R.id.button_to_record);
        buttonToManage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }
}

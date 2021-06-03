package com.xpl.android.inventorymanager.record;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.xpl.android.inventorymanager.R;
import com.xpl.android.inventorymanager.goods.GoodsAdapter;
import com.xpl.android.inventorymanager.goods.GoodsPO;
import com.xpl.android.inventorymanager.utils.database.MyDatabaseHelper;

import java.util.List;


/**
 * Created by 破晓 on 2021/6/1.
 */

public class RecordAdapter extends RecyclerView.Adapter<RecordAdapter.ViewHolder> {

    private MyDatabaseHelper dbHelper;
    private List<RecordPO> mRecordPOs;
    private Context mContext;
    private String goodsName;

    static class ViewHolder extends RecyclerView.ViewHolder {
        View recordView;
        TextView goodsName;
        TextView goodsCount;
        TextView ioTypeName;
        Button recordDetail;

        public ViewHolder(View view) {
            super(view);
            recordView = view;
            goodsName = (TextView) view.findViewById(R.id.goods_name);
            goodsCount = (TextView) view.findViewById(R.id.goods_count);
            ioTypeName = (TextView) view.findViewById(R.id.io_type_name);
            recordDetail = (Button) view.findViewById(R.id.record_detail);

        }
    }

    public RecordAdapter(List<RecordPO> recordPOs, Context context) {
        mRecordPOs = recordPOs;
        mContext = context;
    }

    @Override
    public RecordAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.record_item, parent, false);
        final RecordAdapter.ViewHolder holder = new RecordAdapter.ViewHolder(view);

        dbHelper = new MyDatabaseHelper(mContext, "inventoryManager.db", null, 2);

        holder.recordDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = holder.getAdapterPosition();
                RecordPO recordPO = mRecordPOs.get(position);

                Intent intent = new Intent();
                intent.setClass(mContext, RecordDetailActivity.class);
                intent.putExtra("id", recordPO.getId());
                intent.putExtra("recordSn", recordPO.getRecordSn());
                intent.putExtra("goodsId", recordPO.getGoodsId());
                intent.putExtra("goodsName", recordPO.getGoodsName());
                intent.putExtra("goodsCount", recordPO.getGoodsCount());
                intent.putExtra("ioType", recordPO.getIoType());
                intent.putExtra("createTime", recordPO.getCreateTime());
                mContext.startActivity(intent);
            }
        });


        return holder;
    }

    @Override
    public void onBindViewHolder(RecordAdapter.ViewHolder holder, int position) {
        RecordPO recordPO = mRecordPOs.get(position);

        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Cursor cursor = db.query("tGoods", null, "id=?", new String[]{String.valueOf(recordPO.getGoodsId())}, null, null, null);
        if (cursor.moveToFirst()) {
            recordPO.setGoodsName(cursor.getString(cursor.getColumnIndex("goodsName")));
        }
        cursor.close();

        holder.goodsName.setText(recordPO.getGoodsName());
        holder.goodsCount.setText(String.valueOf(recordPO.getGoodsCount()));
        holder.ioTypeName.setText(1 == recordPO.getIoType() ? "入库" : "出库");
    }

    @Override
    public int getItemCount() {
        return mRecordPOs.size();
    }



}

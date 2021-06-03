package com.xpl.android.inventorymanager.goods;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.xpl.android.inventorymanager.R;
import com.xpl.android.inventorymanager.utils.database.MyDatabaseHelper;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by 破晓 on 2021/5/31.
 */

public class GoodsAdapter extends RecyclerView.Adapter<GoodsAdapter.ViewHolder> {

    private MyDatabaseHelper dbHelper;
    private SQLiteDatabase db;
    private ContentValues values;
    private List<GoodsPO> mGoodsPOs;
    private Context mContext;
    int typeIn = 1;
    int typeOut = 2;
    int newCount = 0;
    SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
    SimpleDateFormat sdfF = new SimpleDateFormat("yyyy-MM-dd HH:ss:mm");
    int position;

    static class ViewHolder extends RecyclerView.ViewHolder {
        View goodsView;
        ImageView goodsImage;
        TextView goodsName;
        TextView goodsCount;
        Button goodsDelete;
        Button goodsIO;

        public ViewHolder(View view) {
            super(view);
            goodsView = view;
            goodsImage = (ImageView) view.findViewById(R.id.goods_image);
            goodsName = (TextView) view.findViewById(R.id.goods_name);
            goodsCount = (TextView) view.findViewById(R.id.goods_count);
            goodsDelete = (Button) view.findViewById(R.id.goods_delete);
            goodsIO = (Button) view.findViewById(R.id.goods_io);

        }
    }

    public GoodsAdapter(List<GoodsPO> goodsPOs, Context context) {
        mGoodsPOs = goodsPOs;
        mContext = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.goods_item, parent, false);
        final ViewHolder holder = new ViewHolder(view);

        dbHelper = new MyDatabaseHelper(mContext, "inventoryManager.db", null, 2);
        db = dbHelper.getWritableDatabase();
        values = new ContentValues();

        holder.goodsDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                position = holder.getAdapterPosition();
                GoodsPO goodsPO = mGoodsPOs.get(position);

                // 不允许删除尚有库存的商品
                if (0 != goodsPO.getGoodsCount()){
                    Toast.makeText(v.getContext(), "该商品还有库存，不允许删除", Toast.LENGTH_SHORT).show();
                } else {
                    // 软删除
                    values.clear();
                    values.put("goodsState", 2);
                    db.update("tGoods", values, "id = ?", new String[] { String.valueOf(goodsPO.getId()) });

                    mGoodsPOs.remove(position);
                    notifyItemRemoved(position);

                    Toast.makeText(v.getContext(), "删除成功", Toast.LENGTH_SHORT).show();
                }

            }
        });
        holder.goodsIO.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                position = holder.getAdapterPosition();
                GoodsPO goodsPO = mGoodsPOs.get(position);

                showIODialog(goodsPO);

            }
        });


        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        GoodsPO goodsPO = mGoodsPOs.get(position);
        holder.goodsImage.setImageResource(goodsPO.getImgId());
        holder.goodsName.setText(goodsPO.getGoodsName());
        holder.goodsCount.setText(String.valueOf(goodsPO.getGoodsCount()));
    }

    @Override
    public int getItemCount() {
        return mGoodsPOs.size();
    }

    private void showIODialog(final GoodsPO goodsPO) {
        int id = 0;
        /* @setIcon 设置对话框图标
         * @setTitle 设置对话框标题
         * @setMessage 设置对话框消息提示
         * setXXX方法返回Dialog对象，因此可以链式设置属性
         */
        final AlertDialog.Builder ioDialog = new AlertDialog.Builder(mContext);
        final View dialogView = LayoutInflater.from(mContext).inflate(R.layout.goods_io_dialog,null);
        ioDialog.setView(dialogView);
        ioDialog.setPositiveButton("确定",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        Date date = new Date();
                        SQLiteDatabase db = dbHelper.getWritableDatabase();
                        ContentValues values = new ContentValues();

                        EditText editIOCount = (EditText) dialogView.findViewById(R.id.edit_io_count);
                        RadioGroup radioIOType = (RadioGroup) dialogView.findViewById(R.id.radio_io_type);
                        int type = 0;
                        if (R.id.radio_in == radioIOType.getCheckedRadioButtonId()){
                            type = typeIn;
                            newCount = goodsPO.getGoodsCount() + Integer.valueOf(editIOCount.getText().toString());
                        }else {
                            type = typeOut;
                            newCount = goodsPO.getGoodsCount() - Integer.valueOf(editIOCount.getText().toString());
                        }

                        if (0 > newCount){
                            Toast.makeText(mContext, "库存无法支持如此多的出库数量", Toast.LENGTH_SHORT).show();
                        } else {
                            // 添加出入库记录
                            values.clear();
                            values.put("recordSn", "R" + sdf.format(date));
                            values.put("goodsId", goodsPO.getId());
                            values.put("goodsCount", editIOCount.getText().toString());
                            values.put("ioType", type);
                            values.put("createTime", sdfF.format(date));
                            db.insert("tRecord", null, values);
                            // 修改商品数量
                            values.clear();
                            values.put("goodsCount", newCount);
                            db.update("TGoods", values, "id = ?", new String[] { String.valueOf(goodsPO.getId()) });
                            // 修改显示列表
                            goodsPO.setGoodsCount(newCount);
                            mGoodsPOs.remove(position);
                            mGoodsPOs.add(position, goodsPO);
                            notifyItemChanged(position);

                            Toast.makeText(mContext, "操作完成", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
        ioDialog.setNegativeButton("返回", null);
        // 显示
        ioDialog.show();
    }
}

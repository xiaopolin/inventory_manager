package com.xpl.android.inventorymanager;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.xpl.android.inventorymanager.utils.database.MyDatabaseHelper;

/**
 * Created by 破晓 on 2021/5/31.
 */

public class LoginActivity extends AppCompatActivity {

    private MyDatabaseHelper dbHelper;
    EditText editUsername;
    String usernameStr;
    EditText editPassword;
    String passwordStr;
    CheckBox checkBoxShow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_layout);

        // 数据库绑定
        dbHelper = new MyDatabaseHelper(this, "inventoryManager.db", null, 2);

        editUsername = (EditText) findViewById(R.id.edit_username);
        editPassword = (EditText) findViewById(R.id.edit_password);
        checkBoxShow = (CheckBox) findViewById(R.id.checkbox_show);

        // 密码显示
        checkBoxShow.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    //如果选中，显示密码
                    editPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                } else {
                    //否则隐藏密码
                    editPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }
            }
        });


        // 登录
        Button buttonLogin = (Button) findViewById(R.id.button_login);
        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                usernameStr = editUsername.getText().toString();
                passwordStr = editPassword.getText().toString();

                SharedPreferences pref = getSharedPreferences("user", MODE_PRIVATE);
                String getPassword = pref.getString(usernameStr, "");
                if ("".equals(getPassword)){
                    Toast.makeText(LoginActivity.this, "用户名不存在", Toast.LENGTH_SHORT).show();
                } else if (!getPassword.equals(passwordStr)){
                    Toast.makeText(LoginActivity.this, "密码错误", Toast.LENGTH_SHORT).show();
                } else {
                    Intent intent = new Intent();
                    intent.setClass(LoginActivity.this, ManageActivity.class);
                    startActivity(intent);
                    Toast.makeText(LoginActivity.this, "登录成功", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // 注册
        Button buttonRegister = (Button) findViewById(R.id.button_register);
        buttonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                usernameStr = editUsername.getText().toString();
                passwordStr = editPassword.getText().toString();

                SharedPreferences.Editor editor = getSharedPreferences("user", MODE_PRIVATE).edit();
                editor.putString(usernameStr, passwordStr);
                editor.apply();
                Toast.makeText(LoginActivity.this, "注册成功", Toast.LENGTH_SHORT).show();
            }
        });

        // 初始化
        ImageView imageInitialize = (ImageView) findViewById(R.id.image_initialize);
        imageInitialize.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                initializeSharePreferences();
                initializeDatabase();

                Toast.makeText(LoginActivity.this, "系统初始化完成", Toast.LENGTH_SHORT).show();

            }
        });

    }

    private void initializeSharePreferences() {
        SharedPreferences.Editor editor = getSharedPreferences("user", MODE_PRIVATE).edit();
        editor.clear();
        editor.commit();
        editor.putString("admin", "123456");
        editor.apply();
    }

    private void initializeDatabase() {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.execSQL("drop table if exists tGoods");
        db.execSQL("drop table if exists tWarehouse");
        db.execSQL("drop table if exists tRecord");
        db.execSQL(MyDatabaseHelper.CREATE_T_GOODS);
        db.execSQL(MyDatabaseHelper.CREATE_T_WAREHOUSE);
        db.execSQL(MyDatabaseHelper.INSERT_WAREHOUSE);
        db.execSQL(MyDatabaseHelper.CREATE_T_RECORD);
    }

}

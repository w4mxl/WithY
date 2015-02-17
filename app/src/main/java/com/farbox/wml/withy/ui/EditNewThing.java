package com.farbox.wml.withy.ui;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.farbox.wml.withy.R;
import com.farbox.wml.withy.app.MyApplication;
import com.farbox.wml.withy.bean.Thing;
import com.farbox.wml.withy.common.ThingManage;
import com.farbox.wml.withy.util.Constants;

import java.sql.SQLException;

/**已弃用**/
public class EditNewThing extends ActionBarActivity {

    private EditText etNewThing;
    private ImageView postNewThing;
    String thingContent;
    AlertDialog.Builder builder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_new_thing);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        etNewThing = (EditText) findViewById(R.id.etNewThing);
        postNewThing = (ImageView) findViewById(R.id.postNewThing);

        postNewThing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                thingContent = etNewThing.getText().toString().trim();
                Log.e("wml", thingContent);
                if ("".equals(thingContent)) {
                    Toast.makeText(EditNewThing.this, "请先输入要添加的内容", Toast.LENGTH_SHORT).show();
                } else {
                    showAlertDialog();
                }
            }
        });

    }

    private void showAlertDialog() {
        builder = new AlertDialog.Builder(this);
        builder.setMessage("因为添加后是不能更改和删除的,你确定要添加'" + thingContent + "'?")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Thing thing = new Thing(Constants.allThingsCount + 1, thingContent, Constants.allThingsCount, 0);
                        try {
                            boolean flag = ThingManage.getThingManage(MyApplication.getApp().getSQLHelper()).addThing(thing);
                            if (flag) {
                                Toast.makeText(EditNewThing.this, "添加成功！", Toast.LENGTH_SHORT).show();
                                Log.e("wml", "已添加一条到db!");
                                Log.e("wml", "所有，已完成，未完成：" + Constants.allThingsCount + "," + Constants.thingsFinishedCount + "," + Constants.thingsUndoCount);
                                NavUtils.navigateUpFromSameTask(EditNewThing.this);
                                overridePendingTransition(R.anim.in_from_left,R.anim.out_to_right);
                            } else {
                                Toast.makeText(EditNewThing.this, "error！", Toast.LENGTH_SHORT).show();
                            }
                        } catch (SQLException e) {
                            Toast.makeText(EditNewThing.this, "ThingsFragment error:" + e.getMessage(), Toast.LENGTH_SHORT).show();
                            e.printStackTrace();
                        }
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User cancelled the dialog
                    }
                });
        builder.show();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == android.R.id.home) {
            NavUtils.navigateUpFromSameTask(this);
            overridePendingTransition(R.anim.in_from_left, R.anim.out_to_right);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}

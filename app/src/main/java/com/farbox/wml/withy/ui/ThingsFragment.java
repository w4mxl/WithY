package com.farbox.wml.withy.ui;


import android.app.Activity;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.farbox.wml.withy.R;
import com.farbox.wml.withy.adapter.ThingsLvAdapter;
import com.farbox.wml.withy.app.MyApplication;
import com.farbox.wml.withy.bean.Thing;
import com.farbox.wml.withy.common.ThingManage;
import com.farbox.wml.withy.dao.ThingDao;
import com.farbox.wml.withy.util.Constants;
import com.melnykov.fab.FloatingActionButton;

import java.io.File;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Created by wml on 14/11/26
 * The first Navigation : All things .
 */
public class ThingsFragment extends Fragment {

    public static final String ARG_NAV_NUMBER = "nav_number";
    public int getedNavNumber;

    private ArrayList<Thing> things = new ArrayList<Thing>();

    private ListView lvThings;
    private ThingsLvAdapter thingsLvAdapter;

    private ThingDao thingDao;
    ProgressDialog pd;

    public ThingsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (getArguments() != null) {
            getedNavNumber = getArguments().getInt(ARG_NAV_NUMBER);
        }
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_things, container, false);

        String nav = getResources().getStringArray(R.array.nav_array)[getedNavNumber];
        //getActivity().setTitle(nav);

        lvThings = (ListView) rootView.findViewById(R.id.lvThings);

        FloatingActionButton fab = (FloatingActionButton) rootView.findViewById(R.id.fab);
        fab.attachToListView(lvThings);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showNewThingView();
//                Intent intent = new Intent(getActivity(), EditNewThing.class);
//                startActivity(intent);
            }
        });

        //用当前改变了selected状态的thing去更新数据库
        lvThings.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                /* 之前用的这种方式，出现的问题是parent.getChildAt(position)超出一屏的item，点击会报空指针
                Checkable child = (Checkable) parent.getChildAt(position);
                Log.e("wml", "test:"+parent.getCount());
                Log.e("wml", "test:"+parent.getChildCount());
                Thing thing = things.get(position);
                if (child.isChecked()) {
                    thing.setSelected(1);
                } else {
                    thing.setSelected(0);
                }
                */
                Log.e("wml", "clicked position is:" + position);
                Thing thing = things.get(position);
                if (thing.getSelected() == 0) {
                    thing.setSelected(1);
                } else {
                    thing.setSelected(0);
                }

                try {
                    ThingManage.getThingManage(MyApplication.getApp().getSQLHelper()).updateThing(thing);
                    Log.e("wml", "完成状态已更新db!");
                    Log.e("wml", "所有，已完成，未完成：" + Constants.allThingsCount + "," + Constants.thingsFinishedCount + "," + Constants.thingsUndoCount);
                } catch (SQLException e) {
                    Toast.makeText(getActivity(), "ThingsFragment error:" + e.getMessage(), Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
            }
        });

        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        Log.e("wml", "onActivityCreated...");

        if (!new File("/data/data/com.farbox.wml.withy/databases/withY.db").exists()) {
            pd = new ProgressDialog(getActivity());
            pd.setMessage("请稍后,首次启动初始化数据中...");
            pd.show();
        }
        new RequestDbTask().execute();
        super.onActivityCreated(savedInstanceState);
    }

    private class RequestDbTask extends AsyncTask<Void, Void, ArrayList<Thing>> {

        @Override
        protected ArrayList<Thing> doInBackground(Void... params) {
            /** 初始化数据*/
            try {
                things.clear();
                things = (ArrayList<Thing>) ThingManage.getThingManage(MyApplication.getApp().getSQLHelper()).getThings();
            } catch (SQLException e) {
                Toast.makeText(getActivity(), "ThingsFragment error:" + e.getMessage(), Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }

            return things;
        }

        @Override
        protected void onPostExecute(ArrayList<Thing> things) {
            if (pd != null) {
                pd.dismiss();
            }

            thingsLvAdapter = new ThingsLvAdapter(getActivity(), things);
            lvThings.setAdapter(thingsLvAdapter);
            lvThings.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
            lvThings.clearChoices();//解决了横屏切换过程中count增加bug
            for (int i = 0; i < things.size(); i++) {
                if (things.get(i).getSelected() == 1) {
                    Log.e("wml", "完成的：" + things.get(i).getName());
                    lvThings.setItemChecked(i, true);
                }
            }

            Log.e("wml", lvThings.getCheckedItemPositions().toString());

            // 保存 drawer list count
            Constants.allThingsCount = lvThings.getCount();
            Constants.thingsFinishedCount = lvThings.getCheckedItemCount();
            Constants.thingsUndoCount = Constants.allThingsCount - Constants.thingsFinishedCount;
            Log.e("wml", "所有，已完成，未完成：" + Constants.allThingsCount + "," + Constants.thingsFinishedCount + "," + Constants.thingsUndoCount);

            super.onPostExecute(things);

        }
    }

    EditText newThingInput;
    View positiveAction;
    String thingContent;

    private void showNewThingView() {
        MaterialDialog dialog = new MaterialDialog.Builder(getActivity())
                .title(R.string.title_new_thing)
                .customView(R.layout.dialog_new_thing)
                .positiveText(R.string.action_add)
                .negativeText(android.R.string.cancel)
                .callback(new MaterialDialog.Callback() {
                    @Override
                    public void onPositive(MaterialDialog dialog) {

                        thingContent = newThingInput.getText().toString();
                        Thing thing = new Thing(Constants.allThingsCount + 1, thingContent, Constants.allThingsCount, 0);
                        try {
                            boolean flag = ThingManage.getThingManage(MyApplication.getApp().getSQLHelper()).addThing(thing);
                            if (flag) {
                                Toast.makeText(getActivity(), "添加成功！", Toast.LENGTH_SHORT).show();
                                Log.e("wml", "已添加一条到db!");
                                Log.e("wml", "所有，已完成，未完成：" + Constants.allThingsCount + "," + Constants.thingsFinishedCount + "," + Constants.thingsUndoCount);
                                //更新adapter
                            } else {
                                Toast.makeText(getActivity(), "error！", Toast.LENGTH_SHORT).show();
                            }
                        } catch (SQLException e) {
                            Toast.makeText(getActivity(), "ThingsFragment error:" + e.getMessage(), Toast.LENGTH_SHORT).show();
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onNegative(MaterialDialog dialog) {
                    }
                }).build();

        positiveAction = dialog.getActionButton(DialogAction.POSITIVE);
        newThingInput = (EditText) dialog.getCustomView().findViewById(R.id.etNewThing);
        newThingInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                positiveAction.setEnabled(s.toString().trim().length() > 0);
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        dialog.show();
        positiveAction.setEnabled(false); // disabled by default
    }
}

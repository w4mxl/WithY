package com.farbox.wml.withy.ui;


import android.app.Activity;
import android.app.Fragment;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.farbox.wml.withy.R;
import com.farbox.wml.withy.app.MyApplication;
import com.farbox.wml.withy.bean.Thing;
import com.farbox.wml.withy.common.ThingManage;

import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Created by wml on 14/11/26
 * The second Navigation : finished .
 */
public class FinishedFragment extends Fragment {

    public static final String ARG_NAV_NUMBER = "nav_number";
    public int getedNavNumber;

    private ListView lvFinished;
    private ArrayList<Thing> things = new ArrayList<Thing>();


    public FinishedFragment() {
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
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.add("").setIcon(R.drawable.smiling_face).setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_finished, container, false);

        String nav = getResources().getStringArray(R.array.nav_array)[getedNavNumber];
        //getActivity().setTitle(nav);

        lvFinished = (ListView) rootView.findViewById(R.id.lvFinished);
        lvFinished.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getActivity(), "clicked position:" + position, Toast.LENGTH_SHORT).show();
            }
        });

        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {

        new RequestDbTask().execute();

        super.onActivityCreated(savedInstanceState);
    }

    private class RequestDbTask extends AsyncTask<Void,Void,ArrayList<String>>{

        @Override
        protected ArrayList<String> doInBackground(Void... params) {
            /** 初始化数据*/
            try {
                things = (ArrayList<Thing>) ThingManage.getThingManage(MyApplication.getApp().getSQLHelper()).getThings();
            } catch (SQLException e) {
                Toast.makeText(getActivity(), "FinishedFragment error:" + e.getMessage(), Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }

            ArrayList<String> finishedName = new ArrayList<String>();
            for (int i = 0; i < things.size(); i++) {
                if (things.get(i).getSelected() == 1) {
                    Thing thing = things.get(i);
                    String strNum = "";
                    if (thing.getId() < 10) {
                        strNum = "0" + (thing.getId());
                    } else {
                        strNum = "" + (thing.getId());
                    }
                    finishedName.add(strNum + " " + thing.getName());
                }
            }

            return finishedName;
        }

        @Override
        protected void onPostExecute(ArrayList<String> strings) {
            lvFinished.setAdapter(new MyAdapter(strings));
            super.onPostExecute(strings);
        }
    }

    private class MyAdapter extends BaseAdapter{

        public ArrayList<String> finishedNames;

        private MyAdapter(ArrayList<String> finishedNames) {
            this.finishedNames = finishedNames;
        }

        @Override
        public int getCount() {
            return finishedNames.size();
        }

        @Override
        public String getItem(int position) {
            return finishedNames.get(position);
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            convertView = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_finished_item,parent,false);
            TextView tv = (TextView) convertView.findViewById(R.id.finished_thing_name);
            Typeface tf = Typeface.createFromAsset(getActivity().getAssets(),"fonts/american_typewriter.ttf");
            tv.setTypeface(tf);
            tv.setText(getItem(position));
            return convertView;
        }
    }
}

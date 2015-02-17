package com.farbox.wml.withy.dao;

import android.content.ContentValues;

import com.farbox.wml.withy.bean.Thing;

import java.util.List;
import java.util.Map;

/**
 * Created by wml on 14/11/27 13:42.
 */

public interface ThingDaoInterface {

    public boolean addCache(Thing item);

    public boolean deleteCache(String whereClause, String[] whereArgs);

    public boolean updateCache(ContentValues values, String whereClause,
                               String[] whereArgs);

    public Map<String, String> viewCache(String selection,String[] selectionArgs);

    public List<Map<String, String>> listCache(String selection,String[] selectionArgs);

    public void clearThingTable();
}

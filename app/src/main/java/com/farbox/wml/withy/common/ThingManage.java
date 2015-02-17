package com.farbox.wml.withy.common;

import android.content.ContentValues;

import com.farbox.wml.withy.bean.Thing;
import com.farbox.wml.withy.dao.ThingDao;
import com.farbox.wml.withy.db.SQLHelper;
import com.farbox.wml.withy.util.Constants;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 管理事情（Thing）
 * Created by wml on 14/11/27 13:22.
 */
public class ThingManage {

    public static ThingManage thingManage;

    /**
     * 默认显示的所有事情
     */
    public static List<Thing> defaultThings;

    /**
     * 默认已经完成的事情
     */
    public static List<Thing> defaultFinishedThings;

    /**
     * 默认未完成的事情
     */
    public static List<Thing> defaultUndoThings;

    /**
     * 判断数据库中是否存在用户数据
     */
    private boolean userExist = false;

    static {
        defaultThings = new ArrayList<Thing>();
        defaultThings.add(new Thing(1, "一起站在镜子面前刷牙", 1, 0));
        defaultThings.add(new Thing(2, "一起看场好电影", 2, 1));
        defaultThings.add(new Thing(3, "一起做一顿饭", 3, 0));
        defaultThings.add(new Thing(4, "一起牵手散步", 4, 1));
        defaultThings.add(new Thing(5, "一起去台湾旅行", 5, 0));
        defaultThings.add(new Thing(6, "一起养一只小动物", 6, 0));
        defaultThings.add(new Thing(7, "一起去看日出", 7, 0));
        defaultThings.add(new Thing(8, "一起去理发", 8, 0));
        defaultThings.add(new Thing(9, "一起逛菜市场 你讨价还价 菜我拎", 9, 1));
        defaultThings.add(new Thing(10, "一起登山看日落", 10, 0));
        defaultThings.add(new Thing(11, "一起听场演唱会", 11, 0));
        defaultThings.add(new Thing(12, "一起看大雪飘飞", 12, 0));
        defaultThings.add(new Thing(13, "给对方洗头并慢慢吹干", 13, 1));
        defaultThings.add(new Thing(14, "一起做泡面吃", 14, 1));
        defaultThings.add(new Thing(15, "用影视剧对白说我俩才明白的话", 15, 1));
        defaultThings.add(new Thing(16, "给对方起一个专用外号", 16, 0));
        defaultThings.add(new Thing(17, "偷偷买回你想买却没舍得买的东西", 17, 0));
        defaultThings.add(new Thing(18, "冬天的时候一起在家里吃火锅", 18, 0));
        defaultThings.add(new Thing(19, "戴着情侣戒指", 19, 1));
        defaultThings.add(new Thing(20, "一起锻炼身体", 20, 0));
        defaultThings.add(new Thing(21, "一起选床单和窗帘的颜色", 21, 0));
        defaultThings.add(new Thing(22, "有好消息/坏消息要第一个告诉对方", 22, 0));
        defaultThings.add(new Thing(23, "永远不说谢谢", 23, 0));
        defaultThings.add(new Thing(24, "下雨天同撑一把伞", 24, 0));
        defaultThings.add(new Thing(25, "养花 互相提醒浇水和给它们晒太阳", 25, 0));
        defaultThings.add(new Thing(26, "异口同声地说出同一句话", 26, 0));
        defaultThings.add(new Thing(27, "一起深夜观星", 27, 0));
        defaultThings.add(new Thing(28, "一起布置我们的小窝", 28, 0));
        defaultThings.add(new Thing(29, "一起背靠着背听歌", 29, 0));
        defaultThings.add(new Thing(30, "倚着我肩膀睡着", 30, 1));
        defaultThings.add(new Thing(31, "一人一半，比赛啃西瓜", 31, 0));
        defaultThings.add(new Thing(32, "为你剪指甲", 32, 0));
        defaultThings.add(new Thing(33, "一起露营", 33, 0));
        defaultThings.add(new Thing(34, "一起吃路边摊", 34, 0));
        defaultThings.add(new Thing(35, "你怕冷。常搂你进怀里", 35, 0));
        defaultThings.add(new Thing(36, "钱夹里总有合照", 36, 0));
        defaultThings.add(new Thing(37, "一起描述未来的样子", 37, 0));
        defaultThings.add(new Thing(38, "一起划船", 38, 0));
        defaultThings.add(new Thing(39, "约定可以生气但好了后要讲出来", 39, 0));
        defaultThings.add(new Thing(40, "给对方父母买礼物", 40, 0));
        defaultThings.add(new Thing(41, "给对方写一封信藏在屋子某个角落", 41, 0));
        defaultThings.add(new Thing(42, "相互诚实", 42, 0));

        defaultFinishedThings = new ArrayList<Thing>();

        defaultUndoThings = new ArrayList<Thing>();
        defaultUndoThings = defaultThings;
    }

    private ThingDao thingDao;

    private ThingManage(SQLHelper sqlHelper) throws SQLException {
        if (thingDao == null) {
            thingDao = new ThingDao(sqlHelper.getContext());
        }
        return;
    }

    /**
     * 初始化Thing管理类
     *
     * @param sqlHelper
     * @return
     * @throws SQLException
     */
    public static ThingManage getThingManage(SQLHelper sqlHelper) throws SQLException {
        if (thingManage == null) {
            thingManage = new ThingManage(sqlHelper);
        }

        return thingManage;
    }

    /**
     * 清除所有的thing
     */
    public void deleteAllThing() {
        thingDao.clearThingTable();
    }

    /**
     * 更新 thing 的 select 状态
     *
     * @param thing
     */
    public void updateThing(Thing thing) {

        if (thing.getSelected() == 0) {
            Constants.thingsFinishedCount = Constants.thingsFinishedCount - 1;
            Constants.thingsUndoCount = Constants.thingsUndoCount + 1;
        } else if (thing.getSelected() == 1) {
            Constants.thingsFinishedCount = Constants.thingsFinishedCount + 1;
            Constants.thingsUndoCount = Constants.thingsUndoCount - 1;
        }


        ContentValues values = new ContentValues();
        values.put("selected", thing.getSelected());
        thingDao.updateCache(values, "id=?", new String[]{thing.getId() + ""});
    }

    /**
     * 添加一条新的thing
     *
     * @param thing
     */
    public boolean addThing(Thing thing) {

        Constants.thingsUndoCount = Constants.thingsUndoCount + 1;
        Constants.allThingsCount = Constants.allThingsCount + 1;

        return thingDao.addCache(thing);
    }

    /**
     * 获取所有的事情
     *
     * @return 数据库存在用户配置 ? 数据库内的用户选择 : 默认用户选择 ;
     */
    public List<Thing> getThings() {
        Object cacheList = thingDao.listCache("", new String[]{});
        if (cacheList != null && !((List) cacheList).isEmpty()) {
            userExist = true;
            List<Map<String, String>> maplist = (List) cacheList;
            int count = maplist.size();
            List<Thing> list = new ArrayList<Thing>();
            for (int i = 0; i < count; i++) {
                Thing navigate = new Thing();
                navigate.setId(Integer.valueOf(maplist.get(i).get(SQLHelper.ID)));
                navigate.setName(maplist.get(i).get(SQLHelper.NAME));
                navigate.setOrderId(Integer.valueOf(maplist.get(i).get(SQLHelper.ORDERID)));
                navigate.setSelected(Integer.valueOf(maplist.get(i).get(SQLHelper.SELECTED)));
                list.add(navigate);
            }
            return list;
        }
        initDefaultThing();
        return defaultThings;
    }

    /**
     * 获取已完成的事件
     *
     * @return 数据库存在用户配置 ? 数据库内的已经完成 : 默认的已经完成 ;
     */
    public List<Thing> getFinishedThing() {
        Object cacheList = thingDao.listCache(SQLHelper.SELECTED + "= ?", new String[]{"1"});
        List<Thing> list = new ArrayList<Thing>();
        if (cacheList != null && !((List) cacheList).isEmpty()) {
            List<Map<String, String>> maplist = (List) cacheList;
            int count = maplist.size();
            for (int i = 0; i < count; i++) {
                Thing navigate = new Thing();
                navigate.setId(Integer.valueOf(maplist.get(i).get(SQLHelper.ID)));
                navigate.setName(maplist.get(i).get(SQLHelper.NAME));
                navigate.setOrderId(Integer.valueOf(maplist.get(i).get(SQLHelper.ORDERID)));
                navigate.setSelected(Integer.valueOf(maplist.get(i).get(SQLHelper.SELECTED)));
                list.add(navigate);
            }
            return list;
        }
        if (userExist) {
            return list;
        }
        cacheList = defaultFinishedThings;
        return (List<Thing>) cacheList;
    }

    /**
     * 获取未完成的事件
     *
     * @return 数据库存在用户配置 ? 数据库内的未完成 : 默认的未完成 ;
     */
    public List<Thing> getUndoThing() {
        Object cacheList = thingDao.listCache(SQLHelper.SELECTED + "= ?", new String[]{"0"});
        List<Thing> list = new ArrayList<Thing>();
        if (cacheList != null && !((List) cacheList).isEmpty()) {
            List<Map<String, String>> maplist = (List) cacheList;
            int count = maplist.size();
            for (int i = 0; i < count; i++) {
                Thing navigate = new Thing();
                navigate.setId(Integer.valueOf(maplist.get(i).get(SQLHelper.ID)));
                navigate.setName(maplist.get(i).get(SQLHelper.NAME));
                navigate.setOrderId(Integer.valueOf(maplist.get(i).get(SQLHelper.ORDERID)));
                navigate.setSelected(Integer.valueOf(maplist.get(i).get(SQLHelper.SELECTED)));
                list.add(navigate);
            }
            return list;
        }
        if (userExist) {
            return list;
        }
        cacheList = defaultUndoThings;
        return (List<Thing>) cacheList;
    }

    /**
     * 初始化默认的所有事情
     */
    private void initDefaultThing() {
        deleteAllThing();
        saveThings(defaultThings);
    }

    /**
     * 把 things 保存到数据库
     *
     * @param things
     */
    public void saveThings(List<Thing> things) {
        for (int i = 0; i < things.size(); i++) {
            Thing thing = things.get(i);
            thingDao.addCache(thing);
        }
    }
}

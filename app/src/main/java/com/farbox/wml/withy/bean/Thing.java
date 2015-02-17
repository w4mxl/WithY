package com.farbox.wml.withy.bean;

/**
 * Created by wml on 14/11/27 13:12.
 */
public class Thing {

    public Integer id;//事情id
    public String name;//事情名称
    public Integer orderId;//事情在整体中的排序
    public Integer selected;//事情是否被选中（完成）

    public Thing() {
    }

    public Thing(int id, String name, int orderId, int selected) {
        this.id = Integer.valueOf(id);
        this.name = name;
        this.orderId = Integer.valueOf(orderId);
        this.selected = Integer.valueOf(selected);
    }

    @Override
    public String toString() {
        return "Thing{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", orderId=" + orderId +
                ", selected=" + selected +
                '}';
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getOrderId() {
        return orderId;
    }

    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
    }

    public Integer getSelected() {
        return selected;
    }

    public void setSelected(Integer selected) {
        this.selected = selected;
    }
}

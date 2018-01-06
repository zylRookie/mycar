package com.example.zyl.dqcar.moudels.bean;


import com.mcxtzhang.indexlib.IndexBar.bean.BaseIndexPinyinBean;

/**
 * Created by zhangxutong .
 * Date: 16/08/28
 */

public class UserNameBean extends BaseIndexPinyinBean {

    private String city;//城市名字
    private boolean isTop;//是否是最上面的 不需要被转化成拼音的
    private int isInGroup;
    private String userId;

    private boolean isSelected;

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public int getIsInGroup() {
        return isInGroup;
    }

    public void setIsInGroup(int isInGroup) {
        this.isInGroup = isInGroup;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getImgKey() {
        return imgKey;
    }

    public void setImgKey(String imgKey) {
        this.imgKey = imgKey;
    }

    private String userName;
    private String imgKey;

    public UserNameBean() {
    }

    public UserNameBean(String city) {
        this.city = city;
    }

    public String getCity() {
        return city;
    }

    public UserNameBean setCity(String city) {
        this.city = city;
        return this;
    }

    public boolean isTop() {
        return isTop;
    }

    public UserNameBean setTop(boolean top) {
        isTop = top;
        return this;
    }

    @Override
    public String getTarget() {
        return city;
    }

    @Override
    public boolean isNeedToPinyin() {
        return !isTop;
    }


    @Override
    public boolean isShowSuspension() {
        return !isTop;
    }
}

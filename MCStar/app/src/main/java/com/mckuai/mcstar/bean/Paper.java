package com.mckuai.mcstar.bean;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * 考卷
 * Created by kyly on 2015/10/12.
 */
public class Paper implements Serializable{
    protected int id;
    protected String name;//名称
    protected String cover;//封面
    protected ArrayList<Questin> questins;//习题列表
    protected ArrayList<MCUser> mUsers; //贡献者

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public ArrayList<Questin> getQuestins() {
        return questins;
    }

    public void setQuestins(ArrayList<Questin> questins) {
        this.questins = questins;
    }

    public ArrayList<MCUser> getmUsers() {
        return mUsers;
    }

    public void setmUsers(ArrayList<MCUser> mUsers) {
        this.mUsers = mUsers;
    }
}

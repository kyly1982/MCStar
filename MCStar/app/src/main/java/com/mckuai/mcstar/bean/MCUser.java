package com.mckuai.mcstar.bean;

import java.io.Serializable;

/**
 * Created by kyly on 2015/9/29.
 */
public class MCUser implements Serializable {
    private int id;// 用户id
    private int topicCount;// 贡献题目数
    private int paperCount;//答题次数
    private int level;// 当前等级
    private long score;// 当前积分
    private String name;//用户名
    private String nike;// 昵称，显示用
    private String token_QQ;//qq令牌
    private String cover;// 头像
    private String gender;// 性别

    public MCUser(){

    }

    public MCUser(String nike){
        this.nike = nike;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getTopicCount() {
        return topicCount;
    }

    public void setTopicCount(int topicCount) {
        this.topicCount = topicCount;
    }

    public int getPaperCount() {
        return paperCount;
    }

    public void setPaperCount(int paperCount) {
        this.paperCount = paperCount;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public long getScore() {
        return score;
    }

    public void setScore(long score) {
        this.score = score;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNike() {
        return nike;
    }

    public void setNike(String nike) {
        this.nike = nike;
    }

    public String getToken_QQ() {
        return token_QQ;
    }

    public void setToken_QQ(String token_QQ) {
        this.token_QQ = token_QQ;
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }
}

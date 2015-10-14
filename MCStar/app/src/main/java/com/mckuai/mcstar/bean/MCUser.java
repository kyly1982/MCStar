package com.mckuai.mcstar.bean;

import java.io.Serializable;

/**
 * Created by kyly on 2015/9/29.
 */
public class MCUser implements Serializable {
    private int id;// 用户id
    private int uploadNum;// 贡献题目数
    private int answerNum;//答题次数
    private int level;// 当前等级
    private long allScore;// 当前积分
    private long ranking;//排名
    private String userName;//用户名，实为openId
    private String nickName;// 昵称，显示用
    private String headImg;// 头像
    private String sex;// 性别

    public MCUser(){

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUploadNum() {
        return uploadNum;
    }

    public void setUploadNum(int uploadNum) {
        this.uploadNum = uploadNum;
    }

    public int getAnswerNum() {
        return answerNum;
    }

    public void setAnswerNum(int answerNum) {
        this.answerNum = answerNum;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public long getAllScore() {
        return allScore;
    }

    public void setAllScore(long allScore) {
        this.allScore = allScore;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }


    public String getHeadImg() {
        return headImg;
    }

    public void setHeadImg(String headImg) {
        this.headImg = headImg;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public long getRanking() {
        return ranking;
    }

    public void setRanking(long ranking) {
        this.ranking = ranking;
    }
}

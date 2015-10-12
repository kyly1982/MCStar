package com.mckuai.mcstar.bean;

import java.io.Serializable;

/**
 * 习题信息
 * Created by kyly on 2015/10/12.
 */
public class Questin implements Serializable {
    protected int id;//编号
    protected int useCount;//回答次数
    protected int correctCount;//正确回答次数
    protected AuditStatus status ;//状态
    protected MCUser contributor;//贡献者
    protected String time;  //添加时间
    protected String topic; // 题目
    protected String options;  //选项，选项间用逗号隔离，第一个为正确选项
    protected String image;  //图片，图片间用逗号隔离

    public Questin(){

    }

    public Questin(int id,MCUser contributor,String topic,String options,String image){
        this.id = id;
        this.contributor = contributor;
        this.topic = topic;
        this.options = options;
        this.image = image;
    }

    public Questin(int id,String userName,String topic,String options,String image){
        this.id = id;
        this.topic = topic;
        this.options = options;
        this.image = image;
        this.contributor = new MCUser();
        this.contributor.setName(userName);
        this.status = AuditStatus.Auditing;
        this.time = "9-30";
        this.useCount = 22;
        this.correctCount = 15;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUseCount() {
        return useCount;
    }

    public void setUseCount(int count) {
        useCount = count;
    }

    public int getCorrectCount() {
        return correctCount;
    }

    public void setCorrectCount(int correctCount) {
        this.correctCount = correctCount;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public MCUser getContributor() {
        return contributor;
    }

    public void setContributor(MCUser contributor) {
        this.contributor = contributor;
    }

    public AuditStatus getStatus() {
        return status;
    }

    public void setStatus(AuditStatus status) {
        this.status = status;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public String getOptions() {
        return options;
    }

    public void setOptions(String options) {
        this.options = options;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    //获取已经打乱顺序了的选项
    public String[] getOptionsEx() {
        if (null != options && !options.isEmpty()) {
            String[] mOptions = options.split(",");
            //打乱顺序
            if (null != mOptions && 0 < mOptions.length) {
                String temp;
                int index;
                for (int i = 0; i < mOptions.length; i++) {
                    index = (int) (Math.random() * mOptions.length);
                    temp = mOptions[index];
                    mOptions[index] = mOptions[i];
                    mOptions[i] = temp;
                }
                //标上序号
                for (int i = 0;i < mOptions.length;i++){
                    if (0 == i){
                        mOptions[i] = "A."+mOptions[i];
                    } else if (1 == i){
                        mOptions[i] = "B."+mOptions[i];
                    } else if (2 == i){
                        mOptions[i] = "C."+mOptions[i];
                    } else if (3 == i){
                        mOptions[i] = "D."+mOptions[i];
                    } else if (4 == i){
                        mOptions[i] = "E."+mOptions[i];
                    } else if (5 == i){
                        mOptions[i] = "F."+mOptions[i];
                    } else if (6 == i){
                        mOptions[i] = "G."+mOptions[i];
                    }
                }
                return mOptions;
            }
        }
        return null;
    }

    //判断答案是否正确
    public boolean isRightOption(String optins){
        if (null != optins && !optins.isEmpty() && null != options && !options.isEmpty()) {
            String[] mOptions = options.split(",");
            return optins.substring(2).equals(mOptions[0]);
        }
        return false;
    }

    public enum AuditStatus{
        Auditing,
        Pass,
        False
    }
}

package com.example.capstone.database;

public class GroupDisplay {
    String local;
    int max;
    int member;
    long starttime;
    String title;

    public GroupDisplay(){}

    public String getLocal() {
        return local;
    }
    public void setLocal(String Local) {
        this.local = local;
    }
    public int getMax() {
        return max;
    }
    public void setMax(int max) {
        this.max = max;
    }
    public int getMember() {
        return member;
    }
    public void setMember(int member) {
        this.member = member;
    }
    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public long getStarttime() {
        return starttime;
    }
    public void setStarttime(long starttime) {
        this.starttime = starttime;
    }



    //이거는 그룹을 생성할때 사용하는 부분
    public GroupDisplay(String title, String local, int max, int member, long starttime) {
        this.title = title;
        this.max = max;
        this.local = local;
        this.starttime = starttime;
        this.member = member;
    }
}

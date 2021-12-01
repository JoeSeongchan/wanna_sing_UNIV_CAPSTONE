package com.example.capstone.database;

public class Group {

        String  id;
        String hostid;
        int max;
        String placeid;
        int starttime;
        int endtime;
        String title;

        public Group(){}

        public String getid() {
                return id;
        }
        public void setid(String id) {
                this.id = id;
        }
        public String gethostid() {
                return hostid;
        }
        public void sethostid(String hostid) {
                this.hostid = hostid;
        }
        public String getplaceid() {
                return placeid;
        }
        public void setplaceid(String placeid) {
                this.placeid = placeid;
        }
        public String gettitle() {
                return title;
        }
        public void setstitle(String title) {
                this.title = title;
        }
        public int getmax() {
                return max;
        }
        public void setmax(int max) {
                this.max = max;
        }
        public int getStarttime() {
                return starttime;
        }
        public void setStarttime(int starttime) {
                this.starttime = starttime;
        }
        public int getEndtime() {
                return endtime;
        }
        public void setEndtime(int endtime) {
                this.endtime = endtime;
        }


        //이거는 그룹을 생성할때 사용하는 부분
        public Group(String title, String id, int max, String hostid, String placeid, int starttime, int endtime) {
                this.id = id;
                this.title = title;
                this.max = max;
                this.hostid = hostid;
                this.starttime = starttime;
                this.endtime = endtime;
                this.placeid=placeid;
        }


}

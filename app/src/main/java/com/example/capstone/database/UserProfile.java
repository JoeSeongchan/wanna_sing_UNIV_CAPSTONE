package com.example.capstone.database;

public class UserProfile {
    String nickname;
    String name;
    boolean gender;
    String primegenre;
    String primelocal;
    String primesong;
    String voicecharac;

    public UserProfile(){}

    public String getNickname() {
        return nickname;
    }
    public void setNickname(String nickname) {
        this.nickname = nickname;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public boolean getGender() {
        return gender;
    }
    public void setGender(boolean gender) {
        this.gender = gender;
    }
    public String getPrimegenre() {
        return primegenre;
    }
    public void setPrimegenre(String primegenre) {
        this.primegenre = primegenre;
    }
    public String getPrimelocal() {
        return primelocal;
    }
    public void setPrimelocal(String primelocal) {
        this.primelocal = primelocal;
    }
    public String getPrimesong() {
        return primesong;
    }
    public void setPrimesong(String primesong) {
        this.primesong = primesong;
    }
    public String getVoicecharac() {
        return voicecharac;
    }
    public void setVoicecharac(String voicecharac) {
        this.voicecharac = voicecharac;
    }


    //이거는 그룹을 생성할때 사용하는 부분
    public UserProfile(String nickname, String name, boolean gender, String primegenre, String primelocal, String primesong, String voicecharac) {
        this.nickname = nickname;
        this.name = name;
        this.gender = gender;
        this.primegenre = primegenre;
        this.primelocal = primelocal;
        this.primesong = primesong;
        this.voicecharac = voicecharac;
    }
}

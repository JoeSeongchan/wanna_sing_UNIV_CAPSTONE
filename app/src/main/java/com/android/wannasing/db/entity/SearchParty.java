package com.android.wannasing.db.entity;

import java.util.List;
import java.util.Objects;

public class SearchParty {

    public String hostID;
    public String partyName;
    public List<Genre> genreList;
    public Gender gender;
    public int age;
    public List<AgeDetail> ageDetailList;


    public SearchParty(String partyName,
                       List<com.android.wannasing.db.entity.SearchParty.Genre> genreList,
                       com.android.wannasing.db.entity.SearchParty.Gender gender, int age,
                       List<com.android.wannasing.db.entity.SearchParty.AgeDetail> ageDetailList) {
        this.partyName = partyName;
        this.genreList = genreList;
        this.gender = gender;
        this.age = age;
        this.ageDetailList = ageDetailList;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SearchParty)) {
            return false;
        }
        SearchParty party = (SearchParty) o;
        return hostID.equals(party.hostID) && partyName.equals(party.partyName);
    }


    @Override
    public int hashCode() {
        return Objects.hash(hostID, partyName);
    }


    public enum AgeDetail {
        EARLY("초반"), MID("중반"), LATE("후반"), FREE("자유");
        private final String value;

        AgeDetail(String value) {
            this.value = value;
        }

        public String getValue() {
            return this.value;
        }
    }

    public enum Genre {

        FREE("자유"),
        TOP("인기"),
        BALAD("발라드"),
        NORMAL_SONG("가요"),
        POP("팝"),
        HIPHOP("힙합"),
        JPOP("JPOP"),
        OTHER("기타"),
        OLD_SONG("트로트");
        private final String value;

        Genre(String value) {
            this.value = value;
        }

        public String getValue() {
            return this.value;
        }
    }

    public enum Gender {
        FEMALE("여성"), MALE("남성"), FREE("자유");
        private final String value;

        Gender(String value) {
            this.value = value;
        }

        public String getValue() {
            return this.value;
        }
    }
}
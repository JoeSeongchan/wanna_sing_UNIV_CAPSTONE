package com.example.detailgrouptest.map;

import java.util.List;

public class ResultSearchKeyword {

  PlaceMeta metadata;
  List<Place> documents;

  public PlaceMeta getMetadata() {
    return metadata;
  }

  public void setMetadata(PlaceMeta metadata) {
    this.metadata = metadata;
  }

  public List<Place> getDocuments() {
    return documents;
  }

  public void setDocuments(List<Place> documents) {
    this.documents = documents;
  }
}

class PlaceMeta {

  int total_count;    //검색어에 검색된 문서 수
  int pageable_count; //total_count 중 노출 가능 문서 수, 최대 45
  Boolean is_end;     //현재 페이지가 마지막 페이지인지 여부, 값이 false면 페이지를 증가시켜 다음 페이지를 요청할 수 있음
  RegionInfo same_name;   //질의어의 지역 및 키워드 분석 정보
}

class RegionInfo {

  List<String> region;    //질의어에서 인식된 지역의 리스트 ex. 중앙로 맛집 > 중앙로에 해당하는 지역리스트
  String keyword;         //질의어에서 지역 정보를 제외한 키워드 ex. 중앙로맛집 > 맛집
  String selected_region; //인식된 지역 리스트 중, 현재 검색
}

class Place {

  String id;
  String place_name;
  String category_name;
  String category_group_code;
  String category_group_name;
  String phone;
  String address_name;
  String road_address_name;
  String x;
  String y;
}
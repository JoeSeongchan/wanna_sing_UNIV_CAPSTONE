package com.android.wannasing.home.fragment.map.api.model;

import java.util.List;

public class RegionInfo {

  public List<String> region;    //질의어에서 인식된 지역의 리스트 ex. 중앙로 맛집 > 중앙로에 해당하는 지역리스트
  public String keyword;         //질의어에서 지역 정보를 제외한 키워드 ex. 중앙로맛집 > 맛집
  public String selected_region; //인식된 지역 리스트 중, 현재 검색
}

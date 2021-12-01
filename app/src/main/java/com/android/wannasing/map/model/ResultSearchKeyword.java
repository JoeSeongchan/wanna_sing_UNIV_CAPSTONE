package com.android.wannasing.map.model;


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

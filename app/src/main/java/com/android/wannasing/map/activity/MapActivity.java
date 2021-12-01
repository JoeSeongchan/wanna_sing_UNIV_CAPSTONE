package com.android.wannasing.map.activity;

import android.Manifest;
import android.Manifest.permission;
import android.os.Bundle;
import android.util.Log;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.android.wannasing.databinding.ActivityMapBinding;
import com.android.wannasing.map.ApiClient;
import com.android.wannasing.map.DetailInfo;
import com.android.wannasing.map.KakaoAPIInterface;
import com.android.wannasing.map.activity.permission.PermissionChecker;
import com.android.wannasing.map.model.Place;
import com.android.wannasing.map.model.ResultSearchKeyword;
import com.android.wannasing.utilities.Utilities;
import com.android.wannasing.utilities.Utilities.LogType;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import net.daum.mf.map.api.MapPOIItem;
import net.daum.mf.map.api.MapPoint;
import net.daum.mf.map.api.MapView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MapActivity extends AppCompatActivity implements MapView.CurrentLocationEventListener,
    MapView.MapViewEventListener, MapView.POIItemEventListener {

  String API_KEY = "KakaoAK 06da443039521a441411e567abc55454"; //REST API 키를 입력합니다 KakaoAK와 함께 전송
  String[] REQUIRED_PERMISSIONS = {Manifest.permission.ACCESS_FINE_LOCATION};

  // 좌표.
  private MapPoint.GeoCoordinate geoCoordinate;
  private MapView mapView;
  private ViewGroup mapViewContainer;

  // 검색 결과 리스트.
  private List<Place> resultLists;

  private ActivityMapBinding binding;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    binding = ActivityMapBinding.inflate(getLayoutInflater());
    setContentView(binding.getRoot());
    requestPermissions();
  }

  // 권한 요청하는 함수.
  private void requestPermissions() {
    PermissionChecker.create(() -> {
      Utilities.log(LogType.d, "granted.");
      setUi();
      setLocationListener();
    }, () -> {
      Utilities.log(LogType.w, "denied.");
      finish();
    }).requestPermissions(
        permission.ACCESS_COARSE_LOCATION,
        permission.ACCESS_FINE_LOCATION);
  }

  // UI 설정하는 함수 (Map Vie 설정)
  private void setUi() {
    mapView = new MapView(this);
    mapViewContainer = binding.mapRlContainer;
    mapViewContainer.addView(mapView);
    binding.mapBtnSearch.setOnClickListener(v -> searchKeyword("노래방"));
  }

  // 현재 위치 띄우게 하는 함수.
  private void setLocationListener() {
    mapView.setMapViewEventListener(this);
    mapView.setCurrentLocationTrackingMode(
        MapView.CurrentLocationTrackingMode.TrackingModeOnWithoutHeading);
    mapView.setPOIItemEventListener(this);
  }


  @Override
  protected void onDestroy() {
    super.onDestroy();
    mapViewContainer.removeAllViews();
  }


  @Override
  public void onCurrentLocationUpdate(MapView mapView, MapPoint currentLocation,
      float accuracyInMeters) {
    MapPoint.GeoCoordinate mapPointGeo = currentLocation.getMapPointGeoCoord();
    Utilities.log(LogType.i, String
        .format(Locale.KOREAN,
            "MapView onCurrentLocationUpdate (%f,%f) accuracy (%f)",
            mapPointGeo.latitude,
            mapPointGeo.longitude,
            accuracyInMeters));
  }

  @Override
  public void onCurrentLocationDeviceHeadingUpdate(MapView mapView, float v) {
  }

  @Override
  public void onCurrentLocationUpdateFailed(MapView mapView) {
  }

  @Override
  public void onCurrentLocationUpdateCancelled(MapView mapView) {
  }

  @Override
  public void onMapViewInitialized(MapView mapView) {

  }

  @Override
  public void onMapViewCenterPointMoved(MapView mapView, MapPoint mapPoint) {

  }

  @Override
  public void onMapViewZoomLevelChanged(MapView mapView, int i) {

  }

  @Override
  public void onMapViewSingleTapped(MapView mapView, MapPoint mapPoint) {

  }

  @Override
  public void onMapViewDoubleTapped(MapView mapView, MapPoint mapPoint) {

  }

  @Override
  public void onMapViewLongPressed(MapView mapView, MapPoint mapPoint) {

  }

  @Override
  public void onMapViewDragStarted(MapView mapView, MapPoint mapPoint) {

  }

  @Override
  public void onMapViewDragEnded(MapView mapView, MapPoint mapPoint) {

  }

  @Override
  public void onMapViewMoveFinished(MapView mapView, MapPoint mapPoint) {

  }

  //노래방 찾는 함수.
  private void searchKeyword(String keyword) {

    //현재 지도의 중심 받아오기
    geoCoordinate = mapView.getMapCenterPoint().getMapPointGeoCoord();
    double mapCenter_x = geoCoordinate.longitude;
    double mapCenter_y = geoCoordinate.latitude;

    String x = Double.toString(mapCenter_x);
    String y = Double.toString(mapCenter_y);

    int radius = 10000; //단위 m

    Log.d("WANTSLEEP",
        "\nCenter" + "x: " + mapCenter_x + "\n" + "y: " + mapCenter_y + "\nString x: " + x
            + "   String y: " + y + "\n\n");

    KakaoAPIInterface spotInterface = ApiClient
        .getApiClient().create(KakaoAPIInterface.class);
    Call<ResultSearchKeyword> call = spotInterface
        .getSearchKeyword(API_KEY, keyword, x, y, radius);

    call.enqueue(new Callback<ResultSearchKeyword>() {
      //연결 성공 시에 실행되는 부분
      @Override
      public void onResponse(
          @NonNull Call<ResultSearchKeyword> call,
          @NonNull Response<ResultSearchKeyword> response) {

        Log.e("onSuccess", String.valueOf(response.raw()));

        System.out.println("RESULT" + response.body());
        System.out.println("RESULT22" + response.body().getDocuments());
        //System.out.println(pageable_count);

        addItems(response.body());
      }


      @Override
      public void onFailure(@NonNull Call<ResultSearchKeyword> call,
          @NonNull Throwable t) {
        Log.e("onfail", "에러 = " + t.getMessage());
      }
    });
  }


  //검색결과처리
  private void addItems(ResultSearchKeyword searchResult) {
    //지도 마커 모두 제거
    mapView.removeAllPOIItems();

    List<Place> dataArr = searchResult.getDocuments();
    resultLists = searchResult.getDocuments();

    ArrayList<MapPOIItem> markerArr = new ArrayList<MapPOIItem>();
    for (Place data : dataArr) {
      MapPOIItem marker = new MapPOIItem();
      marker.setMapPoint(
          MapPoint.mapPointWithGeoCoord(Double.parseDouble(data.y), Double.parseDouble(data.x)));
      marker.setItemName(data.place_name);
      markerArr.add(marker);
      marker.setMarkerType(MapPOIItem.MarkerType.BluePin);
      marker.setSelectedMarkerType(MapPOIItem.MarkerType.RedPin);
      //Log.d("TEST","-markerADD\n"+"data.x: "+data.x+"\n"+"data.y: "+data.y+"\n"+"Place.name: "+data.place_name+"\n");

    }
    mapView.addPOIItems(markerArr.toArray(new MapPOIItem[markerArr.size()]));
  }


  //말풍선 클릭에 관련된
  @Override
  public void onPOIItemSelected(MapView mapView, MapPOIItem mapPOIItem) {

  }

  @Override
  public void onCalloutBalloonOfPOIItemTouched(MapView mapView, MapPOIItem mapPOIItem) {
  }

  @Override
  public void onCalloutBalloonOfPOIItemTouched(MapView mapView, MapPOIItem mapPOIItem,
      MapPOIItem.CalloutBalloonButtonType calloutBalloonButtonType) {

    double lat = mapPOIItem.getMapPoint().getMapPointGeoCoord().latitude;
    double lng = mapPOIItem.getMapPoint().getMapPointGeoCoord().longitude;
    String karaoke_id = "노래방ID";
    String place_name = "노래방이름";
    String phone = "노래방번호";
    String address_name = "노래방 지번주소";
    String road_address_name = "노래방 도로명주소";

    for (Place data : resultLists) {
      double D_x = Double.parseDouble(data.x);
      double D_y = Double.parseDouble(data.y);
      //Log.d("TEST","\nWHY"+"D_x: "+Math.floor(D_x*1000)/10000.0+"\n"+"D_y: "+(Math.floor(D_y*100000)/1000000.0)+"\n");
      //Log.d("TEST","\nWHY2"+"LNG: "+(Math.floor(lng*1000)/10000.0)+"\n"+"LAT: "+(Math.floor(lat*100000)/1000000.0)+"\n\n");

      if (((Math.floor(D_x * 1000) / 1000.0) == (Math.floor(lng * 1000) / 1000.0))
          && ((Math.floor(D_y * 100000) / 100000.0) == (Math.floor(lat * 100000) / 100000.0))) {

        karaoke_id = data.id;
        place_name = data.place_name;
        phone = data.phone;
        address_name = data.address_name;
        road_address_name = data.road_address_name;
        //Log.d("TEST","\n????"+"DONE!"+place_name+"\n");

      }
      //Log.d("TEST","-------\n"+"x: "+Math.floor(D_x*1000000)/1000000.0+"\n"+"D_y: "+(Math.floor(lng*1000000)/1000000.0)+"\n");
    }

    Log.d("TEST",
        "---------\n" + "Place.name: " + place_name + " Phone num: " + phone + " Address1: "
            + address_name + " Address2: " + road_address_name + "\n");
    //Log.d("TEST","-------\n"+"x: "+"\n"+"D_y: "+(Math.floor(lng*1000000)/1000000.0)+"\n");

    DetailInfo detailInfo = DetailInfo
        .newInstance(karaoke_id, place_name, phone, address_name, road_address_name);
    detailInfo.show(getSupportFragmentManager(), "Popup");

  }

  @Override
  public void onDraggablePOIItemMoved(MapView mapView, MapPOIItem mapPOIItem, MapPoint mapPoint) {

  }

}
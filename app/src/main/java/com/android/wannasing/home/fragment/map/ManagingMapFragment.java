package com.android.wannasing.home.fragment.map;


import android.Manifest.permission;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.android.wannasing.R;
import com.android.wannasing.home.fragment.map.api.ApiClient;
import com.android.wannasing.home.fragment.map.api.DetailInfo;
import com.android.wannasing.home.fragment.map.api.KakaoAPIInterface;
import com.android.wannasing.home.fragment.map.api.model.Place;
import com.android.wannasing.home.fragment.map.api.model.ResultSearchKeyword;
import com.android.wannasing.home.fragment.map.permissioncheck.PermissionChecker;
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

public class ManagingMapFragment extends Fragment implements
    MapView.CurrentLocationEventListener,
    MapView.MapViewEventListener, MapView.POIItemEventListener {

  String API_KEY = "KakaoAK 06da443039521a441411e567abc55454"; //REST API 키를 입력합니다 KakaoAK와 함께 전송

  // 좌표.
  private MapPoint.GeoCoordinate geoCoordinate;
  private MapView mapView;
  private ViewGroup mapViewContainer;

  // 검색 결과 리스트.
  private List<Place> resultLists;

  private View rootView;

  private ManagingMapFragment() {
  }

  public static ManagingMapFragment newInstance() {
    ManagingMapFragment instance = new ManagingMapFragment();
    return instance;
  }

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
  }

  @Nullable
  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    return inflater.inflate(R.layout.fragment_managing_map, container, false);
  }

  @Override
  public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    rootView = view;
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
      informActivityThatRequestIsRejected();
    }).requestPermissions(
        permission.ACCESS_COARSE_LOCATION,
        permission.ACCESS_FINE_LOCATION);
  }

  private void informActivityThatRequestIsRejected() {
    Bundle result = new Bundle();
    result.putBoolean("IS_REQUEST_APPROVED", false);
    getParentFragmentManager()
        .setFragmentResult("MAP_RESULT", result);
  }

  private void setUi() {
    setMapView();
    setBtnListener();
  }

  private void setMapView() {
    mapView = new MapView(getActivity());
    mapViewContainer = rootView.findViewById(R.id.map_rl_container);
    mapViewContainer.addView(mapView);
  }

  private void setBtnListener() {
    rootView.findViewById(R.id.map_btn_search)
        .setOnClickListener(v -> searchKeyword("노래방"));
  }

  // 현재 위치 띄우게 하는 함수.
  private void setLocationListener() {
    mapView.setMapViewEventListener(this);
    mapView.setCurrentLocationTrackingMode(
        MapView.CurrentLocationTrackingMode.TrackingModeOnWithoutHeading);
    mapView.setPOIItemEventListener(this);
  }


  @Override
  public void onDestroy() {
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

    KakaoAPIInterface spotInterface = ApiClient
        .getApiClient().create(KakaoAPIInterface.class);
    Call<ResultSearchKeyword> call = spotInterface
        .getSearchKeyword(API_KEY, keyword, x, y, radius);

    call.enqueue(new Callback<ResultSearchKeyword>() {
      //연결 성공 시,
      @Override
      public void onResponse(
          @NonNull Call<ResultSearchKeyword> call,
          @NonNull Response<ResultSearchKeyword> response) {
        addItems(response.body());
      }

      @Override
      public void onFailure(@NonNull Call<ResultSearchKeyword> call,
          @NonNull Throwable t) {
        Utilities.log(LogType.w, "error : " + t.getMessage());
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

      if (((Math.floor(D_x * 1000) / 1000.0) == (Math.floor(lng * 1000) / 1000.0))
          && ((Math.floor(D_y * 100000) / 100000.0) == (Math.floor(lat * 100000) / 100000.0))) {

        karaoke_id = data.id;
        place_name = data.place_name;
        phone = data.phone;
        address_name = data.address_name;
        road_address_name = data.road_address_name;
      }
    }

    DetailInfo detailInfo = DetailInfo
        .newInstance(karaoke_id, place_name, phone, address_name, road_address_name);
    detailInfo.show(getChildFragmentManager(), "Popup");
  }

  @Override
  public void onDraggablePOIItemMoved(MapView mapView, MapPOIItem mapPOIItem, MapPoint mapPoint) {
  }
}
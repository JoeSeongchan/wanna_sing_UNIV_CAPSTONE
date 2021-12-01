package com.example.detailgrouptest.map;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import com.example.detailgrouptest.R;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import net.daum.mf.map.api.MapPOIItem;
import net.daum.mf.map.api.MapPoint;
import net.daum.mf.map.api.MapView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MapActivity extends AppCompatActivity implements MapView.CurrentLocationEventListener,
    MapView.MapViewEventListener, MapView.POIItemEventListener {

  private static final String LOG_TAG = "MainActivity";
  private static final int GPS_ENABLE_REQUEST_CODE = 2001;
  private static final int PERMISSIONS_REQUEST_CODE = 100;
  private static int REQUEST_ACCESS_FINE_LOCATION = 1000; //권한체크
  String BASE_URL = "https://dapi.kakao.com/";
  String API_KEY = "KakaoAK 534e9adc37b6c26f6473f96cbb564cd7"; //REST API 키를 입력합니다 KakaoAK와 함께 전송
  String[] REQUIRED_PERMISSIONS = {Manifest.permission.ACCESS_FINE_LOCATION};
  //지도 중심 좌표
  MapPoint.GeoCoordinate geoCoordinate;
  private MapView mapView;
  private ViewGroup mapViewContainer;
  //검색결과리스트
  private List<com.example.detailgrouptest.map.Place> resultLists;

  //hashkey
  private void getHashKey() {
    PackageInfo packageInfo = null;
    try {
      packageInfo = getPackageManager()
          .getPackageInfo(getPackageName(), PackageManager.GET_SIGNATURES);
    } catch (PackageManager.NameNotFoundException e) {
      e.printStackTrace();
    }
    if (packageInfo == null) {
      Log.e("KeyHash", "KeyHash:null");
    }

    for (Signature signature : packageInfo.signatures) {
      try {
        MessageDigest md = MessageDigest.getInstance("SHA");
        md.update(signature.toByteArray());
        Log.d("KeyHash", Base64.encodeToString(md.digest(), Base64.DEFAULT));
      } catch (NoSuchAlgorithmException e) {
        Log.e("KeyHash", "Unable to get MessageDigest. signature=" + signature, e);
      }
    }
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_map);

    mapView = new MapView(this);
    mapViewContainer = (ViewGroup) findViewById(R.id.map_view);
    mapViewContainer.addView(mapView);

    //현재위치띄우기
    mapView.setMapViewEventListener(this);
    mapView.setCurrentLocationTrackingMode(
        MapView.CurrentLocationTrackingMode.TrackingModeOnWithoutHeading);

    mapView.setPOIItemEventListener(this);

    //search 버튼
    ImageButton SearchB = (ImageButton) findViewById(R.id.SearchButton);
    SearchB.setOnClickListener(new View.OnClickListener() {
      public void onClick(View v) {

        searchKeyword("노래방");

      }
    });

    //해시키얻어와야함!!!!!!!!
    //getHashKey();

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
    Log.i(LOG_TAG, String
        .format("MapView onCurrentLocationUpdate (%f,%f) accuracy (%f)", mapPointGeo.latitude,
            mapPointGeo.longitude, accuracyInMeters));
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


  private void onFinishReverseGeoCoding(String result) {
//        Toast.makeText(LocationDemoActivity.this, "Reverse Geo-coding : " + result, Toast.LENGTH_SHORT).show();
  }

  // ActivityCompat.requestPermissions를 사용한 퍼미션 요청의 결과를 리턴받는 메소드
  @Override
  public void onRequestPermissionsResult(int permsRequestCode,
      @NonNull String[] permissions,
      @NonNull int[] grandResults) {

    super.onRequestPermissionsResult(permsRequestCode, permissions, grandResults);
    if (permsRequestCode == PERMISSIONS_REQUEST_CODE
        && grandResults.length == REQUIRED_PERMISSIONS.length) {

      // 요청 코드가 PERMISSIONS_REQUEST_CODE 이고, 요청한 퍼미션 개수만큼 수신되었다면
      boolean check_result = true;

      // 모든 퍼미션을 허용했는지 체크합니다.
      for (int result : grandResults) {
        if (result != PackageManager.PERMISSION_GRANTED) {
          check_result = false;
          break;
        }
      }

      if (check_result) {
        Log.d("@@@", "start");
        //위치 값을 가져올 수 있음

      } else {
        // 거부한 퍼미션이 있다면 앱을 사용할 수 없는 이유를 설명해주고 앱을 종료합니다.2 가지 경우가 있다
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, REQUIRED_PERMISSIONS[0])) {
          Toast.makeText(MapActivity.this, "퍼미션이 거부되었습니다. 앱을 다시 실행하여 퍼미션을 허용해주세요.",
              Toast.LENGTH_LONG).show();
          finish();
        } else {
          Toast.makeText(MapActivity.this, "퍼미션이 거부되었습니다. 설정(앱 정보)에서 퍼미션을 허용해야 합니다. ",
              Toast.LENGTH_LONG).show();
        }
      }
    }
  }

  void checkRunTimePermission() {

    //런타임 퍼미션 처리
    // 1. 위치 퍼미션을 가지고 있는지 체크합니다.
    int hasFineLocationPermission = ContextCompat.checkSelfPermission(MapActivity.this,
        Manifest.permission.ACCESS_FINE_LOCATION);

    if (hasFineLocationPermission == PackageManager.PERMISSION_GRANTED) {
      // 2. 이미 퍼미션을 가지고 있다면
      // ( 안드로이드 6.0 이하 버전은 런타임 퍼미션이 필요없기 때문에 이미 허용된 걸로 인식합니다.)
      // 3.  위치 값을 가져올 수 있음

    } else {  //2. 퍼미션 요청을 허용한 적이 없다면 퍼미션 요청이 필요합니다. 2가지 경우(3-1, 4-1)가 있습니다.
      // 3-1. 사용자가 퍼미션 거부를 한 적이 있는 경우에는
      if (ActivityCompat
          .shouldShowRequestPermissionRationale(MapActivity.this, REQUIRED_PERMISSIONS[0])) {
        // 3-2. 요청을 진행하기 전에 사용자가에게 퍼미션이 필요한 이유를 설명해줄 필요가 있습니다.
        Toast.makeText(MapActivity.this, "이 앱을 실행하려면 위치 접근 권한이 필요합니다.", Toast.LENGTH_LONG).show();
        // 3-3. 사용자게에 퍼미션 요청을 합니다. 요청 결과는 onRequestPermissionResult에서 수신됩니다.
        ActivityCompat.requestPermissions(MapActivity.this, REQUIRED_PERMISSIONS,
            PERMISSIONS_REQUEST_CODE);
      } else {
        // 4-1. 사용자가 퍼미션 거부를 한 적이 없는 경우에는 퍼미션 요청을 바로 합니다.
        // 요청 결과는 onRequestPermissionResult에서 수신됩니다.
        ActivityCompat.requestPermissions(MapActivity.this, REQUIRED_PERMISSIONS,
            PERMISSIONS_REQUEST_CODE);
      }
    }
  }

  //여기부터는 GPS 활성화를 위한 메소드들
  private void showDialogForLocationServiceSetting() {

    AlertDialog.Builder builder = new AlertDialog.Builder(MapActivity.this);
    builder.setTitle("위치 서비스 비활성화");
    builder.setMessage("앱을 사용하기 위해서는 위치 서비스가 필요합니다.\n"
        + "위치 설정을 수정하시겠습니까?");
    builder.setCancelable(true);
    builder.setPositiveButton("설정", new DialogInterface.OnClickListener() {
      @Override
      public void onClick(DialogInterface dialog, int id) {
        Intent callGPSSettingIntent
            = new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
        startActivityForResult(callGPSSettingIntent, GPS_ENABLE_REQUEST_CODE);
      }
    });
    builder.setNegativeButton("취소", new DialogInterface.OnClickListener() {
      @Override
      public void onClick(DialogInterface dialog, int id) {
        dialog.cancel();
      }
    });
    builder.create().show();
  }

  @Override
  protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);

    switch (requestCode) {
      case GPS_ENABLE_REQUEST_CODE:
        //사용자가 GPS 활성 시켰는지 검사
        if (checkLocationServicesStatus()) {
          if (checkLocationServicesStatus()) {
            Log.d("@@@", "onActivityResult : GPS 활성화 되어있음");
            checkRunTimePermission();
            return;
          }
        }
        break;
    }
  }

  public boolean checkLocationServicesStatus() {
    LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

    return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
        || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
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


  //노래방서치
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

    com.example.detailgrouptest.map.KakaoAPIInterface spotInterface = com.example.detailgrouptest.map.ApiClient
        .getApiClient().create(com.example.detailgrouptest.map.KakaoAPIInterface.class);
    Call<com.example.detailgrouptest.map.ResultSearchKeyword> call = spotInterface
        .getSearchKeyword(API_KEY, keyword, x, y, radius);

    call.enqueue(new Callback<com.example.detailgrouptest.map.ResultSearchKeyword>() {
      //연결 성공 시에 실행되는 부분
      @Override
      public void onResponse(
          @NonNull Call<com.example.detailgrouptest.map.ResultSearchKeyword> call,
          @NonNull Response<com.example.detailgrouptest.map.ResultSearchKeyword> response) {

        Log.e("onSuccess", String.valueOf(response.raw()));

        System.out.println("RESULT" + response.body());
        System.out.println("RESULT22" + response.body().getDocuments());
        //System.out.println(pageable_count);

        addItems(response.body());
      }


      @Override
      public void onFailure(@NonNull Call<com.example.detailgrouptest.map.ResultSearchKeyword> call,
          @NonNull Throwable t) {
        Log.e("onfail", "에러 = " + t.getMessage());
      }
    });
  }


  //검색결과처리
  private void addItems(com.example.detailgrouptest.map.ResultSearchKeyword searchResult) {
    //지도 마커 모두 제거
    mapView.removeAllPOIItems();

    List<com.example.detailgrouptest.map.Place> dataArr = searchResult.getDocuments();
    resultLists = searchResult.getDocuments();

    ArrayList<MapPOIItem> markerArr = new ArrayList<MapPOIItem>();
    for (com.example.detailgrouptest.map.Place data : dataArr) {
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

    for (com.example.detailgrouptest.map.Place data : resultLists) {
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

    com.example.detailgrouptest.map.DetailInfo detailInfo = com.example.detailgrouptest.map.DetailInfo
        .newInstance(karaoke_id, place_name, phone, address_name, road_address_name);
    detailInfo.show(getSupportFragmentManager(), "Popup");

  }

  @Override
  public void onDraggablePOIItemMoved(MapView mapView, MapPOIItem mapPOIItem, MapPoint mapPoint) {

  }

}

package com.kartheek.healthybillion.task3;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.android.volley.NetworkResponse;
import com.android.volley.VolleyError;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.kartheek.healthybillion.OnStringResponseListener;
import com.kartheek.healthybillion.R;
import com.kartheek.healthybillion.utils.ResponseUtilities;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MapsActivity extends AppCompatActivity implements OnStringResponseListener {
    private static final String REQUEST_URL = "http://thehealthybillion.com/assignment/q4.csv";
    private static final int REQUEST_ID = 3011;
    private static final String REQUEST_TAG = "get_lat_lang";
    private static final String TAG = MapsActivity.class.getSimpleName();
    private static final int REQUEST_PLACES = 3012;
    private static final String REQUEST_PALCES_TAG = "getPlaces";
    private GoogleMap mMap; // Might be null if Google Play services APK is not available.
    private int requestPlaces;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps2);
        setUpMapIfNeeded();
        initToolbar();
        requestData();
    }

    private void requestData() {
        ResponseUtilities.getInstance().getStringResponseFromUrl(this, REQUEST_ID, this, REQUEST_URL, REQUEST_TAG);
    }


    @Override
    protected void onResume() {
        super.onResume();
        setUpMapIfNeeded();
    }

    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("Maps");
    }

    /**
     * Sets up the map if it is possible to do so (i.e., the Google Play services APK is correctly
     * installed) and the map has not already been instantiated.. This will ensure that we only ever
     * call {@link #setUpMap()} once when {@link #mMap} is not null.
     * <p/>
     * If it isn't installed {@link SupportMapFragment} (and
     * {@link com.google.android.gms.maps.MapView MapView}) will show a prompt for the user to
     * install/update the Google Play services APK on their device.
     * <p/>
     * A user can return to this FragmentActivity after following the prompt and correctly
     * installing/updating/enabling the Google Play services. Since the FragmentActivity may not
     * have been completely destroyed during this process (it is likely that it would only be
     * stopped or paused), {@link #onCreate(Bundle)} may not be called again so we should call this
     * method in {@link #onResume()} to guarantee that it will be called.
     */
    private void setUpMapIfNeeded() {
        // Do a null check to confirm that we have not already instantiated the map.
        if (mMap == null) {
            // Try to obtain the map from the SupportMapFragment.
            mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map))
                    .getMap();
            // Check if we were successful in obtaining the map.
            if (mMap != null) {
                setUpMap();
            }
        }
    }

    /**
     * This is where we can add markers or lines, add listeners or move the camera. In this case, we
     * just add a marker near Africa.
     * <p/>
     * This should only be called once and when we are sure that {@link #mMap} is not null.
     */
    private void setUpMap() {

    }

    @Override
    public void onResponse(String response, int requestId) {
        Log.d(TAG, "Response : " + response);
        if (response.length() > 0) {
            if (requestId == REQUEST_ID) {
                String[] points_arr = response.trim().split("\\n");
                List<LatBeans> pointsList = new ArrayList<>();
                for (int i = 0; i < points_arr.length; i++) {
                    String[] point = points_arr[i].split(",");
                    Log.d(TAG, "point : " + Arrays.toString(point));
                    double lat = Double.parseDouble(point[1].trim());
                    double lang = Double.parseDouble(point[2].trim());
                    LatBeans bean = new LatBeans(point[0].trim(), lat, lang, point[3].trim());
                    pointsList.add(bean);
                    if (i == 0) {
                        setLocation(lat, lang);
                        String url = getFormattedPlacesUrl("Hospital", new LatLng(lat, lang));
                        ResponseUtilities.getInstance().getStringResponseFromUrl(this, REQUEST_PLACES, this, url, REQUEST_PALCES_TAG);
                    }
                }
                addMarkers(pointsList);
            } else if (requestId == REQUEST_PLACES) {
                ObjectMapper mapper = new ObjectMapper(); // can reuse, share globally
                try {
                    NearbyPlacesBean placesBean = mapper.readValue(response.getBytes(), NearbyPlacesBean.class);
                    List<PlaceBean> results = placesBean.getResults();
                    BitmapDescriptor bitmap = BitmapDescriptorFactory.defaultMarker(getColorConst("Violet"));
                    for (PlaceBean placeBean : results) {
                        mMap.addMarker(new MarkerOptions().position(new LatLng(placeBean.location().getLat(), placeBean.location().getLng())).title(placeBean.getName()).icon(bitmap));
                    }
                    Log.w(TAG, "places bean : " + placesBean);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_maps, menu);
        return true;
    }

    private float getColorConst(String color) {
        switch (color) {
            case "Red":
                return BitmapDescriptorFactory.HUE_RED;
            case "Yellow":
                return BitmapDescriptorFactory.HUE_YELLOW;
            case "Blue":
                return BitmapDescriptorFactory.HUE_BLUE;
            case "Green":
                return BitmapDescriptorFactory.HUE_GREEN;
            case "Violet":
                return BitmapDescriptorFactory.HUE_VIOLET;
            default:
                return BitmapDescriptorFactory.HUE_RED;
        }
    }

    private void setLocation(double latitude, double logitude) {
        CameraUpdate cameraUpdate;
        cameraUpdate = CameraUpdateFactory.newLatLngZoom(new LatLng(latitude, logitude), (latitude == 0 && logitude == 0) ? 0 : 12);
        mMap.animateCamera(cameraUpdate);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_view_type_hybrid:
                if (mMap.getMapType() != GoogleMap.MAP_TYPE_HYBRID) {
                    mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
                }
                return true;

            case R.id.menu_view_type_normal:
                if (mMap.getMapType() != GoogleMap.MAP_TYPE_NORMAL) {
                    mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                }
                return true;

            case R.id.menu_view_type_satellite:
                if (mMap.getMapType() != GoogleMap.MAP_TYPE_SATELLITE) {
                    mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
                }
                return true;

            case R.id.menu_view_type_terrain:
                if (mMap.getMapType() != GoogleMap.MAP_TYPE_TERRAIN) {
                    mMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    private void addMarkers(List<LatBeans> pointsList) {
        for (LatBeans bean : pointsList) {
            BitmapDescriptor bitmap = BitmapDescriptorFactory.defaultMarker(getColorConst(bean.getColor()));
            mMap.addMarker(new MarkerOptions().position(new LatLng(bean.getLat(), bean.getLang())).icon(bitmap).title(bean.getName()));
        }
    }

    public String getFormattedPlacesUrl(String src, LatLng from) {
        return String.format("https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=%s,%s&radius=5000&types=hospital&key=%s", String.valueOf(from.latitude), String.valueOf(from.longitude), getResources().getString(R.string.google_maps_server_key));
    }

    @Override
    public void onErrorResponse(VolleyError errorResponse, int requestId) {
        errorResponse.printStackTrace();
    }

    @Override
    public void parseNetworkResponse(NetworkResponse response, int requestId) {

    }

    @Override
    public Map<String, String> getParams(int requestId) {
        return new HashMap<>();
    }
}

package xyz.jcdc.iss;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.afollestad.materialdialogs.MaterialDialog;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.maps.android.SphericalUtil;

import java.text.DecimalFormat;
import java.util.Timer;
import java.util.TimerTask;

import xyz.jcdc.iss.model.Diwata;
import xyz.jcdc.iss.model.ISS;
import xyz.jcdc.iss.model.ISSPosition;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback {

    private Context context;

    private Timer ISSTimer;
    private Timer DiwataTimer;

    private GoogleMap googleMap;
    private Marker marker;
    private Marker marker_diwata;

    private ISSPosition issPosition;

    private BitmapDescriptor issIcon;
    private BitmapDescriptor diwataIcon;

    private LatLng prevLatLng;
    private LatLng prevLatLng_diwata;

    private MaterialDialog materialDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        SupportMapFragment mapFragment =
                (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        materialDialog = new MaterialDialog.Builder(context)
                .title(R.string.iss_dialog_title)
                .content(R.string.iss_dialog_body)
                .progress(true, 0)
                .show();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;
        googleMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(
                this, R.raw.lunar_landscape));
        issIcon = BitmapDescriptorFactory.fromResource(R.mipmap.ic_iss);
        diwataIcon = BitmapDescriptorFactory.fromResource(R.mipmap.ic_diwata);

        getIssPosition();
        getDiwataPosition();
    }

    private void getDiwataPosition() {
        new GetDiwata(new Diwata.DiwataPositionListener() {
            @Override
            public void onStartTracking() {

            }

            @Override
            public void onDiwataPositionReceived(Diwata diwata) {
                setDiwataPosition(diwata);
            }
        }).execute();
    }

    private void setDiwataPosition(final Diwata diwata) {
        if (googleMap != null) {
            if (marker_diwata != null)
                marker_diwata.remove();

            LatLng latLng = new LatLng(diwata.getGeometry().getCoordinates().get(0), diwata.getGeometry().getCoordinates().get(1));

            marker_diwata = googleMap.addMarker(new MarkerOptions()
                    .position(latLng)
                    .title(latLng.latitude + "," + latLng.longitude)
                    .icon(diwataIcon)
                    .anchor(0.5f,0.5f));

            if (prevLatLng_diwata != null) {
                Polyline line = googleMap.addPolyline(new PolylineOptions()
                        .add(prevLatLng_diwata, latLng)
                        .width(5)
                        .color(ContextCompat.getColor(context, R.color.dilawan)));

            //    googleMap.moveCamera(CameraUpdateFactory.newCameraPosition(new CameraPosition.Builder().target(latLng).build()));
            } else {
            //    googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(new CameraPosition.Builder().target(latLng).build()));
            }

            prevLatLng_diwata = latLng;
        }

        DiwataTimer = new Timer();
        DiwataTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                getDiwataPosition();
            }
        }, 1000);
    }

    private void getIssPosition() {
        new GetISS(new ISS.ISSPositionListener() {
            @Override
            public void onStartTracking() {

            }

            @Override
            public void onISSPositionReceived(ISS iss) {
                if (materialDialog.isShowing()) {
                    runOnUiThread(new TimerTask() {
                        @Override
                        public void run() {
                            materialDialog.dismiss();
                        }
                    });
                }

                setISSPosition(iss);
            }
        }).execute();
    }

    private void setISSPosition(final ISS iss) {

        if (googleMap != null) {
            if (marker != null)
                marker.remove();

            issPosition = iss.getIssPosition();

            if (issPosition != null) {
                LatLng latLng = new LatLng(Double.valueOf(issPosition.getLatitude()), Double.valueOf(issPosition.getLongitude()));

                marker = googleMap.addMarker(new MarkerOptions()
                        .position(latLng)
                        .title(latLng.latitude + "," + latLng.longitude)
                        .icon(issIcon)
                        .anchor(0.5f,0.5f));

                if (prevLatLng != null) {
                    Polyline line = googleMap.addPolyline(new PolylineOptions()
                            .add(prevLatLng, latLng)
                            .width(5)
                            .color(ContextCompat.getColor(context, R.color.colorAccent)));

                    //    googleMap.moveCamera(CameraUpdateFactory.newCameraPosition(new CameraPosition.Builder().target(latLng).build()));
                } else {
                    //    googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(new CameraPosition.Builder().target(latLng).build()));
                }

                prevLatLng = latLng;
            }
        }

        ISSTimer = new Timer();
        ISSTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                getIssPosition();
            }
        }, 1000);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


}

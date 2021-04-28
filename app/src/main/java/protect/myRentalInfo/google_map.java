package protect.myRentalInfo;

import android.annotation.SuppressLint;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.LocationManager;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public class google_map extends FragmentActivity implements OnMapReadyCallback {

    GoogleMap map;
    SupportMapFragment mapFragment;
    SearchView searchView;
    String myAddress;
    private ClipboardManager myClipboardCopy;
    private ClipData myClip;
    private TextView backToHomepage;
    Button mybuttonShareLocation,copyData;

    public void shareToOtherApplication( double lat, double Lng){

        Uri gmmIntentUri = Uri.parse("geo:"+lat+","+Lng);
        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
        mapIntent.setPackage("com.google.android.apps.maps");
        startActivity(mapIntent);
    }
    public void copyAddress(EditText myview){
        String text = myview.getText().toString();
        myClip = ClipData.newPlainText("text", text);
        myClipboardCopy.setPrimaryClip(myClip);
        Toast.makeText(getApplicationContext(), "Text Copied", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_google_map);

        final EditText myview = findViewById(R.id.addressInfo);
        searchView = findViewById(R.id.sv_location);
        mybuttonShareLocation = findViewById(R.id.shareLocation);
        copyData = findViewById(R.id.copyData);
        myClipboardCopy = (ClipboardManager)getSystemService(CLIPBOARD_SERVICE);
        backToHomepage = findViewById(R.id.backTohomepage);
        Bundle bundle = getIntent().getExtras();




        if(bundle.getString("addressInfo")!=null){
             myAddress = bundle.getString("addressInfo");
            myview.setText(myAddress);

            copyData.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                   copyAddress(myview);
                }
            });

        }else {
            myview.setText("No info");
        }
        backToHomepage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(),PropertiesListActivity.class);
                startActivity(i);
            }
        });


        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.google_maps);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                String location = searchView.getQuery().toString();
                searchView.setQuery(myAddress,false);
                List<Address> addressList = null;

                if(location != null || !location.equals("")){
                    Geocoder geocoder = new Geocoder(google_map.this);
                    try{
                        addressList = geocoder.getFromLocationName(location,1);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    if(addressList != null && addressList.size() > 0) {
                        final Address address = addressList.get(0);
                        LatLng latLng = new LatLng(address.getLatitude(),address.getLongitude());
                        map.addMarker(new MarkerOptions().position(latLng).title(location));
                        map.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng,10));

                        mybuttonShareLocation.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                shareToOtherApplication(address.getLatitude(), address.getLongitude());

                            }
                        });
                    }

                }
                return false;
            }



            @Override
            public boolean onQueryTextChange(String s) {
                return false;
            }
        });
        mapFragment.getMapAsync(this);

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.simple_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        int id = item.getItemId();

        if(id == android.R.id.home)
        {
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }

    }





}

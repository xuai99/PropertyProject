package protect.myRentalInfo;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Locale;

public class myPropertyOverviewActivity extends AppCompatActivity
{
    private static final String TAG = "RentalCalc";

    private DBHelper _db;
    private PropertyInformation _propertyInformation;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.myproperty_overview_layout);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null)
        {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        _db = new DBHelper(this);





    }

    @Override
    public void onResume()
    {
        super.onResume();

        final Bundle b = getIntent().getExtras();

        if(b == null || b.containsKey("id") == false)
        {
            Toast.makeText(this, "No property found", Toast.LENGTH_LONG).show();
            finish();
            return;
        }

        long propertyId = b.getLong("id");
        _propertyInformation = _db.getProperty(propertyId);

        if(_propertyInformation == null)
        {
            Toast.makeText(this, "No property found", Toast.LENGTH_LONG).show();
            finish();
            return;
        }


        final TextView nickname = (TextView)findViewById(R.id.nickname);
        nickname.setText(_propertyInformation.nickname);

        final TextView street = (TextView)findViewById(R.id.street);
        street.setText("Street: "+_propertyInformation.addressStreet);
        final TextView price = (TextView)findViewById(R.id.purchasePrice);
        price.setText("Housing Price RM: "+_propertyInformation.purchasePrice);
        final TextView grossRent = (TextView)findViewById(R.id.HousingGrossRent);
        grossRent.setText("Monthly rental RM: "+_propertyInformation.grossRent);
        final TextView housingft = (TextView)findViewById(R.id.housingSF);
        housingft.setText("Square feet: "+_propertyInformation.propertySqft+" ft");
        final TextView buildYear = (TextView)findViewById(R.id.buildYear);
        buildYear.setText("Build Year: "+_propertyInformation.propertyYear);
        final TextView sBedroom  = findViewById(R.id.bedRoomS);
        sBedroom.setText("Number of Bedrooms: "+_propertyInformation.propertyBeds);
        final TextView sBathroom = findViewById(R.id.bathroomS);
        sBathroom.setText("Number of Bathrooms: "+_propertyInformation.propertyBaths);

        final TextView stateZip = (TextView)findViewById(R.id.stateZip);
        stateZip.setText(String.format(Locale.US, "%s%s%s%s%s","State: "+
                _propertyInformation.addressCity,
                (_propertyInformation.addressCity .isEmpty() == false) && (_propertyInformation.addressState.isEmpty() == false ||  _propertyInformation.addressZip.isEmpty() == false) ? ", " : "",
                _propertyInformation.addressState,
                (_propertyInformation.addressState .isEmpty() == false) && (_propertyInformation.addressZip.isEmpty() == false) ? " " : "",
                _propertyInformation.addressZip));



        final Bundle argBundle = new Bundle();
        argBundle.putLong("id", _propertyInformation.id);

        final View propertyView = findViewById(R.id.propertyView);
        propertyView.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent i = new Intent(getApplicationContext(), PropertyViewActivity.class);
                i.putExtras(argBundle);
                startActivity(i);
            }
        });

        final View propertyWorksheet = findViewById(R.id.propertyWorksheet);
        propertyWorksheet.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent i = new Intent(getApplicationContext(), PropertyWorksheetActivity.class);
                i.putExtras(argBundle);
                startActivity(i);
            }
        });

        final View propertyPictures = findViewById(R.id.propertyPictures);
        propertyPictures.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent i = new Intent(getApplicationContext(), myPropertyPicturesActivity.class);
                i.putExtras(argBundle);
                startActivity(i);
            }
        });

        final View propertyNotes = findViewById(R.id.propertyNotes);
        propertyNotes.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent i = new Intent(getApplicationContext(), PropertyNotesActivity.class);
                i.putExtras(argBundle);
                startActivity(i);
            }
        });



        final View propertyProjections = findViewById(R.id.propertyProjections);
        propertyProjections.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent i = new Intent(getApplicationContext(), myPropertyProjectionsActivity.class);
                i.putExtras(argBundle);
                startActivity(i);
            }
        });
        final  View googleMapInfo = findViewById(R.id.GoogleMaps);
        googleMapInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), google_map.class);
                i.putExtra("addressInfo", _propertyInformation.addressStreet+" "+ _propertyInformation.addressState+" "+ _propertyInformation.addressCity+" "+
                        _propertyInformation.addressZip);
                startActivity(i);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.properties_overview_menu, menu);
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

        if(id == R.id.action_delete)
        {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Delete/Remove Property ");
            builder.setMessage("Do you wish to delete this property?");
            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener()
            {
                @Override
                public void onClick(DialogInterface dialog, int which)
                {
                    Log.e(TAG, "Deleting property: " + _propertyInformation.id);

                    _db.deleteProperty(_propertyInformation.id);

                    finish();
                    dialog.dismiss();
                }
            });
            builder.setNegativeButton("No", new DialogInterface.OnClickListener()
            {
                @Override
                public void onClick(DialogInterface dialog, int which)
                {
                    dialog.dismiss();
                }
            });
            AlertDialog dialog = builder.create();
            dialog.show();

            return true;
        }


        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy()
    {
        _db.close();
        super.onDestroy();
    }
}

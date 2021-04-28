package protect.myRentalInfo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.common.collect.ImmutableList;

import java.util.List;

public class PropertyViewActivity extends AppCompatActivity
{
    private static final String TAG = "RentalCalc";
    private DBHelper _db;

    private EditText _nicknameField;
    private EditText _streetField;
    private EditText _cityField;
    private EditText _stateField;
    private EditText _zipField;
    private Spinner _typeSpinner;
    private Spinner _bedsSpinner;
    private Spinner _bathsSpinner;
    private EditText _sqftField;
    private EditText _lotField;
    private EditText _yearField;
    private Spinner _parkingSpinner;
    private EditText _zoningField;
    private EditText _mlsField;

    private PropertyInformation _existingPropertyInformation;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.property_add_layout);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null)
        {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        _db = new DBHelper(this);


        _bathsSpinner = (Spinner)findViewById(R.id.baths);
        _sqftField = (EditText)findViewById(R.id.sqft);
        _lotField = (EditText)findViewById(R.id.lot);

        //_mlsField = (EditText)findViewById(R.id.mls);
        _nicknameField = (EditText)findViewById(R.id.nickname);
        _streetField = (EditText)findViewById(R.id.street);
        _cityField = (EditText)findViewById(R.id.city);
        _stateField = (EditText)findViewById(R.id.state);
        _yearField = (EditText)findViewById(R.id.year);
        _parkingSpinner = (Spinner)findViewById(R.id.parking);
        _zoningField = (EditText)findViewById(R.id.zoning);
        _zipField = (EditText)findViewById(R.id.zip);
        _typeSpinner = (Spinner)findViewById(R.id.type);
        _bedsSpinner = (Spinner)findViewById(R.id.beds);

        final Bundle b = getIntent().getExtras();
        _existingPropertyInformation = (b != null && b.containsKey("id")) ? _db.getProperty(b.getLong("id")) : null;

        final List<String> typeValues = ImmutableList.of(
                getString(myPropertyTypeInformation.BLANK.stringId),
                getString(myPropertyTypeInformation.COMMERCIAL.stringId),
                getString(myPropertyTypeInformation.CONDO.stringId),
                getString(myPropertyTypeInformation.HOUSE.stringId),
                getString(myPropertyTypeInformation.LAND.stringId),
                getString(myPropertyTypeInformation.MULTI_FAMILY.stringId),
                getString(myPropertyTypeInformation.MANUFACTURED.stringId),
                getString(myPropertyTypeInformation.OTHER.stringId)
        );
        final ArrayAdapter<String> typeValueAdapter = new ArrayAdapter<>(this, R.layout.spinner_textview, typeValues);
        _typeSpinner.setAdapter(typeValueAdapter);

        final ImmutableList.Builder<String> bedValueBuilder = ImmutableList.builder();
        for(int bed = 1; bed <= 14; bed++)
        {
            bedValueBuilder.add(Integer.toString(bed));
        }
        bedValueBuilder.add("15+");
        final List<String> bedValues = bedValueBuilder.build();
        ArrayAdapter<String> bedValueAdapter = new ArrayAdapter<>(this, R.layout.spinner_textview, bedValues);
        _bedsSpinner.setAdapter(bedValueAdapter);

        ImmutableList.Builder<String> bathValueBuilder = ImmutableList.builder();
        for(int bath = 1; bath <= 9; bath++)
        {
            bathValueBuilder.add(Integer.toString(bath));
            bathValueBuilder.add(Integer.toString(bath) + ".5");
        }
        bathValueBuilder.add("10+");
        final List<String> bathValues = bathValueBuilder.build();
        ArrayAdapter<String> bathValueAdapter = new ArrayAdapter<>(this, R.layout.spinner_textview, bathValues);
        _bathsSpinner.setAdapter(bathValueAdapter);

        final List<String> parkingValues = ImmutableList.of(
                getString(myParkingInfomationType.BLANK.stringId),
                getString(myParkingInfomationType.CAR_PORT.stringId),
                getString(myParkingInfomationType.GARAGE.stringId),
                getString(myParkingInfomationType.OFF_STREET.stringId),
                getString(myParkingInfomationType.ON_STREET.stringId),
                getString(myParkingInfomationType.NONE.stringId),
                getString(myParkingInfomationType.PRIVATE_LOT.stringId)
        );
        ArrayAdapter<String> parkingValueAdapter = new ArrayAdapter<>(this, R.layout.spinner_textview, parkingValues);
        _parkingSpinner.setAdapter(parkingValueAdapter);

        // Pre-populate the values if we have a property to display
        if(_existingPropertyInformation != null)
        {
            _nicknameField.setText(_existingPropertyInformation.nickname);
            _streetField.setText(_existingPropertyInformation.addressStreet);
            _cityField.setText(_existingPropertyInformation.addressCity);
            _stateField.setText(_existingPropertyInformation.addressState);
            _zipField.setText(_existingPropertyInformation.addressZip);
            _typeSpinner.setSelection(_existingPropertyInformation.propertyType);
            int bedIndex = bedValues.indexOf(_existingPropertyInformation.propertyBeds);
            _bedsSpinner.setSelection(bedIndex >= 0 ? bedIndex : 0);
            int bathIndex = bathValues.indexOf(_existingPropertyInformation.propertyBaths);
            _bathsSpinner.setSelection(bathIndex >= 0 ? bathIndex : 0);
            if(_existingPropertyInformation.propertySqft > 0)
            {
                _sqftField.setText(Integer.toString(_existingPropertyInformation.propertySqft));
            }
            if(_existingPropertyInformation.propertyLot > 0)
            {
                _lotField.setText(Integer.toString(_existingPropertyInformation.propertyLot));
            }
            if(_existingPropertyInformation.propertyYear > 0)
            {
                _yearField.setText(Integer.toString(_existingPropertyInformation.propertyYear));
            }
            _parkingSpinner.setSelection(_existingPropertyInformation.propertyParking);
            _zoningField.setText(_existingPropertyInformation.propertyZoning);
            //_mlsField.setText(_existingPropertyInformation.propertyMls);
        }
    }

    private void doSave()
    {
        final String nickname = _nicknameField.getText().toString();
        if(nickname.isEmpty())
        {
            Toast.makeText(PropertyViewActivity.this, "Nickname missing but required", Toast.LENGTH_LONG).show();
            return;
        }

        final String street = _streetField.getText().toString();
        final String city = _cityField.getText().toString();
        final String state = _stateField.getText().toString();
        final String zip = _zipField.getText().toString();

        final myPropertyTypeInformation proopertyType = myPropertyTypeInformation.fromString(PropertyViewActivity.this, (String)_typeSpinner.getSelectedItem());
        final int propertyTypePosition = proopertyType.ordinal();

        final String beds = (String)_bedsSpinner.getSelectedItem();
        final String baths = (String)_bathsSpinner.getSelectedItem();
        final String sqftStr = _sqftField.getText().toString();
        int sqft = 0;
        if(sqftStr.isEmpty() == false)
        {
            try
            {
                sqft = Integer.parseInt(sqftStr);
            }
            catch(NumberFormatException e)
            {
                Toast.makeText(PropertyViewActivity.this, "Square footage not an number: " + sqftStr, Toast.LENGTH_LONG).show();
                return;
            }
        }

        final String lotStr = _lotField.getText().toString();
        int lot = 0;
        if(lotStr.isEmpty() == false)
        {
            try
            {
                lot = Integer.parseInt(lotStr);
            }
            catch(NumberFormatException e)
            {
                Toast.makeText(PropertyViewActivity.this, "Lot size not an number: " + lotStr, Toast.LENGTH_LONG).show();
                return;
            }
        }

        final String yearStr = _yearField.getText().toString();
        int year = 0;
        if(yearStr.isEmpty() == false)
        {
            try
            {
                year = Integer.parseInt(yearStr);
            }
            catch(NumberFormatException e)
            {
                Toast.makeText(PropertyViewActivity.this, "Year not an number: " + yearStr, Toast.LENGTH_LONG).show();
                return;
            }
        }

        final myParkingInfomationType myParkingInfomationType = protect.myRentalInfo.myParkingInfomationType.fromString(PropertyViewActivity.this, (String)_parkingSpinner.getSelectedItem());
        final int parkingPosition = myParkingInfomationType.ordinal();

        final String zoning = _zoningField.getText().toString();
//        final String mls = _mlsField.getText().toString();

        final long id = (_existingPropertyInformation != null) ? _existingPropertyInformation.id : 0;

        PropertyInformation newPropertyInformation = (_existingPropertyInformation != null) ? new PropertyInformation(_existingPropertyInformation) : new PropertyInformation();
        newPropertyInformation.id = id;
        newPropertyInformation.nickname = nickname;
        newPropertyInformation.addressStreet = street;
        newPropertyInformation.addressCity = city;
        newPropertyInformation.addressState = state;
        newPropertyInformation.addressZip = zip;
        newPropertyInformation.propertyType = propertyTypePosition;
        newPropertyInformation.propertyBeds = beds;
        newPropertyInformation.propertyBaths = baths;
        newPropertyInformation.propertySqft = sqft;
        newPropertyInformation.propertyLot = lot;
        newPropertyInformation.propertyYear = year;
        newPropertyInformation.propertyParking = parkingPosition;
        newPropertyInformation.propertyZoning = zoning;
        //newPropertyInformation.propertyMls = mls;

        if(_existingPropertyInformation == null)
        {
            Log.i(TAG, "Adding property");
            long newId = _db.insertProperty(newPropertyInformation);

            // Load the overview activity so that the user can start working
            // with the new property.
            Intent i = new Intent(this, myPropertyOverviewActivity.class);
            final Bundle b = new Bundle();
            b.putLong("id", newId);
            i.putExtras(b);
            startActivity(i);
        }
        else
        {
            Log.i(TAG, "Updating property " + newPropertyInformation.id);
            _db.updateProperty(newPropertyInformation);
        }

        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.save_menu, menu);
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

        if(id == R.id.action_save)
        {
            doSave();
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

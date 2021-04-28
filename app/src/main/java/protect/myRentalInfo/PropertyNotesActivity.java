package protect.myRentalInfo;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;


public class PropertyNotesActivity extends AppCompatActivity
{
    private static final String TAG = "RentalCalc";

    private DBHelper _db;
    private PropertyInformation _propertyInformation;
    private EditText _notes;
    private FloatingActionButton mySaveButtons;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.property_notes_layout);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null)
        {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        mySaveButtons = findViewById(R.id.mySaveFloatingButton);
        _db = new DBHelper(this);

        final Bundle b = getIntent().getExtras();

        if (b == null || b.containsKey("id") == false)
        {
            Toast.makeText(this, "No property found", Toast.LENGTH_LONG).show();
            finish();
            return;
        }

        final long propertyId = b.getLong("id");
        _propertyInformation = _db.getProperty(propertyId);

        if (_propertyInformation == null)
        {
            Toast.makeText(this, "No property found", Toast.LENGTH_LONG).show();
            finish();
            return;
        }

        _notes = (EditText)findViewById(R.id.notes);
        _notes.setText(_propertyInformation.notes);
        mySaveButtons.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                doSave();
            }
        });
    }

    private void doSave()
    {
        PropertyInformation updatedPropertyInformation = new PropertyInformation(_propertyInformation);
        updatedPropertyInformation.notes = _notes.getText().toString();

        Log.i(TAG, "Updating notes for property " + updatedPropertyInformation.id);
        _db.updateProperty(updatedPropertyInformation);

        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.note_menu, menu);
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
    protected void onDestroy()
    {
        _db.close();
        super.onDestroy();
    }
}

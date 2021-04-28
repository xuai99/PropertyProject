package protect.myRentalInfo;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;


public class PropertiesListActivity extends AppCompatActivity
{
    private static final String TAG = "RentalCalc";
    private FloatingActionButton myActionButtons;

    private DBHelper _db;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        _db = new DBHelper(this);
        myActionButtons = findViewById(R.id.myAddFloatingButton);


        SharedPreferences prefs = getSharedPreferences("protect.rentalcalc", MODE_PRIVATE);
        if (prefs.getBoolean("firstrun", true)) {
            startIntro();
            prefs.edit().putBoolean("firstrun", false).commit();
        }
        myActionButtons.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), PropertyViewActivity.class);
                startActivity(i);
            }
        });
    }

    @Override
    public void onResume()
    {
        super.onResume();

        final ListView propertyList = (ListView) findViewById(R.id.list);
        final TextView helpText = (TextView) findViewById(R.id.helpText);

        if (_db.getPropertyCount() > 0)
        {
            propertyList.setVisibility(View.VISIBLE);
            helpText.setVisibility(View.GONE);
        }
        else
        {
            propertyList.setVisibility(View.GONE);
            helpText.setVisibility(View.VISIBLE);
        }

        final Cursor properties = _db.getProperties();
        final myPropertyCursorAdapter adapter = new myPropertyCursorAdapter(this, properties);
        propertyList.setAdapter(adapter);

        propertyList.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                Cursor selected = (Cursor)parent.getItemAtPosition(position);
                if(selected == null)
                {
                    Log.w(TAG, "Clicked transaction at position " + position + " is null");
                    return;
                }

                PropertyInformation propertyInformation = PropertyInformation.toProperty(selected);

                Intent i = new Intent(view.getContext(), myPropertyOverviewActivity.class);
                final Bundle b = new Bundle();
                b.putLong("id", propertyInformation.id);
                i.putExtras(b);
                startActivity(i);
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.properties_list_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        int id = item.getItemId();


        if(id == R.id.action_intro)
        {
            startIntro();
            return true;
        }
        if(id == R.id.Graph)
        {
            Intent a = new Intent(getApplicationContext(), graph.class);
            startActivity(a);
            return true;
        }



        return super.onOptionsItemSelected(item);
    }



    private void startIntro()
    {
        Intent intent = new Intent(this, IntroActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onDestroy()
    {
        _db.close();
        super.onDestroy();
    }


}

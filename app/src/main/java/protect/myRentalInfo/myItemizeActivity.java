package protect.myRentalInfo;

import android.app.Activity;
import android.content.Intent;
import android.database.DataSetObserver;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

public class myItemizeActivity extends AppCompatActivity
{
    private myItemizationAdapter _listAdapter;
    private List<myItemization> _items;
    private void updateTotal()
    {
        TextView totalView = (TextView)findViewById(R.id.totalValue);
        int total = 0;

        for(int index = 0; index < _listAdapter.getCount(); index++)
        {
            myItemization item = _listAdapter.getItem(index);
            if(item != null && item.value != null)
            {
                total += item.value;
            }
        }

        totalView.setText(String.format(Locale.US, "%d", total));
    }
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.itemize_layout);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null)
        {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        final Bundle b = getIntent().getExtras();

        if (b == null || b.containsKey("title") == false || b.containsKey("description") == false
                || b.containsKey("items") == false)
        {
            Toast.makeText(this, "Incomplete itemization request", Toast.LENGTH_LONG).show();
            setResult(Activity.RESULT_CANCELED);
            finish();
            return;
        }

        final int titleId = b.getInt("title");
        setTitle(titleId);

        final int definitionId = b.getInt("description");
        TextView definitionView = (TextView)findViewById(R.id.description);
        definitionView.setText(definitionId);

        final HashMap<String, Integer> originalItems = (HashMap<String, Integer>)b.getSerializable("items");
        final ListView list = (ListView) findViewById(R.id.list);

        _items = new LinkedList<>();

        if(originalItems != null)
        {
            for(HashMap.Entry<String, Integer> entry : originalItems.entrySet())
            {
                _items.add(new myItemization(entry.getKey(), entry.getValue()));
            }
        }

        _listAdapter = new myItemizationAdapter(this, _items);
        list.setAdapter(_listAdapter);

        updateTotal();

        _listAdapter.registerDataSetObserver(new DataSetObserver()
        {
            @Override
            public void onChanged()
            {
                updateTotal();
            }
        });

        _listAdapter.setOnValueChangedListener(new myItemizationAdapter.OnValueChangedListener()
        {
            @Override
            public void onValueChanged()
            {
                updateTotal();
            }
        });
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.itemize_menu, menu);
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

        if(id == R.id.action_add)
        {
            _items.add(new myItemization("", 0));
            _listAdapter.notifyDataSetChanged();
            return true;
        }

        if(id == R.id.action_done)
        {
            HashMap<String, Integer> items = new HashMap<>();

            for(int index = 0; index < _items.size(); index++)
            {
                myItemization data = _items.get(index);
                if(data != null && data.name.isEmpty() == false && data.value > 0)
                {
                    items.put(data.name, data.value);
                }
            }

            Intent i = new Intent();
            Bundle bundle = new Bundle();
            bundle.putSerializable("items", items);
            i.putExtras(bundle);
            setResult(RESULT_OK, i);
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}

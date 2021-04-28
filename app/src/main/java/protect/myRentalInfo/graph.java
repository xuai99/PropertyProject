package protect.myRentalInfo;

import android.database.Cursor;
import android.graphics.Color;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;

import java.util.ArrayList;
import java.util.List;

public class graph extends AppCompatActivity {
     TextView myView;
     DBHelper myhelper;
     float value=0;
     private PieDataSet pieDataSet;
    final int [] colorArray =new int[]{Color.RED,Color.BLUE,Color.GRAY,Color.DKGRAY,Color.rgb(4,99,7),Color.MAGENTA,Color.rgb(14,89,150),Color.rgb(14,44,69)};
    private PieChart myPiechart;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graph);
        myView = findViewById(R.id.viewTotalRentIncome);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null)
        {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }


      myhelper = new DBHelper(this);
      myPiechart = findViewById(R.id.myPieChartV);

      Cursor cursor = myhelper.getProperties();
      Bundle extra = getIntent().getExtras();

        List<PieEntry> dataValue= new ArrayList<>();
        while (cursor.moveToNext()){
            String name = cursor.getString(1);
            Float income = Float.parseFloat(cursor.getString(25));
            Float vacancy = Float.parseFloat(cursor.getString(29));
            Float total = income*12;
            Float grossYearlyRental = total-(total*(vacancy/100));
            value = value+grossYearlyRental;
            Toast.makeText(getApplicationContext(),name+" "+vacancy,Toast.LENGTH_SHORT).show();

            dataValue.add(new PieEntry(grossYearlyRental,name));
        }
        myView.setText("RM: "+String.valueOf(value));
        pieDataSet = new PieDataSet(dataValue,"");
        pieDataSet.setColors(colorArray);
        pieDataSet.setValueTextColor(Color.WHITE);
        //show data into as a pie chart format
        PieData pieData = new PieData(pieDataSet);
        myPiechart.setData(pieData);
        myPiechart.getDescription().setEnabled(false);
        myPiechart.getLegend().setEnabled(true);
        myPiechart.invalidate();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.graph_menu, menu);
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
}

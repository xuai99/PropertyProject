package protect.myRentalInfo;

import android.graphics.Color;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

import java.util.ArrayList;
import java.util.List;

public class cash_graph extends AppCompatActivity {
    private TextView myView,myPI,myLoanBalance;
    private TextView getTotalLoanYearvalue;

    private String myinfo,sTotalPI,sPrinciple,sInterest,year,loanDuration,sLoanBalance,sGetTotalLoanYear,sCheckLoan;
    private Double cashflow =0.0,capitalization;
    private double principle,interest,TotalPIValue,checkLoan;
    private PropertyInformation propertyInformation;
    private PieDataSet pieDataSet;
    final int [] colorArray =new int[]{Color.RED,Color.BLUE};
    private PieChart myPiechart;
    private Double pInterest,pPrinciple;
    private int LoanYearsS,getLoanBalance,getLoanYearsTotal;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cash_graph);

        myView = findViewById(R.id.myInformationValue);
        myPiechart = findViewById(R.id.myPieChartInterest);
        myPI = findViewById(R.id.myYearloan);
        myLoanBalance = findViewById(R.id.myLoanBalanace);
        getTotalLoanYearvalue = findViewById(R.id.myTotalLoanYears);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null)
        {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        Bundle bundle = getIntent().getExtras();

        if(bundle.getString("mycashFlow")!=null){
            myinfo = bundle.getString("mycashFlow");
            sTotalPI = bundle.getString("totalPI");
            sPrinciple = bundle.getString("principle");
            sInterest = bundle.getString("Interest");
            year = bundle.getString("year");
            loanDuration = bundle.getString("loanYears");
            sLoanBalance = bundle.getString("loanBalance");
            sGetTotalLoanYear = bundle.getString("TotalLoanyear");
            sCheckLoan = bundle.getString("checkLoan");

        }
        int myYear = Integer.parseInt(year);
        cashflow = Double.parseDouble(myinfo);
        interest = Double.parseDouble(sInterest);
        principle = Double.parseDouble(sPrinciple);
        TotalPIValue = Double.parseDouble(sTotalPI);
        LoanYearsS = Integer.parseInt(loanDuration);
        getLoanYearsTotal = Integer.parseInt(sGetTotalLoanYear);
        getLoanBalance = Integer.parseInt(sLoanBalance);
        checkLoan = Double.parseDouble(sCheckLoan);
        pInterest = interest*100/TotalPIValue;
        pPrinciple = principle*100/TotalPIValue;


        if(principle >checkLoan){
            principle = checkLoan;
            myPI.setText("Principle Payment RM: "+Math.round(principle)+"\n"+"Interest Payment RM: "+ Math.round(interest));
        }else {
            myPI.setText("Principle Payment RM: "+Math.round(principle)+"\n"+"Interest Payment RM: "+ Math.round(interest));
        }

        if(myYear >= LoanYearsS){
            myView.setText("Total Payment (Principle + interest) Rm: "+(Math.round(principle+interest)));
        }else {
            myView.setText("Total Payment (Principle +interest) around Year "+year+" is \n Rm: "+(Math.round(principle+interest)));
        }

        myLoanBalance.setText("Loan Balance Rm: "+getLoanBalance);
        getTotalLoanYearvalue.setText("Total Loan Years at: "+getLoanYearsTotal);
        int iInterest = (int) Math.round(pInterest);
        int iPrinciple =(int) Math.round(pPrinciple);

        myChart(iInterest,iPrinciple);


    }
    public void myChart(int iInterest,int iPrinciple){
        List<PieEntry> dataValue= new ArrayList<>();

        dataValue.add(new PieEntry(iInterest,"Interest"));
        dataValue.add(new PieEntry(iPrinciple,"principle"));

        pieDataSet = new PieDataSet(dataValue," value in %");
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

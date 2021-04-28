package protect.myRentalInfo;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.graphics.pdf.PdfDocument;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.common.collect.ImmutableMap;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class myPropertyProjectionsActivity extends AppCompatActivity {
    private static final String TAG = "RentalCalc";


    private TextView _rentValue;
    private TextView _vancancyValue;
    private TextView _operatingIncomeValue;
    private TextView _operatingExpensesValue;
    private TextView _netOperatingIncomeValue;
    private TextView _mortgageValue;
    private TextView _cashFlowValue;
    private TextView _afterTaxCashFlowValue;
    private TextView _propertyValueValue;
    private TextView _loanBalanceValue;
    private TextView _totalEquityValue;
    private TextView _depreciationValue;
    private TextView _capitalizationRateValue;
    private TextView _cashOnCashValue;
    private TextView _rentToValueValue;
    private TextView _grossRentMultiplierValue;
    private TextView _projectionText;
    private TextView priceValue;
    private TextView financedValue;
    private TextView downPaymentValue;
    private TextView purchaseCostsValue;
    private TextView repairRemodelCostsValue;
    private TextView totalCashNeededValue;
    private TextView pricePerSizeValue;
    private TextView streetNameShow;
    private TextView stateCityZipShow;
    private TextView otherIncomeInfo;
    private TextView monthlyMortgageValue;
    private TextView _mortgageInterestValue;
    private  TextView mortgagePrincipleValue;
    private  TextView totalPrincipleInterestvalue;

    private Double financed,downPayment,pPsquareFeet;
    private int pPurchasePrice,pGrossRent,pOtherIncome,pVacancy,pOperatingIncome,pNetIncome,pOperatingExpenses,pMortgage;
    private int pLoanBalance,pEquity,pCashFlow,pAfterTaxCashFlow,housingft;
    private double getIntrestValue,getPrincipleValue,getTotalPrincipleInterest,getloanBalance,principle=0.0,finance=0.0;

    private int getTotalLoanYear;

    private Double myCashFlow, capitalization;
    private int appreciation, incomeIncrease,year;
    private List<PropertyHousingCalculation> _calculations;
    private Bitmap bmp,scaleBitmap;

    private String StreetName,stateCityZip,pcapitalization,pRentToValue,pGrossRentalMultiplier,pCashOnCash;
    private int LoanYears;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.myproperty_projections_layout);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }


        DBHelper db = new DBHelper(this);

        final Bundle b = getIntent().getExtras();

        if (b == null || b.containsKey("id") == false) {
            Toast.makeText(this, "No propertyInformation found", Toast.LENGTH_LONG).show();
            finish();
            return;
        }

        final long propertyId = b.getLong("id");
        PropertyInformation propertyInformation = db.getProperty(propertyId);

        if (propertyInformation == null) {
            Toast.makeText(this, "No propertyInformation found", Toast.LENGTH_LONG).show();
            finish();
            return;
        }

        _calculations = myRentalCalt.calculateForYears(propertyInformation, 50);


        priceValue = (TextView) findViewById(R.id.priceValue);
        financedValue = (TextView) findViewById(R.id.financedValue);
        downPaymentValue = (TextView) findViewById(R.id.downPaymentValue);
        purchaseCostsValue = (TextView) findViewById(R.id.purchaseCostsValue);
        repairRemodelCostsValue = (TextView) findViewById(R.id.repairRemodelCostsValue);
        totalCashNeededValue = (TextView) findViewById(R.id.totalCashNeededValue);
        pricePerSizeValue = (TextView) findViewById(R.id.pricePerSizeValue);
        otherIncomeInfo = findViewById(R.id.myOtherIncome);
        streetNameShow = findViewById(R.id.streetName);
        stateCityZipShow = findViewById(R.id.stateCityZip);
        _rentValue = (TextView) findViewById(R.id.rentValue);
        _vancancyValue = (TextView) findViewById(R.id.vancancyValue);
        _operatingIncomeValue = (TextView) findViewById(R.id.operatingIncomeValue);
        _operatingExpensesValue = (TextView) findViewById(R.id.operatingExpensesValue);
        _netOperatingIncomeValue = (TextView) findViewById(R.id.netOperatingIncomeValue);
        _mortgageValue = (TextView) findViewById(R.id.mortgageValue);
        monthlyMortgageValue = findViewById(R.id.mortgageValueMonthly);
        _cashFlowValue = (TextView) findViewById(R.id.cashFlowValue);
        _afterTaxCashFlowValue = (TextView) findViewById(R.id.afterTaxCashFlowValue);
        _propertyValueValue = (TextView) findViewById(R.id.propertyValueValue);
        _loanBalanceValue = (TextView) findViewById(R.id.loanBalanceValue);
        _totalEquityValue = (TextView) findViewById(R.id.totalEquityValue);
        //_depreciationValue = (TextView) findViewById(R.id.depreciationValue);

        _mortgageInterestValue = (TextView) findViewById(R.id.mortgageInterestValue);
        mortgagePrincipleValue = findViewById(R.id.principleValue);
        totalPrincipleInterestvalue = findViewById(R.id.TotalPIShow);

        _propertyValueValue = (TextView) findViewById(R.id.propertyValueValue);
        _loanBalanceValue = (TextView) findViewById(R.id.loanBalanceValue);
        _totalEquityValue = (TextView) findViewById(R.id.totalEquityValue);
       // _depreciationValue = (TextView) findViewById(R.id.depreciationValue);
      //  _mortgageInterestValue = (TextView) findViewById(R.id.mortgageInterestValue);
        _capitalizationRateValue = (TextView) findViewById(R.id.capitalizationRateValue);
        _cashOnCashValue = (TextView) findViewById(R.id.cashOnCashValue);
      //  _rentToValueValue = (TextView) findViewById(R.id.rentToValueValue);
        _grossRentMultiplierValue = (TextView) findViewById(R.id.grossRentMultiplierValue);

        _projectionText = (TextView) findViewById(R.id.projectionText);
        appreciation = propertyInformation.appreciation;
        incomeIncrease = propertyInformation.incomeIncrease;

        priceValue.setText(String.format(Locale.US, "%d", propertyInformation.purchasePrice));
        pPurchasePrice = propertyInformation.purchasePrice;

        streetNameShow.setText(propertyInformation.addressStreet);
        StreetName = propertyInformation.addressStreet;
        stateCityZipShow.setText(propertyInformation.addressCity + " " + propertyInformation.addressState + " " + propertyInformation.addressZip);
        stateCityZip = propertyInformation.addressCity+" "+propertyInformation.addressState+" "+propertyInformation.addressZip;
        housingft = propertyInformation.propertySqft;
        getTotalLoanYear = propertyInformation.loanDuration;


        double downPercent;
        if (propertyInformation.useLoan) {
            downPercent = ((double) propertyInformation.downPayment) / 100.0;
        } else {
            downPercent = 1.0;
        }

        double financedPercent = 1.0 - downPercent;
        financed = propertyInformation.purchasePrice * financedPercent;
        finance = financed;
        LoanYears = propertyInformation.loanDuration;
        financedValue.setText(String.format(Locale.US, "%d", Math.round(financed)));

        downPayment = downPercent * (double) propertyInformation.purchasePrice;
        downPaymentValue.setText(String.format(Locale.US, "%d", Math.round(downPayment)));

        double purchaseCost = propertyInformation.purchaseCosts * propertyInformation.purchasePrice / 100.0;
        if (propertyInformation.purchaseCostsItemized.isEmpty() == false) {
            purchaseCost = myRentalCalt.sumMapItems(propertyInformation.purchaseCostsItemized);
        }

        purchaseCostsValue.setText(String.format(Locale.US, "%d", Math.round(purchaseCost)));

        int repairRemodelCosts = propertyInformation.repairRemodelCosts;
        if (propertyInformation.repairRemodelCostsItemized.isEmpty() == false) {
            repairRemodelCosts = myRentalCalt.sumMapItems(propertyInformation.repairRemodelCostsItemized);
        }

        repairRemodelCostsValue.setText(String.format(Locale.US, "%d", repairRemodelCosts));

        double totalCashNeeded = downPayment + purchaseCost + repairRemodelCosts;
        totalCashNeededValue.setText(String.format(Locale.US, "%d", Math.round(totalCashNeeded)));
        // VALUATION
        double pricePerSqft = 0;
        if (propertyInformation.propertySqft > 0) {
            pricePerSqft = (double) propertyInformation.purchasePrice / (double) propertyInformation.propertySqft;
        }
        pPsquareFeet = pricePerSqft;
        pricePerSizeValue.setText(String.format(Locale.US, "%d", Math.round(pricePerSqft)));

        updateDisplay(0);


        SeekBar yearSeeker = (SeekBar) findViewById(R.id.yearSeeker);
        yearSeeker.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int position, boolean fromUser) {
                Log.d(TAG, "Updating projection year to " + position);
                updateDisplay(position);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                // noop
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                // noop
            }
        });

        // Setup help texts
        Map<Integer, DictionaryItem> dictionaryLookups = new ImmutableMap.Builder<Integer, DictionaryItem>()
                .put(R.id.purchasePriceHelp, new DictionaryItem(R.string.purchasePriceHelpTitle, R.string.purchasePriceDefinition))
                .put(R.id.purchaseCostsHelp, new DictionaryItem(R.string.purchaseCostsHelpTitle, R.string.purchaseCostsDefinition))
                .put(R.id.repairRemodelCostsHelp, new DictionaryItem(R.string.repairRemodelHelpTitle, R.string.repairRemodelDefinition))
                .put(R.id.totalCashNeededHelp, new DictionaryItem(R.string.totalCashNeededTitle, R.string.totalCashNeededDefinition, R.string.totalCashNeededFormula))
                .put(R.id.grossRentHelp, new DictionaryItem(R.string.grossRentHelpTitle, R.string.grossRentDefinition))
                .put(R.id.vacancyHelp, new DictionaryItem(R.string.vacancyHelpTitle, R.string.vacancyDefinition, R.string.vacancyFormula))
                .put(R.id.operatingIncomeHelp, new DictionaryItem(R.string.operatingIncomeHelpTitle, R.string.operatingIncomeDefinition, R.string.operatingIncomeFormula))
                .put(R.id.operatingExpensesHelp, new DictionaryItem(R.string.operatingExpensesHelpTitle, R.string.operatingExpensesDefinition))
                .put(R.id.netOperatingIncomeHelp, new DictionaryItem(R.string.netOperatingIncomeHelpTitle, R.string.netOperatingIncomeDefinition, R.string.netOperatingIncomeFormula))
                .put(R.id.cashFlowHelp, new DictionaryItem(R.string.cashFlowHelpTitle, R.string.cashFlowDefinition, R.string.cashFlowFormula))
                .put(R.id.propertyValueHelp, new DictionaryItem(R.string.propertyValueHelpHelpTitle, R.string.propertyValueHelpDefinition))
                .put(R.id.totalEquityHelp, new DictionaryItem(R.string.totalEquityHelpTitle, R.string.totalEquityHelpDefinition, R.string.totalEquityHelpFormula))
               // .put(R.id.depreciationHelp, new DictionaryItem(R.string.depreciationHelpTitle, R.string.depreciationHelpDefinition))
                .put(R.id.mortgageInterestHelp, new DictionaryItem(R.string.mortgageInterestHelpTitle, R.string.mortgageInterestHelpDefinition))
                .put(R.id.capitalizationRateHelp, new DictionaryItem(R.string.capitalizationRateHelpTitle, R.string.capitalizationRateDefinition, R.string.capitalizationRateFormula))
                .put(R.id.cashOnCashHelp, new DictionaryItem(R.string.cashOnCashHelpTitle, R.string.cashOnCashDefinition, R.string.cashOnCashFormula))
               // .put(R.id.rentToValueHelp, new DictionaryItem(R.string.rentToValueHelpTitle, R.string.rentToValueDefinition, R.string.rentToValueFormula))
                .put(R.id.grossRentMultiplierHelp, new DictionaryItem(R.string.grossRentMultiplierHelpTitle, R.string.grossRentMultiplierDefinition, R.string.grossRentMultiplierFormula))
                .build();


        for (final Map.Entry<Integer, DictionaryItem> entry : dictionaryLookups.entrySet()) {
            View view = findViewById(entry.getKey());
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DictionaryItem info = entry.getValue();
                    final Bundle bundle = new Bundle();
                    bundle.putInt("title", info.titleId);
                    bundle.putInt("definition", info.definitionId);
                    if (info.formulaId != null) {
                        bundle.putInt("formula", info.formulaId);
                    }
                    Intent i = new Intent(getApplicationContext(), DictionaryActivity.class);
                    i.putExtras(bundle);
                    startActivity(i);
                }
            });
        }
    }

    private void createPdf() {
        Drawable drawable = ContextCompat.getDrawable(this,R.drawable.ic_analysis_research);
        Bitmap icon = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);

        //scaleBitmap = Bitmap.createScaledBitmap(bmp,100,100,false);
        // create a new document
        PdfDocument document = new PdfDocument();
        // crate a page description
        PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(300, 600, 1).create();
        // start a page
        PdfDocument.Page page = document.startPage(pageInfo);
        Canvas canvas = page.getCanvas();
        Paint paint = new Paint();
        paint.setColor(Color.RED);
        paint.setColor(Color.BLACK);
        //canvas.drawBitmap(icon,40,20,paint);
        canvas.drawText("Quick Analysis report for year " +year, 55, 15, paint);
        canvas.drawText(StreetName, 10, 40, paint);
        canvas.drawText(stateCityZip, 10, 60, paint);
        canvas.drawText(("Property Information "), 20, 100, paint);
        canvas.drawText(("Purchased Price: "), 20, 120, paint);
        canvas.drawText((String.valueOf("Rm "+pPurchasePrice)), 20, 140, paint);
        canvas.drawText(("loan Balance - : "),   20, 160, paint);
        canvas.drawText((String.valueOf("Rm "+Math.round(pLoanBalance))),20, 180, paint);
        canvas.drawText(("property Equity:          "),   20, 200, paint);
        canvas.drawText(String.valueOf("Rm "+pEquity),   20, 220, paint);

        canvas.drawText(("Finances:"), 200, 120, paint);
        canvas.drawText(String.valueOf("Rm: "+financed), 200, 140, paint);
        canvas.drawText(("Down Payment:"), 200, 160, paint);
        canvas.drawText((String.valueOf("RM: "+downPayment)), 200, 180, paint);

        //rental information
        canvas.drawText(("--------------------------------------"), 20, 250, paint);
        canvas.drawText(("Gross rent Yearly:        "), 20, 260, paint);
        canvas.drawText(String.valueOf("Rm: "+Math.round(pGrossRent)), 20, 280, paint);
        canvas.drawText(("Other Income Yearly:        "), 20, 300, paint);
        canvas.drawText(String.valueOf("Rm: "+Math.round(pOtherIncome)), 20, 320, paint);
        canvas.drawText(("operating Income :        "),   20, 340, paint);
        canvas.drawText(("Rm: "+Math.round(pOperatingIncome)),   20, 360, paint);
        canvas.drawText(("operating expenses:     "),   20, 380, paint);
        canvas.drawText(("Rm: "+Math.round(pOperatingExpenses)),   20, 400, paint);

        canvas.drawText(("---------------------------------"), 200, 250, paint);
        canvas.drawText(("Net Income:"),200, 260, paint);
        canvas.drawText(("Rm: "+Math.round(pNetIncome)),   200, 280, paint);
        canvas.drawText(("Mortgage yearly:          "),   200, 300, paint);
        canvas.drawText(("Rm: "+Math.round(pMortgage)),   200, 320, paint);
        canvas.drawText(("Cashflow Yearly:          "),   200, 340, paint);
        canvas.drawText(("Rm: "+Math.round(pCashFlow)),   200, 360, paint);
        canvas.drawText(("After Tax :               "),   200, 380, paint);
        canvas.drawText(("Rm: "+Math.round(pAfterTaxCashFlow)),200, 400, paint);

        //returns
        canvas.drawText(("Return Analysis"),   20, 460, paint);
        canvas.drawText(("Capitalization : "+pcapitalization+" %"),   20, 480, paint);
        canvas.drawText(("Cash on Cash : ")+pCashOnCash+" %",   20, 500, paint);
       // canvas.drawText(("Rent to value : ")+pRentToValue+" %",   20, 520, paint);
        canvas.drawText(("Rent Multiplier : ")+pGrossRentalMultiplier+" %",   20, 520, paint);
        //property feet
        canvas.drawText(("Further Info"), 200, 460, paint);
        canvas.drawText(("Housing (ft):"), 200, 480, paint);
        canvas.drawText((housingft+" ft"), 200, 500, paint);
        canvas.drawText(("Price per (ft):"), 200, 520, paint);
        canvas.drawText(("Rm: "+Math.round(pPsquareFeet)), 200, 540, paint);



        // finish the page
        document.finishPage(page);
// draw text on the graphics object of the page
        // Create Page 2

        // write the document content
        String directory_path = Environment.getExternalStorageDirectory().getPath() + "/mypdf/";
        File file = new File(directory_path);
        if (!file.exists()) {
            file.mkdirs();
        }
        String targetPdf = directory_path + "Quick_investment_Analysis.pdf";
        File filePath = new File(targetPdf);
        try {
            document.writeTo(new FileOutputStream(filePath));
            Toast.makeText(this, "Quick Analysis Report Successfully Generated for Year "+year, Toast.LENGTH_LONG).show();
        } catch (IOException e) {
            Log.e("main", "error " + e.toString());
            Toast.makeText(this, "Error generated report: " + e.toString(), Toast.LENGTH_LONG).show();
        }
        // close the document
        document.close();
    }





    public void updateDisplay(int position)
    {
        // The passed value starts a 0, but we count the years starting at 1
        year = position+1;
        PropertyHousingCalculation calc = _calculations.get(position);

        // OPERATION
        _rentValue.setText(String.format(Locale.US, "%d", (int)Math.round(calc.grossRent)));
        pGrossRent =(int)Math.round(calc.grossRent);
        otherIncomeInfo.setText(String.format(Locale.US, "%d", (int)Math.round(calc.otherIncome)));
        pOtherIncome = (int)Math.round(calc.otherIncome);
        _vancancyValue.setText(String.format(Locale.US, "%d", (int)Math.round(calc.vacancy)));
        pVacancy = (int)Math.round(calc.vacancy);
        _operatingIncomeValue.setText(String.format(Locale.US, "%d", (int)Math.round(calc.operatingIncome)));
        pOperatingIncome = (int)Math.round(calc.operatingIncome);
        _operatingExpensesValue.setText(String.format(Locale.US, "%d", (int)Math.round(calc.totalExpenses)));
        pOperatingExpenses = (int)Math.round(calc.totalExpenses);
        _netOperatingIncomeValue.setText(String.format(Locale.US, "%d", (int)Math.round(calc.netOperatingIncome)));
        pNetIncome = (int)Math.round(calc.netOperatingIncome);
        _mortgageValue.setText(String.format(Locale.US, "%d", (int)Math.round(calc.loanPayments)));
        pMortgage = (int)Math.round(calc.loanPayments);
        monthlyMortgageValue.setText(String.format(Locale.US, "%d", (int)Math.round(calc.loanPayments/12)));

        _cashFlowValue.setText(String.format(Locale.US, "%d", (int)Math.round(calc.cashFlow)));
        pCashFlow = (int)Math.round(calc.cashFlow);
        _afterTaxCashFlowValue.setText(String.format(Locale.US, "%d", (int)Math.round(calc.afterTaxCashFlow)));
        pAfterTaxCashFlow = (int)Math.round(calc.afterTaxCashFlow);

        // EQUITY
        _propertyValueValue.setText(String.format(Locale.US, "%d", (int)Math.round(calc.propertyValue)));
        _loanBalanceValue.setText(String.format(Locale.US, "%d",  (int)Math.round(calc.loanBalance)));
        pLoanBalance =  (int)Math.round(calc.loanBalance);
        _totalEquityValue.setText(String.format(Locale.US, "%d",  (int)Math.round(calc.totalEquity)));
        pEquity= (int)Math.round(calc.totalEquity);

        // TAXES
       // _depreciationValue.setText(String.format(Locale.US, "%d",  (int)Math.round(calc.depreciation)));
        _mortgageInterestValue.setText(String.format(Locale.US, "%d", (int)Math.round(calc.calcInterest)));
        getIntrestValue = calc.calcInterest;
        getPrincipleValue = calc.calcPrinciple;
        getTotalPrincipleInterest=calc.TotalPI;
        if(calc.calcPrinciple > finance){
            calc.calcPrinciple = finance;
            mortgagePrincipleValue.setText(String.format(Locale.US, "%d", ((int)Math.round(calc.calcPrinciple))));
            totalPrincipleInterestvalue.setText(String.format(Locale.US, "%d", (int)Math.round(calc.calcPrinciple+calc.calcInterest)));

        }else {
            mortgagePrincipleValue.setText(String.format(Locale.US, "%d", ((int)Math.round(calc.calcPrinciple))));
            totalPrincipleInterestvalue.setText(String.format(Locale.US, "%d", (int)Math.round(calc.calcPrinciple+calc.calcInterest)));
        }

        // RETURNS
        _capitalizationRateValue.setText(String.format(Locale.US, "%.1f", calc.capitalization));
        pcapitalization = String.format("%.1f", calc.capitalization);
        _cashOnCashValue.setText(String.format(Locale.US, "%.1f", calc.cashOnCash));
        pCashOnCash = String.format(Locale.US, "%.1f", calc.cashOnCash);
      //  _rentToValueValue.setText(String.format(Locale.US, "%.1f", calc.rentToValue));
        pRentToValue = String.format(Locale.US, "%.1f", calc.rentToValue);
        _grossRentMultiplierValue.setText(String.format(Locale.US, "%.1f", calc.grossRentMultiplier));
        pGrossRentalMultiplier = String.format(Locale.US, "%.1f", calc.grossRentMultiplier);
        myCashFlow = calc.afterTaxCashFlow;
        capitalization = calc.capitalization;
        _projectionText.setText(String.format(getString(R.string.projectionText), year));
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
        PropertyHousingCalculation calc = new PropertyHousingCalculation();

        int id = item.getItemId();

        if(id == android.R.id.home)
        {
            finish();
            return true;
        }

        if(id == R.id.GraphInformation)
        {
            Intent i = new Intent(getApplicationContext(), cash_graph.class);
            i.putExtra("mycashFlow",String.valueOf(myCashFlow));
            i.putExtra("totalPI",String.valueOf(getTotalPrincipleInterest));
            i.putExtra("principle",String.valueOf(getPrincipleValue));
            i.putExtra("Interest",String.valueOf(getIntrestValue));
            i.putExtra("year",String.valueOf(year));
            i.putExtra("loanBalance",String.valueOf(pLoanBalance));
            i.putExtra("loanYears",String.valueOf(LoanYears));
            i.putExtra("TotalLoanyear",String.valueOf(getTotalLoanYear));
            i.putExtra("checkLoan",String.valueOf(financed));

            Toast.makeText(getApplicationContext(),getIntrestValue+" "+getPrincipleValue,Toast.LENGTH_SHORT).show();
            startActivity(i);
        }
        if(id == R.id.GenerateGraph){
            createPdf();
        }
        return super.onOptionsItemSelected(item);


    }
}

package protect.myRentalInfo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;


public class PropertyWorksheetActivity extends AppCompatActivity
{
    private static final String TAG = "RentalCalc";

    private static final int FOR_RESULT_ITEMIZE = 1;
    private Map<String, Integer> _currentForResultItemize;

    private DBHelper _db;

    private EditText _price;
    private EditText _afterRepairsValue;
    private ToggleButton _financing;
    private EditText _downPayment;
    private EditText _interestRate;
    private EditText _loanDuration;
    private EditText _purchaseCost;
    private View _purchaseCostPercentageLayout;
    private TextView _purchaseCostItemize;
    private View _purchaseCostItemizeCurrency;
    private EditText _repairCost;
    private TextView _repairCostItemize;
    private EditText _rent;
    private EditText _otherIncome;
    private EditText _totalExpenses;
    private View _totalExpensesPercentLayout;
    private View _totalExpensesItemizeLayout;
    private TextView _totalExpensesItemizeCurrency;
    private TextView _totalExpensesItemize;
    private EditText _vacancy;
    private EditText _appreciation;
    private EditText _incomeIncrease;
    private EditText _expensesIncrease;
    private EditText _sellingCosts;
    private EditText _landValue;
    private EditText _incomeTaxRate;

    private PropertyInformation _propertyInformation;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.myproperty_financial_layout);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null)
        {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        _db = new DBHelper(this);

        final Bundle b = getIntent().getExtras();

        if(b == null || b.containsKey("id") == false)
        {
            Toast.makeText(this, "No property found", Toast.LENGTH_LONG).show();
            finish();
            return;
        }

        final long propertyId = b.getLong("id");
        _propertyInformation = _db.getProperty(propertyId);

        if(_propertyInformation == null)
        {
            Toast.makeText(this, "No property found", Toast.LENGTH_LONG).show();
            finish();
            return;
        }

        _price = (EditText)findViewById(R.id.price);
        _afterRepairsValue = (EditText)findViewById(R.id.afterRepairsValue);
        _financing = (ToggleButton)findViewById(R.id.financing);
        _downPayment = (EditText)findViewById(R.id.downPayment);
        _interestRate = (EditText)findViewById(R.id.interestRate);
        _loanDuration = (EditText)findViewById(R.id.loanDuration);
        _purchaseCost = (EditText)findViewById(R.id.purchaseCost);
        _purchaseCostItemize = (TextView)findViewById(R.id.purchaseCostItemize);
        _purchaseCostPercentageLayout = findViewById(R.id.purchaseCostPercentageLayout);
        _purchaseCostItemizeCurrency = findViewById(R.id.purchaseCostItemizeCurrency);
        _repairCost = (EditText)findViewById(R.id.repairCost);
        _repairCostItemize = (TextView)findViewById(R.id.repairCostItemize);
        _rent = (EditText)findViewById(R.id.rent);
        _otherIncome = (EditText)findViewById(R.id.otherIncome);
        _totalExpenses = (EditText)findViewById(R.id.totalExpenses);
        _totalExpensesPercentLayout = findViewById(R.id.totalExpensesPercentLayout);
        _totalExpensesItemizeLayout = findViewById(R.id.totalExpensesItemizeLayout);
        _totalExpensesItemizeCurrency = (TextView)findViewById(R.id.totalExpensesItemizeCurrency);
        _totalExpensesItemize = (TextView)findViewById(R.id.totalExpensesItemize);
        _vacancy = (EditText)findViewById(R.id.vacancy);
        _appreciation = (EditText)findViewById(R.id.appreciation);
        _incomeIncrease = (EditText)findViewById(R.id.incomeIncrease);
        _expensesIncrease = (EditText)findViewById(R.id.expensesIncrease);
        //_sellingCosts = (EditText)findViewById(R.id.sellingCosts);
        //_landValue = (EditText)findViewById(R.id.landValue);
        _incomeTaxRate = (EditText)findViewById(R.id.incomeTaxRate);

        CompoundButton.OnCheckedChangeListener listener = new CompoundButton.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
            {
                List<View> views = ImmutableList.of(
                    findViewById(R.id.loanBorder),
                    findViewById(R.id.loanRow),
                    findViewById(R.id.interestBorder),
                    findViewById(R.id.interestRow),
                    findViewById(R.id.downPaymentBorder),
                    findViewById(R.id.downPaymentRow)
                );

                for(View view : views)
                {
                    view.setVisibility(isChecked ? View.VISIBLE : View.GONE);
                }
            }
        };
        _financing.setOnCheckedChangeListener(listener);

        if(_propertyInformation.purchasePrice > 0)
        {
            _price.setText(String.format(Locale.US, "%d", _propertyInformation.purchasePrice));
        }

        if(_propertyInformation.afterRepairsValue > 0)
        {
            _afterRepairsValue.setText(String.format(Locale.US, "%d", _propertyInformation.afterRepairsValue));
        }

        _financing.setChecked(_propertyInformation.useLoan);
        listener.onCheckedChanged(_financing, _propertyInformation.useLoan);

        if(_propertyInformation.downPayment > 0)
        {
            _downPayment.setText(String.format(Locale.US, "%d", _propertyInformation.downPayment));
        }

        if(_propertyInformation.interestRate > 0)
        {
            _interestRate.setText(String.format(Locale.US, "%.3f", _propertyInformation.interestRate));
        }

        if(_propertyInformation.loanDuration > 0)
        {
            _loanDuration.setText(String.format(Locale.US, "%d", _propertyInformation.loanDuration));
        }

        if(_propertyInformation.purchaseCosts > 0)
        {
            _purchaseCost.setText(String.format(Locale.US, "%d", _propertyInformation.purchaseCosts));
        }

        if(_propertyInformation.repairRemodelCosts > 0)
        {
            _repairCost.setText(String.format(Locale.US, "%d", _propertyInformation.repairRemodelCosts));
        }

        if(_propertyInformation.grossRent > 0)
        {
            _rent.setText(String.format(Locale.US, "%d", _propertyInformation.grossRent));
        }

        if(_propertyInformation.otherIncome > 0)
        {
            _otherIncome.setText(String.format(Locale.US, "%d", _propertyInformation.otherIncome));
        }

        if(_propertyInformation.expenses > 0)
        {
            _totalExpenses.setText(String.format(Locale.US, "%d", _propertyInformation.expenses));
        }

        if(_propertyInformation.vacancy > 0)
        {
            _vacancy.setText(String.format(Locale.US, "%d", _propertyInformation.vacancy));
        }

        if(_propertyInformation.appreciation > 0)
        {
            _appreciation.setText(String.format(Locale.US, "%d", _propertyInformation.appreciation));
        }

        if(_propertyInformation.incomeIncrease > 0)
        {
            _incomeIncrease.setText(String.format(Locale.US, "%d", _propertyInformation.incomeIncrease));
        }

        if(_propertyInformation.expenseIncrease > 0)
        {
            _expensesIncrease.setText(String.format(Locale.US, "%d", _propertyInformation.expenseIncrease));
        }

      //  if(_propertyInformation.sellingCosts > 0)
     //   {
     //       _sellingCosts.setText(String.format(Locale.US, "%d", _propertyInformation.sellingCosts));
    //    }

        if(_propertyInformation.landValue > 0)
        {
            _landValue.setText(String.format(Locale.US, "%d", _propertyInformation.landValue));
        }

        if(_propertyInformation.incomeTaxRate > 0)
        {
            _incomeTaxRate.setText(String.format(Locale.US, "%d", _propertyInformation.incomeTaxRate));
        }

        // Once the price is set, if the after repairs value is not yet populated,
        // fill it in with the price. It is a good first guess.
        _price.setOnFocusChangeListener(new View.OnFocusChangeListener()
        {
            @Override
            public void onFocusChange(View v, boolean hasFocus)
            {
                if(hasFocus == false)
                {
                    String price = _price.getText().toString();
                    if(price.isEmpty() == false && price.equals("0") == false &&
                            _afterRepairsValue.getText().toString().isEmpty())
                    {
                        _afterRepairsValue.setText(price);
                    }
                }
            }
        });

        // Setup help texts
        Map<Integer, DictionaryItem> dictionaryLookups = new ImmutableMap.Builder<Integer, DictionaryItem>()
            .put(R.id.purchasePriceHelp, new DictionaryItem(R.string.purchasePriceHelpTitle, R.string.purchasePriceDefinition))
            .put(R.id.afterRepairsHelp, new DictionaryItem(R.string.afterRepairsValueHelpTitle, R.string.afterRepairsValueDefinition))
            .put(R.id.purchaseCostsHelp, new DictionaryItem(R.string.purchaseCostsHelpTitle, R.string.purchaseCostsDefinition))
            .put(R.id.repairRemodelCostsHelp, new DictionaryItem(R.string.repairRemodelHelpTitle, R.string.repairRemodelDefinition))
            .put(R.id.grossRentHelp, new DictionaryItem(R.string.grossRentHelpTitle, R.string.grossRentDefinition))
            .put(R.id.otherIncomeHelp, new DictionaryItem(R.string.otherIncomeHelpTitle, R.string.otherIncomeDefinition))
            .put(R.id.expensesHelp, new DictionaryItem(R.string.operatingExpensesHelpTitle, R.string.operatingExpensesDefinition))
            .put(R.id.vacancyHelp, new DictionaryItem(R.string.vacancyHelpTitle, R.string.vacancyDefinition, R.string.vacancyFormula))
            .put(R.id.appreciationHelp, new DictionaryItem(R.string.appreciationHelpTitle, R.string.appreciationDefinition))
            .put(R.id.incomeIncreaseHelp, new DictionaryItem(R.string.incomeIncreaseHelpTitle, R.string.incomeIncreaseDefinition))
            .put(R.id.expensesIncreaseHelp, new DictionaryItem(R.string.expensesIncreaseHelpTitle, R.string.expensesIncreaseDefinition))
            //.put(R.id.sellingCostsHelp, new DictionaryItem(R.string.sellingCostsHelpTitle, R.string.sellingCostsDefinition))
           // .put(R.id.landValueHelp, new DictionaryItem(R.string.landValueHelpTitle, R.string.landValueDefinition))
            .build();

        for(final Map.Entry<Integer, DictionaryItem> entry : dictionaryLookups.entrySet())
        {
            View view = findViewById(entry.getKey());
            view.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    DictionaryItem info = entry.getValue();
                    final Bundle bundle = new Bundle();
                    bundle.putInt("title", info.titleId);
                    bundle.putInt("definition", info.definitionId);
                    if(info.formulaId != null)
                    {
                        bundle.putInt("formula", info.formulaId);
                    }
                    Intent i = new Intent(getApplicationContext(), DictionaryActivity.class);
                    i.putExtras(bundle);
                    startActivity(i);
                }
            });
        }

        // Setup itemize activity links
        Map<Integer, ItemizeOptionItem> itemizeOptionLookups = new ImmutableMap.Builder<Integer, ItemizeOptionItem>()
            .put(R.id.purchaseCostsItemize, new ItemizeOptionItem(R.string.purchaseCostsItemizeTitle, R.string.purchaseCostsItemizeHelp, _propertyInformation.purchaseCostsItemized))
            .put(R.id.repairCostsItemize, new ItemizeOptionItem(R.string.repairCostsItemizeTitle, R.string.repairCostsItemizeHelp, _propertyInformation.repairRemodelCostsItemized))
            .put(R.id.expensesItemize, new ItemizeOptionItem(R.string.expensesItemizeTitle, R.string.expensesItemizeHelp, _propertyInformation.expensesItemized))
            .build();

        for(final Map.Entry<Integer, ItemizeOptionItem> entry : itemizeOptionLookups.entrySet())
        {
            View view = findViewById(entry.getKey());
            view.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    ItemizeOptionItem itemizations = entry.getValue();

                    _currentForResultItemize = itemizations.items;

                    Bundle bundle = new Bundle();
                    bundle.putInt("title", itemizations.titleId);
                    bundle.putInt("description", itemizations.descriptionId);
                    bundle.putSerializable("items", itemizations.items);
                    Intent i = new Intent(getApplicationContext(), myItemizeActivity.class);
                    i.putExtras(bundle);
                    startActivityForResult(i, FOR_RESULT_ITEMIZE);
                }
            });
        }
    }

    @Override
    protected void onResume()
    {
        super.onResume();

        _purchaseCostPercentageLayout.setVisibility(_propertyInformation.purchaseCostsItemized.isEmpty() ? View.VISIBLE : View.GONE);
        _purchaseCostItemizeCurrency.setVisibility(_propertyInformation.purchaseCostsItemized.isEmpty() ? View.GONE : View.VISIBLE);
        _purchaseCostItemize.setVisibility(_propertyInformation.purchaseCostsItemized.isEmpty() ? View.GONE : View.VISIBLE);
        int purchaseCostItemized = myRentalCalt.sumMapItems(_propertyInformation.purchaseCostsItemized);
        _purchaseCostItemize.setText(String.format(Locale.US, "%d", purchaseCostItemized));

        _repairCost.setVisibility(_propertyInformation.repairRemodelCostsItemized.isEmpty() ? View.VISIBLE : View.GONE);
        _repairCostItemize.setVisibility(_propertyInformation.repairRemodelCostsItemized.isEmpty() ? View.GONE : View.VISIBLE);
        int repairCostItemized = myRentalCalt.sumMapItems(_propertyInformation.repairRemodelCostsItemized);
        _repairCostItemize.setText(String.format(Locale.US, "%d", repairCostItemized));

        _totalExpensesPercentLayout.setVisibility(_propertyInformation.expensesItemized.isEmpty() ? View.VISIBLE : View.GONE);
        _totalExpensesItemizeCurrency.setVisibility(_propertyInformation.expensesItemized.isEmpty() ? View.GONE : View.VISIBLE);
        _totalExpensesItemizeLayout.setVisibility(_propertyInformation.expensesItemized.isEmpty() ? View.GONE : View.VISIBLE);
        int expensesItemized = myRentalCalt.sumMapItems(_propertyInformation.expensesItemized);
        _totalExpensesItemize.setText(String.format(Locale.US, "%d", expensesItemized));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if (requestCode == FOR_RESULT_ITEMIZE)
        {
            if (resultCode == RESULT_OK)
            {
                // New items were entered. Remove old items and save off new items.
                Bundle extras = data.getExtras();
                HashMap<String, Integer> newItems = (HashMap<String, Integer>)extras.getSerializable("items");

                _currentForResultItemize.clear();
                if(newItems != null)
                {
                    _currentForResultItemize.putAll(newItems);
                }
            }
        }
    }

    private void doSave()
    {
        PropertyInformation updatedPropertyInformation = new PropertyInformation(_propertyInformation);
        updatedPropertyInformation.purchasePrice = extractInt(_price, 0);
        updatedPropertyInformation.afterRepairsValue = extractInt(_afterRepairsValue, 0);
        updatedPropertyInformation.useLoan = _financing.isChecked();
        updatedPropertyInformation.downPayment = extractInt(_downPayment, 0);
        updatedPropertyInformation.interestRate = extractDouble(_interestRate, 0);
        updatedPropertyInformation.loanDuration = extractInt(_loanDuration, 0);
        updatedPropertyInformation.purchaseCosts = extractInt(_purchaseCost, 0);
        updatedPropertyInformation.repairRemodelCosts = extractInt(_repairCost, 0);
        updatedPropertyInformation.grossRent = extractInt(_rent, 0);
        updatedPropertyInformation.otherIncome = extractInt(_otherIncome, 0);
        updatedPropertyInformation.expenses = extractInt(_totalExpenses, 0);
        updatedPropertyInformation.vacancy = extractInt(_vacancy, 0);
        updatedPropertyInformation.appreciation = extractInt(_appreciation, 0);
        updatedPropertyInformation.incomeIncrease = extractInt(_incomeIncrease, 0);
        updatedPropertyInformation.expenseIncrease = extractInt(_expensesIncrease, 0);
//        updatedPropertyInformation.sellingCosts = extractInt(_sellingCosts, 0);
//        updatedPropertyInformation.landValue = extractInt(_landValue, 0);
        updatedPropertyInformation.incomeTaxRate = extractInt(_incomeTaxRate, 0);

        Log.i(TAG, "Updating property " + updatedPropertyInformation.id);
        _db.updateProperty(updatedPropertyInformation);

        finish();
    }

    private int extractInt(EditText view, int defaultValue)
    {
        String string = view.getText().toString();
        if(string.isEmpty() == false)
        {
            try
            {
                return Integer.parseInt(string);
            }
            catch(NumberFormatException e)
            {
                Log.w(TAG, "Failed to parse " + string, e);
            }
        }

        return defaultValue;
    }

    private double extractDouble(EditText view, double defaultValue)
    {
        String string = view.getText().toString();
        if(string.isEmpty() == false)
        {
            try
            {
                return Double.parseDouble(string);
            }
            catch(NumberFormatException e)
            {
                Log.w(TAG, "Failed to parse " + string, e);
            }
        }

        return defaultValue;
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

class ItemizeOptionItem
{
    final Integer titleId;
    final Integer descriptionId;
    final HashMap<String, Integer> items;

    ItemizeOptionItem(int title, int description, HashMap<String, Integer> itemizations)
    {
        titleId = title;
        descriptionId = description;
        items = itemizations;
    }
}
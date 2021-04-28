package protect.myRentalInfo;

import android.database.Cursor;
import android.util.JsonReader;
import android.util.Log;

import com.google.common.collect.ImmutableMap;

import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

class PropertyInformation
{
    long id;
    String nickname;
    String addressStreet;
    String addressCity;
    String addressState;
    String addressZip;
    int propertyType;
    String propertyBeds;
    String propertyBaths;
    int propertySqft;
    int propertyLot;
    int propertyYear;
    int propertyParking;
    String propertyZoning;
    String propertyMls;
    int purchasePrice;
    int afterRepairsValue;
    boolean useLoan;
    int downPayment;
    double interestRate;
    int loanDuration;
    int purchaseCosts; // not itemized, % of rent
    HashMap<String, Integer> purchaseCostsItemized;
    int repairRemodelCosts; // not itemized, $
    HashMap<String, Integer> repairRemodelCostsItemized;
    int grossRent;
    int otherIncome;
    int expenses; // not itemized, % of rent
    HashMap<String, Integer> expensesItemized;
    int vacancy;
    int appreciation;
    int incomeIncrease;
    int expenseIncrease;
    int sellingCosts;
    int landValue;
    int incomeTaxRate;
    String notes;
    LinkedList<File> pictures;

    private static String toBlankIfNull(final String string)
    {
        if(string != null)
        {
            return string;
        }
        else
        {
            return "";
        }
    }

    PropertyInformation()
    {
        // Default all strings to "" instead of null
        for(Field field : PropertyInformation.class.getDeclaredFields())
        {
            if(field.getType() == String.class)
            {
                try
                {
                    field.set(this, "");
                }
                catch (IllegalAccessException e)
                {
                    Log.e("RentalCalc", "Failed to assign field", e);
                }
            }
        }

        // Fill in default values

        propertyBeds = "1";
        propertyBaths = "1";
        useLoan = true;
        downPayment = 20; // 20%
        interestRate = 5; // 5%
        loanDuration = 30; // 30 years
        purchaseCosts = 3; // 3 %
        purchaseCostsItemized = new HashMap<>();
        repairRemodelCosts = 0; // 0 %
        repairRemodelCostsItemized = new HashMap<>();
        grossRent = 0; // $0/month
        otherIncome = 0; // $0/month
        expenses = 30; // 30% of rent
        expensesItemized = new HashMap<>();
        vacancy = 10; // 10% vacancy rate
        appreciation = 3; // 3% appreciation per year
        incomeIncrease = 2; // 2% increase per year
        expenseIncrease = 2;
        sellingCosts = 6; // 6% of sale price
        landValue = 0; // cost of the land
        incomeTaxRate = 25; // 25% tax rate
        pictures = new LinkedList<>();
    }

    PropertyInformation(final PropertyInformation original)
    {
        for(Field field : PropertyInformation.class.getDeclaredFields())
        {
            try
            {
                Object data = field.get(original);
                field.set(this, data);
            }
            catch (IllegalAccessException e)
            {
                Log.e("RentalCal", "Failed to assign field", e);
            }
        }

        // Override the map references to be a new map instance

        purchaseCostsItemized = new HashMap<>(original.purchaseCostsItemized);
        repairRemodelCostsItemized = new HashMap<>(original.repairRemodelCostsItemized);
        expensesItemized = new HashMap<>(original.expensesItemized);
        pictures = new LinkedList<>(original.pictures);
    }

    static PropertyInformation toProperty(Cursor cursor)
    {
        PropertyInformation propertyInformation = new PropertyInformation();
        propertyInformation.id = cursor.getLong(cursor.getColumnIndexOrThrow(DBHelper.PropertyDbIds.ID));
        propertyInformation.nickname = toBlankIfNull(cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.PropertyDbIds.NICKNAME)));
        propertyInformation.addressStreet = toBlankIfNull(cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.PropertyDbIds.ADDRESS_STREET)));
        propertyInformation.addressCity  = toBlankIfNull(cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.PropertyDbIds.ADDRESS_CITY)));
        propertyInformation.addressState = toBlankIfNull(cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.PropertyDbIds.ADDRESS_STATE)));
        propertyInformation.addressZip = toBlankIfNull(cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.PropertyDbIds.ADDRESS_ZIP)));
        propertyInformation.propertyType = cursor.getInt(cursor.getColumnIndexOrThrow(DBHelper.PropertyDbIds.PROPERTY_TYPE));
        propertyInformation.propertyBeds = toBlankIfNull(cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.PropertyDbIds.PROPERTY_BEDS)));
        propertyInformation.propertyBaths = toBlankIfNull(cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.PropertyDbIds.PROPERTY_BATHS)));
        propertyInformation.propertySqft = cursor.getInt(cursor.getColumnIndexOrThrow(DBHelper.PropertyDbIds.PROPERTY_SQUARE_FOOTAGE));
        propertyInformation.propertyLot = cursor.getInt(cursor.getColumnIndexOrThrow(DBHelper.PropertyDbIds.PROPERTY_LOT));
        propertyInformation.propertyYear = cursor.getInt(cursor.getColumnIndexOrThrow(DBHelper.PropertyDbIds.PROPERTY_YEAR));
        propertyInformation.propertyParking = cursor.getInt(cursor.getColumnIndexOrThrow(DBHelper.PropertyDbIds.PROPERTY_PARKING));
        propertyInformation.propertyZoning = toBlankIfNull(cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.PropertyDbIds.PROPERTY_ZONING)));
        propertyInformation.propertyMls = toBlankIfNull(cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.PropertyDbIds.PROPERTY_MLS)));
        propertyInformation.purchasePrice = cursor.getInt(cursor.getColumnIndexOrThrow(DBHelper.PropertyDbIds.PURCHASE_PRICE));
        propertyInformation.afterRepairsValue = cursor.getInt(cursor.getColumnIndexOrThrow(DBHelper.PropertyDbIds.AFTER_REPAIRS_VALUE));
        propertyInformation.useLoan = cursor.getInt(cursor.getColumnIndexOrThrow(DBHelper.PropertyDbIds.USE_LOAN)) > 0;
        propertyInformation.downPayment = cursor.getInt(cursor.getColumnIndexOrThrow(DBHelper.PropertyDbIds.DOWN_PAYMENT));
        propertyInformation.interestRate = cursor.getDouble(cursor.getColumnIndexOrThrow(DBHelper.PropertyDbIds.INTEREST_RATE));
        propertyInformation.loanDuration = cursor.getInt(cursor.getColumnIndexOrThrow(DBHelper.PropertyDbIds.LOAN_DURATION));
        propertyInformation.purchaseCosts = cursor.getInt(cursor.getColumnIndexOrThrow(DBHelper.PropertyDbIds.PURCHASE_COSTS));
        propertyInformation.repairRemodelCosts = cursor.getInt(cursor.getColumnIndexOrThrow(DBHelper.PropertyDbIds.REPAIR_REMODEL_COSTS));
        propertyInformation.grossRent = cursor.getInt(cursor.getColumnIndexOrThrow(DBHelper.PropertyDbIds.GROSS_RENT));
        propertyInformation.otherIncome = cursor.getInt(cursor.getColumnIndexOrThrow(DBHelper.PropertyDbIds.OTHER_INCOME));
        propertyInformation.expenses = cursor.getInt(cursor.getColumnIndexOrThrow(DBHelper.PropertyDbIds.EXPENSES));
        propertyInformation.vacancy = cursor.getInt(cursor.getColumnIndexOrThrow(DBHelper.PropertyDbIds.VACANCY));
        propertyInformation.appreciation = cursor.getInt(cursor.getColumnIndexOrThrow(DBHelper.PropertyDbIds.APPRECIATION));
        propertyInformation.incomeIncrease = cursor.getInt(cursor.getColumnIndexOrThrow(DBHelper.PropertyDbIds.INCOME_INCREASE));
        propertyInformation.expenseIncrease = cursor.getInt(cursor.getColumnIndexOrThrow(DBHelper.PropertyDbIds.EXPENSE_INCREASE));
        propertyInformation.sellingCosts = cursor.getInt(cursor.getColumnIndexOrThrow(DBHelper.PropertyDbIds.SELLING_COSTS));
        propertyInformation.landValue = cursor.getInt(cursor.getColumnIndexOrThrow(DBHelper.PropertyDbIds.LAND_VALUE));
        propertyInformation.incomeTaxRate = cursor.getInt(cursor.getColumnIndexOrThrow(DBHelper.PropertyDbIds.INCOME_TAX_RATE));
        propertyInformation.notes = cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.PropertyDbIds.NOTES));

        {
            String jsonText = cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.PropertyDbIds.PICTURES));
            if(jsonText != null)
            {
                StringReader reader = new StringReader(jsonText);
                JsonReader parser = new JsonReader(reader);

                try
                {
                    parser.beginArray();

                    while(parser.hasNext())
                    {
                        propertyInformation.pictures.addLast(new File(parser.nextString()));
                    }

                    parser.endArray();
                }
                catch(IOException e)
                {
                    Log.w("RentalCalc", "Failed to parse pictures", e);
                }

            }
        }

        // All the itemizations are stored as a JSON string in the database. They need to be extracted
        // into a map.
        Map<String, Map<String, Integer>> itemizeOptionLookups = new ImmutableMap.Builder<String, Map<String, Integer>>()
                .put(DBHelper.PropertyDbIds.PURCHASE_COSTS_ITEMIZED, propertyInformation.purchaseCostsItemized)
                .put(DBHelper.PropertyDbIds.REPAIR_REMODEL_COSTS_ITEMIZED, propertyInformation.repairRemodelCostsItemized)
                .put(DBHelper.PropertyDbIds.EXPENSES_ITEMIZED, propertyInformation.expensesItemized)
                .build();

        for(final Map.Entry<String, Map<String, Integer>> entry : itemizeOptionLookups.entrySet())
        {
            String databaseColumn = entry.getKey();
            String jsonText = cursor.getString(cursor.getColumnIndexOrThrow(databaseColumn));
            if(jsonText != null)
            {
                try
                {
                    Map<String, Integer> itemizations = entry.getValue();
                    StringReader reader = new StringReader(jsonText);
                    JsonReader parser = new JsonReader(reader);

                    parser.beginObject();

                    while(parser.hasNext())
                    {
                        String name = parser.nextName();
                        int value = parser.nextInt();

                        itemizations.put(name, value);
                    }

                }
                catch(IOException e)
                {
                    Log.w("RentalCalc", "Failed to parse itemizations for " + databaseColumn, e);
                }
            }
        }

        return propertyInformation;
    }
}
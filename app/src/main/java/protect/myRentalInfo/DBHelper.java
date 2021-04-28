package protect.myRentalInfo;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.JsonWriter;
import android.util.Log;

import com.google.common.collect.ImmutableMap;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.util.Map;

/**
 * Helper class for managing data in the database
 */
class DBHelper extends SQLiteOpenHelper
{
    private static final String TAG = "myPropertyCalc";

    private static final String DATABASE_NAME = "myProperty.db";

    static final int ORIGINAL_DATABASE_VERSION = 1;
    static final int DATABASE_VERSION = 3;

    /**
     * All strings used with the budget table
     */
    static class PropertyDbIds
    {
        static final String TABLE = "properties";
        static final String ID = "_id";

        static final String NICKNAME = "nickname";
        static final String ADDRESS_STREET = "addressStreet";
        static final String ADDRESS_CITY = "addressCity";
        static final String ADDRESS_STATE = "addressState";
        static final String ADDRESS_ZIP = "addressZip";
        static final String PROPERTY_TYPE = "propertyType";
        static final String PROPERTY_BEDS = "propertyBeds";
        static final String PROPERTY_BATHS = "propertyBaths";
        static final String PROPERTY_SQUARE_FOOTAGE = "propertySqft";
        static final String PROPERTY_LOT = "propertyLot";
        static final String PROPERTY_YEAR = "propertyYear";
        static final String PROPERTY_PARKING = "propertyParking";
        static final String PROPERTY_ZONING = "propertyZoning";
        static final String PROPERTY_MLS = "propertyMls";

        static final String PURCHASE_PRICE = "purchasePrice";
        static final String AFTER_REPAIRS_VALUE = "afterRepairsValue";
        static final String USE_LOAN = "useLoan";
        static final String DOWN_PAYMENT = "downPayment";
        static final String INTEREST_RATE = "interestRate";
        static final String LOAN_DURATION = "loanDuration";
        static final String PURCHASE_COSTS = "purchaseCosts";
        static final String PURCHASE_COSTS_ITEMIZED = "purchaseCostsItemized";
        static final String REPAIR_REMODEL_COSTS = "repairRemodelCosts";
        static final String REPAIR_REMODEL_COSTS_ITEMIZED = "repairRemodelCostsItemized";
        static final String GROSS_RENT = "grossRent";
        static final String OTHER_INCOME = "otherIncome";
        static final String EXPENSES = "expenses";
        static final String EXPENSES_ITEMIZED = "expensesItemized";
        static final String VACANCY = "vacancy";
        static final String APPRECIATION = "appreciation";
        static final String INCOME_INCREASE = "incomeIncrease";
        static final String EXPENSE_INCREASE = "expenseIncrease";
        static final String SELLING_COSTS = "sellingCosts";
        static final String LAND_VALUE = "landValue";
        static final String INCOME_TAX_RATE = "incomeTaxRate";

        static final String NOTES = "notes";
        static final String PICTURES = "pictures";
    }

    DBHelper(Context context)
    {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {

        // create table for properties
        db.execSQL("create table " + PropertyDbIds.TABLE + "(" +
                PropertyDbIds.ID + " INTEGER primary key autoincrement," +
                PropertyDbIds.NICKNAME + " TEXT," +
                PropertyDbIds.ADDRESS_STREET + " TEXT," +
                PropertyDbIds.ADDRESS_CITY + " TEXT," +
                PropertyDbIds.ADDRESS_STATE + " TEXT," +
                PropertyDbIds.ADDRESS_ZIP + " TEXT," +
                PropertyDbIds.PROPERTY_TYPE + " INTEGER," +
                PropertyDbIds.PROPERTY_BEDS + " TEXT," +
                PropertyDbIds.PROPERTY_BATHS + " TEXT," +
                PropertyDbIds.PROPERTY_SQUARE_FOOTAGE + " INTEGER," +
                PropertyDbIds.PROPERTY_LOT + " INTEGER," +
                PropertyDbIds.PROPERTY_YEAR + " INTEGER," +
                PropertyDbIds.PROPERTY_PARKING + " INTEGER," +
                PropertyDbIds.PROPERTY_ZONING + " INTEGER," +
                PropertyDbIds.PROPERTY_MLS + " TEXT," +
                PropertyDbIds.PURCHASE_PRICE + " INTEGER," +
                PropertyDbIds.AFTER_REPAIRS_VALUE + " INTEGER," +
                PropertyDbIds.USE_LOAN + " INTEGER," +
                PropertyDbIds.DOWN_PAYMENT + " INTEGER," +
                PropertyDbIds.INTEREST_RATE + " REAL," +
                PropertyDbIds.LOAN_DURATION + " INTEGER," +
                PropertyDbIds.PURCHASE_COSTS + " INTEGER," +
                PropertyDbIds.PURCHASE_COSTS_ITEMIZED + " TEXT," +
                PropertyDbIds.REPAIR_REMODEL_COSTS + " INTEGER," +
                PropertyDbIds.REPAIR_REMODEL_COSTS_ITEMIZED + " TEXT," +
                PropertyDbIds.GROSS_RENT + " INTEGER," +
                PropertyDbIds.OTHER_INCOME + " INTEGER," +
                PropertyDbIds.EXPENSES + " INTEGER," +
                PropertyDbIds.EXPENSES_ITEMIZED + " TEXT," +
                PropertyDbIds.VACANCY + " INTEGER," +
                PropertyDbIds.APPRECIATION + " INTEGER," +
                PropertyDbIds.INCOME_INCREASE + " INTEGER," +
                PropertyDbIds.EXPENSE_INCREASE + " INTEGER," +
                PropertyDbIds.SELLING_COSTS + " INTEGER," +
                PropertyDbIds.LAND_VALUE + " INTEGER," +
                PropertyDbIds.INCOME_TAX_RATE + " INTEGER," +
                PropertyDbIds.NOTES + " TEXT," +
                PropertyDbIds.PICTURES + " TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        // Upgrade from version 1 to version 2
        if(oldVersion < 2 && newVersion >= 2)
        {
            db.execSQL("ALTER TABLE " + PropertyDbIds.TABLE + " ADD COLUMN " + PropertyDbIds.INCOME_TAX_RATE + " INTEGER DEFAULT 0");
            db.execSQL("ALTER TABLE " + PropertyDbIds.TABLE + " ADD COLUMN " + PropertyDbIds.PURCHASE_COSTS_ITEMIZED + " TEXT");
            db.execSQL("ALTER TABLE " + PropertyDbIds.TABLE + " ADD COLUMN " + PropertyDbIds.REPAIR_REMODEL_COSTS_ITEMIZED + " TEXT");
            db.execSQL("ALTER TABLE " + PropertyDbIds.TABLE + " ADD COLUMN " + PropertyDbIds.EXPENSES_ITEMIZED + " TEXT");
        }

        // Upgrade from version 2 to version 3
        if(oldVersion < 3 && newVersion >= 3)
        {
            db.execSQL("ALTER TABLE " + PropertyDbIds.TABLE + " ADD COLUMN " + PropertyDbIds.PICTURES + " TEXT");
        }
    }

    private ContentValues toContentValues(PropertyInformation propertyInformation)
    {
        ContentValues contentValues = new ContentValues();
        contentValues.put(PropertyDbIds.NICKNAME, propertyInformation.nickname);
        contentValues.put(PropertyDbIds.ADDRESS_STREET, propertyInformation.addressStreet);
        contentValues.put(PropertyDbIds.ADDRESS_CITY, propertyInformation.addressCity);
        contentValues.put(PropertyDbIds.ADDRESS_STATE, propertyInformation.addressState);
        contentValues.put(PropertyDbIds.ADDRESS_ZIP, propertyInformation.addressZip);
        contentValues.put(PropertyDbIds.PROPERTY_TYPE, propertyInformation.propertyType);
        contentValues.put(PropertyDbIds.PROPERTY_BEDS, propertyInformation.propertyBeds);
        contentValues.put(PropertyDbIds.PROPERTY_BATHS, propertyInformation.propertyBaths);
        contentValues.put(PropertyDbIds.PROPERTY_SQUARE_FOOTAGE, propertyInformation.propertySqft);
        contentValues.put(PropertyDbIds.PROPERTY_LOT, propertyInformation.propertyLot);
        contentValues.put(PropertyDbIds.PROPERTY_YEAR, propertyInformation.propertyYear);
        contentValues.put(PropertyDbIds.PROPERTY_PARKING, propertyInformation.propertyParking);
        contentValues.put(PropertyDbIds.PROPERTY_ZONING, propertyInformation.propertyZoning);
        contentValues.put(PropertyDbIds.PROPERTY_MLS, propertyInformation.propertyMls);
        contentValues.put(PropertyDbIds.PURCHASE_PRICE, propertyInformation.purchasePrice);
        contentValues.put(PropertyDbIds.AFTER_REPAIRS_VALUE, propertyInformation.afterRepairsValue);
        contentValues.put(PropertyDbIds.USE_LOAN, propertyInformation.useLoan);
        contentValues.put(PropertyDbIds.DOWN_PAYMENT, propertyInformation.downPayment);
        contentValues.put(PropertyDbIds.INTEREST_RATE, propertyInformation.interestRate);
        contentValues.put(PropertyDbIds.LOAN_DURATION, propertyInformation.loanDuration);
        contentValues.put(PropertyDbIds.PURCHASE_COSTS, propertyInformation.purchaseCosts);
        contentValues.put(PropertyDbIds.REPAIR_REMODEL_COSTS, propertyInformation.repairRemodelCosts);
        contentValues.put(PropertyDbIds.GROSS_RENT, propertyInformation.grossRent);
        contentValues.put(PropertyDbIds.OTHER_INCOME, propertyInformation.otherIncome);
        contentValues.put(PropertyDbIds.EXPENSES, propertyInformation.expenses);
        contentValues.put(PropertyDbIds.VACANCY, propertyInformation.vacancy);
        contentValues.put(PropertyDbIds.APPRECIATION, propertyInformation.appreciation);
        contentValues.put(PropertyDbIds.INCOME_INCREASE, propertyInformation.incomeIncrease);
        contentValues.put(PropertyDbIds.EXPENSE_INCREASE, propertyInformation.expenseIncrease);
        contentValues.put(PropertyDbIds.INCOME_TAX_RATE, propertyInformation.incomeTaxRate);
        contentValues.put(PropertyDbIds.SELLING_COSTS, propertyInformation.sellingCosts);
        contentValues.put(PropertyDbIds.LAND_VALUE, propertyInformation.landValue);
        contentValues.put(PropertyDbIds.NOTES, propertyInformation.notes);
        contentValues.put(PropertyDbIds.NOTES, propertyInformation.notes);

        {
            StringWriter jsonText = new StringWriter();
            JsonWriter writer = new JsonWriter(jsonText);

            try
            {
                writer.beginArray();

                for(File picture : propertyInformation.pictures)
                {
                    if(picture.exists() && picture.isFile())
                    {
                        writer.value(picture.getAbsolutePath());
                    }
                }

                writer.endArray();
                writer.close();
            }
            catch(IOException e)
            {
                Log.w(TAG, "Failed to convert pictures to json", e);
            }

            contentValues.put(PropertyDbIds.PICTURES, jsonText.toString());
        }

        // All the itemizations are stored as a JSON string in the database. They need to be converted
        // from maps to JSON strings.
        Map<String, Map<String, Integer>> itemizeOptionLookups = new ImmutableMap.Builder<String, Map<String, Integer>>()
                .put(DBHelper.PropertyDbIds.PURCHASE_COSTS_ITEMIZED, propertyInformation.purchaseCostsItemized)
                .put(DBHelper.PropertyDbIds.REPAIR_REMODEL_COSTS_ITEMIZED, propertyInformation.repairRemodelCostsItemized)
                .put(DBHelper.PropertyDbIds.EXPENSES_ITEMIZED, propertyInformation.expensesItemized)
                .build();

        for(final Map.Entry<String, Map<String, Integer>> entry : itemizeOptionLookups.entrySet())
        {
            Map<String, Integer> itemizations = entry.getValue();
            String databaseColumn = entry.getKey();

            StringWriter jsonText = new StringWriter();
            JsonWriter writer = new JsonWriter(jsonText);

            try
            {
                writer.beginObject();

                for(final Map.Entry<String, Integer> item : itemizations.entrySet())
                {
                    writer.name(item.getKey()).value(item.getValue());
                }

                writer.endObject();
                writer.close();
            }
            catch(IOException e)
            {
                Log.w(TAG, "Failed to convert itemizations to json for " + databaseColumn, e);
            }

            contentValues.put(databaseColumn, jsonText.toString());
        }

        return contentValues;
    }

    /**
     * Insert a propertyInformation into the database. The ID of the
     * propertyInformation will be ignored, as it will be auto-assigned.
     *
     * @return the index of the new value if successful,
     * -1 otherwise
     */
    long insertProperty(final PropertyInformation propertyInformation)
    {
        ContentValues contentValues = toContentValues(propertyInformation);

        SQLiteDatabase db = getWritableDatabase();
        long newId = db.insert(PropertyDbIds.TABLE, null, contentValues);
        db.close();

        return newId;
    }

    /**
     * Insert a propertyInformation into the database, using a provided
     * writable database instance. This is useful if
     * multiple insertions will occur in the same transaction.
     * The ID of the propertyInformation will be used, and the operation
     * will be rejected if the ID is already in use.
     *
     * @param writableDb
     *      writable database instance to use
     * @return the index of the new value if successful,
     * -1 otherwise
     */
    long insertProperty(final SQLiteDatabase writableDb, final PropertyInformation propertyInformation)
    {
        ContentValues contentValues = toContentValues(propertyInformation);

        long newId = writableDb.insert(PropertyDbIds.TABLE, null, contentValues);

        return newId;
    }

    /**
     * Update the propertyInformation in the database. The unique ID for
     * the propertyInformation will be used, all other fields represent
     * the values as will be updated in the database.
     *
     * @return true if the provided propertyInformation exists and the value
     * was successfully updated, false otherwise.
     */
    boolean updateProperty(final PropertyInformation propertyInformation)
    {
        ContentValues contentValues = toContentValues(propertyInformation);

        SQLiteDatabase db = getWritableDatabase();
        int rowsUpdated = db.update(PropertyDbIds.TABLE, contentValues,
                PropertyDbIds.ID + "=?",
                new String[]{Long.toString(propertyInformation.id)});
        db.close();

        return (rowsUpdated == 1);
    }

    /**
     * Get PropertyInformation object for the named property in the database,
     *
     * @param id
     *      id of the property to query
     * @return PropertyInformation object representing the named property,
     * or null if it could not be queried
     */
    PropertyInformation getProperty(final long id)
    {
        SQLiteDatabase db = getReadableDatabase();
        Cursor data = db.rawQuery("select * from " + PropertyDbIds.TABLE +
                " where " + PropertyDbIds.ID + "=?", new String[]{Long.toString(id)});

        PropertyInformation propertyInformation = null;

        if(data.getCount() == 1)
        {
            data.moveToFirst();
            propertyInformation = PropertyInformation.toProperty(data);
        }

        data.close();
        db.close();

        return propertyInformation;
    }

    /**
     * Returns the number of properties in the database
     * of the provided type.
     *
     * @return the number of properties in the database
     * of the given type
     */
    int getPropertyCount()
    {
        SQLiteDatabase db = getReadableDatabase();
        Cursor data =  db.rawQuery("SELECT Count(*) FROM " + PropertyDbIds.TABLE, new String[]{});

        int numItems = 0;

        if(data.getCount() == 1)
        {
            data.moveToFirst();
            numItems = data.getInt(0);
        }

        data.close();
        db.close();

        return numItems;
    }

    /**
     * Delete a given property from the database
     *
     * @param id
     *      id of the property to delete
     * @return if the property was successfully deleted,
     * false otherwise
     */
    boolean deleteProperty(final long id)
    {
        SQLiteDatabase db = getWritableDatabase();
        int rowsDeleted =  db.delete(PropertyDbIds.TABLE,
                PropertyDbIds.ID + " = ? ",
                new String[]{Long.toString(id)});
        db.close();

        return (rowsDeleted == 1);
    }

    /**
     * Returns a cursor pointing to all properties.
     */
    Cursor getProperties()
    {
        SQLiteDatabase db = getReadableDatabase();

        String query = "select * from " + PropertyDbIds.TABLE;

        query += " ORDER BY " + PropertyDbIds.NICKNAME;

        Cursor res =  db.rawQuery(query, new String[0]);
        return res;
    }

}

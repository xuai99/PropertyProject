package protect.myRentalInfo;

import android.content.Context;
import android.util.Log;

enum myPropertyTypeInformation
{
    BLANK(R.string.blank),
    COMMERCIAL(R.string.commercial),
    CONDO(R.string.condo),
    HOUSE(R.string.house),
    LAND(R.string.land),
    MULTI_FAMILY(R.string.multifamily),
    MANUFACTURED(R.string.manufactured),
    OTHER(R.string.other),
    ;

    private static final String TAG = "RentalCalc";

    final int stringId;

    myPropertyTypeInformation(int stringId)
    {
        this.stringId = stringId;
    }

    static myPropertyTypeInformation fromString(Context context, String string)
    {
        if(string != null)
        {
            for(myPropertyTypeInformation type : myPropertyTypeInformation.values())
            {
                if(string.equals(context.getString(type.stringId)))
                {
                    return type;
                }
            }
        }

        Log.w(TAG, "Failed to lookup myPropertyTypeInformation id " + string);

        return myPropertyTypeInformation.BLANK;
    }
}

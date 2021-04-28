package protect.myRentalInfo;

import android.content.Context;
import android.util.Log;

enum myParkingInfomationType
{
    BLANK(R.string.blank),
    CAR_PORT(R.string.carport),
    GARAGE(R.string.garage),
    OFF_STREET(R.string.offstreet),
    ON_STREET(R.string.onstreet),
    NONE(R.string.none),
    PRIVATE_LOT(R.string.privatelot),
    ;

    private static final String TAG = "RentalCalc";

    final int stringId;

    myParkingInfomationType(int stringId)
    {
        this.stringId = stringId;
    }

    static myParkingInfomationType fromString(Context context, String string)
    {
        if(string != null)
        {
            for(myParkingInfomationType type : myParkingInfomationType.values())
            {
                if(string.equals(context.getString(type.stringId)))
                {
                    return type;
                }
            }
        }

        Log.w(TAG, "Failed to lookup myParkingInfomationType id " + string);

        return myParkingInfomationType.BLANK;
    }
}

package protect.myRentalInfo;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import java.util.Locale;

class myPropertyCursorAdapter extends CursorAdapter
{
    myPropertyCursorAdapter(Context context, Cursor cursor)
    {
        super(context, cursor, 0);
    }

    private static class ViewHolder
    {
        TextView nicknameField;
        TextView streetNameF;
        TextView shortAddressField;
        TextView priceField;
        TextView rentalF;
        TextView housingFT;
        TextView housingFTprice;
        TextView buildYear;
        TextView myproperty;
        TextView myparking;
    }

    // The newView method is used to inflate a new view and return it,
    // you don't bind any data to the view at this point.
    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent)
    {
        View view = LayoutInflater.from(context).inflate(R.layout.property_cursor_layout, parent, false);

        ViewHolder holder = new ViewHolder();
        holder.nicknameField = (TextView) view.findViewById(R.id.nickname);
        holder.shortAddressField = (TextView) view.findViewById(R.id.shortAddress);
        holder.priceField = (TextView) view.findViewById(R.id.price);
        holder.streetNameF = view.findViewById(R.id.StreetName);
        holder.housingFT = view.findViewById(R.id.housingFT);
        holder.rentalF = view.findViewById(R.id.muhRental);
        holder.housingFTprice = view.findViewById(R.id.housingFTPrice);
        holder.buildYear = view.findViewById(R.id.mybuildYear);
        holder.myproperty = view.findViewById(R.id.mypropertyInfo);
        holder.myparking = view.findViewById(R.id.myParkingInfo);
        view.setTag(holder);

        return view;
    }

    // The bindView method is used to bind all data to a given view
    // such as setting the text on a TextView.
    @Override
    public void bindView(View view, Context context, Cursor cursor)
    {
        ViewHolder holder = (ViewHolder)view.getTag();

        // Extract properties from cursor
        PropertyInformation propertyInformation = PropertyInformation.toProperty(cursor);
        holder.streetNameF.setText(propertyInformation.addressStreet);
        holder.housingFT.setText("Square feet : "+propertyInformation.propertySqft+"ft");
        holder.rentalF.setText("Monthly Rental RM: "+Integer.toString(propertyInformation.grossRent));
        holder.priceField.setText("Purchase Cost RM: "+Integer.toString(propertyInformation.purchasePrice));
        holder.buildYear.setText("Property Build Year: "+propertyInformation.propertyYear);

        setPropertyInformation(propertyInformation,view);
        myparkinginformation(propertyInformation,view);

        if(propertyInformation.purchasePrice == 0 || propertyInformation.propertySqft == 0){
            holder.housingFTprice.setText("Price Per (ft) Rm: 0");
        }else if(propertyInformation.purchasePrice != 0 || propertyInformation.propertySqft != 0){
            holder.housingFTprice.setText("Price Per (ft) Rm: "+propertyInformation.purchasePrice/propertyInformation.propertySqft);
        }
        // Populate fields with extracted properties
        holder.nicknameField.setText(propertyInformation.nickname);
        if(propertyInformation.addressCity.isEmpty() == false && propertyInformation.addressState.isEmpty() == false)
        {
            holder.shortAddressField.setText(String.format("%s, %s", propertyInformation.addressCity, propertyInformation.addressState));
        }
        else if(propertyInformation.addressState.isEmpty() == false)
        {
            holder.shortAddressField.setText(propertyInformation.addressState);
        }
        else if(propertyInformation.addressCity.isEmpty() == false)
        {
            holder.shortAddressField.setText(propertyInformation.addressCity);
        }
        else
        {
            holder.streetNameF.setText(0);

            holder.shortAddressField.setText("");
        }

    }
    public void myparkinginformation(PropertyInformation propertyInformation,View view){
        ViewHolder holder = (ViewHolder)view.getTag();
        if(propertyInformation.propertyParking == 0){
            holder.myparking.setText("Type of parking: Not Set");
        }
        if(propertyInformation.propertyParking == 1){
            holder.myparking.setText("Type of parking: Carport");
        }
        if(propertyInformation.propertyParking == 2){
            holder.myparking.setText("Type of parking: Garage");
        }
        if(propertyInformation.propertyParking == 3){
            holder.myparking.setText("Type of parking: Off-street parking");
        }
        if(propertyInformation.propertyParking == 4){
            holder.myparking.setText("Type of parking: On-street parking");
        }
        if(propertyInformation.propertyParking == 5){
            holder.myparking.setText("Type of parking: No lot available");
        }
        if(propertyInformation.propertyParking == 6){
            holder.myparking.setText("Type of parking: Private Lot");
        }



    }
    public void setPropertyInformation(PropertyInformation propertyInformation, View view){
        ViewHolder holder = (ViewHolder)view.getTag();
        if(propertyInformation.propertyType == 0){

            holder.myproperty.setText("Property type : Not Set");
        }
        if(propertyInformation.propertyType == 1){

            holder.myproperty.setText("Property type : Commercial Lot");
        }
        if(propertyInformation.propertyType == 2){

            holder.myproperty.setText("Property type : Condominum");
        }

        if(propertyInformation.propertyType == 3){

            holder.myproperty.setText("Property type : Apartment");
        }
        if(propertyInformation.propertyType == 4){

            holder.myproperty.setText("Property type : Landed-housing");
        }
        if(propertyInformation.propertyType == 5){

            holder.myproperty.setText("Property type : Multi family housing");
        }
        if(propertyInformation.propertyType == 6){

            holder.myproperty.setText("Property type : Manufactured housing");
        }
        if(propertyInformation.propertyType == 7){

            holder.myproperty.setText("Property type : others");
        }

    }


}

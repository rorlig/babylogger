package com.rorlig.babylog.ui.adapter;

import android.app.Activity;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Build;
import android.provider.ContactsContract;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.rorlig.babylog.R;
import com.rorlig.babylog.model.ContactEntry2;
import com.rorlig.babylog.model.ContactItem;
import com.rorlig.babylog.otto.ScopedBus;
import com.rorlig.babylog.ui.activity.InjectableActivity;
import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

/**
 * Created by elkintr on 3/24/2014.
 */
public class ContactsAdapter extends BaseAdapter implements Filterable {

    public static final int TYPE_CALL_LOG_ENTRY = 0;
    public static final int TYPE_CONTACT_ENTRY = 1;
    public static final int TYPE_DIVIDER = 2;
    private List<ContactItem> items;
    private List<ContactItem> filteredItems;
    private Context context;
    private LayoutInflater inflater;
    private ScopedBus bus;
    private Typeface supportTypeface; //used for API < 16
    private String callTextPlural;
    private String callTextSingular;
    private String lastCallText;
    private float density;
//    private Map<String, Uri> brokenUris;
    private DateFormat dateFormatter = DateFormat.getDateInstance();
    private ContactsFilter mFilter = new ContactsFilter();


    @Inject
    Picasso picasso;
    private String TAG ="ContactsAdapter";

    private static final String[] PHOTO_BITMAP_PROJECTION = new String[] {
            ContactsContract.CommonDataKinds.Photo.PHOTO
    };

    public ContactsAdapter(Activity activity, List<ContactItem> items) {
        this.context = activity;
        this.items = new ArrayList<ContactItem>(items);
        this.filteredItems = new ArrayList<ContactItem>(items);
        inflater = LayoutInflater.from(context);
        supportTypeface = Typeface.create("sans-serif-light", Typeface.NORMAL);
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        density = metrics.density;
//        brokenUris = new HashMap<String, Uri>();
        ((InjectableActivity)activity).inject(this);
    }

//    public ContactsAdapter(Context context, List<ContactItem> items, ScopedBus bus) {
//        this.context = context;
//        this.items = new ArrayList<ContactItem>(items);
//        this.filteredItems = new ArrayList<ContactItem>(items);
//        this.bus = bus;
////        SideshareApplication.get(context).inject(this);
//        inflater = LayoutInflater.from(context);
//        supportTypeface = Typeface.create("sans-serif-light", Typeface.NORMAL);
////        callTextPlural = context.getString(R.string.call_text_plural);
////        callTextSingular = context.getString(R.string.call_text_singular);
//        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
//        density = metrics.density;
//        brokenUris = new HashMap<String, Uri>();
//    }

    public ContactItem get(int position) {
        if(position<filteredItems.size())
            return filteredItems.get(position);
        else
            return null;
    }

    @Override
    public int getCount() {
        return filteredItems.size();
    }

    @Override
    public Object getItem(int position) {
        return filteredItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public int getItemViewType(int position) {
        return filteredItems.get(position).getViewType();
    }

    @Override
    public int getViewTypeCount() {
        return 3;
    }

    @Override
    public boolean areAllItemsEnabled () {
        return true;
    }

    @Override
    public boolean isEnabled(int position) {
        return filteredItems.get(position).getViewType() != TYPE_DIVIDER;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = null;
        return  getContactEntryView(position, convertView, parent);
//        int type = filteredItems.get(position).getViewType();
//        switch(type) {
////            case TYPE_CONTACT_ENTRY:
//                v = getContactEntryView(position, convertView, parent);
//                break;
//            case TYPE_DIVIDER:
//                v = getDividerView(position, convertView, parent);
//                break;
//        }
//
//        return v;
    }

//    private View getDividerView(int position, View convertView, ViewGroup parent) {
//        ViewHolderHeader vh = null;
//        if(convertView == null) {
//            convertView = inflater.inflate(R.layout.offline_contacts_divider, parent, false);
//            if(convertView != null) {
//                vh = new ViewHolderHeader();
//                vh.header = (TextView) convertView.findViewById(R.id.callog_header_text);
//                convertView.setTag(vh);
//            }
//        }
//        else {
//            vh = (ViewHolderHeader) convertView.getTag();
//        }
//
//        ContactSectionDivider header = (ContactSectionDivider) filteredItems.get(position);
//        vh.header.setText(header.getText());
//        return convertView;
//    }

//    private View getCallLogEntryView(int position, View convertView, ViewGroup parent) {
//        CalllogViewHolder vh = null;
//        if(convertView == null) {
//            convertView = inflater.inflate(R.layout.offline_contacts_row, parent, false);
//            if(convertView != null) {
//                vh = new CalllogViewHolder();
//                vh.detailsButton = convertView.findViewById(R.id.contact_details_button);
//                vh.name = (TextView) convertView.findViewById(R.id.contact_name);
//                //If less than API16, fontFamily attribute doesn't work so must manually set
//                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
//                    vh.name.setTypeface(supportTypeface);
//                }
//                vh.calls = (TextView) convertView.findViewById(R.id.contact_number_calls);
//                vh.lastCalled = (TextView) convertView.findViewById(R.id.contact_last_called);
//                vh.actionRow = (ContactEntryActionRow) convertView.findViewById(R.id.contact_action_row);
//                vh.picture = (ImageView) convertView.findViewById(R.id.contact_picture);
//                convertView.setTag(vh);
//            }
//        }
//        else {
//            vh = (CalllogViewHolder) convertView.getTag();
//        }
//
//        final ContactEntry item = (ContactEntry) filteredItems.get(position);
//
//        //If the item is selected, then show the action row
//        if(item.isSelected()) {
//            vh.actionRow.setVisibility(View.VISIBLE);
//        }
//        else {
//            vh.actionRow.setVisibility(View.GONE);
//        }
//
//        //Display the contact photo of the person
//        int size = (int) (64 * density);
//        if(item.getPictureUri() != null && !brokenUris.containsKey(item.getPictureUri().toString())) {
//            picasso.load(item.getPictureUri())
//                    .resize(size, size)
//                    .centerInside()
//                    .error(R.drawable.ic_icon_contact)
//                    .into(vh.picture, new Callback() {
//                        @Override
//                        public void onSuccess() { }
//
//                        @Override
//                        public void onError() {
//                            Log.d("CallLog", "onError");
//                            brokenUris.put(item.getPictureUri().toString(), item.getPictureUri());
//                        }
//                    });
//        }
//        //If none found, then load default
//        else {
//            picasso.load(R.drawable.ic_icon_contact)
//                    .resize(size, size)
//                    .centerInside()
//                    .into(vh.picture);
//        }
//
//        //Add button listener for opening contact details fragment
//        vh.detailsButton.setOnClickListener(getDetailsButtonClickListener(position));
//
//        //Display name
//        if(item.getName() != null) {
//            vh.name.setText(item.getName());
//        }
//        else if(item.getFirstNumber() != null) {
//            vh.name.setText(StringUtils.formatNumber(item.getFirstNumber()));
//        }
//
//        //Set calls.  Hide if 0 to make look nicer for contact list
//        if(item.getCalls() == null && item.getLastCalled() == null) {
//            vh.calls.setVisibility(View.GONE);
//            vh.lastCalled.setVisibility(View.GONE);
//        }
//        else {
//            if(item.getCalls() > 1) {
//                vh.calls.setText(item.getCalls() + " " + callTextPlural);
//            }
//            else {
//                vh.calls.setText(item.getCalls() + " " + callTextSingular);
//            }
//            String date =StringUtils.getDate(item.getLastCalled());
//            vh.lastCalled.setText(StringUtils.getDate(item.getLastCalled()));
//            vh.calls.setVisibility(View.VISIBLE);
//            vh.lastCalled.setVisibility(View.VISIBLE);
//        }
//
//        return convertView;
//    }

    private View getContactEntryView(int position, View convertView, ViewGroup parent) {
        ContactViewHolder vh = null;
        if(convertView == null) {
            convertView = inflater.inflate(R.layout.list_item_addressbook_contact, parent, false);
            if(convertView != null) {
                vh = new ContactViewHolder();
//                vh.detailsButton = convertView.findViewById(R.id.contact_details_button);
                vh.name = (TextView) convertView.findViewById(R.id.contact_name);
                //If less than API16, fontFamily attribute doesn't work so must manually set
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
                    vh.name.setTypeface(supportTypeface);
                }
//                vh.number = (TextView) convertView.findViewById(R.id.contact_number);
//                vh.actionRow = (ContactEntryActionRow) convertView.findViewById(R.id.contact_action_row);
                vh.picture = (ImageView) convertView.findViewById(R.id.contact_picture);
                convertView.setTag(vh);
            }
        }
        else {
            vh = (ContactViewHolder) convertView.getTag();
        }

        final ContactEntry2 item = (ContactEntry2) filteredItems.get(position);

        Log.d(TAG, "contactItem " + item);
        vh.name.setText(item.getName());


        //If the item is selected, then show the action row
//        if(item.isSelected()) {
//            vh.actionRow.setVisibility(View.VISIBLE);
//        }
//        else {
//            vh.actionRow.setVisibility(View.GONE);
//        }

        //Display the contact photo of the person

        Log.d(TAG, "picture uri " + item.getPictureUri());

        int size = (int) (64 * density);

        if (item.getPictureResId()!=0) {
         vh.picture.setImageBitmap(fetchThumbnail(item.getPictureResId()));
        }  else {
            picasso.load(R.drawable.ic_action_user)
                    .resize(size, size)
                    .centerInside()
                    .into(vh.picture);
        }


//        if(item.getPictureUri() != null && !brokenUris.containsKey(item.getPictureUri().toString())) {
//            picasso.load(item.getPictureUri())
//                    .resize(size, size)
//                    .centerInside()
//                    .error(R.drawable.ic_icon_contact)
//                    .into(vh.picture, new Callback() {
//                        @Override
//                        public void onSuccess() { }
//
//                        @Override
//                        public void onError() {
//                            Log.d("CallLog", "onError");
//                            brokenUris.put(item.getPictureUri().toString(), item.getPictureUri());
//                        }
//                    });
//        }
        //If none found, then load default

        //Add button listener for opening contact details fragment

        //Display name
//        if(item.getName() != null) {
//            vh.name.setText(item.getName());
//
//            //Set calls.  Hide if 0 to make look nicer for contact list
//            if(item.getNumberCount() == 1) {
//                String number = StringUtils.formatNumber(item.getFirstNumber());
//                vh.number.setText(number);
//            }
//            else {
//                StringBuilder builder = new StringBuilder();
//                for(String number : item.getNumberList()) {
//                    builder.append(StringUtils.formatNumber(number));
//                    builder.append("\n");
//                }
//                builder.setLength(builder.length() - 1); //knock off extra ", "
//                vh.number.setText(builder.toString());
//            }
//            vh.number.setVisibility(View.VISIBLE);
//        }
//        else if(item.getFirstNumber() != null) {
//            vh.name.setText(StringUtils.formatNumber(item.getFirstNumber()));
//            vh.number.setVisibility(View.GONE);
//        }

        return convertView;
    }



    @Override
    public Filter getFilter() {
        return mFilter;
    }

    /*
     * Updates the adapter...
     */
    public void update(List<ContactItem> items){

        this.items = new ArrayList<ContactItem>(items);
        this.filteredItems = new ArrayList<ContactItem>(items);
        notifyDataSetChanged();
    }


    private final Bitmap fetchThumbnail(final int thumbnailId) {

        final Uri uri = ContentUris.withAppendedId(ContactsContract.Data.CONTENT_URI, thumbnailId);
        final Cursor cursor = context.getContentResolver().query(uri, PHOTO_BITMAP_PROJECTION, null, null, null);

        try {
            Bitmap thumbnail = null;
            if (cursor.moveToFirst()) {
                final byte[] thumbnailBytes = cursor.getBlob(0);
                if (thumbnailBytes != null) {
                    thumbnail = BitmapFactory.decodeByteArray(thumbnailBytes, 0, thumbnailBytes.length);
                }
            }
            return thumbnail;
        }
        finally {
            cursor.close();
        }

    }

    private static class ContactViewHolder {
        TextView name;
        TextView number;
//        View detailsButton;
        ImageView picture;
    }

    private static class ViewHolderHeader {
        TextView header;
    }

    private class ContactsFilter extends Filter {

        private String TAG = "ContactsFilter";

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {

            Log.d(TAG, "constraint " + constraint);

            String filterString = constraint.toString().toLowerCase();

            Log.d(TAG, "filterString " + filterString);
            FilterResults results = new FilterResults();

            int count = items.size();

            //count of number of dividers...
            int dividerCount = 0;

            final List<ContactItem> nlist = new ArrayList<ContactItem>(count);


            if (!filterString.equals("")) {
//                Log.d(TAG, "filter is not blank");
                for (ContactItem contactItem : items) {
                    Log.d(TAG, " item type " + contactItem.getViewType());

                        final ContactEntry2 contactEntry = (ContactEntry2) contactItem;
                        Log.d(TAG, "contact or call type " + contactEntry);
                        if ((contactEntry.getName()!=null && contactEntry.getName().toLowerCase().contains(filterString))) {
                            Log.d(TAG, "adding item");
                            nlist.add(contactItem);
                        }
//                    } else {
//                        Log.d(TAG, "divider");
//                    }

                }
                results.values = nlist;
                results.count = nlist.size();
            } else {
                //all this to not do re index...
                Log.d(TAG, "filter is blank");
                results.values = items;
                results.count = items.size();
            }
            return results;
        }

        @SuppressWarnings("unchecked")
        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            Log.d(TAG, "publishResults ");
            filteredItems = (ArrayList<ContactItem>) results.values;
            Log.d(TAG, "filterData size " + filteredItems.size());

            notifyDataSetChanged();
        }

        private boolean phoneListContains(String filterString, List<String> numberList){
            for (String phoneNumber: numberList){
                if (phoneNumber.contains(filterString)) {
                    return true;
                }
            }
            return false;
        }

    }

}

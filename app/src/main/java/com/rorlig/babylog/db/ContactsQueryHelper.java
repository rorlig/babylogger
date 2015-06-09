package com.rorlig.babylog.db;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.provider.ContactsContract;
import android.util.Log;

import com.rorlig.babylog.model.ContactEntry2;
import com.rorlig.babylog.model.ContactItem;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by gaurav
 */
public class ContactsQueryHelper {

    private static String TAG = "ContactsQueryHelper";

    public static List<ContactItem> contactWithEmail(Context context){
        ContentResolver cr = context.getContentResolver();
        String[] PROJECTION = new String[] { ContactsContract.RawContacts._ID,
                ContactsContract.Contacts.DISPLAY_NAME,
                ContactsContract.Contacts.PHOTO_ID,
                ContactsContract.CommonDataKinds.Email.DATA,
                ContactsContract.CommonDataKinds.Photo.CONTACT_ID };
        String order = "CASE WHEN "
                + ContactsContract.Contacts.DISPLAY_NAME
                + " NOT LIKE '%@%' THEN 1 ELSE 2 END, "
                + ContactsContract.Contacts.DISPLAY_NAME
                + ", "
                + ContactsContract.CommonDataKinds.Email.DATA
                + " COLLATE NOCASE";
        String filter = ContactsContract.CommonDataKinds.Email.DATA + " NOT LIKE ''";
        Cursor cursor = cr.query(ContactsContract.CommonDataKinds.Email.CONTENT_URI, PROJECTION, filter, null, order);
        ArrayList<ContactItem> contactArrayList = new ArrayList<ContactItem>();
        if (cursor!=null && cursor.getCount()>0) {
            Log.d(TAG, " count of the email " + cursor.getCount());
            cursor.moveToFirst();
            while(cursor.moveToNext()) {
                 String fullName = cursor.getString(1);
                 int photpRes = cursor.getInt(2);
                 String email = cursor.getString(3);
                 ContactEntry2 contactItem = new ContactEntry2(fullName, email, photpRes);
                 Log.d(TAG, "contact item " + contactItem);
                 contactArrayList.add(contactItem);
            }
        }
        return contactArrayList;

    }


}

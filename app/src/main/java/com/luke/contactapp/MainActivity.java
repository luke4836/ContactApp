package com.luke.contactapp;

import android.Manifest;
import android.content.ContentResolver;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_CONTACT = 1;
    private Cursor c;
    private ListView lv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        lv = (ListView) findViewById(R.id.listView);

        //先Check 權限
        int permission = ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS);

        if (permission== PackageManager.PERMISSION_DENIED){
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_CONTACTS}, REQUEST_CONTACT);
        }else{
            viewCursor();
        }




    }
    //確認RequestPermission權限的結果
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode==REQUEST_CONTACT){
            if (grantResults[0]>0 && grantResults[0]==PackageManager.PERMISSION_GRANTED){
                viewCursor();
            }
        }else{

        }
    }

    private void viewCursor() {
        Log.d("ViewContacts", "OK");
        ContentResolver cr = getContentResolver();
//        c = cr.query(ContactsContract.Contacts.CONTENT_URI,null,null,null,null); //跑回圈查兩次, 適用下面的註解程式碼
        //下面是類似使用Join的方式(Implicit Join), Join兩個Table後, 只抓有電話的人
        c = cr.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI
                ,new String[]{ContactsContract.Contacts.DISPLAY_NAME,
                        ContactsContract.CommonDataKinds.Phone.NUMBER,
                        ContactsContract.Contacts._ID}
                ,null,null,null);
        String[] from = {ContactsContract.Contacts.DISPLAY_NAME, ContactsContract.CommonDataKinds.Phone.NUMBER};
        int[] to = {android.R.id.text1, android.R.id.text2};
        SimpleCursorAdapter adapter = new SimpleCursorAdapter(this,
                android.R.layout.simple_list_item_2,c,from, to,0);
        lv.setAdapter(adapter);

//        while(c.moveToNext()){// Show 出全部
//            int index = c.getColumnIndex(ContactsContract.Contacts._ID);
//            int id = c.getInt(index);
//            String name = c.getString(c.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
//            Log.d("Phone", id+"/"+name);
//
//            int hasPhone = c.getInt(c.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER));
//            Log.d("hasPhone", hasPhone+"");
//
//            if (hasPhone==1){ //2次查詢, 查這裡 CommonDataKinds.Phone.CONTENT_URI
//                Cursor c2 = cr.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
//                        null, ContactsContract.CommonDataKinds.Phone.CONTACT_ID+"=?"
//                        ,new String[]{id+""}
//                        ,null);
//                while (c2.moveToNext()){
//                    String phone = c2.getString(c2.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
//                    Log.d("Phone", phone);
//                }
//            }
//
//        }

    }
}

package com.q4u.getcontacts;

import static io.flutter.util.ViewUtils.getActivity;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Build;
import android.provider.ContactsContract;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import io.flutter.Log;
import io.flutter.embedding.engine.plugins.FlutterPlugin;
import io.flutter.embedding.engine.plugins.activity.ActivityAware;
import io.flutter.embedding.engine.plugins.activity.ActivityPluginBinding;
import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.MethodChannel;
import io.flutter.plugin.common.MethodChannel.MethodCallHandler;
import io.flutter.plugin.common.MethodChannel.Result;

/** GetcontactsPlugin */
public class GetcontactsPlugin implements FlutterPlugin, MethodCallHandler ,ActivityAware{
  public static final int REQUEST_READ_CONTACTS = 79;
  /// The MethodChannel that will the communication between Flutter and native Android
  ///
  /// This local reference serves to register the plugin with the Flutter Engine and unregister it
  /// when the Flutter Engine is detached from the Activity
  private MethodChannel channel;
  private Context context;
  private Activity activity;

  @Override
  public void onAttachedToEngine(@NonNull FlutterPluginBinding flutterPluginBinding) {
    channel = new MethodChannel(flutterPluginBinding.getBinaryMessenger(), "getcontactsAmit");
    channel.setMethodCallHandler(this);
    context = flutterPluginBinding.getApplicationContext();

  }

  @Override
  public void onMethodCall(@NonNull MethodCall call,@NonNull Result result) {
    if (call.method.equals("getPlatformVersion")) {
      result.success("Android " + Build.ID);
    }
    else if (call.method.equals("getContactList")) {
      Log.e("mPlugin","Pass");
      //Log.e("mPlugin",getAllContacts().toString());
      ArrayList<HashMap> nList = getAllContacts();
      /*
      ArrayList<String> nList = new ArrayList<>();
      nList.add("Amit");
      nList.add("Sumit");
      nList.add("Aman");
      nList.add("Sam");
       */
      result.success(nList);
    }
    else {
      result.notImplemented();
    }
  }

  @Override
  public void onDetachedFromEngine(@NonNull FlutterPluginBinding binding) {
    channel.setMethodCallHandler(null);
  }

  public ArrayList<HashMap> getAllContacts() {
    ArrayList<HashMap> nameList = new ArrayList<>();
    //Log.e("mPlugin",context.toString());
    //Log.e("mPlugin",activity.toString());
    ContentResolver cr = context.getContentResolver();
    Cursor cur = cr.query(ContactsContract.Contacts.CONTENT_URI,
            null, null, null, null);
    if ((cur != null ? cur.getCount() : 0) > 0) {
      while (cur != null && cur.moveToNext()) {
        String id = cur.getString(
                cur.getColumnIndex(ContactsContract.Contacts._ID));
        //String name = cur.getString(cur.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
        //String name = cur.getString(cur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
        //nameList.add(name);
        if (cur.getInt(cur.getColumnIndex( ContactsContract.Contacts.HAS_PHONE_NUMBER)) > 0) {
          Cursor pCur = cr.query(
                  ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                  null,
                  ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?",
                  new String[]{id}, null);
          while (pCur.moveToNext()) {
            String phoneNo = pCur.getString(pCur.getColumnIndex(
                    ContactsContract.CommonDataKinds.Phone.NUMBER));
            String name = cur.getString(cur.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
            HashMap<String,String> map = new HashMap<>();
            map.put("Name",name);
            map.put("Phone",phoneNo);
            nameList.add(map);
          }
          pCur.close();
        }
      }
    }
    if (cur != null) {
      cur.close();
    }
    return nameList;
  }

  @Override
  public void onAttachedToActivity(@NonNull ActivityPluginBinding binding) {
    Log.e("mPlugin","Activity Binded");
    Log.e("mPlugin",binding.getActivity().toString());

  }

  @Override
  public void onDetachedFromActivityForConfigChanges() {

  }

  @Override
  public void onReattachedToActivityForConfigChanges(@NonNull ActivityPluginBinding binding) {

  }

  @Override
  public void onDetachedFromActivity() {

  }
}

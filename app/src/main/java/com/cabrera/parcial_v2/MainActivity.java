package com.cabrera.parcial_v2;

import android.Manifest;
import android.app.SearchManager;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.database.Cursor;
import android.os.Build;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.Menu;
import android.view.MenuInflater;
import android.widget.SearchView;
import android.widget.Toast;

import com.cabrera.parcial_v2.Adapter.ViewPagerAdapter;
import com.cabrera.parcial_v2.Frament.ContactFragment_Click;
import com.cabrera.parcial_v2.Frament.Contacto_RecyclerView;
import com.cabrera.parcial_v2.Modelo.Contacto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity implements Serializable {
    private static final int PERMISSIONS_REQUEST_READ_CONTACTS = 100;
    public  ArrayList<Contacto> contactos = new ArrayList<>();
    private ViewPagerAdapter viewPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Intent intent = this.getIntent();


        if(savedInstanceState!=null){
            contactos = (ArrayList<Contacto>) savedInstanceState.getSerializable("CONTACT_ADAPTER");
            viewPagerAdapter= (ViewPagerAdapter) savedInstanceState.getSerializable("VIEW_PAGER");
        }
        if (contactos.size() == 0) {
            showContacts();
        }
        else if(intent !=null){
            Contacto C = intent.getParcelableExtra("ADDED");
            if(C!=null){
                contactos.add(0,C);
                getIntent().removeExtra("ADDED");
            }
        }

        FragmentManager valueof = getSupportFragmentManager();
        if(viewPagerAdapter==null)
            viewPagerAdapter = new ViewPagerAdapter(valueof);
        ViewPager viewPager = findViewById(R.id.Pager);
        viewPager.setAdapter(viewPagerAdapter);


        viewPagerAdapter.addFragment(Contacto_RecyclerView.newInstance(viewPagerAdapter, 0,contactos), getString(R.string.normal));
        viewPagerAdapter.addFragment(Contacto_RecyclerView.newInstance(viewPagerAdapter, 1,contactos), getString(R.string.favorites));


        TabLayout tabLayout = findViewById(R.id.tabLayout);
        tabLayout.setupWithViewPager(viewPager);

        handleIntent(getIntent());

        if(getResources().getConfiguration().orientation== Configuration.ORIENTATION_LANDSCAPE){
            FragmentTransaction ft = valueof.beginTransaction();
            // Replace the contents of the container with the new fragment
            ft.replace(R.id.fragmentHorizontal, new ContactFragment_Click());
            // Complete the changes added above
            ft.commit();
        }
        else{

        }

    }


    private void showContacts() {
        // Check the SDK version and whether the permission is already granted or not.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && checkSelfPermission(Manifest.permission.READ_CONTACTS)
                != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.READ_CONTACTS}, PERMISSIONS_REQUEST_READ_CONTACTS);
        } else {
            addContacts();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                           int[] grantResults) {
        if (requestCode == PERMISSIONS_REQUEST_READ_CONTACTS) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                showContacts();
            } else {
                Toast.makeText(this, R.string.GrantPhone, Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void addContacts() {
        try {
            ContentResolver cr = getContentResolver();
            Cursor general = cr.query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null);
            //contactos.add(new Contact("MONKA", "2342-2323", null, "zulul@u.ganda", false, "Test"));
            //contactos.add(new Contact("MONKAOMEGA", "2334-2323", null, "jose@b.ig", true, "Test"));
            while (general.moveToNext()) {
                String id = general.getString(general.getColumnIndex(ContactsContract.Contacts._ID));

                String name = general.getString(general.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
                String phoneNumber = "";
                String type = ReturnString(general.getColumnIndex(ContactsContract.CommonDataKinds.Phone.TYPE));
                String profile = general.getString(general.getColumnIndex(ContactsContract.CommonDataKinds.Phone.PHOTO_URI));
                String thumbnailUri = general.getString(general.getColumnIndex(ContactsContract.CommonDataKinds.Phone.PHOTO_THUMBNAIL_URI));
                String email = "";
                profile = thumbnailUri != null ? thumbnailUri : profile;

                if (Integer.parseInt(general.getString(general.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER))) > 0) {
                    Cursor pCur = cr.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?", new String[]{id}, null);
                    while (pCur.moveToNext()) {
                        phoneNumber = pCur.getString(pCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                    }
                    pCur.close();
                }

                Cursor emailCur = cr.query(ContactsContract.CommonDataKinds.Email.CONTENT_URI, null, ContactsContract.CommonDataKinds.Email.CONTACT_ID + " = ?", new String[]{id}, null);
                while (emailCur.moveToNext()) {
                    email = emailCur.getString(emailCur.getColumnIndex(ContactsContract.CommonDataKinds.Email.DATA));
                }
                emailCur.close();



                contactos.add(new Contacto(name,phoneNumber,profile,email,false,type));
            }
            general.close();

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.searchable, menu);

        // Associate searchable configuration with the SearchView
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.search).getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        //Button

        return true;
    }

    @Override
    protected void onNewIntent(Intent intent) {
        handleIntent(intent);
    }

    private void handleIntent(Intent intent) {
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            Pattern p = Pattern.compile(query);
            Matcher m;
            for (Contacto c : contactos) {
                m = p.matcher(c.getName());
                if (m.find()) {
                    c.setSearched(true);
                }
            }
            viewPagerAdapter.setSearched(true);
            viewPagerAdapter.notifyDataSetChanged();
        }
    }

    private String ReturnString(int type) {
        switch (type) {
            case ContactsContract.CommonDataKinds.Phone.TYPE_HOME:
                return getString(R.string.HomeReturn);
            case ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE:
                return getString(R.string.MobileReturn);
            case ContactsContract.CommonDataKinds.Phone.TYPE_WORK:
                return getString(R.string.WorkReturn);
            case ContactsContract.CommonDataKinds.Phone.TYPE_FAX_HOME:
                return getString(R.string.HomeFaxReturn);
            case ContactsContract.CommonDataKinds.Phone.TYPE_FAX_WORK:
                return getString(R.string.WorkFaxReturn);
            case ContactsContract.CommonDataKinds.Phone.TYPE_MAIN:
                return getString(R.string.MainReturn);
            case ContactsContract.CommonDataKinds.Phone.TYPE_OTHER:
                return getString(R.string.OtherReturn);
            case ContactsContract.CommonDataKinds.Phone.TYPE_CUSTOM:
                return getString(R.string.CustomReturn);
            case ContactsContract.CommonDataKinds.Phone.TYPE_PAGER:
                return getString(R.string.PagerReturn);
            case ContactsContract.CommonDataKinds.Phone.TYPE_ASSISTANT:
                return getString(R.string.AssistantReturn);
            case ContactsContract.CommonDataKinds.Phone.TYPE_CALLBACK:
                return getString(R.string.CallBackReturn);
            case ContactsContract.CommonDataKinds.Phone.TYPE_CAR:
                return getString(R.string.CarReturn);
            case ContactsContract.CommonDataKinds.Phone.TYPE_COMPANY_MAIN:
                return getString(R.string.CompanyReturn);
            case ContactsContract.CommonDataKinds.Phone.TYPE_ISDN:
                return "ISDN";
            case ContactsContract.CommonDataKinds.Phone.TYPE_MMS:
                return "MMS";
            case ContactsContract.CommonDataKinds.Phone.TYPE_OTHER_FAX:
                return "Other Fax";
            case ContactsContract.CommonDataKinds.Phone.TYPE_RADIO:
                return "Radio";
            case ContactsContract.CommonDataKinds.Phone.TYPE_TELEX:
                return "Telex";
            case ContactsContract.CommonDataKinds.Phone.TYPE_TTY_TDD:
                return "TTY TDD";
            case ContactsContract.CommonDataKinds.Phone.TYPE_WORK_MOBILE:
                return getString(R.string.WorkMobileReturn);
            case ContactsContract.CommonDataKinds.Phone.TYPE_WORK_PAGER:
                return getString(R.string.WorkPagerReturn);
            default:
                return getString(R.string.CustomReturn);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable("CONTACT_ADAPTER",contactos);
        outState.putSerializable("VIEW_PAGER",viewPagerAdapter);
    }
}


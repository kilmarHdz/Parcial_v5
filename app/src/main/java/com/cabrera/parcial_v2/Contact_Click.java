package com.cabrera.parcial_v2;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import com.cabrera.parcial_v2.Frament.ContactFragment_Click;

import java.io.Serializable;

public class Contact_Click extends AppCompatActivity implements Serializable {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clicked_contact);

        // Begin the transaction
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        // Replace the contents of the container with the new fragment
        ft.add(R.id.FragmentContact, new ContactFragment_Click());
        // Complete the changes added above
        ft.commitAllowingStateLoss();



    }

}

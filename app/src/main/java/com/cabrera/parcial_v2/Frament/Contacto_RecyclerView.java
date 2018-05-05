package com.cabrera.parcial_v2.Frament;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cabrera.parcial_v2.Adapter.ContactoAdapter;
import com.cabrera.parcial_v2.Adapter.ViewPagerAdapter;
import com.cabrera.parcial_v2.Modelo.Contacto;
import com.cabrera.parcial_v2.R;

import java.io.Serializable;
import java.util.ArrayList;


public class Contacto_RecyclerView extends Fragment implements Serializable {
    private ViewPagerAdapter vpa;
    private transient RecyclerView rv;
    private transient FloatingActionButton plus;
    private ContactoAdapter adapter;
    private transient GridLayoutManager manager;
    private ArrayList<Contacto> contactos;
    private int type;

    public static Contacto_RecyclerView newInstance(ViewPagerAdapter vpa,int type,ArrayList<Contacto> contactos){
        Contacto_RecyclerView fragment = new Contacto_RecyclerView();
        fragment.vpa = vpa;
        fragment.type = type;
        fragment.contactos = contactos;
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_contact_recycler, container, false);

        rv = v.findViewById(R.id.recycler);
        rv.setHasFixedSize(true);


        adapter = new ContactoAdapter(container.getContext(), vpa, type,contactos);

        manager = new GridLayoutManager(container.getContext(),3);

        rv.setLayoutManager(manager);
        rv.setAdapter(adapter);

        plus = v.findViewById(R.id.AddRecycler);

        plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(v.getContext(),AddContacto_Fragment.class);
                i.putExtra("KEY", adapter.getListaContacto());
                v.getContext().startActivity(i);
            }
        });

        return v;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            getFragmentManager().beginTransaction().detach(this).attach(this).commit();
        }
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

}


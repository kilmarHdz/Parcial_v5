package com.cabrera.parcial_v2.Frament;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.cabrera.parcial_v2.Modelo.Contacto;
import com.cabrera.parcial_v2.R;

import java.io.Serializable;

public class ContactFragment_Click extends Fragment implements Serializable {
    ImageView Profile;
    TextView Name;
    TextView Number;
    TextView Email;
    TextView Type;
    ImageButton Call;
    ImageButton Share;
    String path;
    private static final int PERMISSIONS_REQUEST_PHONE_CALL = 101;
    Contacto C;

    public ContactFragment_Click() {
        // Required empty public constructor
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        Profile = getActivity().findViewById(R.id.ImageClicked);
        Name = getActivity().findViewById(R.id.NameClicked);
        Number = getActivity().findViewById(R.id.PhoneClicked);
        Type = getActivity().findViewById(R.id.TypeClicked);
        Email = getActivity().findViewById(R.id.EmailClicked);
        Call = getActivity().findViewById(R.id.SendCall);
        Share = getActivity().findViewById(R.id.Share);

        if (C != null && Profile!= null) {
            if (C.getImageUri() == null) {
                path = "";
                Profile.setImageResource(R.drawable.ic_account_circle_black_36dp);
            } else {
                path = C.getImageUri();
                Profile.setImageURI(Uri.parse(C.getImageUri()));
            }

            Name.setText(C.getName());
            Number.setText(C.getNumber());
            Type.setText(C.getType());
            Email.setText(C.getEmail());
            Call.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(Intent.ACTION_CALL);
                    intent.setData(Uri.parse("tel:" + Number.getText().toString().replace("-", "")));
                    if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CALL_PHONE}, PERMISSIONS_REQUEST_PHONE_CALL);
                    } else startActivity(intent);
                }
            });

            Share.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Uri imageUri;
                    if (path == "")
                        imageUri = Uri.parse("android.resource://com.example.alexbig.parcialpdm1/drawable/ic_account_circle_black_36dp");
                    else
                        imageUri = Uri.parse(path);

                    Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);
                    emailIntent.setType("image/*");
                    emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, getString(R.string.ShareFrag));
                    emailIntent.putExtra(android.content.Intent.EXTRA_TEXT, getString(R.string.NameFrag) + Name.getText() + "\n" +
                            getString(R.string.PhoneFrag) + Number.getText() + "\n" + getString(R.string.EmailFrag) + Email.getText());
                    emailIntent.putExtra(Intent.EXTRA_STREAM, imageUri);
                    startActivity(Intent.createChooser(emailIntent, getString(R.string.Sending)));
                }
            });
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        super.onViewCreated(container, savedInstanceState);

        Intent intent = getActivity().getIntent();

        if (savedInstanceState != null) {
            C = (Contacto)savedInstanceState.getSerializable("KEY");
        }
        if (intent.hasExtra("KEY"))
            C = (Contacto)intent.getExtras().getSerializable("KEY");




        return inflater.inflate(R.layout.fragment_clicked_contact, container, false);
    }
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable("KEY", C);
    }


}

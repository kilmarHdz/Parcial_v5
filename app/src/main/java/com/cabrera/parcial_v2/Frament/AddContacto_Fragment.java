package com.cabrera.parcial_v2.Frament;


import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.cabrera.parcial_v2.MainActivity;
import com.cabrera.parcial_v2.Modelo.Contacto;
import com.cabrera.parcial_v2.R;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.Serializable;

public class AddContacto_Fragment extends AppCompatActivity implements Serializable {
    private Spinner TypeSelected;
    private ImageView Profilepic;
    private EditText Name;
    private EditText Number;
    private EditText Email;
    private int RESULT_LOAD_IMG = 1;
    private FloatingActionButton Upload;
    private Button Insert;
    private Uri ImageUriU;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_contact);

        TypeSelected = findViewById(R.id.TypeAdd);
        Profilepic = findViewById(R.id.ImageAdd);
        Name = findViewById(R.id.NameAdd);
        Number = findViewById(R.id.NumberAdd);
        Email = findViewById(R.id.EmailAdd);
        Upload = findViewById(R.id.UploadAdd);
        Insert = findViewById(R.id.InsertNewC);

        ImageUriU = Uri.parse("android.resource://com.example.alexbig.parcialpdm1/" + R.drawable.ic_account_circle_black_36dp);

        Upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                photoPickerIntent.setType("image/*");
                startActivityForResult(photoPickerIntent, RESULT_LOAD_IMG);
            }
        });
        Insert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(),MainActivity.class);
                intent.putExtra("ADDED", new Contacto(Name.getText().toString(),
                        Number.getText().toString(),ImageUriU+""
                        ,Email.getText().toString(),false,
                        TypeSelected.getSelectedItem().toString()));
                v.getContext().startActivity(intent);
            }
        });
    }
    @Override
    protected void onActivityResult(int reqCode, int resultCode, Intent data) {
        super.onActivityResult(reqCode, resultCode, data);


        if (resultCode == RESULT_OK) {
            try {
                final Uri imageUri = data.getData();
                ImageUriU = imageUri;
                final InputStream imageStream = getContentResolver().openInputStream(imageUri);
                final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                Profilepic.setImageBitmap(selectedImage);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                Toast.makeText(this, R.string.SomeWrong, Toast.LENGTH_LONG).show();
            }

        }else {
            Toast.makeText(this, R.string.PickImage,Toast.LENGTH_LONG).show();
        }
    }
}


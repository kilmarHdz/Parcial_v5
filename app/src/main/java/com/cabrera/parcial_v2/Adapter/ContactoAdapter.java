package com.cabrera.parcial_v2.Adapter;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;

import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.cabrera.parcial_v2.Contact_Click;
import com.cabrera.parcial_v2.MainActivity;
import com.cabrera.parcial_v2.Modelo.Contacto;
import com.cabrera.parcial_v2.R;

import java.io.Serializable;
import java.util.ArrayList;

public class ContactoAdapter extends RecyclerView.Adapter<ContactoAdapter.ContactViewHolder> implements Serializable {
    private Context mContext;
    private ViewPagerAdapter vpa;
    private ArrayList<Contacto> ListaContacto=new ArrayList<>();
    private int type;

    public ContactoAdapter(Context mContext, ViewPagerAdapter vpa, int tipo, ArrayList<Contacto> contactos)  {
        this.mContext = mContext;
        this.vpa = vpa;
        type = tipo;
        if(vpa.isSearched())
            type+=2;
        switch (type) {
            //Favorite
            case 1:
                for (Contacto C : contactos) {
                    if (C.isFav())
                        ListaContacto.add(C);
                }
                break;
            //Search
            case 2:
                for (Contacto C : contactos) {
                    if (C.isSearched())
                        ListaContacto.add(C);
                }
                break;
            //Favorite & Search
            case 3:
                for (Contacto C : contactos) {
                    if (C.isSearched() && C.isFav())
                        ListaContacto.add(C);
                }
                break;
            default:
                for (Contacto C : contactos) {
                    ListaContacto.add(C);
                }
                break;
        }

    }

    @Override
    public void onBindViewHolder(final ContactViewHolder holder, final int position) {
        holder.favoriteImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Contacto C = ListaContacto.get(holder.getAdapterPosition());
                if (!C.isFav()) {
                    C.setFav(true);
                    vpa.notifyDataSetChanged();
                }else{
                    C.setFav(false);
                    vpa.notifyDataSetChanged();
                }
            }
        });
        holder.CardContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(v.getResources().getConfiguration().orientation != Configuration.ORIENTATION_LANDSCAPE){
                    Intent i = new Intent(v.getContext(),Contact_Click.class);
                    Bundle arguments = new Bundle();
                    arguments.putSerializable( "KEY", ListaContacto.get(position));
                    i.putExtras(arguments);
                    mContext.startActivity(i);

                }
                else{
                    Intent i = new Intent(v.getContext(),MainActivity.class);
                    Bundle arguments = new Bundle();
                    arguments.putSerializable( "KEY", ListaContacto.get(position));

                }
            }
        });
        holder.nameTextView.setText(ListaContacto.get(position).getName());

        if(ListaContacto.get(position).getImageUri()==null)
            holder.imgImageView.setImageResource(R.drawable.ic_account_circle_black_36dp);
        else
            holder.imgImageView.setImageURI(Uri.parse(ListaContacto.get(position).getImageUri()));

        if(ListaContacto.get(position).isFav()) holder.favoriteImageButton.setImageResource(R.drawable.ic_favorite_black_18dp);
        else holder.favoriteImageButton.setImageResource(R.drawable.ic_favorite_border_black_18dp);



    }

    @Override
    public int getItemCount() {
        return ListaContacto.size();
    }

    public ContactViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View v = inflater.inflate(R.layout.activity_contact_card, parent, false);
        return new ContactViewHolder(v);
    }
    protected class ContactViewHolder extends RecyclerView.ViewHolder{
        TextView nameTextView;
        ImageView imgImageView;
        ImageButton favoriteImageButton;
        CardView CardContact;

        public ContactViewHolder(View itemView) {
            super(itemView);
            CardContact = itemView.findViewById(R.id.CardContact);
            nameTextView = itemView.findViewById(R.id.nameCard);
            imgImageView = itemView.findViewById(R.id.profilePicture);
            favoriteImageButton = itemView.findViewById(R.id.favoriteButton);
        }
    }
    public void SetItems(ArrayList<Contacto> items){
        ListaContacto = items;
    }

    public ArrayList<Contacto> getListaContacto() {
        return ListaContacto;
    }
}

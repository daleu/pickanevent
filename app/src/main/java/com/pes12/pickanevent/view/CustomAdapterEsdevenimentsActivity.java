package com.pes12.pickanevent.view;

/**
 * Created by Jou on 21/10/2016.
 */

import com.pes12.pickanevent.R;
import com.pes12.pickanevent.business.Info;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

class CustomAdapterEsdevenimentsActivity extends ArrayAdapter<Info>  {

    public CustomAdapterEsdevenimentsActivity(Context context, ArrayList<Info> info) {
        super(context, R.layout.lista_esdeveniments, info);
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View customView = inflater.inflate(R.layout.lista_esdeveniments, parent, false);

        Bitmap img = getItem(position).img;
        String nombreEsdv = getItem(position).primeraLinea;
        String fechaEsdv = getItem(position).segonaLinea;

        TextView nombreE = (TextView) customView.findViewById(R.id.editTextNombreEsdv);
        TextView fechaE = (TextView) customView.findViewById(R.id.editTextDataEsdevniment);
        ImageView imagenE = (ImageView) customView.findViewById(R.id.imageViewEsdv);


        nombreE.setText(nombreEsdv);
        fechaE.setText(fechaEsdv);
        imagenE.setImageBitmap(img);//Cambiar por imagenes de los esdeveniments

        return customView;
    }
}

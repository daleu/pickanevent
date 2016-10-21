package com.pes12.pickanevent.view;

/**
 * Created by Jou on 21/10/2016.
 */

import com.pes12.pickanevent.R;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

class CustomAdapterEsdeveniments extends ArrayAdapter<String>  {
    public CustomAdapterEsdeveniments(Context context, String[] nombres) {
        super(context, R.layout.lista_esdeveniments, nombres);
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View customView = inflater.inflate(R.layout.lista_esdeveniments, parent, false);

        String nombreEsdv = getItem(position);
        String fechaEsdv = null;
        String imagenEsdv = null;

        TextView nombreE = (TextView) customView.findViewById(R.id.editTextNombreEsdv);
        TextView fechaE = (TextView) customView.findViewById(R.id.editTextDataEsdevniment);
        ImageView imagenE = (ImageView) customView.findViewById(R.id.imageViewEsdv);


        nombreE.setText(nombreEsdv);
        fechaE.setText(fechaEsdv);
        imagenE.setImageResource(R.drawable.oso); //Cambiar por imagenes de los esdeveniments

        return customView;
    }
}

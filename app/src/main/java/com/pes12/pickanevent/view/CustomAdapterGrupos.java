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

import java.util.List;


class CustomAdapterGrupos extends ArrayAdapter<Info> {

    public CustomAdapterGrupos(Context context, List<Info> info) {
        super(context, R.layout.lista_grupos, info);
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View customView = inflater.inflate(R.layout.lista_grupos, parent, false);

        String nombreGrupo = getItem(position).primeraLinea;
        Bitmap img = getItem(position).img;

        TextView nombreG = (TextView) customView.findViewById(R.id.editTextNombreGrupo);
        ImageView imagenG = (ImageView) customView.findViewById(R.id.imageViewGrupo);

        nombreG.setText(nombreGrupo);
        imagenG.setImageBitmap(img);; //Cambiar por imagenes de los grupos

        return customView;
    }
}

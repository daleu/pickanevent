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


class CustomAdapterGrupos extends ArrayAdapter<String> {

    public CustomAdapterGrupos(Context context, String[] info) {
        super(context, R.layout.lista_grupos, info);
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View customView = inflater.inflate(R.layout.lista_grupos, parent, false);

        String nombreGrupo = getItem(position);
        TextView nombreG = (TextView) customView.findViewById(R.id.editTextNombreGrupo);
        ImageView imagenG = (ImageView) customView.findViewById(R.id.imageViewGrupo);

        nombreG.setText(nombreGrupo);
        imagenG.setImageResource(R.drawable.oso); //Cambiar por imagenes de los grupos

        return customView;
    }
}

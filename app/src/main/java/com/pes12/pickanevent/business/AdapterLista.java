package com.pes12.pickanevent.business;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.pes12.pickanevent.R;

import java.util.ArrayList;

/**
 * Created by Clara on 20/10/2016.
 */

public class AdapterLista extends ArrayAdapter<Info> {

    // Info datos[];
    ArrayList<Info> infos;
    int layoutId;
    private Context context;


    public AdapterLista(Context _context, int _layoutId, ArrayList<Info> _info) {
        super(_context, _layoutId, _info);
        this.context = _context;
        this.layoutId = _layoutId;
        infos = _info;

    }

    @Override
    public int getCount() {
        return infos.size();
    }


    @Override
    public long getItemId(int _position) {
        return _position;
    }

    @Override
    public Info getItem(int _position) {
        return infos.get(_position);
    }

    @Override
    public View getView(int _position, View _convertView, ViewGroup _parent) {
        AdapterHolder holder;
        View vistaEvent = _convertView;
        if (vistaEvent == null) {
            LayoutInflater inf = ((Activity) context).getLayoutInflater();
            vistaEvent = inf.inflate(layoutId, _parent, false);

            holder = new AdapterHolder();
            holder.img = (ImageView) vistaEvent.findViewById(R.id.icon);
            holder.linea1 = (TextView) vistaEvent.findViewById(R.id.text1);
            holder.linea2 = (TextView) vistaEvent.findViewById(R.id.text2);
            holder.button = (Button) vistaEvent.findViewById(R.id.button);

            vistaEvent.setTag(holder);
        } else {
            holder = (AdapterHolder) vistaEvent.getTag();
        }

        Info componentes = getItem(_position);
        holder.img.setImageBitmap(componentes.img);
        holder.linea1.setText(componentes.primeraLinea);
        holder.linea2.setText(componentes.segonaLinea);
        holder.button.setText(componentes.textoBoton);
        return vistaEvent;
    }

    static class AdapterHolder {
        ImageView img;
        TextView linea1;
        TextView linea2;
        Button button;
    }

}

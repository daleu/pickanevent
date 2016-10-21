package com.pes12.pickanevent.business;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.pes12.pickanevent.R;

/**
 * Created by Clara on 20/10/2016.
 */

public class AdapterListaEventos extends ArrayAdapter<Info> {

    Info datos[];
    int layoutId;
    private Context context;



    public AdapterListaEventos(Context context, int layoutId, Info[] info) {
        super(context, layoutId, info);
        this.context = context;
        this.layoutId = layoutId;
        datos = info;

    }

    @Override
    public int getCount() {
        return datos.length;
    }


    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        AdapterHolder holder;
        View vistaEvent =convertView;
        if (vistaEvent == null) {
            LayoutInflater inf = ((Activity)context).getLayoutInflater();
            vistaEvent = inf.inflate(layoutId, parent, false);

            holder = new AdapterHolder();
            holder.img = (ImageView)vistaEvent.findViewById(R.id.icon);
            holder.l1 = (TextView)vistaEvent.findViewById(R.id.text1);
            holder.l2 = (TextView)vistaEvent.findViewById(R.id.text2);

            vistaEvent.setTag(holder);
        }
        else {
            holder = (AdapterHolder)vistaEvent.getTag();
        }

        Info componentes = datos[position];
        holder.img.setImageBitmap(componentes.img);
        holder.l1.setText(componentes.primeraLinea);
        holder.l2.setText(componentes.segonaLinea);
        return vistaEvent;
    }

    static class AdapterHolder {
        ImageView img;
        TextView l1;
        TextView l2;
    }

}

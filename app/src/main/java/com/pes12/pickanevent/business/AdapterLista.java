package com.pes12.pickanevent.business;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.pes12.pickanevent.R;
import com.pes12.pickanevent.view.BaseActivity;
import com.pes12.pickanevent.view.EditarEventoActivity;
import com.pes12.pickanevent.view.VerInfoEventoActivity;
import com.pes12.pickanevent.view.VerInfoGrupoActivity;
import com.pes12.pickanevent.view.VerInfoOtroUsuarioActivity;
import com.squareup.picasso.Picasso;

import android.content.Context;
import org.w3c.dom.Text;

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
            holder.lay = (LinearLayout) vistaEvent.findViewById(R.id.layoutAdapter);
            holder.id = (TextView) vistaEvent.findViewById(R.id.id);
            holder.tipus = (TextView) vistaEvent.findViewById(R.id.tipus);

            vistaEvent.setTag(holder);
        } else {
            holder = (AdapterHolder) vistaEvent.getTag();
        }

        final Info componentes = getItem(_position);
        Picasso.with(context).load(componentes.img).into(holder.img);
       // holder.img.setImageBitmap(componentes.img);
        holder.linea1.setText(componentes.primeraLinea);
        holder.linea2.setText(componentes.segonaLinea);
        holder.button.setText(componentes.textoBoton);
        final String aux = componentes.textoBoton;
        if(componentes.id!=null){
            holder.id.setText(componentes.id);
        }
        if(componentes.tipus!=null){
            holder.tipus.setText(componentes.tipus);
        }

        holder.lay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Log.e("tipus",componentes.tipus);
                if(componentes.tipus=="event") {
                    Intent intent = new Intent(context, VerInfoEventoActivity.class);
                    intent.putExtra("key", componentes.id);
                    context.startActivity(intent);
                }
                else if(componentes.tipus=="usuari") {
                    Intent intent = new Intent(context, VerInfoOtroUsuarioActivity.class);
                    intent.putExtra("key", componentes.id);
                    context.startActivity(intent);
                }
                else if(componentes.tipus=="grup") {
                    Intent intent = new Intent(context, VerInfoGrupoActivity.class);
                    intent.putExtra("key", componentes.id);
                    context.startActivity(intent);
                }
            }
        });

        holder.button.setOnClickListener(new View.OnClickListener(){ //FALTA FER QUE CONSTI!!!!!!!!!
            public void onClick(View v) {
                // TODO Auto-generated method stub
                //Log.e("tipus",componentes.tipus);
                Log.e("action",aux);
                Log.e("teoric", getContext().getResources().getString(R.string.DEFAULT_EDITAR_GRUPO));
                if(componentes.tipus=="event" && aux.equals(getContext().getResources().getString(R.string.DEFAULT_NO_ASSISTIR))) { //ASSISTIR EVENT
                    Intent intent = new Intent(context, VerInfoEventoActivity.class);
                    intent.putExtra("action","assistir");
                    intent.putExtra("key", componentes.id);
                    context.startActivity(intent);
                }
                else if(componentes.tipus=="event" && aux.equals(getContext().getResources().getString(R.string.DEFAULT_EDITAR_EVENTO))){//EDITAR GRUP
                    Intent intent = new Intent(context, EditarEventoActivity.class);
                    intent.putExtra("key", componentes.id);
                    context.startActivity(intent);
                }
                else if(componentes.tipus=="event" && !aux.equals(getContext().getResources().getString(R.string.DEFAULT_NO_ASSISTIR))){//DEIXAR DASSISTIR EVENT
                    Intent intent = new Intent(context, VerInfoEventoActivity.class);
                    intent.putExtra("action","noassistir");
                    intent.putExtra("key", componentes.id);
                    context.startActivity(intent);
                }
                else if(componentes.tipus=="usuari") {
                    Intent intent = new Intent(context, VerInfoOtroUsuarioActivity.class);
                    intent.putExtra("action","noseguir");
                    intent.putExtra("key", componentes.id);
                    context.startActivity(intent);
                }
                else if(componentes.tipus=="grup" && aux.equals(getContext().getResources().getString(R.string.DEFAULT_NO_SEGUIR))) {
                    Intent intent = new Intent(context, VerInfoGrupoActivity.class);
                    intent.putExtra("action","noseguir");
                    intent.putExtra("key", componentes.id);
                    context.startActivity(intent);
                }
                else if(componentes.tipus=="grup" && aux.equals(getContext().getResources().getString(R.string.DEFAULT_EDITAR_GRUPO))) {
                    Intent intent = new Intent(context, VerInfoGrupoActivity.class);
                    intent.putExtra("key", componentes.id);
                    context.startActivity(intent);
                }
            }
        });

        return vistaEvent;
    }

    static class AdapterHolder {
        ImageView img;
        TextView linea1;
        TextView linea2;
        Button button;
        LinearLayout lay;
        TextView id;
        TextView tipus;
    }

}

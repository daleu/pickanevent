package com.pes12.pickanevent.business;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.pes12.pickanevent.R;

import java.util.ArrayList;

/**
 * Created by usuario on 24/11/2016.
 */

public class AdapterTags extends ArrayAdapter<String> {

    // Info datos[];
    ArrayList<String> tags;
    int layoutId;
    private Context context;



    public AdapterTags(Context _context, int _layoutId, ArrayList<String> _tags) {
        super(_context, _layoutId, _tags);
        this.context = _context;
        this.layoutId = _layoutId;
        tags = _tags;

    }

    @Override
    public int getCount() {
        return tags.size();
    }


    @Override
    public long getItemId(int _position) {
        return _position;
    }

    @Override
    public String getItem(int _position) {
        return tags.get(_position);
    }

    @Override
    public View getView(int _position, View _convertView, ViewGroup _parent) {
        AdapterHolder holder;
        View vistaTags =_convertView;
        if (vistaTags == null) {
            LayoutInflater inf = ((Activity)context).getLayoutInflater();
            vistaTags = inf.inflate(layoutId, _parent, false);

            holder = new AdapterHolder();
            holder.nombreTag = (TextView)vistaTags.findViewById(R.id.nombre);

            vistaTags.setTag(holder);
        }
        else {
            holder = (AdapterHolder)vistaTags.getTag();
        }

        String componentes = getItem(_position);
        holder.nombreTag.setText(componentes);
        return vistaTags;
    }

    static class AdapterHolder {
        TextView nombreTag;
    }

}

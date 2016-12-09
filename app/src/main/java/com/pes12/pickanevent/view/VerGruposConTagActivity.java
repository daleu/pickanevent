package com.pes12.pickanevent.view;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.widget.ListView;

import com.pes12.pickanevent.R;
import com.pes12.pickanevent.business.Info;
import com.pes12.pickanevent.business.MGRFactory;
import com.pes12.pickanevent.business.Tag.TagMGR;
import com.pes12.pickanevent.persistence.entity.Tag.TagEntity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class VerGruposConTagActivity extends BaseActivity {

    ListView listaGrupos;
    ArrayList<Info> grupos;

    TagMGR tMGR;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ver_grupos_con_tag);

        //Progres dialog
        showProgressDialog();

        //Inicializaciones
        listaGrupos = (ListView) findViewById(R.id.ListaGruposConTag);
        grupos = new ArrayList<Info>();
        ///////////////////////////////////////////////////////////////////////

        Bundle b = getIntent().getExtras(); //Para pruebas
        Map<String,String> tagMap = new HashMap<String, String>();


        tMGR = MGRFactory.getInstance().getTagMGR();
        String tag = b.getString("nombreTag");
        tMGR.getInfoTagGrupos(this,tag);
    }

    public void mostrarGruposTag(TagEntity info){
        System.out.println("aaaaaaaaaaaaaaaaaaaaaaa  " + info);
        hideProgressDialog();
    }

    private Bitmap StringToBitMap(String encodedString) {
        try {
            byte[] encodeByte = Base64.decode(encodedString, Base64.DEFAULT);
            Bitmap bitmap = BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
            return bitmap;
        } catch (Exception e) {
            e.getMessage();
            return null;
        }
    }
}

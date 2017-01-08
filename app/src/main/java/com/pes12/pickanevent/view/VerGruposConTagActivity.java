package com.pes12.pickanevent.view;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ListView;

import com.pes12.pickanevent.R;
import com.pes12.pickanevent.business.AdapterLista;
import com.pes12.pickanevent.business.Grupo.GrupoMGR;
import com.pes12.pickanevent.business.Info;
import com.pes12.pickanevent.business.MGRFactory;
import com.pes12.pickanevent.business.Tag.TagMGR;
import com.pes12.pickanevent.persistence.entity.Grupo.GrupoEntity;
import com.pes12.pickanevent.persistence.entity.Tag.TagEntity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class VerGruposConTagActivity extends BaseActivity {

    ListView listaGrupos;
    ArrayList<Info> grupos;

    TagMGR tMGR;
    GrupoMGR gMGR;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ver_grupos_con_tag);

        ImageButton searchImage = (ImageButton) findViewById(R.id.searchact);
        if (searchImage!=null && getUsuarioActual().getCm()) searchImage.setVisibility(View.INVISIBLE);

        //Progres dialog
        showProgressDialog();

        //Inicializaciones
        listaGrupos = (ListView) findViewById(R.id.ListaGruposConTag);
        grupos = new ArrayList<Info>();
        ///////////////////////////////////////////////////////////////////////

        Bundle b = getIntent().getExtras(); //Para pruebas
        Map<String,String> tagMap = new HashMap<String, String>();


        tMGR = MGRFactory.getInstance().getTagMGR();
        gMGR = MGRFactory.getInstance().getGrupoMGR();
        String tag = b.getString("nombreTag");
        //System.out.println("aaaaaaaaaaaaaaaaaaaaaaa  " + tag);  Venganza

        tMGR.getInfoTagGrupos(this,tag);
    }

    public void mostrarGruposTag(TagEntity info){
        //System.out.println("aaaaaaaaaaaaaaaaaaaaaaa  " + info.getIdGrupos());
        for (Map.Entry<String, String> entry : info.getIdGrupos().entrySet())
        {
            System.out.println(entry.getKey() + "/" + entry.getValue());
            gMGR.getGruposByNombreTag(this,entry.getValue());
            //gMGR.getGruposB
        }
        //hideProgressDialog();
    }

    public void añadirGrupos(ArrayList<Info> info){
        for (int i=0; i < info.size(); ++i) {
            grupos.add(info.get(i));
        }
        AdapterLista ale = new AdapterLista(VerGruposConTagActivity.this, R.layout.vista_adapter_lista, grupos);
        listaGrupos.setAdapter(ale);
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

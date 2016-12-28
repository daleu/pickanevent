package com.pes12.pickanevent.view;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.pes12.pickanevent.R;
import com.pes12.pickanevent.business.AdapterLista;
import com.pes12.pickanevent.business.Evento.EventoMGR;
import com.pes12.pickanevent.business.Grupo.GrupoMGR;
import com.pes12.pickanevent.business.Info;
import com.pes12.pickanevent.business.MGRFactory;
import com.pes12.pickanevent.business.Tag.TagMGR;
import com.pes12.pickanevent.persistence.entity.Grupo.GrupoEntity;
import com.pes12.pickanevent.persistence.entity.Tag.TagEntity;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

import static com.pes12.pickanevent.R.id.Primero;
import static com.pes12.pickanevent.R.id.Tags;

public class VerInfoGrupoActivity extends BaseActivity {

    TextView nombre;
    TextView descripcion;
    ImageView foto;
    ListView eventos;
    Button boton;
    Button editarTags;

    String idGrupo;
    GrupoMGR gMGR;
    EventoMGR eMGR;
    TagMGR tMGR;
    Boolean cm = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ver_info_grupo);
        //////inicializacion elementos pantalla//////////
        nombre = (TextView) findViewById(R.id.nombreGrupo);
        descripcion = (TextView) findViewById(R.id.descripcion);
        foto = (ImageView) findViewById(R.id.imagenGrupo);
        //tags = (TextView) findViewById(R.id.tags);
        eventos = (ListView) findViewById(R.id.event);
        boton = (Button) findViewById(R.id.seguir);
        eMGR = MGRFactory.getInstance().getEventoMGR();
        gMGR = MGRFactory.getInstance().getGrupoMGR();
        tMGR = MGRFactory.getInstance().getTagMGR();
        editarTags = (Button) findViewById(R.id.editarTags);
        /////////////////////////////////////////////////

        Bundle b = getIntent().getExtras(); //Para pruebas
        cm = b.getBoolean("CM");
        //System.out.println("Valor CM "+ cm);

        Bundle param = getIntent().getExtras();
        idGrupo = "-KZlbhl-cvEnRUXKORqx";
        if(param.getString("key")!=null){
            idGrupo = param.getString("key");
        }

        showProgressDialog();

        gMGR.getInfoGrupo(this, idGrupo);

        if (!getUsuarioActual().getCm()) { //si no es com no vera el boton para editar tags
            editarTags.setVisibility(View.INVISIBLE);
        }


    }

    public void mostrarInfoGrupo(GrupoEntity _grupo) {

        if (_grupo.getIdEventos() != null) eMGR.getInfoEventosGrupo(this, _grupo.getIdEventos(), cm);
        Map<String, String> tagsMap = _grupo.getIdTags();
        if (tagsMap == null) tagsMap = new LinkedHashMap<>();
        tagsMap.put(_grupo.getidTagGeneral(), "blabla");
        tMGR.getInfoTag(this, tagsMap);

        nombre.setText(_grupo.getNombreGrupo());
        descripcion.setText(_grupo.getDescripcion());
        String texto = getString(R.string.DEFAULT_SEGUIR);
        if (cm) texto = getString(R.string.DEFAULT_EDITAR);
        boton.setText(texto);
        String img = _grupo.getImagen();
        Bitmap imgBM = StringToBitMap(img);
        foto.setImageBitmap(imgBM);
        foto.setScaleType(ImageView.ScaleType.FIT_XY);

        if (_grupo.getIdEventos() == null) hideProgressDialog();
    }

    public void mostrarEventosGrupo(ArrayList<Info> info) {
        AdapterLista ale = new AdapterLista(VerInfoGrupoActivity.this, R.layout.vista_adapter_lista, info);
        eventos.setAdapter(ale);
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

    public void mostrarTags(final ArrayList<String> info) {
        LinearLayout linearLayout = (LinearLayout) findViewById(Tags);
        TextView primero = (TextView) findViewById(Primero);
        if (info.size() > 0) {
            //int id = 0;
            primero.setText(info.get(0));
            primero.setPadding(3,2,3,2);
            primero.setTextColor(Color.rgb(100,100,100));
            primero.hasOnClickListeners();
            primero.setBackgroundColor(Color.rgb(130,255,130));
            //primero.setId(id);

            primero.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    startActivity(new Intent(VerInfoGrupoActivity.this, VerGruposConTagActivity.class).putExtra(TagEntity.ATTRIBUTES.NOMBRETAG.getValue(), info.get(0).toString()));
                }
            });

            for (int i = 1; i < info.size(); ++i) {
                TextView siguiente = new TextView(this);
                siguiente.setText(info.get(i));
                siguiente.setPadding(3, 2, 3, 2);
                siguiente.setTextColor(Color.rgb(100,100,100));
                siguiente.hasOnClickListeners();
                siguiente.setBackgroundColor(Color.rgb(130,255,130));
                //RelativeLayout.LayoutParams params1 = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                //params1.addRule(RelativeLayout.RIGHT_OF,id);
                //++id;
                //siguiente.setId(id);
                siguiente.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        startActivity(new Intent(VerInfoGrupoActivity.this, VerGruposConTagActivity.class).putExtra(TagEntity.ATTRIBUTES.NOMBRETAG.getValue(), info.get(0).toString()));
                    }
                });
                linearLayout.addView(siguiente);
            }
        }
        /*TextView test2 = new TextView(this);
        test2.setText("aaaaaaaaaaaaaaaaaaaaaa");
        test2.setPadding(3,3,6,3);
        linearLayout.addView(test2);
        TextView test3 = new TextView(this);
        test3.setText("aaaaaaaaaaaaaaaaaaaaaa");
        test3.setPadding(3,3,6,3);
        linearLayout.addView(test3);
        TextView test4 = new TextView(this);
        test4.setText("aaaaaaaaaaaaaaaaaaaaaa");
        test4.setPadding(3,3,6,3);
        linearLayout.addView(test4);*/
    }

    /*@Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        if ((keyCode == KeyEvent.KEYCODE_BACK))
        {
            finish();
        }
        return super.onKeyDown(keyCode, event);
    }*/

    public void editarTags(View view) {
        startActivity(new Intent(VerInfoGrupoActivity.this, IndicarTagsActivity.class).putExtra("idGrupo", idGrupo));
    }

    //se tiene que poner para evitar que al volver de la edicion de tags se quede bloqueado si poder volver hacia atras
    @Override
    public void onBackPressed() {
        startActivity(new Intent(VerInfoGrupoActivity.this, MainActivity.class));
    }
}

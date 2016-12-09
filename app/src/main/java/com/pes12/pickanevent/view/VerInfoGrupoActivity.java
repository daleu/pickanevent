package com.pes12.pickanevent.view;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.pes12.pickanevent.R;
import com.pes12.pickanevent.business.AdapterLista;
import com.pes12.pickanevent.business.Evento.EventoMGR;
import com.pes12.pickanevent.business.Grupo.GrupoMGR;
import com.pes12.pickanevent.business.Info;
import com.pes12.pickanevent.business.MGRFactory;
import com.pes12.pickanevent.business.Tag.TagMGR;
import com.pes12.pickanevent.persistence.entity.Grupo.GrupoEntity;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

import static com.pes12.pickanevent.R.id.Primero;
import static com.pes12.pickanevent.R.id.Tags;
import static com.pes12.pickanevent.R.id.activity_ver_info_grupo;
import static com.pes12.pickanevent.R.id.etiquetas;

public class VerInfoGrupoActivity extends BaseActivity {

    TextView nombre;
    TextView descripcion;
    ImageView foto;
    TextView tags;
    ListView eventos;
    Button boton;

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
        /////////////////////////////////////////////////

        Bundle b = getIntent().getExtras(); //Para pruebas
        cm = b.getBoolean("CM");
        //System.out.println("Valor CM "+ cm);

        idGrupo = "grp11-1480690194870";


        showProgressDialog();

        gMGR.getInfoGrupo(this, idGrupo);


        //PARA HACER QUE SEA SCROLLEABLE
        //tags.setMovementMethod(new ScrollingMovementMethod());


        /* COMPRESION IMAGEN PARA GUARDARLA EN FIREBASE COMO STRING*/
        /*Bitmap bm = BitmapFactory.decodeResource(getResources(), R.drawable.redhot);
        ByteArrayOutputStream bYtE = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.PNG, 100, bYtE);
        bm.recycle();
        byte[] byteArray = bYtE.toByteArray();
        String imageFile = Base64.encodeToString(byteArray, Base64.DEFAULT);


        GrupoEntity ge = new GrupoEntity("FCB D", "gooool", imageFile, "barcelona", "123", "321");
        Map<String,Boolean> relaciones = new HashMap<>();
        relaciones.put("key1",true);
        relaciones.put("key2",true);
        ge.setIdEventos(relaciones);
        ge.setIdTags(relaciones);*/

        //gMGR.crear(ge);


    }

    public void mostrarInfoGrupo(GrupoEntity _grupo) {

        if (_grupo.getIdEventos() != null) eMGR.getInfoEventosGrupo(this, _grupo.getIdEventos(), cm);
        Map<String, String> tagsMap = _grupo.getIdTags();
        if (tagsMap == null) tagsMap = new LinkedHashMap<>();
        tagsMap.put(_grupo.getidTagGeneral(), "blabla");
        tMGR.getInfoTag(this, tagsMap);

        nombre.setText(_grupo.getNombreGrupo());
        descripcion.setText(_grupo.getDescripcion());
        String texto = "SEGUIR!";
        if (cm) texto = "EDITAR";
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
            primero.setText(info.get(0));
            primero.setPadding(3, 3, 3, 3);
            primero.hasOnClickListeners();

            primero.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    startActivity(new Intent(VerInfoGrupoActivity.this, VerGruposConTagActivity.class).putExtra("nombreTag", info.get(0).toString()));
                }
            });

            for (int i = 1; i < info.size(); ++i) {
                TextView test = new TextView(this);
                test.setText(info.get(i));
                test.setPadding(3, 3, 3, 3);
                linearLayout.addView(test);
            }
        }
        TextView test2 = new TextView(this);
        test2.setText("aaaaaaaaaaaaaaaaaaaaaa");
        test2.setPadding(3,3,3,3);
        linearLayout.addView(test2);
        TextView test3 = new TextView(this);
        test3.setText("aaaaaaaaaaaaaaaaaaaaaa");
        test3.setPadding(3,3,3,3);
        linearLayout.addView(test3);
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
}

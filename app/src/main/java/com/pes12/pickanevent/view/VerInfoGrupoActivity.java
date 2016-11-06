package com.pes12.pickanevent.view;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.pes12.pickanevent.R;
import com.pes12.pickanevent.business.AdapterLista;
import com.pes12.pickanevent.business.Grupo.GrupoMGR;
import com.pes12.pickanevent.business.Info;
import com.pes12.pickanevent.business.MGRFactory;
import com.pes12.pickanevent.persistence.entity.Grupo.GrupoEntity;

import java.util.ArrayList;

public class VerInfoGrupoActivity extends BaseActivity {

    TextView nombre;
    TextView descripcion;
    ImageView foto;
    TextView tags;
    ListView eventos;

    String idGrupo;
    GrupoMGR gMGR;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ver_info_grupo);
        //////inicializacion elementos pantalla//////////
        nombre = (TextView)findViewById(R.id.nombreGrupo);
        descripcion = (TextView)findViewById(R.id.descripcion);
        foto = (ImageView)findViewById(R.id.imagenGrupo);
        tags = (TextView) findViewById(R.id.tags);
        eventos = (ListView) findViewById(R.id.event);
        /////////////////////////////////////////////////

        gMGR = MGRFactory.getInstance().getGrupoMGR();
        idGrupo = "-KUbHqRIqgL1eDGWpHT0";

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

    public void mostrarInfoGrupo(GrupoEntity grupo) {
        nombre.setText(grupo.getNombreGrupo());
        descripcion.setText(grupo.getDescripcion());
        String img = grupo.getImagen();
        Bitmap imgBM = StringToBitMap(img);
        foto.setImageBitmap(imgBM);
        foto.setScaleType(ImageView.ScaleType.FIT_XY);
        String tagsAux = "Deportes      Futbol      Deportes de equipo      Partidos";
        //tags.setText(grupo.getTagsAsString());
        tags.setText(tagsAux);


        ArrayList<Info> listaEventos = new ArrayList<Info>();
        listaEventos.add(new Info(imgBM,"Titulo evento 1","detalles evento 1", "seguir!"));
        listaEventos.add(new Info(imgBM,"Titulo evento 2","detalles evento 2", "seguir!"));

        //arraylist Append
        AdapterLista ale = new AdapterLista(VerInfoGrupoActivity.this,R.layout.vista_adapter_lista,listaEventos);
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

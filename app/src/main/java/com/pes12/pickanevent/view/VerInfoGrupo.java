package com.pes12.pickanevent.view;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Base64;
import android.view.KeyEvent;
import android.view.View;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.pes12.pickanevent.R;
import com.pes12.pickanevent.business.AdapterListaEventos;
import com.pes12.pickanevent.business.Grupo.GrupoMGR;
import com.pes12.pickanevent.business.Info;
import com.pes12.pickanevent.persistence.entity.Grupo.GrupoEntity;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.Map;

public class VerInfoGrupo extends AppCompatActivity {

    TextView nombre;
    TextView descripcion;
    ImageView foto;
    TextView tags;
    ListView eventos;

    LinearLayout events;

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

        //PARA HACER QUE SEA SCROLLEABLE
        //tags.setMovementMethod(new ScrollingMovementMethod());


        /* COMPRESION IMAGEN PARA GUARDARLA EN FIREBASE COMO STRING*/
        Bitmap bm = BitmapFactory.decodeResource(getResources(), R.drawable.oso);
        ByteArrayOutputStream bYtE = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.PNG, 100, bYtE);
        bm.recycle();
        byte[] byteArray = bYtE.toByteArray();
        String imageFile = Base64.encodeToString(byteArray, Base64.DEFAULT);


        gMGR = new GrupoMGR().getInstance();
        /*GrupoEntity ge = new GrupoEntity("FCB B", "gooool", imageFile, "barcelona", "123", "321");
        Map<String,String> relaciones = new HashMap<>();
        relaciones.put("key1","a");
        relaciones.put("key2","b");
        relaciones.put("key3","c");
        ge.setIdEventos(relaciones);
        ge.setIdTags(relaciones);

        gMGR.crear(ge);*/

        gMGR.getInfoGrupo(this);

    }

    public void mostrarInfoGrupo(Map<String,GrupoEntity> ge) {
        idGrupo = "-KUaw6R1kR5Pld7gd5d3";
        GrupoEntity grupo = ge.get(idGrupo);
        nombre.setText(grupo.getNombreGrupo());
        descripcion.setText(grupo.getDescripcion());
        String img = grupo.getImagen();
        Bitmap imgBM = StringToBitMap(img);
        foto.setImageBitmap(imgBM);
        String tagsAux = "Deportes      Futbol      Deportes de equipo      Partidos";
        //tags.setText(grupo.getTagsAsString());
        tags.setText(tagsAux);

        //eventos NO VAAA
        Info info[] = new Info[] {
                new Info(imgBM, "hola", "adeu"),
                new Info(imgBM, "hola2", "adeu2")
        };

        AdapterListaEventos ad = new AdapterListaEventos(this, R.layout.vista_evento_en_lista , info);
        View header = getLayoutInflater().inflate(R.layout.vista_evento_en_lista, null);
        eventos.addHeaderView(header);

        eventos.setAdapter(ad);

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

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        if ((keyCode == KeyEvent.KEYCODE_BACK))
        {
            finish();
        }
        return super.onKeyDown(keyCode, event);
    }
}

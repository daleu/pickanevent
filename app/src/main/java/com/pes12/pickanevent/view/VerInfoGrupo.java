package com.pes12.pickanevent.view;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Base64;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.pes12.pickanevent.R;
import com.pes12.pickanevent.business.Grupo.GrupoMGR;
import com.pes12.pickanevent.persistence.dao.Grupo.GrupoDAO;
import com.pes12.pickanevent.persistence.entity.Grupo.GrupoEntity;
import com.pes12.pickanevent.persistence.entity.Usuario.UsuarioEntity;

import java.io.ByteArrayOutputStream;
import java.util.Map;

public class VerInfoGrupo extends AppCompatActivity {

    TextView descripcion;
    LinearLayout events;
    ImageView foto;
    String idGrupo;
    GrupoMGR gMGR;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ver_info_grupo);
        descripcion = (TextView)findViewById(R.id.descripcion);
        descripcion.setMovementMethod(new ScrollingMovementMethod());

        //events = (LinearLayout)findViewById(R.id.ev);

        //GrupoDAO gDAO = new GrupoDAO(this);
        gMGR = new GrupoMGR().getInstance();
        gMGR.getInfoGrupo(this);

        //GrupoEntity ge = new GrupoEntity("FCB", "gooool", "", "barcelona", "123", "321");
        //gDAO.crear(ge);


        //gEntity.getNombreGrupo();

    }


    public void mostrarInfoGrupo(Map<String,GrupoEntity> ge) {
        idGrupo = "-KUS-QRDjJ3SuTFktAZd";
        GrupoEntity gEntity = ge.get(idGrupo);
        descripcion.setText(gEntity.getDescripcion());
        foto = (ImageView)findViewById(R.id.imagenGrupo);

        /* COMPRESION IMAGEN PARA GUARDARLA EN FIREBASE COMO STRING
        Bitmap bm = BitmapFactory.decodeResource(getResources(), R.drawable.oso);
        ByteArrayOutputStream bYtE = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.PNG, 100, bYtE);
        bm.recycle();
        byte[] byteArray = bYtE.toByteArray();
        String imageFile = Base64.encodeToString(byteArray, Base64.DEFAULT);
        */

        Drawable myDrawable = getResources().getDrawable(R.drawable.oso);
        foto.setImageDrawable(myDrawable);
    }
}

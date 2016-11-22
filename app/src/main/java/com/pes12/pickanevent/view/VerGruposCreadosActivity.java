package com.pes12.pickanevent.view;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.widget.ListView;

import com.pes12.pickanevent.R;
import com.pes12.pickanevent.business.AdapterLista;
import com.pes12.pickanevent.business.Grupo.GrupoMGR;
import com.pes12.pickanevent.business.Info;
import com.pes12.pickanevent.business.MGRFactory;
import com.pes12.pickanevent.business.Usuario.UsuarioMGR;
import com.pes12.pickanevent.persistence.entity.Grupo.GrupoEntity;
import com.pes12.pickanevent.persistence.entity.Usuario.UsuarioEntity;

import java.util.ArrayList;
import java.util.Map;

public class VerGruposCreadosActivity extends BaseActivity {

    String idUsuario;
    String idGrupo;
    ListView listaGrupos;

    ArrayList<Info> grupos;

    UsuarioMGR uMGR;
    GrupoMGR gMGR;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ver_grupos_creados);

        //Progres dialog
        showProgressDialog();

        //Inicializaciones
        listaGrupos = (ListView) findViewById(R.id.ListaGruposCreados);
        grupos = new ArrayList<Info>();
        ///////////////////////////////////////////////////////////////////////

        uMGR = MGRFactory.getInstance().getUsuarioMGR();
        gMGR = MGRFactory.getInstance().getGrupoMGR();

        idUsuario = "-KUHeQd1dR1FT3FmbPLu";
        uMGR.getInfoUsuarioGrupos(this, idUsuario);

    }

    public void mostrarInfoUsuarioGrupos(UsuarioEntity usuario) {

        Map<String,Boolean> idGrupos = usuario.getIdGrupos();

        //for (Map.Entry<String, Boolean> entry : idGrupos.entrySet()){
            //System.out.println(entry.getKey() + "/" + entry.getValue());
            //if(entry.getValue()) {
                idGrupo = "-KUbHqRIqgL1eDGWpHT0";
                gMGR.getInfoGruposCreados(this,/*entry.getKey()*/idGrupo);
            //}
        //}
    }

    public void rellenarListaGrupos (GrupoEntity grupo){

        String img = grupo.getImagen();
        Bitmap imBM = StringToBitMap(img);
        String nombreGrupo = grupo.getNombreGrupo();
        Info info = new Info(imBM, nombreGrupo, "adeu", "seguir!");
        grupos.add(info);
        //System.out.println(grupos.get(0).primeraLinea);
        AdapterLista ale = new AdapterLista(VerGruposCreadosActivity.this,R.layout.vista_adapter_lista,grupos);
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
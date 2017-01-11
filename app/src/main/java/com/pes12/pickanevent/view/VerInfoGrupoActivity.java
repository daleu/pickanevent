package com.pes12.pickanevent.view;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.pes12.pickanevent.R;
import com.pes12.pickanevent.business.AdapterLista;
import com.pes12.pickanevent.business.Evento.EventoMGR;
import com.pes12.pickanevent.business.Grupo.GrupoMGR;
import com.pes12.pickanevent.business.Info;
import com.pes12.pickanevent.business.MGRFactory;
import com.pes12.pickanevent.business.Tag.TagMGR;
import com.pes12.pickanevent.persistence.entity.Grupo.GrupoEntity;
import com.pes12.pickanevent.persistence.entity.Tag.TagEntity;
import com.pes12.pickanevent.persistence.entity.Usuario.UsuarioEntity;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

import static com.pes12.pickanevent.R.id.Primero;
import static com.pes12.pickanevent.R.id.Tags;
import static com.pes12.pickanevent.R.id.borrarCuenta;

public class VerInfoGrupoActivity extends BaseActivity {

    private TextView nombre;
    private TextView descripcion;
    private ImageView foto;
    private ListView eventos;
    private Button boton;
    private Button editarTags;
    private Button editar;
    private Button addEvento;
    private RelativeLayout layoutEditar;
    private RelativeLayout layoutTags;
    private RelativeLayout layoutCrearEvento;

    private String idGrupo;
    private GrupoEntity grupo;
    private GrupoMGR gMGR;
    private EventoMGR eMGR;
    private TagMGR tMGR;
    private Boolean cm = false;
    private Button borrarGrupo;
    private String desti;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ver_info_grupo);

        ImageButton searchImage = (ImageButton) findViewById(R.id.searchact);
        if (searchImage!=null && getUsuarioActual().getCm()) searchImage.setVisibility(View.INVISIBLE);

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
        editar = (Button) findViewById(R.id.editar);
        addEvento = (Button) findViewById(R.id.addEvento);
        layoutCrearEvento = (RelativeLayout) findViewById(R.id.layoutCrearEvento);
        layoutEditar = (RelativeLayout) findViewById(R.id.layoutEditar);
        layoutTags = (RelativeLayout) findViewById(R.id.layoutEditarTags);
        /////////////////////////////////////////////////

        //Bundle b = getIntent().getExtras(); //Para pruebas
        //cm = b.getBoolean("CM");
        //System.out.println("Valor CM "+ cm);

        Bundle param = getIntent().getExtras();
        //idGrupo = "-K_yYxivpF4D7ou8j-fT";
        if(param.getString("key")!=null){
            idGrupo = param.getString("key");
        }
        if(param.getString("action")!=null){
            if(param.getString("action")=="noseguir") dejarSeguirGrupo(idGrupo);
        }

        if(param.getString("origen")!=null){
            if(param.getString("origen").equals("crear")) desti = "intent";
            else desti = "enrere";
        }
        else desti = "enrere";

        showProgressDialog();
        gMGR.getInfoGrupo(this, idGrupo);

        //Boton eliminar grupo
        borrarGrupo = (Button) findViewById(R.id.borrarGrupo);
        if (getUsuarioActual().getCm()) {
            boton.setVisibility(View.INVISIBLE);
            borrarGrupo.setOnClickListener(new View.OnClickListener() {
                Boolean esCM = getUsuarioActual().getCm();

                @Override
                public void onClick(View v) {
                    LayoutInflater inflater = getLayoutInflater();
                    View dialoglayout = null;
                    dialoglayout = inflater.inflate(R.layout.dialog_borrar_grupo, null);
                    AlertDialog.Builder builder = new AlertDialog.Builder(VerInfoGrupoActivity.this);
                    builder.setView(dialoglayout);
                    final AlertDialog alert = builder.create();
                    alert.show();
                    Button aceptar = (Button) dialoglayout.findViewById(borrarCuenta);
                    Button cancelar = (Button) dialoglayout.findViewById(R.id.funcionVacia);
                    aceptar.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            Boolean noError = borrarGrupo(idGrupo, false);
                            String msg = noError ? getString(R.string.BORRADO_GRUPO_CORRECTO) : getString(R.string.ERROR_BORRAR);
                            Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
                            alert.hide();
                            if (noError) signOut();
                        }
                    });
                    cancelar.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            alert.hide();
                        }
                    });
                }
            });
        } else {
            borrarGrupo.setVisibility(View.INVISIBLE);
            layoutEditar.setVisibility(View.INVISIBLE);
            layoutTags.setVisibility(View.INVISIBLE);
            layoutCrearEvento.setVisibility(View.INVISIBLE);
        }


    }

    public void mostrarInfoGrupo(GrupoEntity _grupo) {
        grupo = _grupo;
        if (_grupo.getIdEventos() != null) eMGR.getInfoEventosGrupo(this, _grupo.getIdEventos(), cm);
        Map<String, String> tagsMap = _grupo.getIdTags();
        if (tagsMap == null) tagsMap = new LinkedHashMap<>();
        tagsMap.put(_grupo.getidTagGeneral(), "blabla");
        tMGR.getInfoTag(this, tagsMap);

        nombre.setText(_grupo.getNombreGrupo());
        descripcion.setText(_grupo.getDescripcion());
        String texto;
        if (cm) texto = getString(R.string.DEFAULT_EDITAR);
        else {
            if (siguiendoGrupo(idGrupo)) texto = getString(R.string.DEFAULT_NO_SEGUIR);
            else texto = getString(R.string.DEFAULT_SEGUIR);
        }
        boton.setText(texto);

        Picasso.with(this).load(_grupo.getImagen()).into(foto);

        /*String img = _grupo.getImagen();
        Bitmap imgBM = StringToBitMap(img);
        foto.setImageBitmap(imgBM);
        foto.setScaleType(ImageView.ScaleType.FIT_XY);*/

        if (_grupo.getIdEventos() == null) hideProgressDialog();
    }

    public void mostrarEventosGrupo(ArrayList<Info> info) {
        AdapterLista ale = new AdapterLista(VerInfoGrupoActivity.this, R.layout.vista_adapter_lista, info);
        eventos.setAdapter(ale);
        hideProgressDialog();
    }

    public void mostrarTags(final ArrayList<String> info) {
        LinearLayout linearLayout = (LinearLayout) findViewById(Tags);
        TextView primero = (TextView) findViewById(Primero);
        if (info.size() > 0) {
            //int id = 0;
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
            params.setMargins(10,0,10,0);
            primero.setLayoutParams(params);
            primero.setText(info.get(0));
            primero.setPadding(15,7,15,7);
            primero.setTextColor(Color.rgb(100,100,100));
            primero.hasOnClickListeners();
            primero.setBackground(getResources().getDrawable(R.drawable.tag_layout));
            //primero.setBackgroundColor(Color.rgb(130,255,130));
            //primero.setId(id);

            primero.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    if (!getUsuarioActual().getCm()) startActivity(new Intent(VerInfoGrupoActivity.this, VerGruposConTagActivity.class).putExtra(TagEntity.ATTRIBUTES.NOMBRETAG.getValue(), info.get(0).toString()));
                }
            });

            for (int i = 1; i < info.size(); ++i) {
                TextView siguiente = new TextView(this);
                siguiente.setLayoutParams(params);
                siguiente.setText(info.get(i));
                siguiente.setPadding(15, 7, 15, 7);
                siguiente.setTextColor(Color.rgb(100,100,100));
                siguiente.hasOnClickListeners();
                siguiente.setBackground(getResources().getDrawable(R.drawable.tag_layout));
                //siguiente.setBackgroundColor(Color.rgb(130,255,130));
                //RelativeLayout.LayoutParams params1 = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                //params1.addRule(RelativeLayout.RIGHT_OF,id);
                //++id;
                //siguiente.setId(id);
                siguiente.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        if (!getUsuarioActual().getCm()) startActivity(new Intent(VerInfoGrupoActivity.this, VerGruposConTagActivity.class).putExtra(TagEntity.ATTRIBUTES.NOMBRETAG.getValue(), info.get(0).toString()));
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
        startActivity(new Intent(VerInfoGrupoActivity.this, IndicarTagsActivity.class).putExtra("key", idGrupo));
    }

    public void editar(View view) {
        startActivity(new Intent(VerInfoGrupoActivity.this, EditarGrupoActivity.class).putExtra("key", idGrupo));
    }

    public void addEvento(View view) {
        startActivity(new Intent(VerInfoGrupoActivity.this, CrearEventoActivity.class).putExtra("key", idGrupo).putExtra("grupo", grupo));
    }

    //se tiene que poner para evitar que al volver de la edicion de tags se quede bloqueado si poder volver hacia atras
    @Override
    public void goBack(View _view) {
        if(desti.equals("enrere")) onBackPressed();
        else if (desti.equals("intent"))startActivity(new Intent(VerInfoGrupoActivity.this, NavigationDrawer.class));
    }

    public void seguirDejarDeSeguir(View view) {
        UsuarioEntity currentUser = getUsuarioActual();
        String texto;
        if (!currentUser.getCm()) { //solo se seguira o se dejara de seguir en caso de ser usuario "normal"
            if (siguiendoGrupo(idGrupo)) { //el usuario quiere dejar de seguir el grupo
                dejarSeguirGrupo(idGrupo);
                texto = getString(R.string.DEFAULT_SEGUIR);
            }
            else { //el usuario quiere seguir el grupo
                seguirGrupo(idGrupo, grupo.getNombreGrupo());
                texto = getString(R.string.DEFAULT_NO_SEGUIR);
            }
            boton.setText(texto);
        }
    }
}

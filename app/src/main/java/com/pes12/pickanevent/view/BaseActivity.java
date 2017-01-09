package com.pes12.pickanevent.view;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.WebView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.pes12.pickanevent.R;
import com.pes12.pickanevent.business.Evento.EventoMGR;
import com.pes12.pickanevent.business.Grupo.GrupoMGR;
import com.pes12.pickanevent.business.MGRFactory;
import com.pes12.pickanevent.business.Usuario.UsuarioMGR;
import com.pes12.pickanevent.persistence.entity.Usuario.UsuarioEntity;
import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.TwitterCore;
import com.twitter.sdk.android.core.TwitterSession;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Legault on 25/10/2016.
 */

public class BaseActivity extends AppCompatActivity {

    private static UsuarioEntity usuarioActual;
    private ProgressDialog mProgressDialog;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseAuth mAuth;
    private UsuarioMGR uMGR;
    private GrupoMGR gMGR;
    private EventoMGR eMGR;
    private String token;

    public static UsuarioEntity getUsuarioActual() {
        return usuarioActual;
    }

    public void setUsuarioActual(UsuarioEntity _usuarioActual) {
        usuarioActual = _usuarioActual;
    }

    public void showProgressDialog() {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(this);
            mProgressDialog.setMessage(getString(R.string.loading));
            mProgressDialog.setIndeterminate(true);
        }

        mProgressDialog.show();
    }

    public void hideProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
    }

    public void actualizarUsuario() {
        uMGR.actualizar(mAuth.getCurrentUser().getUid(), usuarioActual);
    }


    public FirebaseAuth getAuth() {
        return mAuth;
    }

    ;

    @Override
    protected void onCreate(Bundle _savedInstanceState) {
        super.onCreate(_savedInstanceState);
        initAuth();

    }

    public void signOut() {
        mAuth.signOut();
        usuarioActual = null;
    }

    private void initAuth() {
        uMGR = MGRFactory.getInstance().getUsuarioMGR();
        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    uMGR.getUsuarioLogin(BaseActivity.this, user.getUid());

                } else {
                    // User is signed out

                }
                // ...
            }
        };
        // ...
    }

    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }


    @Override
    public void onStop() {
        super.onStop();
        hideProgressDialog();
    }

    public void goBack(View _view) {
        onBackPressed();
    }

    public void goBusqueda(View _view){
        Intent intent = new Intent(this,BuscarEventoActivity.class);
        startActivity(intent);
    }

    /*public EventoEntity parseEventViewToEntity(Bitmap image, String lat, String lng) {
        EditText nomEvent = (EditText) findViewById(R.id.editorNEvento);
        EditText descripcio = (EditText) findViewById(R.id.editorDescr);
        CheckBox gratuit = (CheckBox) findViewById(R.id.checkBoxGratis);
        String preu = (gratuit.isChecked()) ? null : ((EditText) findViewById(R.id.editorPrecio)).toString();
        EditText url = (EditText) findViewById(R.id.editorEntradas);
        EditText localitzacio = (EditText) findViewById(R.id.editorLugar);
        EditText primerDia = (EditText) findViewById(R.id.editorFecha);
        EditText ultimoDia = (EditText) findViewById(R.id.editorFechaFinal);

        String imatge = null;
        if (image != null) {
            ByteArrayOutputStream bYtE = new ByteArrayOutputStream();
            image.compress(Bitmap.CompressFormat.JPEG, 75, bYtE);
            image.recycle();
            byte[] byteArray = bYtE.toByteArray();
            imatge = Base64.encodeToString(byteArray, Base64.DEFAULT);
        }

        Date dataIn = new Date(primerDia.getText().toString());
        Date dataFi = new Date(ultimoDia.getText().toString());
        EventoEntity ee = new EventoEntity(nomEvent.getText().toString(), descripcio.getText().toString(), imatge, preu, url.getText().toString(), localitzacio.getText().toString(), lat, lng, dataIn, dataFi);
        return ee;
    }*/


    /* METODOS PARA LAS RELACIONES */

    /***
     * USUARIO
     */
    public boolean siguiendoUsuario(String _idSeguido) {
        return siguiendoUsuario(getUsuarioActual(), _idSeguido);
    }
    public boolean siguiendoUsuario(UsuarioEntity _seguidor, String _idSeguido) {
        return _seguidor.getIdUsuarios() != null && _seguidor.getIdUsuarios().containsKey(_idSeguido);
    }

    public void seguirUsuario(String _idSeguido, String _nicknameSeguido) {
        seguirUsuario(getAuth().getCurrentUser().getUid(), getUsuarioActual(), _idSeguido, _nicknameSeguido);
    }
    public void seguirUsuario(String _idSeguidor, UsuarioEntity _seguidor, String _idSeguido, String _nicknameSeguido) {
        Map<String, String> siguiendo = _seguidor.getIdUsuarios();
        if (siguiendo == null) {
            siguiendo = new HashMap<String, String>();
            _seguidor.setIdUsuarios(siguiendo);
        }
        siguiendo.put(_idSeguido, _nicknameSeguido);
        String nick = _nicknameSeguido;

        UsuarioMGR uMGR = MGRFactory.getInstance().getUsuarioMGR();
        uMGR.actualizar(_idSeguidor, _seguidor);

        Toast.makeText(this, getString(R.string.SIGUIENDO_A) + nick, Toast.LENGTH_LONG).show();
    }

    public void dejarSeguirUsuario(String _idSeguido) {
        dejarSeguirUsuario(getAuth().getCurrentUser().getUid(), getUsuarioActual(), _idSeguido);
    }
    public void dejarSeguirUsuario(String _idSeguidor, UsuarioEntity _seguidor, String _idSeguido) {
        if (siguiendoUsuario(_seguidor, _idSeguido))
            _seguidor.getIdUsuarios().remove(_idSeguido);

        UsuarioMGR uMGR = MGRFactory.getInstance().getUsuarioMGR();
        uMGR.actualizar(_idSeguidor, _seguidor);

        Toast.makeText(this, getString(R.string.USUARIO_UNFOLLOWED), Toast.LENGTH_LONG).show();
    }

    /***
     * GRUPO
     */
    public boolean siguiendoGrupo(String _idGrupo) {
        return siguiendoGrupo(getUsuarioActual(), _idGrupo);
    }
    public boolean siguiendoGrupo(UsuarioEntity _seguidor, String _idGrupo) {
        if (_seguidor == null) return siguiendoGrupo(_idGrupo);
        return _seguidor.getIdGrupos() != null && _seguidor.getIdGrupos().containsKey(_idGrupo);
    }

    public void seguirGrupo(String _idGrupo, String _nombreGrupo) {
        seguirGrupo(getAuth().getCurrentUser().getUid(), getUsuarioActual(), _idGrupo, _nombreGrupo);
    }
    public void seguirGrupo(String _idSeguidor, UsuarioEntity _seguidor, String _idGrupo, String _nombreGrupo) {
        Map<String, String> siguiendo = _seguidor.getIdGrupos();
        if (siguiendo == null) {
            siguiendo = new HashMap<String, String>();
            _seguidor.setIdGrupos(siguiendo);
        }
        siguiendo.put(_idGrupo, _nombreGrupo);
        String nombre = _nombreGrupo;

        UsuarioMGR uMGR = MGRFactory.getInstance().getUsuarioMGR();
        uMGR.actualizar(_idSeguidor, _seguidor);

        Toast.makeText(this, getString(R.string.SIGUIENDO_A) + nombre, Toast.LENGTH_LONG).show();
    }

    public void dejarSeguirGrupo(String _idGrupo) {
        dejarSeguirGrupo(getAuth().getCurrentUser().getUid(), getUsuarioActual(), _idGrupo);
    }
    public void dejarSeguirGrupo(String _idSeguidor, UsuarioEntity _seguidor, String _idGrupo) {
        if (siguiendoGrupo(_seguidor, _idGrupo))
            _seguidor.getIdGrupos().remove(_idGrupo);

        UsuarioMGR uMGR = MGRFactory.getInstance().getUsuarioMGR();
        uMGR.actualizar(_idSeguidor, _seguidor);

        Toast.makeText(this, getString(R.string.GRUPO_UNFOLLOWED), Toast.LENGTH_LONG).show();
    }


    /***
     * EVENTO
     */
    public boolean asistiendoEvento(String _idEvento) {
        return asistiendoEvento(getUsuarioActual(), _idEvento);
    }
    public boolean asistiendoEvento(UsuarioEntity _asistidor, String _idEvento) {
        return _asistidor.getIdEventos() != null && _asistidor.getIdEventos().containsKey(_idEvento);
    }
    public void asistirEvento(String _idEvento, String _tituloEvento) {
        asistirEvento(getAuth().getCurrentUser().getUid(), getUsuarioActual(), _idEvento, _tituloEvento);
    }
    public void asistirEvento(String _idAsistidor, UsuarioEntity _asistidor, String _idEvento, String _tituloEvento) {
        Map<String, String> asistiendo = _asistidor.getIdEventos();
        if (asistiendo == null) {
            asistiendo = new HashMap<String, String>();
            _asistidor.setIdGrupos(asistiendo);
        }
        asistiendo.put(_idEvento, _tituloEvento);
        String titulo = _tituloEvento;

        UsuarioMGR uMGR = MGRFactory.getInstance().getUsuarioMGR();
        uMGR.actualizar(_idAsistidor, _asistidor);

        Toast.makeText(this, getString(R.string.ASISTIENDO_A) + titulo, Toast.LENGTH_LONG).show();
    }
    public void cancelarAsistenciaEvento(String _idEvento) {
        cancelarAsistenciaEvento(getAuth().getCurrentUser().getUid(), getUsuarioActual(), _idEvento);
    }
    public void cancelarAsistenciaEvento(String _idAsistidor, UsuarioEntity _asistidor, String _idEvento) {
        if (asistiendoEvento(_asistidor, _idEvento))
            _asistidor.getIdEventos().remove(_idEvento);

        UsuarioMGR uMGR = MGRFactory.getInstance().getUsuarioMGR();
        uMGR.actualizar(_idAsistidor, _asistidor);

        Toast.makeText(this, getString(R.string.EVENTO_ASISTENCIA_CANCELADA), Toast.LENGTH_LONG).show();
    }

    /***
     * BORRADOS
     */
    public Boolean borrarCurrentUser() {
        UsuarioEntity user = getUsuarioActual();
        if (user.getCm())
            return borrarCM();
        else
            return borrarUsuario();
    }
    public Boolean borrarEvento(String _idEvento, boolean vieneDeBorrarGrupo) {
        if (!vieneDeBorrarGrupo) {
            //borrar evento en el mapEventos del grupo
            eMGR.borrarEventoEnGrupo(_idEvento, gMGR);
            //borrar evento en el mapEventos del usuario
            if (getUsuarioActual().getIdEventos().containsKey(_idEvento))
                getUsuarioActual().getIdEventos().remove(_idEvento);
            uMGR.actualizar(getAuth().getCurrentUser().getUid(), getUsuarioActual());
        }
        eMGR.borrarEvento(_idEvento);
        return true;
    }
    public Boolean borrarGrupo(String _idGrupo, boolean vieneDeBorrarCM) {
        //borramos los eventos del grupo y el grupo
        if (!vieneDeBorrarCM)
            gMGR.borrarEventosYRelacionesGrupo(this, _idGrupo, uMGR);
        gMGR.borrarGrupo(_idGrupo);
        return true;
    }
    public Boolean borrarCM() {
        String key = getAuth().getCurrentUser().getUid();
        Map<String, String> grupos = getUsuarioActual().getIdGrupos();
        for (String idGrupo : grupos.keySet())
            borrarGrupo(idGrupo, true);
        for (String idEvento : getUsuarioActual().getIdEventos().keySet())
            borrarEvento(idEvento, true);
        borrarUsuario();
        return true;
    }
    public Boolean borrarUsuario() {
        uMGR.borrarUsuario(getAuth().getCurrentUser().getUid());
        getAuth().getCurrentUser().delete().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    System.out.println("User account deleted");
                } else {
                    System.out.println("Something went wrong");
                }
            }
        });
        return true;
    }

}

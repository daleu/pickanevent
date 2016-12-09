package com.pes12.pickanevent.view;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.pes12.pickanevent.R;
import com.pes12.pickanevent.business.Grupo.GrupoMGR;
import com.pes12.pickanevent.business.MGRFactory;
import com.pes12.pickanevent.business.Usuario.UsuarioMGR;
import com.pes12.pickanevent.persistence.entity.Evento.EventoEntity;
import com.pes12.pickanevent.persistence.entity.Grupo.GrupoEntity;
import com.pes12.pickanevent.persistence.entity.Usuario.UsuarioEntity;

import java.io.ByteArrayOutputStream;
import java.util.Date;
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

    public EventoEntity parseEventViewToEntity(Bitmap image, String lat, String lng) {
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

        //Date dataIn = new Date(primerDia.getText().toString());
        //Date dataFi = new Date(ultimoDia.getText().toString());
        EventoEntity ee = new EventoEntity(nomEvent.getText().toString(), descripcio.getText().toString(), imatge, preu, url.getText().toString(), localitzacio.getText().toString(), lat, lng);//, dataIn, dataFi);
        return ee;
    }


    /* METODOS PARA LAS RELACIONES */

    /***
     * USUARIO
     */

    public boolean siguiendoUsuario(UsuarioEntity _seguidor, UsuarioEntity _seguido, String _idSeguido) {
        return _seguidor.getIdUsuarios() != null && _seguidor.getIdUsuarios().containsKey((_idSeguido == null) ? _seguido.getId() : _idSeguido);
    }

    public void seguirUsuario(UsuarioEntity _seguidor, UsuarioEntity _seguido, String _idSeguido, String _nicknameSeguido) {
        Map<String, String> siguiendo = _seguidor.getIdUsuarios();
        if (siguiendo == null) {
            siguiendo = new HashMap<String, String>();
            _seguidor.setIdUsuarios(siguiendo);
        }
        String nick = "";
        if (_idSeguido != null && _nicknameSeguido != null) {
            siguiendo.put(_idSeguido, _nicknameSeguido);
            nick = _nicknameSeguido;
        } else if (_seguido != null) {
            siguiendo.put(_seguido.getId(), _seguido.getNickname());
            nick = _seguido.getNickname();
        }

        UsuarioMGR uMGR = MGRFactory.getInstance().getUsuarioMGR();
        uMGR.actualizar(_seguidor.getId(), _seguidor);

        Toast.makeText(this, R.string.SIGUIENDO_A + nick, Toast.LENGTH_LONG).show();
    }

    public void dejarSeguirUsuario(UsuarioEntity _seguidor, UsuarioEntity _seguido, String _idSeguido) {
        if (siguiendoUsuario(_seguidor, _seguido, _idSeguido))
            _seguidor.getIdUsuarios().remove((_idSeguido == null) ? _seguido.getId() : _idSeguido);

        UsuarioMGR uMGR = MGRFactory.getInstance().getUsuarioMGR();
        uMGR.actualizar(_seguidor.getId(), _seguidor);

        Toast.makeText(this, R.string.USUARIO_UNFOLLOWED, Toast.LENGTH_LONG).show();
    }

    /***
     * GRUPO
     */

    public boolean siguiendoGrupo(UsuarioEntity _seguidor, GrupoEntity _grupo, String _idGrupo) {
        return _seguidor.getIdGrupos() != null && _seguidor.getIdGrupos().containsKey((_idGrupo == null) ? _grupo.getId() : _idGrupo);
    }

    public void seguirGrupo(UsuarioEntity _seguidor, GrupoEntity _grupo, String _idGrupo, String _nombreGrupo) {
        Map<String, String> siguiendo = _seguidor.getIdGrupos();
        if (siguiendo == null) {
            siguiendo = new HashMap<String, String>();
            _seguidor.setIdGrupos(siguiendo);
        }
        String nombre = "";
        if (_idGrupo != null && _nombreGrupo != null) {
            siguiendo.put(_idGrupo, _nombreGrupo);
            nombre = _nombreGrupo;
        } else if (_grupo != null) {
            siguiendo.put(_grupo.getId(), _grupo.getNombreGrupo());
            nombre = _grupo.getNombreGrupo();
        }

        UsuarioMGR uMGR = MGRFactory.getInstance().getUsuarioMGR();
        uMGR.actualizar(_seguidor.getId(), _seguidor);

        Toast.makeText(this, R.string.SIGUIENDO_A + nombre, Toast.LENGTH_LONG).show();
    }

    public void dejarSeguirGrupo(UsuarioEntity _seguidor, GrupoEntity _grupo, String _idGrupo) {
        if (siguiendoGrupo(_seguidor, _grupo, _idGrupo))
            _seguidor.getIdGrupos().remove((_idGrupo == null) ? _grupo.getId() : _idGrupo);

        UsuarioMGR uMGR = MGRFactory.getInstance().getUsuarioMGR();
        uMGR.actualizar(_seguidor.getId(), _seguidor);

        Toast.makeText(this, R.string.GRUPO_UNFOLLOWED, Toast.LENGTH_LONG).show();
    }

    /***
     * EVENTO
     */

    public boolean asistindiendoEvento(UsuarioEntity _asistidor, EventoEntity _evento, String _idEvento) {
        return _asistidor.getIdEventos() != null && _asistidor.getIdEventos().containsKey((_idEvento == null) ? _evento.getId() : _idEvento);
    }

    public void asistirEvento(UsuarioEntity _asistidor, EventoEntity _evento, String _idEvento, String _tituloEvento) {
        Map<String, String> asistiendo = _asistidor.getIdEventos();
        if (asistiendo == null) {
            asistiendo = new HashMap<String, String>();
            _asistidor.setIdGrupos(asistiendo);
        }
        String titulo = "";
        if (_idEvento != null && _tituloEvento != null) {
            asistiendo.put(_idEvento, _tituloEvento);
            titulo = _tituloEvento;
        } else if (_evento != null) {
            asistiendo.put(_evento.getId(), _evento.getTitulo());
            titulo = _evento.getTitulo();
        }

        UsuarioMGR uMGR = MGRFactory.getInstance().getUsuarioMGR();
        uMGR.actualizar(_asistidor.getId(), _asistidor);

        Toast.makeText(this, R.string.ASISTIENDO_A + titulo, Toast.LENGTH_LONG).show();
    }

    public void cancelarAsistenciaEvento(UsuarioEntity _asistidor, EventoEntity _evento, String _idEvento) {
        if (asistindiendoEvento(_asistidor, _evento, _idEvento))
            _asistidor.getIdEventos().remove((_idEvento == null) ? _evento.getId() : _idEvento);

        UsuarioMGR uMGR = MGRFactory.getInstance().getUsuarioMGR();
        uMGR.actualizar(_asistidor.getId(), _asistidor);

        Toast.makeText(this, R.string.EVENTO_ASISTENCIA_CANCELADA, Toast.LENGTH_LONG).show();
    }

}

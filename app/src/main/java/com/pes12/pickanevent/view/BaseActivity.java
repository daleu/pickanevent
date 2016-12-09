package com.pes12.pickanevent.view;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.pes12.pickanevent.R;
import com.pes12.pickanevent.business.MGRFactory;
import com.pes12.pickanevent.business.Usuario.UsuarioMGR;
import com.pes12.pickanevent.persistence.entity.Evento.EventoEntity;
import com.pes12.pickanevent.persistence.entity.Usuario.UsuarioEntity;

import java.io.ByteArrayOutputStream;
import java.util.Date;

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
}

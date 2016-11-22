package com.pes12.pickanevent.view;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.pes12.pickanevent.R;
import com.pes12.pickanevent.business.MGRFactory;
import com.pes12.pickanevent.business.Usuario.UsuarioMGR;
import com.pes12.pickanevent.persistence.entity.Usuario.UsuarioEntity;

/**
 * Created by Legault on 25/10/2016.
 */

public class BaseActivity extends AppCompatActivity {

    private ProgressDialog mProgressDialog;
    private static UsuarioEntity  usuarioActual;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseAuth mAuth;
    private UsuarioMGR uMGR;

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

    public static UsuarioEntity getUsuarioActual()
    {
        return usuarioActual;
    }
    public void setUsuarioActual(UsuarioEntity _usuarioActual)
    {
        usuarioActual=_usuarioActual;
    }

    public FirebaseAuth getAuth(){return mAuth;};

    @Override
    protected void onCreate(Bundle _savedInstanceState) {
        super.onCreate(_savedInstanceState);
        initAuth();

    }

    public void signOut(){
        mAuth.signOut();
        usuarioActual=null;
    }

    private void initAuth()
    {
        uMGR = MGRFactory.getInstance().getUsuarioMGR();
        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    uMGR.getUsuarioLogin(BaseActivity.this,user.getUid());

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
}

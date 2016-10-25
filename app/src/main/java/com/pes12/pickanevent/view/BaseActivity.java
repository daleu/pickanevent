package com.pes12.pickanevent.view;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.pes12.pickanevent.R;
import com.pes12.pickanevent.business.MGRFactory;
import com.pes12.pickanevent.persistence.entity.Usuario.UsuarioEntity;

/**
 * Created by Legault on 25/10/2016.
 */

public class BaseActivity extends AppCompatActivity {

    private ProgressDialog mProgressDialog;
    private static UsuarioEntity  usuarioActual;

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

    public UsuarioEntity getUsuarioActual()
    {
        return usuarioActual;
    }
    public void setUsuarioActual(UsuarioEntity _usuarioActual)
    {
        usuarioActual=_usuarioActual;
    }
    @Override
    public void onStop() {
        super.onStop();
        hideProgressDialog();
    }
}

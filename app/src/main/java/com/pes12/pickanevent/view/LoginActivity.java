package com.pes12.pickanevent.view;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.pes12.pickanevent.R;
import com.pes12.pickanevent.business.EncodeUtil;
import com.pes12.pickanevent.business.MGRFactory;
import com.pes12.pickanevent.business.Usuario.UsuarioMGR;


public class LoginActivity extends BaseActivity {

    private EditText correo;
    private EditText pass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login);
        correo = (EditText) findViewById(R.id.correo);
        pass = (EditText) findViewById(R.id.pass);
    }

    public void login(View view) {

        String password = pass.getText().toString();
        getAuth().signInWithEmailAndPassword(correo.getText().toString(), password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (!task.isSuccessful()) {
                    Toast.makeText(LoginActivity.this, getString(R.string.LOGIN_INCORRECTO),
                            Toast.LENGTH_SHORT).show();
                    return;
                }

                if (task.getResult().getUser().isEmailVerified()) {
                    Toast.makeText(LoginActivity.this, getString(R.string.LOGIN_CORRECTO) + correo.getText().toString(),
                            Toast.LENGTH_SHORT).show();
                    UsuarioMGR uMGR = MGRFactory.getInstance().getUsuarioMGR();

                    uMGR.getUsuarioLogin(LoginActivity.this, task.getResult().getUser().getUid());
                    LoginActivity.this.finish();
                } else {
                    Toast.makeText(LoginActivity.this, getString(R.string.CORREO_NO_VERIFICADO) + correo.getText().toString(),
                            Toast.LENGTH_SHORT).show();
                    signOut();
                }


                // ...
            }
        });

    }


}

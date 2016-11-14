package com.pes12.pickanevent.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.pes12.pickanevent.R;
import com.pes12.pickanevent.business.Constantes;
import com.pes12.pickanevent.business.EncodeUtil;
import com.pes12.pickanevent.business.MGRFactory;
import com.pes12.pickanevent.business.Usuario.UsuarioMGR;
import com.pes12.pickanevent.persistence.entity.Usuario.UsuarioEntity;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends BaseActivity {

    private EditText correo;
    private  EditText pass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        correo= (EditText)findViewById(R.id.correo);
        pass= (EditText)findViewById(R.id.pass);

    }


    public void login(View view)
    {

        String password = EncodeUtil.encodePasswordSHA1(pass.getText().toString());
        getAuth().signInWithEmailAndPassword(correo.getText().toString(),password).addOnCompleteListener(this,new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (!task.isSuccessful()) {
                    Toast.makeText(LoginActivity.this,"Login incorrecto",
                            Toast.LENGTH_SHORT).show();
                    return;
                }

                if (task.getResult().getUser().isEmailVerified()) {
                    Toast.makeText(LoginActivity.this,  "Login Correcto! " +correo.getText().toString(),
                            Toast.LENGTH_SHORT).show();
                    UsuarioMGR uMGR = MGRFactory.getInstance().getUsuarioMGR();

                    uMGR.getUsuarioLogin(LoginActivity.this,task.getResult().getUser().getUid());
                    LoginActivity.this.finish();
                }
                else
                {
                    Toast.makeText(LoginActivity.this,  "Correo no verificado! " +correo.getText().toString(),
                            Toast.LENGTH_SHORT).show();
                    signOut();
                }



                // ...
            }
        });

    }




}

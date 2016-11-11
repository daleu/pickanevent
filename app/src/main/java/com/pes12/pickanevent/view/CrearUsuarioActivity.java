package com.pes12.pickanevent.view;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
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

import java.util.Random;

/**
 * Created by Legault on 08/11/2016.
 */

public class CrearUsuarioActivity extends BaseActivity {


    private  EditText correo;
    private  EditText pass;
    private  EditText username;
    private  CheckBox cm;

    @Override
    protected void onCreate(Bundle _savedInstanceState) {
        super.onCreate(_savedInstanceState);
        setContentView(R.layout.activity_crear_usuario);
        correo= (EditText)findViewById(R.id.correo);
        pass= (EditText)findViewById(R.id.pass);
        username=(EditText)findViewById(R.id.username);
        cm=(CheckBox)findViewById(R.id.cm);
    }


    public void crearUsuario(View _view)
    {

        String password = EncodeUtil.encodePasswordSHA1(pass.getText().toString());
        getAuth().createUserWithEmailAndPassword(correo.getText().toString(),password).addOnCompleteListener(this,new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                Toast.makeText(CrearUsuarioActivity.this, Constantes.LOG_USUARI_CREADO_CORRECTO + ' ' +correo.getText().toString(),
                        Toast.LENGTH_SHORT).show();
                 UsuarioMGR uMGR = MGRFactory.getInstance().getUsuarioMGR();

                uMGR.actualizar(task.getResult().getUser().getUid(),new UsuarioEntity(username.getText().toString(),cm.isChecked()));
                CrearUsuarioActivity.this.finish();
                if (!task.isSuccessful()) {
                    Toast.makeText(CrearUsuarioActivity.this,Constantes.ERROR_CREAR_USUARIO,
                            Toast.LENGTH_SHORT).show();
                }

                // ...
            }
        });

    }
}

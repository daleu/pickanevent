package com.pes12.pickanevent.view;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.pes12.pickanevent.R;
import com.pes12.pickanevent.business.Constantes;
import com.pes12.pickanevent.business.EncodeUtil;
import com.pes12.pickanevent.business.ImagenPerfilUsuario.ImagenPerfilUsuarioMGR;
import com.pes12.pickanevent.business.MGRFactory;
import com.pes12.pickanevent.business.Usuario.UsuarioMGR;
import com.pes12.pickanevent.persistence.entity.Usuario.UsuarioEntity;

import java.io.FileNotFoundException;
import java.io.InputStream;

/**
 * Created by Legault on 08/11/2016.
 */

public class CrearUsuarioActivity extends BaseActivity {


    private EditText correo;
    private EditText pass;
    private EditText repepass;
    private EditText username;
    private CheckBox cm;
    private ImagenPerfilUsuarioMGR iMGR;
    String uId;


    @Override
    protected void onCreate(Bundle _savedInstanceState) {
        super.onCreate(_savedInstanceState);
        setContentView(R.layout.activity_crear_usuario);
        correo = (EditText) findViewById(R.id.correo);
        pass = (EditText) findViewById(R.id.pass);
        repepass = (EditText) findViewById(R.id.repepass);
        username = (EditText) findViewById(R.id.username);
        cm = (CheckBox) findViewById(R.id.cm);

    }


    public void crearUsuario(View _view) {
        if (!pass.getText().toString().equals(repepass.getText().toString())) {
            Toast.makeText(CrearUsuarioActivity.this, getString(R.string.ERROR_PASSWORDS_DIFERENTES),
                    Toast.LENGTH_SHORT).show();
            return;
        }
        String password = pass.getText().toString();
        getAuth().createUserWithEmailAndPassword(correo.getText().toString(), password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (!task.isSuccessful()) {
                    Toast.makeText(CrearUsuarioActivity.this, Constantes.ERROR_CREAR_USUARIO,
                            Toast.LENGTH_SHORT).show();
                    return;
                }
                Toast.makeText(CrearUsuarioActivity.this, Constantes.LOG_USUARI_CREADO_CORRECTO + ' ' + correo.getText().toString(),
                        Toast.LENGTH_SHORT).show();

                iMGR = MGRFactory.getInstance().getImagenPerfilUsuarioMGR();
                InputStream isImagen;

                task.getResult().getUser().sendEmailVerification();

                Uri uri = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE +
                        "://" + getResources().getResourcePackageName(R.drawable.profile)
                        + '/' + getResources().getResourceTypeName(R.drawable.profile) + '/' + getResources().getResourceEntryName(R.drawable.profile) );

                try {
                    isImagen = getContentResolver().openInputStream(uri);
                    MGRFactory.getInstance().getImagenPerfilUsuarioMGR().subirImagenCreacionUsuario(isImagen, task.getResult().getUser(), CrearUsuarioActivity.this);

                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }

                uId = task.getResult().getUser().getUid();





            }
        });



    }

    public void addImagenPorDefecto(String img) {
        UsuarioMGR uMGR = MGRFactory.getInstance().getUsuarioMGR();
        UsuarioEntity usuarioNuevo = new UsuarioEntity(username.getText().toString(), cm.isChecked());
        usuarioNuevo.setUrlPhoto(img);
        uMGR.actualizar(uId, usuarioNuevo);

        if (!cm.isChecked())startActivity(new Intent(CrearUsuarioActivity.this, IndicarTagsActivity.class).putExtra("usuarioReg", usuarioNuevo).putExtra("keyUsuR", uId));
        else startActivity(new Intent(CrearUsuarioActivity.this, NavigationDrawer.class));
    }
}

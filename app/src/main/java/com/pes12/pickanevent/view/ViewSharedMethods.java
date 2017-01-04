package com.pes12.pickanevent.view;

import android.content.Context;
import android.widget.Toast;

import com.pes12.pickanevent.R;
import com.pes12.pickanevent.business.MGRFactory;
import com.pes12.pickanevent.business.Usuario.UsuarioMGR;
import com.pes12.pickanevent.persistence.entity.Usuario.UsuarioEntity;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by p4 on 28/10/16.
 */

public class ViewSharedMethods extends BaseActivity {

    //Aquesta classe incloura totes les funcions comunes que son utilitzades per mes de una classe de la capa view

    public static final String getNomMes(int m, Context _context) {
        if (m == 1) return _context.getString(R.string.MES_ENERO);
        if (m == 2) return _context.getString(R.string.MES_FEBRERO);
        if (m == 3) return _context.getString(R.string.MES_MARZO);
        if (m == 4) return _context.getString(R.string.MES_ABRIL);
        if (m == 5) return _context.getString(R.string.MES_MAYO);
        if (m == 6) return _context.getString(R.string.MES_JUNIO);
        if (m == 7) return _context.getString(R.string.MES_JULIO);
        if (m == 8) return _context.getString(R.string.MES_AGOSTO);
        if (m == 9) return _context.getString(R.string.MES_SETIEMBRE);
        if (m == 10) return _context.getString(R.string.MES_OCTUBRE);
        if (m == 11) return _context.getString(R.string.MES_NOVIEMBRE);
        if (m == 12) return _context.getString(R.string.MES_DICIEMBRE);
        return "Unknown";
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
    public static Boolean borrarCurrentUser() {
        UsuarioEntity user = getUsuarioActual();
        if (user.getCm())
            return borrarCM();
        else
            return borrarUsuario();
    }
    public static Boolean borrarEvento(String _idEvento) {
        //1st: recorrer todos los usuarios y borrar el evento del mapEventos de los que asistiran a el
        //2nd: obtener el grupo y borrar el evento de dicho grupo
        //3rd: obtener el CM (del grupo) y borrar el evento del mapEventos del CM
        return false;
    }
    public static Boolean borrarGrupo(String _idGrupo) {
        //1st: recorrer todos los eventos del grupo y ejecutar la funcion borrar evento
        //2nd: recorrer todos los mapGrupos de todos los usuarios y borrar el grupo con id _idGrupo
        //3rd: obtener los tag en tagGrupos del grupo y editarlos borrando el grupo del tagsGrupo de tags
        //4th: borrar el grupo de la base de datos
        //5th: borrar el grupo del mapGrupos del currentUser
        return false;
    }
    public static Boolean borrarCM() {
        //1st: funcion borrarGrupo por cada grupo del mapGrupos del CM
        //2nd: borrar CM
        return false;
    }
    public static Boolean borrarUsuario() {
        //1st: recorrer usuarios para ver quien le sigue, borrar usuario del mapUsuario de sus seguidores
        //2nd: borrar usuario
        return false;
    }

}

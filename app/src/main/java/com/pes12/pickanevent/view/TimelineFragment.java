package com.pes12.pickanevent.view;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.pes12.pickanevent.R;
import com.pes12.pickanevent.business.AdapterLista;
import com.pes12.pickanevent.business.Evento.EventoMGR;
import com.pes12.pickanevent.business.Grupo.GrupoMGR;
import com.pes12.pickanevent.business.Info;
import com.pes12.pickanevent.business.MGRFactory;
import com.pes12.pickanevent.business.Usuario.UsuarioMGR;
import com.pes12.pickanevent.persistence.entity.Evento.EventoEntity;
import com.pes12.pickanevent.persistence.entity.Grupo.GrupoEntity;
import com.pes12.pickanevent.persistence.entity.Usuario.UsuarioEntity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link TimelineFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link TimelineFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TimelineFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    EventoMGR eMGR;
    UsuarioMGR uMGR;
    GrupoMGR gMGR;
    ListView eventos;
    String idUsuario;
    UsuarioEntity myUser;
    Map<String, Map<String, String>> listEvents;

    Map<String,String> my_events;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private ProgressDialog mProgressDialog;
    private OnFragmentInteractionListener mListener;

    public TimelineFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment TimelineFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static TimelineFragment newInstance(String param1, String param2) {
        TimelineFragment fragment = new TimelineFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        showProgressDialog();

        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

        eMGR = MGRFactory.getInstance().getEventoMGR();
        uMGR = MGRFactory.getInstance().getUsuarioMGR();
        gMGR = MGRFactory.getInstance().getGrupoMGR();

        idUsuario = "usr14-1480690194878";
        //idUsuario = "usr22-1480690194879";

        uMGR.getUserFromFragment(this, idUsuario);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_timeline, container, false);
        eventos = (ListView) view.findViewById(R.id.eventtimeline);
        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public void getUsuarioEvents(UsuarioEntity _usuario) {
        myUser = _usuario;
        if (myUser.getIdEventos() != null) my_events = myUser.getIdEventos();
        if (_usuario.getIdUsuarios() != null) {
            uMGR.getUsersForFragment(this, _usuario.getIdUsuarios());
        } else getAllUsersEvents(null);
    }

    public void getAllUsersEvents(Map<String, Map<String, String>> _usrs) {
        if (_usrs != null) {
            listEvents = _usrs;
        }
        else listEvents = new LinkedHashMap<String, Map<String, String>>();
        if (myUser.getIdEventos()!= null) listEvents.put(myUser.getUsername(), myUser.getIdEventos());
        if (myUser.getIdGrupos() == null) getAllGrupoEvents(null);
        else gMGR.getGrupoEventosForFragment(this, myUser.getIdGrupos());
    }

    public void getAllGrupoEvents(Map<String, Map<String, String>> _info) {
        if (_info != null) listEvents.putAll(_info);
        Map<String, String> map = new HashMap<String, String>();
        for (String key : listEvents.keySet()) {
            Map<String, String> aux = listEvents.get(key);
            if (aux != null) map.putAll(aux);
        }
        eMGR.getInfoEventosUsuarioFromFragment(this, map);
    }

    public void mostrarEventosUsuario(Map<String, EventoEntity> events) {
        Iterator it = events.entrySet().iterator();
        ArrayList<Info> infoAdapter = new ArrayList();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry)it.next();
            System.out.println(pair.getKey() + " = " + pair.getValue());
            EventoEntity e = (EventoEntity)pair.getValue();
            Info aux;
            if(my_events.containsKey(pair.getKey()))aux = new Info(StringToBitMap(e.getImagen()), e.getTitulo(), EventDate(e.getDataInici(),e.getDataFinal()), getString(R.string.DEFAULT_NO_ASSISTIR));
            else aux = new Info(StringToBitMap(e.getImagen()), e.getTitulo(), EventDate(e.getDataInici(),e.getDataFinal()), getString(R.string.DEFAULT_ASSISTIR));
            aux.setId((String)pair.getKey());
            infoAdapter.add(aux);
        }

        AdapterLista ale = new AdapterLista(getActivity(), R.layout.vista_adapter_lista, infoAdapter);
        eventos.setAdapter(ale);

        hideProgressDialog();
    }

    public void showProgressDialog() {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(getContext());
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

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    public String EventDate(Date ini, Date fi){
        SimpleDateFormat sdfDate = new SimpleDateFormat("dd/MM/yyyy");
        String inici = sdfDate.format(ini);
        String fina = sdfDate.format(fi);
        String data = inici + " - " + fina;
        return data;
    }

    private Bitmap StringToBitMap(String _encodedString) {
        try {
            byte[] encodeByte = Base64.decode(_encodedString, Base64.DEFAULT);
            Bitmap bitmap = BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
            return bitmap;
        } catch (Exception e) {
            e.getMessage();
            return null;
        }
    }

}

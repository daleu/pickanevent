package com.pes12.pickanevent.view;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.pes12.pickanevent.R;
import com.pes12.pickanevent.business.AdapterLista;
import com.pes12.pickanevent.business.Constantes;
import com.pes12.pickanevent.business.Info;
import com.pes12.pickanevent.business.MGRFactory;
import com.pes12.pickanevent.business.Usuario.UsuarioMGR;
import com.pes12.pickanevent.persistence.entity.Usuario.UsuarioEntity;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link AmistadesFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link AmistadesFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AmistadesFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    UsuarioMGR uMGR;

    ListView eventos;
    String idUsuario;
    UsuarioEntity myUser;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private ProgressDialog mProgressDialog;
    private OnFragmentInteractionListener mListener;

    public AmistadesFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AmistadesFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AmistadesFragment newInstance(String param1, String param2) {
        AmistadesFragment fragment = new AmistadesFragment();
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

        uMGR = MGRFactory.getInstance().getUsuarioMGR();

        idUsuario = ((NavigationDrawer)getActivity()).getUsuariActual();
        //idUsuario = "usr47-1480690194879";

        uMGR.getUserFromFragmentAmistades(this, idUsuario);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_amistades, container, false);
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

    public void getAmistadesFromUser(UsuarioEntity _usuario) {
        myUser = _usuario;
        if (_usuario.getIdUsuarios() != null) {
            uMGR.getUsersForFragmentAmistades(this, _usuario.getIdUsuarios());
        } else setAmistades(null);
    }

    public void setAmistades(Map<String, UsuarioEntity> info) {
        if(info!=null){
            ArrayList<Info> infoAdapter = new ArrayList();

            if(info!=null){
                Iterator it = info.entrySet().iterator();
                while (it.hasNext()) {
                    Map.Entry pair = (Map.Entry) it.next();
                    System.out.println(pair.getKey() + " = " + pair.getValue());
                    UsuarioEntity ge = (UsuarioEntity) pair.getValue();
                    Info aux = new Info(ge.getUrlPhoto(), ge.getNickname(), "", getString(R.string.DEFAULT_NO_SEGUIR));
                    aux.setId((String) pair.getKey());
                    aux.setTipus(Constantes.INFO_USUARI);
                    infoAdapter.add(aux);
                }
            }

            AdapterLista ale = new AdapterLista(getActivity(), R.layout.vista_adapter_lista, infoAdapter);
            eventos.setAdapter(ale);

        }
        hideProgressDialog();
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
}

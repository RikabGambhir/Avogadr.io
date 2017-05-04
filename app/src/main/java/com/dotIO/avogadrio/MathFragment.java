package com.dotIO.avogadrio;

import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link MathFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link MathFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MathFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_math, container, false);



        return view;
    }
}

package com.dotIO.avogadrio;

import android.app.Activity;
import android.app.DialogFragment;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class MoleculeDialogFragment extends DialogFragment {

    String moleculeName,
            briefDescription,
            phase,
            density,
            meltingPoint,
            boilingPoint,
            specificHeat,
            enthalpy,
            vaporization,
            pubChem,
            chemSpider,
            smiles;

    EditText moleculeNameEditText,
            briefDescriptionEditText,
            phaseEditText,
            densityEditText,
            meltingPointEditText,
            boilingPointEditText,
            specificHeatEditText,
            enthalpyEditText,
            vaporizationEditText,
            pubChemEditText,
            chemSpiderEditText,
            smilesEditText;

    final String[] properties = {"Name", "Brief Description", "Phase at Room Temperature", "Melting Point", "Boiling Point", "Enthalpy of Formation", "PubChem ID", "SMILES"};
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    final DatabaseReference myRef = database.getReference("molecules");
    final DatabaseReference nameRef = database.getReference("Molecule Names");


    public MoleculeDialogFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        Bundle bundle = getArguments();
        final String m = bundle.getString("Key");

        View view = inflater.inflate(R.layout.fragment_molecule_dialog, container, false);
        getDialog().setTitle("Enter Data for " + m);
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

        final OnSubmitListener listener = (OnSubmitListener)getTargetFragment();

        moleculeNameEditText = (EditText)view.findViewById(R.id.edit_molecule_name);
        briefDescriptionEditText = (EditText)view.findViewById(R.id.edit_molecule_description);
        phaseEditText = (EditText)view.findViewById(R.id.edit_molecule_phase);
        densityEditText = (EditText)view.findViewById(R.id.edit_molecule_density);
        meltingPointEditText = (EditText)view.findViewById(R.id.edit_molecule_melting);
        boilingPointEditText = (EditText)view.findViewById(R.id.edit_molecule_boiling);
        specificHeatEditText = (EditText)view.findViewById(R.id.edit_molecule_specific_heat);
        enthalpyEditText = (EditText)view.findViewById(R.id.edit_molecule_enthalpy);
        vaporizationEditText = (EditText)view.findViewById(R.id.edit_molecule_vaporization);
        pubChemEditText = (EditText)view.findViewById(R.id.edit_molecule_pubchem);
        chemSpiderEditText = (EditText)view.findViewById(R.id.edit_molecule_chemspider);
        smilesEditText = (EditText)view.findViewById(R.id.edit_molecule_smiles);

        Button submit = (Button)view.findViewById(R.id.edit_molecule_submit);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                moleculeName = moleculeNameEditText.getText().toString();
                briefDescription = briefDescriptionEditText.getText().toString();
                phase = phaseEditText.getText().toString();
                density = densityEditText.getText().toString();
                meltingPoint = meltingPointEditText.getText().toString();
                boilingPoint = boilingPointEditText.getText().toString();
                specificHeat = specificHeatEditText.getText().toString();
                enthalpy = enthalpyEditText.getText().toString();
                vaporization = vaporizationEditText.getText().toString();
                pubChem = enthalpyEditText.getText().toString();
                chemSpider = chemSpiderEditText.getText().toString();
                smiles = smilesEditText.getText().toString();


                String[] data = {moleculeName, briefDescription, phase, density,meltingPoint,boilingPoint, specificHeat,enthalpy,vaporization, pubChem,chemSpider,smiles};

                uploadData(m,data);

                listener.onSubmit();
                dismiss();
            }
        });

        Button dismiss = (Button) view.findViewById(R.id.dismiss);
        dismiss.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        return view;
    }

    public interface OnSubmitListener{
        public void onSubmit();
    }

//    public ArrayList<String>[] loadProperties(String molecule){
//        ArrayList<String>[] result;
//        for (int i = 0; i < properties.length; i++){
//            DatabaseReference m = myRef.child(molecule).child(properties[i]);
//            ValueEventListener propertyListener = new ValueEventListener() {
//                @Override
//                public void onDataChange(DataSnapshot dataSnapshot) {
//                    // Get Post object and use the values to update the UI
//                    Post post = dataSnapshot.getValue(Post.class);
//                    // ...
//                }
//        }
//        return result;
//    }

    public void uploadData(String molecule, String[] data){
        for (int i = 0; i < properties.length; i++){
            if (!data[i].equals("") && data[i] != null){
                String key = myRef.child(molecule).child(properties[i]).push().getKey();
                Map<String,Object> updates = new HashMap<>();
                updates.put("/" + molecule + "/" + properties[i] + "/" + key, data[i]);
                myRef.updateChildren(updates);
                Map<String,Object> nameUpdates = new HashMap<>();
                nameUpdates.put("/" + data[0] + "/" + properties[i] + "/" + key, data[i]);
                if(i!=0 && !data[0].equals("")) nameRef.updateChildren(nameUpdates);
            }
        }
        if (!data[0].equals("") && data[0] != null) {
            String name = data[0];
            String key = myRef.child(name).child("Formula").push().getKey();
            Map<String, Object> updates = new HashMap<>();
            updates.put("/" + name + "/" + "Formula" + "/" + key, molecule);
            nameRef.updateChildren(updates);
        }
    }

}
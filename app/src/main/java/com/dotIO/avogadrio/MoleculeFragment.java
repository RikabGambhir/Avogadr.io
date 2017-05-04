package com.dotIO.avogadrio;

import android.app.Activity;
import android.app.DialogFragment;
import android.app.FragmentManager;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.support.design.widget.FloatingActionButton;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.text.DecimalFormat;
import java.util.Map;

public class MoleculeFragment extends Fragment implements MoleculeDialogFragment.OnSubmitListener{

    Molecule m;
    TextView moleculeName;
    TextView molarMass;
    TextView moleculeFormula;
    TableLayout tl;
    FloatingActionButton moleculeButton;
    View view;
    Fragment fragment = this;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_molecule, container, false);

        Bundle bundle = getArguments();
        m = new Molecule(bundle.getString("Key"));
        molarMass = (TextView)view.findViewById(R.id.molar_mass);
        moleculeButton = (FloatingActionButton)view.findViewById(R.id.edit_molecule_button);
//        moleculeName = (TextView)view.findViewById(R.id.molecule_name);
        moleculeFormula = (TextView)view.findViewById(R.id.molecule);
        tl = (TableLayout)view.findViewById(R.id.molecule_table);

        DecimalFormat df = new DecimalFormat("#.000");
        setMolarMass(df.format(m.getMass()) + " g/mol");
        setMoleculeFormula("" + m.toString());
//        setMoleculeName();

        float scale = getResources().getDisplayMetrics().density;
        int dpAsPixels = (int) (48*scale + 0.5f);

        moleculeButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                FragmentManager fm = getFragmentManager();
                MoleculeDialogFragment dialogFragment = new MoleculeDialogFragment();
                dialogFragment.setStyle(DialogFragment.STYLE_NORMAL, R.style.CustomDialog);
                dialogFragment.setTargetFragment(fragment, 0);
                Bundle bundle = new Bundle();
                bundle.putString("Key", m.toString());
                dialogFragment.setArguments(bundle);
                dialogFragment.show(fm, "Sample Fragment");
            }
        });


        for (Map.Entry<Element, Integer> entry : m.getElements().entrySet()){
            TableRow tr = new TableRow(getActivity());
            tr.setLayoutParams(new TableRow.LayoutParams(
                    TableRow.LayoutParams.MATCH_PARENT,
                    dpAsPixels));

            TextView element = new TextView(getActivity());
            element.setText(entry.getKey().toString());
            element.setTextSize(13);
            element.setGravity(Gravity.LEFT);
            element.setTextColor(Color.parseColor("#212121"));
            TableRow.LayoutParams params = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT);
            params.setMargins(dpAsPixels/2, 0,0,0);
            element.setLayoutParams(params);
            tr.addView(element);

            double molarMass = entry.getValue() * entry.getKey().getAtomicWeight();
            TextView mass = new TextView(getActivity());
            mass.setText(df.format(molarMass) + " g/mol");
            mass.setGravity(Gravity.RIGHT);
            mass.setTextSize(13);
            mass.setTextColor(Color.parseColor("#212121"));
            mass.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));
            tr.addView(mass);

            TextView percent = new TextView(getActivity());
            percent.setText(df.format(100 * molarMass / m.getMass()) + "%");
            percent.setGravity(Gravity.RIGHT);
            percent.setTextSize(13);
            percent.setTextColor(Color.parseColor("#212121"));
            TableRow.LayoutParams params2 = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT);
            params2.setMargins(0,0,dpAsPixels/2,0);
            percent.setLayoutParams(params2);
            tr.addView(percent);

            tl.addView(tr, new TableLayout.LayoutParams(
                    TableRow.LayoutParams.MATCH_PARENT,
                    TableRow.LayoutParams.WRAP_CONTENT));
        }


        return view;
    }

    @Override
    public void onSubmit() {
        TextView message = (TextView)view.findViewById(R.id.message);
        message.setText("Thank you for your contribution!");
        moleculeButton.setEnabled(false);
        moleculeButton.setImageResource(R.drawable.checkmark);
        moleculeButton.setBackgroundTintList(getResources().getColorStateList(R.color.green));
    }

    private void setMolarMass(String s){
        molarMass.setText(s);
    }

    private void setMoleculeFormula(String s) {
        moleculeFormula.setText(s);
    }

    private void setMoleculeName(){
        try{
            moleculeName.setText(m.getName());
        }
        catch (Exception e){
            moleculeName.setText("No Data");
            moleculeName.setTypeface(null, Typeface.ITALIC);
        }
    }
}

package com.dotIO.avogadrio;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Spinner;

import android.widget.TextView;

import org.w3c.dom.Text;

import java.text.DecimalFormat;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

public class EquationFragment extends Fragment {

    AutoResizeTextView result;
    String inp;
    EditText input;
    TextView amount;
    Button button;
    ChemicalEquation eq;
    boolean error = false;

    RelativeLayout loading;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            inp = getArguments().getString("Key");
            try{
                eq = new ChemicalEquation(inp);
                inp = eq.toString();
            }catch (TimeoutException e){error = true;}
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_equation, container, false);
        result = (AutoResizeTextView)view.findViewById(R.id.eqnresult) ;

        getActivity().getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        Bundle bundle = getArguments();
        inp = bundle.getString("Key");
        if(!error){
            inp = eq.toString();

            input = (EditText) view.findViewById(R.id.stoich_input);
            amount = (TextView) view.findViewById(R.id.answer);
            button = (Button) view.findViewById(R.id.calculate);

            result.setText(inp);
            final Spinner inUnits = (Spinner) view.findViewById(R.id.input_units);
            String[] items = new String[]{"moles", "grams"};
            ArrayAdapter adapter = new ArrayAdapter(getActivity(), android.R.layout.simple_spinner_dropdown_item, items);
            inUnits.setAdapter(adapter);

            final Spinner inMolecule = (Spinner) view.findViewById(R.id.input_molecule);
            final String[] molecules = getMolecules(eq);
            final Molecule[] molecules2 = getMolecules2(eq);
            adapter = new ArrayAdapter(getActivity(), android.R.layout.simple_spinner_dropdown_item, molecules);
            inMolecule.setAdapter(adapter);

            final Spinner outUnits = (Spinner) view.findViewById(R.id.out_units);
            adapter = new ArrayAdapter(getActivity(), android.R.layout.simple_spinner_dropdown_item, items);
            outUnits.setAdapter(adapter);

            final Spinner outMolecule = (Spinner) view.findViewById(R.id.output_molecule);
            adapter = new ArrayAdapter(getActivity(), android.R.layout.simple_spinner_dropdown_item, molecules);
            outMolecule.setAdapter(adapter);
            outMolecule.setSelection(eq.LHS.size());

            button.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {

                    String inUn = inUnits.getSelectedItem().toString();
                    String outUn = outUnits.getSelectedItem().toString();
                    Molecule inMol = molecules2[inMolecule.getSelectedItemPosition()];
                    Molecule outMol = molecules2[outMolecule.getSelectedItemPosition()];
                    double x;
                    try {
                        x = Double.parseDouble(input.getText().toString());
                    } catch (Exception e) {
                        x = 0;
                    }
                    double y;

                    if (inUn.equals("grams")) {
                        x /= inMol.getMass();
                    }

                    y = eq.stoichiometry(x, inMol, outMol);
                    if (outUn.equals("grams")) {
                        y *= outMol.getMass();
                    }

                    DecimalFormat df = new DecimalFormat("#.00000");
                    amount.setText(df.format(y));
                    if (y == 0) amount.setText("Output");
                }
            });
        }else{
            result.setText("Avogadr.io could not balance this equation.\n" +
                    "\u2022 Elements must be properly capitalized.\n" +
                    "\u2022 Elements must match on both sides.\n" +
                    "\u2022 The equation might not be balanceable.");
        }

        return view;
    }

    public String[] getMolecules(ChemicalEquation eq){
        String[] list = new String[eq.LHS.size() + eq.RHS.size()];
        int counter = 0;
        for (Map.Entry<Molecule, Integer> entry : eq.LHS.entrySet()){
            list[counter] = entry.getKey().toString();
            counter++;
        }

        for (Map.Entry<Molecule, Integer> entry : eq.RHS.entrySet()){
            list[counter] = entry.getKey().toString();
            counter++;
        }
        return list;
    }
    public Molecule[] getMolecules2(ChemicalEquation eq){
        Molecule[] list = new Molecule[eq.LHS.size() + eq.RHS.size()];
        int counter = 0;
        for (Map.Entry<Molecule, Integer> entry : eq.LHS.entrySet()){
            list[counter] = entry.getKey();
            counter++;
        }

        for (Map.Entry<Molecule, Integer> entry : eq.RHS.entrySet()){
            list[counter] = entry.getKey();
            counter++;
        }
        return list;
    }

}
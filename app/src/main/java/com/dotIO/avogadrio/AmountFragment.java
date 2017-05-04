package com.dotIO.avogadrio;

import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.util.Map;


public class AmountFragment extends Fragment {
    Amount amount;
    TextView title;
    TextView molecules;
    TextView mole;
    TextView gram;
    TableLayout tl;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_amount, container, false);

        DecimalFormat df = new DecimalFormat("#.000");

        Bundle bundle = getArguments();
        amount = new Amount(bundle.getString("Key"));
        String s = amount.molecule.toString();
        String u = amount.unit.toString();
        title = (TextView)view.findViewById(R.id.amount_title);
        title.setText(u + " of " + s + " (" + df.format(amount.molecule.getMass()) + " g/mol)");
        molecules = (TextView)view.findViewById(R.id.amount_molecule);
        mole = (TextView)view.findViewById(R.id.amount_moles);
        gram = (TextView)view.findViewById(R.id.amount_grams);
        tl = (TableLayout)view.findViewById(R.id.amount_table);

        molecules.setText(amount.molecule.toString());
        mole.setText(df.format(amount.moles) + " mol");
        gram.setText(df.format(amount.grams) + " g");

        float scale = getResources().getDisplayMetrics().density;
        int dpAsPixels = (int) (48*scale + 0.5f);

        for (Map.Entry<Element, Integer> entry : amount.molecule.getElements().entrySet()){
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
            mass.setText(df.format(amount.moles * entry.getValue()) + " mol");
            mass.setGravity(Gravity.RIGHT);
            mass.setTextSize(13);
            mass.setTextColor(Color.parseColor("#212121"));
            mass.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));
            tr.addView(mass);

            TextView percent = new TextView(getActivity());
            percent.setText(df.format(amount.moles * entry.getValue() * entry.getKey().getAtomicWeight()) + " g");
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

}

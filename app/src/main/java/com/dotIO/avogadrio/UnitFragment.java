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


public class UnitFragment extends Fragment {

    Unit u;
    TableLayout tableLayout;
    TextView title;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState){
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_unit, container, false);

        Bundle bundle = getArguments();
        try {u = new Unit(bundle.getString("Key"));}
        catch (Exception e){}
        double[] conversions = u.convert();

        title = (TextView)view.findViewById(R.id.units_title);
        tableLayout = (TableLayout)view.findViewById(R.id.unit_table);
        setTitle();

        float scale = getResources().getDisplayMetrics().density;
        int dpAsPixels = (int) (48*scale + 0.5f);

        for (int i = 0; i < u.units[u.unitTypeIndex].length; i++){
            TableRow tr = new TableRow(getActivity());
            tr.setLayoutParams(new TableRow.LayoutParams(
                    TableRow.LayoutParams.MATCH_PARENT,
                    dpAsPixels));

            TextView values = new TextView(getActivity());
            values.setText(toScientific(conversions[i], 6));
            values.setTextSize(13);
            values.setTextColor(Color.parseColor("#212121"));
            TableRow.LayoutParams params = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT);
            params.setMargins(dpAsPixels/2, 0,0,0);
            values.setLayoutParams(params);
            tr.addView(values);

            TextView un = new TextView(getActivity());
            un.setText("" + u.units[u.unitTypeIndex][i]);
            un.setGravity(Gravity.LEFT);
            un.setTextSize(13);
            un.setTextColor(Color.parseColor("#212121"));
            un.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));
            tr.addView(un);

            TextView sym = new TextView(getActivity());
            sym.setText("" + u.symbols[u.unitTypeIndex][i]);
            sym.setGravity(Gravity.LEFT);
            sym.setTextSize(13);
            sym.setTextColor(Color.parseColor("#212121"));
            sym.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));
            tr.addView(sym);

            tableLayout.addView(tr, new TableLayout.LayoutParams(
                    TableRow.LayoutParams.MATCH_PARENT,
                    TableRow.LayoutParams.WRAP_CONTENT));
        }

        return view;
    }

    public void setTitle(){
        String lower = u.type.toString().toLowerCase();
        String titleCase = lower.substring(0,1).toUpperCase() + lower.substring(1,lower.length()) + " Conversions";
        title.setText(titleCase);
    }

    public static String toScientific(double num,int places) {
        String result="";
        String sign="";

        int power=(int)(Math.log(num)/Math.log(10));
        if(power < 0) power--;
        double fraction=num/Math.pow(10,power);
        fraction=round(fraction,places);
        result += fraction;
        if(power != 0)
        result += "\u00D710"  + superScript(power);
        return result;
    }

    public static String superScript(int power){
        String p = Integer.toString(power);
        String result = "";
        for (int i = 0; i < p.length(); i++){
            char ch = p.charAt(i);
            result += superScript(ch);
        }
        return result;
    }

    public static char superScript(char c){
        switch (c) {
            case '1':
                return '\u00B9';
            case '2':
                return '\u00B2';
            case '3':
                return '\u00B3';
            case '4':
                return '\u2074';
            case '5':
                return '\u2075';
            case '6':
                return '\u2076';
            case '7':
                return '\u2077';
            case '8':
                return '\u2078';
            case '9':
                return '\u2079';
            case '0':
                return '\u2074';
            case '-':
                return '\u207B';
            default:
                return '0';
        }
    }

    public static double round(double value, int decimalPlace) {
        double power_of_ten = 1;
        while (decimalPlace-- > 0) power_of_ten *= 10.0;
        return Math.round(value * power_of_ten) / power_of_ten;
    }

}

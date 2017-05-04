package com.dotIO.avogadrio;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import junit.framework.Assert;

import java.lang.reflect.Field;
import java.text.DecimalFormat;


public class ElementFragment extends Fragment {

    String inp;
    ImageView image;
    PeriodicTable periodicTable = new PeriodicTable();
    TextView name, sym, z, a, group;
    TextView type;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_element, container, false);
        image = (ImageView)view.findViewById(R.id.element_square);

        name = (TextView)view.findViewById(R.id.element_name);
        sym = (TextView)view.findViewById(R.id.element_symbol);
        type = (TextView)view.findViewById(R.id.element_type);
        z = (TextView)view.findViewById(R.id.element_number);
        a = (TextView)view.findViewById(R.id.element_weight);
        group = (TextView)view.findViewById(R.id.element_group);

        Bundle bundle = getArguments();
        inp = bundle.getString("Key");
        Element element =  periodicTable.getElement(inp);
        String symbol = element.getSymbol().toLowerCase();

        DecimalFormat df = new DecimalFormat("#.000");

        setStats(element);

        image.setImageResource(getResId(symbol, R.drawable.class));


        return view;
    }

    public static int getResId(String resName, Class<?> c) {

        try {
            Field idField = c.getDeclaredField(resName);
            return idField.getInt(idField);
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    private void setStats(Element e){
        DecimalFormat df = new DecimalFormat("#.000");

        name.setText(e.getName());
        sym.setText(e.getSymbol());
        type.setText(e.getType());
        if(e.getType().equals("Alkaline Earth Metal"))
            type.setText("Alk. E. Metal");
        if(e.getType().equals("Transition Metal"))
            type.setText("Trans. Metal");
        z.setText("" + e.getAtomicNumber());
        a.setText(df.format(e.getAtomicWeight()));
        group.setText("" + e.getGroup());
    }

}

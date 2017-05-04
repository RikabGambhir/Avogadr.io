package com.dotIO.avogadrio;

        import android.content.Context;
        import android.net.Uri;
        import android.os.Bundle;
        import android.app.Fragment;
        import android.view.LayoutInflater;
        import android.view.View;
        import android.view.ViewGroup;
        import android.widget.ProgressBar;
        import android.widget.RelativeLayout;
        import android.widget.TextView;

public class BlankFragment extends Fragment {

    TextView hint;
    RelativeLayout bar;
    Boolean loading;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_blank, container, false);

        Bundle bundle = getArguments();
        loading = bundle.getBoolean("Key");

        hint = (TextView)view.findViewById(R.id.hint_text);
        bar = (RelativeLayout)view.findViewById(R.id.loadingPanel);

        if(loading){
            hint.setVisibility(View.GONE);
            bar.setVisibility(View.VISIBLE);
        }else{
            hint.setVisibility(View.VISIBLE);
            bar.setVisibility(View.GONE);
        }

        return view;
    }
}
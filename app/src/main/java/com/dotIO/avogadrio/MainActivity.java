package com.dotIO.avogadrio;

import android.app.ProgressDialog;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatDelegate;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.app.Fragment;
import android.view.View;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.mopub.mobileads.MoPubView;
import com.mopub.mobileads.MoPubView.BannerAdListener;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    EditText input;
    Button button;
    TextView result;
    PeriodicTable periodicTable = new PeriodicTable();
    String in, out;
    RelativeLayout loading;
    private FirebaseAnalytics mFirebaseAnalytics;

    static{    AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
    }

//    FIREBASE
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    final DatabaseReference myRef = database.getReference("Searches");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        AppRater appRater = new AppRater(this);
        appRater.setDaysBeforePrompt(0);
        appRater.setLaunchesBeforePrompt(5);
        appRater.setPhrases("Rate this app", "If you enjoy using Atom.io, make sure to rate it!", "Rate now", "Later", "No, thanks");
        appRater.show();

//        GOOGLE ADMOBS
        MobileAds.initialize(getApplicationContext(), getString(R.string.banner_ad_unit_id));
        AdView mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        FragmentManager fm = getFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        Fragment fragment = newBlankFragment(false);
        ft.replace(R.id.fragment_container, fragment);
        ft.commit();

        input = (EditText)findViewById((R.id.input));
        button = (Button)findViewById(R.id.button);
        result = (TextView)findViewById(R.id.result);
        // Obtain the FirebaseAnalytics instance.
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        final View fragmentView = findViewById(R.id.fragment_container) ;

        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        final ProgressDialog progress = new ProgressDialog(this);


        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                in = input.getText().toString();

                Bundle event = new Bundle();
                event.putString(FirebaseAnalytics.Param.ITEM_ID, in);
                String log = "Blank";

                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);

                progress.setTitle("Loading");
                progress.setMessage("Wait while loading...");
                progress.setCancelable(false); // disable dismiss by tapping outside of the dialog

                if (ChemicalEquation.isChemicalEquation(in)) {
                    //                 //CREATE NEW EQUATIONFRAGMENT
                    FragmentManager fm = getFragmentManager();
                    progress.show();
                    Fragment fragment = newEquationFragment(in);
                    FragmentTransaction ft = fm.beginTransaction();
                    ft.setCustomAnimations(R.animator.slide_in_left, R.animator.slide_out_right);;
                    ft.replace(R.id.fragment_container, fragment);
                    ft.commit();
                    log = "Chemical Equation";

                }

                else if(periodicTable.isElement(in)){
                    Element e = periodicTable.getElement(in);
                    FragmentManager fm = getFragmentManager();
                    FragmentTransaction ft = fm.beginTransaction();
                    ft.setCustomAnimations(R.animator.slide_in_left, R.animator.slide_out_right);
                    Fragment fragment = newElementFragment(in);
                    ft.replace(R.id.fragment_container, fragment);
                    ft.commit();
                    log = "Element";
                }

                else if (Amount.isAmount(in)){
                    FragmentManager fm = getFragmentManager();
                    FragmentTransaction ft = fm.beginTransaction();
                    ft.setCustomAnimations(R.animator.slide_in_left, R.animator.slide_out_right);
                    Fragment fragment = newAmountFragment(in);
                    ft.replace(R.id.fragment_container, fragment);
                    ft.commit();
                    log = "Amount";
                }

                else if (Unit.isUnit(in)){
                    FragmentManager fm = getFragmentManager();
                    FragmentTransaction ft = fm.beginTransaction();
                    ft.setCustomAnimations(R.animator.slide_in_left, R.animator.slide_out_right);
                    Fragment fragment = newUnitFragment(in);
                    ft.replace(R.id.fragment_container, fragment);
                    ft.commit();
                    log = "Unit";
                }

                else if (Molecule.isMolecule(in)){
                    FragmentManager fm = getFragmentManager();
                    FragmentTransaction ft = fm.beginTransaction();
                    ft.setCustomAnimations(R.animator.slide_in_left, R.animator.slide_out_right);
                    Fragment fragment = newMoleculeFragment(in);
                    ft.replace(R.id.fragment_container, fragment);
                    ft.commit();
                    log = "Molecule";
                }

                else if (Molecule.isMolecule(in.toUpperCase())){
                    Toast.makeText(MainActivity.this, "Please ensure your molecule is properly capitalized!", Toast.LENGTH_LONG).show();
                }

                else if (HalfReaction.isHalfReaction(in)){
                    Toast.makeText(MainActivity.this, "Please enter a full chemical reaction!", Toast.LENGTH_LONG).show();
                }

                else if(MathExpression.isMathExpression(in)){
                    MathExpression m = new MathExpression(in);
                    input.setText("");
                    DecimalFormat df = new DecimalFormat("0.#####");
                    try{input.append(df.format(Double.parseDouble(m.getResult())));}
                    catch(Exception e){}
//                    imm.showSoftInput(input, InputMethodManager.SHOW_IMPLICIT);
                    log = "Math";
                }

                else{
                    FragmentManager fm = getFragmentManager();
                    FragmentTransaction ft = fm.beginTransaction();
                    ft.setCustomAnimations(R.animator.slide_in_left, R.animator.slide_out_right);
                    Fragment fragment = newBlankFragment(false);
                    ft.replace(R.id.fragment_container, fragment);
                    ft.commit();
                    log = "Could not Process";
                }

                if (in.equals("")){
                    log = "Blank";
                }

                progress.dismiss();

                event.putString(FirebaseAnalytics.Param.CONTENT_TYPE, log);
                mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, event);
                String time = DateFormat.getDateTimeInstance().format(new Date());
                String key = myRef.push().getKey();
                SearchDataPoint searchDataPoint = new SearchDataPoint(log, in, time);
                myRef.child(key).setValue(searchDataPoint);
            }
        });
    }

    protected void onPause(){
        super.onPause();
    }

    protected void onDestroy(){
        super.onDestroy();
    }

    public Fragment newEquationFragment(String eqn){
        Fragment fragment = new EquationFragment();
        Bundle bundle = new Bundle();
        bundle.putString("Key", eqn);

        fragment.setArguments(bundle);
        return fragment;
    }

    public Fragment newElementFragment(String eqn){
        Fragment fragment = new ElementFragment();
        Bundle bundle = new Bundle();
        bundle.putString("Key", eqn);
        fragment.setArguments(bundle);
        return fragment;
    }

    public Fragment newMoleculeFragment(String eqn){
        Fragment fragment = new MoleculeFragment();
        Bundle bundle = new Bundle();
        bundle.putString("Key", eqn);
        fragment.setArguments(bundle);
        return fragment;
    }

    public Fragment newAmountFragment(String eqn){
        Fragment fragment = new AmountFragment();
        Bundle bundle = new Bundle();
        bundle.putString("Key", eqn);
        fragment.setArguments(bundle);
        return fragment;
    }

    public Fragment newUnitFragment(String eqn){
        Fragment fragment = new UnitFragment();
        Bundle bundle = new Bundle();
        bundle.putString("Key", eqn);
        fragment.setArguments(bundle);
        return fragment;
    }

    public Fragment newBlankFragment(Boolean loading){
        Fragment fragment = new BlankFragment();
        Bundle bundle = new Bundle();
        bundle.putBoolean("Key", loading);
        fragment.setArguments(bundle);
        return fragment;
    }

    public Fragment newEmptyFragment(){
        Fragment fragment = new EmptyFragment();
        return fragment;
    }

    public String getData(){
        return in;
    }

}

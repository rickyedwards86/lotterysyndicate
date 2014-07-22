package com.boltify.lotterysyndicate;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.*;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;

public class MainActivity extends Activity {

    String errorMsg;
    EditText etAmount;
    CheckBox cbLotto;
    EditText etLottoDraws;
    EditText etLottoLines;
    CheckBox cbEuro;
    EditText etEuroDraws;
    EditText etEuroLines;
    CheckBox cbHealth;
    EditText etHealthDraws;
    EditText etHealthLines;
    CheckBox cbPlus;
    EditText etPlusDraws;
    EditText etPlusLines;
    EditText etWeeks;
    Spinner spinLottoDraws;
    Spinner spinEuroDraws;
    Spinner spinHealthDraws;
    Button btnCalculate;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        AdView layout = (AdView)findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR) //emulator
                .addTestDevice("2A22E853ABE1BFECDAACD77D565A8487") //Nexus 5
                .build();
        layout.loadAd(adRequest);

        etAmount = (EditText)findViewById(R.id.etAmount);
        cbLotto = (CheckBox)findViewById(R.id.cbLotto);
        etLottoDraws = (EditText)findViewById(R.id.etLottoDraw);
        etLottoLines = (EditText)findViewById(R.id.etLottoLines);
        cbEuro = (CheckBox)findViewById(R.id.cbEuroMillions);
        etEuroDraws = (EditText)findViewById(R.id.etEuroMillionsDraws);
        etEuroLines = (EditText)findViewById(R.id.etEuroMillionsLines);
        cbHealth = (CheckBox)findViewById(R.id.cbHealthLotto);
        etHealthDraws = (EditText)findViewById(R.id.etHealthLottoDraws);
        etHealthLines = (EditText)findViewById(R.id.etHealthLottoLines);
        cbPlus = (CheckBox)findViewById(R.id.cbPlus5);
        etPlusDraws = (EditText)findViewById(R.id.etPlus5Draws);
        etPlusLines = (EditText)findViewById(R.id.etPlus5Lines);
        etWeeks = (EditText)findViewById(R.id.etWeeks);
        spinLottoDraws = (Spinner)findViewById(R.id.Lottospinner);
        spinEuroDraws = (Spinner)findViewById(R.id.EuroSpinner);
        spinHealthDraws = (Spinner)findViewById(R.id.HealthSpinner);
        btnCalculate = (Button)findViewById(R.id.btnCalculate);

        spinLottoDraws.setEnabled(false);
        etLottoLines.setEnabled(false);
        spinEuroDraws.setEnabled(false);
        etEuroLines.setEnabled(false);
        spinHealthDraws.setEnabled(false);
        etHealthLines.setEnabled(false);
        btnCalculate.setEnabled(false);
        etWeeks.setEnabled(false);

        //Setup Options to only show EuroMillions if not in UK. Further work to add other country lotto's
        setupOptions();

        setupSpinner();

        final Button button = btnCalculate;
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                if (Validated())
                {
                    String CalculatedResult = CalculateCosts();
                    CreateDialog(CalculatedResult);
                }
                else
                {
                    Context context = getApplicationContext();
                    int duration = Toast.LENGTH_SHORT;

                    Toast toast = Toast.makeText(context, errorMsg, duration);
                    toast.show();
                }
            }
        });
    }

    private void setupOptions()
    {
        Locale locale = Locale.getDefault();
        if (!locale.equals(Locale.UK))
        {
            cbLotto.setEnabled(false);
            cbHealth.setEnabled(false);
        }
    }

    private void showAbout()
    {
        String strVersion = "1.0.0";
        TextView tvVersion;
        // Create custom dialog object
        final Dialog dialog = new Dialog(this);
        // Include dialog.xml file
        dialog.setContentView(R.layout.dialog_about);
        // Set dialog title
        dialog.setTitle("About");
        try {
            strVersion = getPackageManager().getPackageInfo(getPackageName(), 0).versionName;
        }
        catch(PackageManager.NameNotFoundException e) {

        }
        dialog.show();

        //Set correct Version Number
        tvVersion = (TextView)dialog.findViewById(R.id.tvVersion);
        tvVersion.setText("v"+strVersion);

        Button btnOk = (Button)dialog.findViewById(R.id.btnOk);
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Close dialog
                dialog.dismiss();
            }
        });

    }

    private void setupSpinner() {
// Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.week_array, android.R.layout.simple_spinner_item);
// Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
// Apply the adapter to the spinner
        spinLottoDraws.setAdapter(adapter);
        spinEuroDraws.setAdapter(adapter);
        spinHealthDraws.setAdapter(adapter);
    }

    public void enableDraw(View view)
    {
       CheckBox checkBox = (CheckBox)view;

       if (checkBox.getText().toString().matches("Lotto"))
       {
           if (checkBox.isChecked())
           {
                spinLottoDraws.setEnabled(true);
                etLottoLines.setEnabled(true);
           }
           else
           {
               spinLottoDraws.setEnabled(false);
               etLottoLines.setEnabled(false);
           }
       }
        else if (checkBox.getText().toString().matches("EuroMillions"))
       {
           if (checkBox.isChecked())
           {
           spinEuroDraws.setEnabled(true);
           etEuroLines.setEnabled(true);
           }
           else
           {
               spinEuroDraws.setEnabled(false);
               etEuroLines.setEnabled(false);
           }
       }
        else if (checkBox.getText().toString().matches("Health Lottery"))
       {
           if (checkBox.isChecked())
           {
               spinHealthDraws.setEnabled(true);
               etHealthLines.setEnabled(true);
           }
           else
           {
               spinHealthDraws.setEnabled(false);
               etHealthLines.setEnabled(false);
           }
       }

        if (cbLotto.isChecked() || cbEuro.isChecked() || cbHealth.isChecked())
        {
            btnCalculate.setEnabled(true);
            etWeeks.setEnabled(true);
        }
        else
        {
            btnCalculate.setEnabled(false);
            etWeeks.setEnabled(false);
        }

    }


    private Boolean Validated()
    {
        Boolean Passed = true;

        if (etAmount.getText().toString().matches(""))
        {
            errorMsg = getResources().getString(R.string.AmountRequired);
            Passed = false;
        }
        else if (cbLotto.isChecked() || cbEuro.isChecked() || cbHealth.isChecked() || cbPlus.isChecked())
        {
        if (cbLotto.isChecked())
        {
           if (etLottoLines.getText().toString().matches(""))
                {
                    errorMsg = getResources().getString(R.string.LottoLinesRequired);
                    Passed = false;
                }
        }
        if (cbEuro.isChecked())
        {
           if (etEuroLines.getText().toString().matches(""))
             {
                errorMsg = getResources().getString(R.string.EuroLinesRequired);
                Passed = false;
             }
        }
        if (cbHealth.isChecked())
        {
           if (etHealthLines.getText().toString().matches(""))
           {
                errorMsg = getResources().getString(R.string.HealthLinesRequired);
                Passed = false;
           }
        }
        }

        if (etWeeks.getText().toString().matches("") && Passed)
        {
            errorMsg = getResources().getString(R.string.WeeksRequired);
            Passed = false;
        }

        return Passed;
    }

    private static boolean isNumeric(String str)
    {
        try
        {
            double d = Double.parseDouble(str);
        }
        catch(NumberFormatException nfe)
        {
            return false;
        }
        return true;
    }

    private void CreateDialog(String _Result)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(_Result + " is required from each syndicate member")
                .setTitle(R.string.Result);
        builder.setPositiveButton(R.string.Ok, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                //When pressing OK, ask if an email is to be sent.
                //Need to add code!
                dialog.cancel();


            }
        });

// 3. Get the AlertDialog from create()
        AlertDialog dialog = builder.create();
        dialog.show();
    }

private String CalculateCosts()
{
    int intLottoDraws = 0;
    int intLottoLines = 0;
    int intEuroDraws = 0;
    int intEuroLines = 0;
    int intHealthDraws = 0;
    int intHealthLines = 0;
    int intPlus5Draws = 0;
    int intPlus5Lines = 0;
    int SyndicateNo = 0;
    double LottoTotal = 0;
    double EuroTotal = 0;
    double HealthTotal = 0;
    double Plus5Total = 0;
    double totalsResult = 0;
    double costTotal = 0;
    double finalResult = 0;
    int weeksNo = 0;

    weeksNo = Integer.parseInt(etWeeks.getText().toString());
    SyndicateNo = Integer.parseInt(etAmount.getText().toString());

    if (cbLotto.isChecked())
    {
        //intLottoDraws = Integer.parseInt(etLottoDraws.getText().toString());
        intLottoDraws = Integer.parseInt(spinLottoDraws.getSelectedItem().toString());
        intLottoLines = Integer.parseInt(etLottoLines.getText().toString());
        LottoTotal = intLottoLines * 2 * intLottoDraws;
    }
    if (cbEuro.isChecked())
    {
        intEuroDraws = Integer.parseInt(spinEuroDraws.getSelectedItem().toString());
        intEuroLines = Integer.parseInt(etEuroLines.getText().toString());
        EuroTotal = intEuroLines * 2 * intEuroDraws;
    }
    if (cbHealth.isChecked())
    {
        intHealthDraws = Integer.parseInt(spinHealthDraws.getSelectedItem().toString());
        intHealthLines = Integer.parseInt(etHealthLines.getText().toString());
        HealthTotal = intHealthLines * 1 * intHealthDraws;
    }

    totalsResult = (LottoTotal + EuroTotal + HealthTotal) * 100;
    costTotal = Math.round((totalsResult * weeksNo) / SyndicateNo);

    DecimalFormat twoDForm = new DecimalFormat("##.##");
    finalResult = Float.parseFloat(twoDForm.format(costTotal));
    finalResult = finalResult / 100;

    NumberFormat n = NumberFormat.getCurrencyInstance();
    String s = n.format(finalResult);

    //return String.valueOf(s);
    return s;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.main, menu);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.menuAbout:
                showAbout();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    /*@Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {
            case R.id.action_settings:
                return true;
        }
        return super.onOptionsItemSelected(item);
    }*/

}

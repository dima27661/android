package com.obd.infrared.sample;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.obd.infrared.InfraRed;
import com.obd.infrared.log.LogToEditText;
import com.obd.infrared.patterns.PatternAdapter;
import com.obd.infrared.patterns.PatternConverter;
import com.obd.infrared.patterns.PatternType;
import com.obd.infrared.transmit.TransmitInfo;
import com.obd.infrared.transmit.TransmitterType;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends ActionBarActivity implements View.OnClickListener {

    private LogToEditText log;
    private InfraRed infraRed;
    private TransmitInfo[] patterns;
    TextView textView;
    EditText editText1;
    Button SetIntervalBtn;
    Button transmitButton;

    private int[] SignalsData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        String TAG = "IR";

        transmitButton = (Button) this.findViewById(R.id.transmit_button);
        transmitButton.setOnClickListener(this);

        SetIntervalBtn = (Button) this.findViewById(R.id.button1);
        SetIntervalBtn.setOnClickListener(this);

        editText1 = (EditText) findViewById(R.id.editText1);

        // Log messages print to EditText
        EditText console = (EditText) this.findViewById(R.id.console);
        log = new LogToEditText(console, TAG);

        textView = (TextView) this.findViewById(R.id.textView);

        // Log messages print with Log.d(), Log.w(), Log.e()
        // LogToConsole log = new LogToConsole(TAG);

        // Turn off log
        // LogToAir log = new LogToAir(TAG);

        infraRed = new InfraRed(this, log);
        // detect transmitter type
        TransmitterType transmitterType = infraRed.detect();

        // initialize transmitter by type
        infraRed.createTransmitter(transmitterType);



//---------------------------------------------------------------------------------------------------------------------
        // initialize raw patterns
//        List<PatternConverter> rawPatterns = new ArrayList<>();
//
//        SignalsData = new int[] { 2000, 27800, 400, 1600, 400, 3600, 400, 200};
//        // Nikon v.3
//        rawPatterns.add(new PatternConverter(PatternType.Intervals, 38000, 2000, 27800, 400, 1600, 400, 3600, 400, 200));
//        rawPatterns.add(new PatternConverter(PatternType.Intervals, 38000, SignalsData));
//
//
//        // Canon
//        // rawPatterns.add(new PatternConverter(PatternType.Intervals, 33000, 500, 7300, 500, 200));
//        // Nikon v.2
//        //    rawPatterns.add(new PatternConverter(PatternType.Cycles, 38400, 77, 1069, 16, 61, 16, 137, 16, 2427, 77, 1069, 16, 61, 16, 137, 16));
//        // Nikon v.3 fromString
//        // rawPatterns.add(PatternConverterUtils.fromString(PatternType.Intervals, 38000, "2000, 27800, 400, 1600, 400, 3600, 400, 200"));
//        // Nikon v.3 fromHexString
//        // rawPatterns.add(PatternConverterUtils.fromHexString(PatternType.Intervals, 38000, "0x7d0 0x6c98 0x190 0x640 0x190 0xe10 0x190 0xc8"));
//        // Nikon v.3 fromHexString without 0x
//        // rawPatterns.add(PatternConverterUtils.fromHexString(PatternType.Intervals, 38000, "7d0 6c98 190 640 190 e10 190 c8"));
//
//
//        // adapt the patterns for the device that is used to transmit the patterns
//        PatternAdapter patternAdapter = new PatternAdapter(log);
//
//        TransmitInfo[] transmitInfoArray = new TransmitInfo[rawPatterns.size()];
//        for (int i = 0; i < transmitInfoArray.length; i++) {
//            transmitInfoArray[i] = patternAdapter.createTransmitInfo(rawPatterns.get(i));
//        }
//        this.patterns = transmitInfoArray;
//
//        for (TransmitInfo transmitInfo : this.patterns) {
//            log.log(transmitInfo.toString());
//        }

  //------------------------------------------------------------------------------------------------------------------


    } // end onCreate


    @Override
    protected void onResume() {
        super.onResume();
        infraRed.start();
    }


    private int currentPattern = 0;

    @Override
    public void onClick(View v) {
        TransmitInfo transmitInfo;


        switch (v.getId()) {
            case R.id.transmit_button:

                transmitInfo = patterns[currentPattern++];
                if (currentPattern >= patterns.length) currentPattern = 0;
                infraRed.transmit(transmitInfo);

                break;
            case R.id.button1:






//                textView.setText("Кнопку нажал");

                // initialize raw patterns
                List<PatternConverter> rawPatterns = new ArrayList<>();
//                SignalsData = new int[] { 2000, 27800, 400, 1600, 400, 3600, 400, 200};
                SignalsData = StringToIntArray (editText1.getText().toString() ) ;

                // Nikon v.3
//                rawPatterns.add(new PatternConverter(PatternType.Intervals, 38000, 2000, 27800, 400, 1600, 400, 3600, 400, 200));
                rawPatterns.add(new PatternConverter(PatternType.Intervals, 38400, SignalsData));
                rawPatterns.add(new PatternConverter(PatternType.Intervals, 38400, SignalsData));

                // adapt the patterns for the device that is used to transmit the patterns
                PatternAdapter patternAdapter = new PatternAdapter(log);

                TransmitInfo[] transmitInfoArray = new TransmitInfo[rawPatterns.size()];
                for (int i = 0; i < transmitInfoArray.length; i++) {
                    transmitInfoArray[i] = patternAdapter.createTransmitInfo(rawPatterns.get(i));
                }
                this.patterns = transmitInfoArray;

//        for (transmitInfo : this.patterns) {
//            log.log(transmitInfo.toString());
//        }

                transmitInfo = patterns[currentPattern++];
                if (currentPattern >= patterns.length) currentPattern = 0;
                infraRed.transmit(transmitInfo);




                break;

            default:
                break;

        }



    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        infraRed.stop();

        if (log != null) {
            log.destroy();
        }
    }

    public static int[] StringToIntArray(String stringFrom) {
     // String stringFrom = "12 41 21 19 15 10";
     // The string you want to be an integer array.
        String[] integerStrings = stringFrom.split(",");
     // Splits each spaced integer into a String array.
        int[] integers = new int[integerStrings.length];
     // Creates the integer array.
        for (int i = 0; i < integers.length; i++) {
            integers[i] = Integer.parseInt( integerStrings[i].trim() );
            //Parses the integer for each string.
        }
        return integers;
    }



}

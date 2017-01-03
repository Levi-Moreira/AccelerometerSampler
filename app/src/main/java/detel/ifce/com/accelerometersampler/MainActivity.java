package detel.ifce.com.accelerometersampler;

import android.Manifest;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.widget.EditText;
import android.widget.Toast;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.karumi.dexter.listener.single.PermissionListener;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity implements SensorEventListener {

    private SensorManager senSensorManager;
    private Sensor senAccelerometer;
    private long lastUpdate;
    private boolean mSampleOn = false;
    private ArrayList<String> mDataX = new ArrayList<>();
    private ArrayList<String> mDataY = new ArrayList<>();
    private ArrayList<String> mDataZ = new ArrayList<>();

    private ArrayList<String> mDataFinalX = new ArrayList<>();
    private ArrayList<String> mDataFinalY = new ArrayList<>();
    private ArrayList<String> mDataFinalZ = new ArrayList<>();

    private float[] gravity = new float[3];

    @BindView(R.id.output)
    EditText mOutputArea;
    private int sampleCount = 1;
    private int sessionCOunt = 1;

    Context theContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);
        theContext = this;

        senSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        senAccelerometer = senSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        senSensorManager.registerListener(this, senAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);
        Dexter.initialize(this);


    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        Sensor mySensor = sensorEvent.sensor;

        if (mySensor.getType() == Sensor.TYPE_ACCELEROMETER) {

            final float alpha = 0.8f;


            gravity[0] = alpha * gravity[0] + (1 - alpha) * sensorEvent.values[0];
            gravity[1] = alpha * gravity[1] + (1 - alpha) * sensorEvent.values[1];
            gravity[2] = alpha * gravity[2] + (1 - alpha) * sensorEvent.values[2];

            float x = sensorEvent.values[0] - gravity[0];
            float y = sensorEvent.values[1] - gravity[1];
            float z = sensorEvent.values[2] - gravity[2];

            long curTime = System.currentTimeMillis();


            if ((curTime - lastUpdate) > 40) {
                long diffTime = (curTime - lastUpdate);
                lastUpdate = curTime;
                if (mSampleOn) {
                    mDataX.add(x + ",");
                    mDataY.add(y + ",");
                    mDataZ.add(z + ",");
                    mOutputArea.append(x + "," + y + "," + z + "\n");
                }
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    protected void onPause() {
        super.onPause();
        senSensorManager.unregisterListener(this);
    }

    protected void onResume() {
        super.onResume();
        senSensorManager.registerListener(this, senAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);
    }

    public void generateNoteOnSD(String sFileName, ArrayList<String> mDataX, ArrayList<String> mDataY, ArrayList<String> mDataZ) {
        try {

            //saving x
            File root = new File(Environment.getExternalStorageDirectory(), "Notes");
            if (!root.exists()) {
                root.mkdirs();
            }

            File gpxfile = new File(root,"X"+sFileName);
            FileWriter writer = new FileWriter(gpxfile);
            for (String sBody : mDataX) {
                writer.append(sBody+"\n");
            }
            writer.flush();
            writer.close();


            //saving y

            File gpyfile = new File(root, "Y"+sFileName);
            writer = new FileWriter(gpyfile);
            for (String sBody : mDataY) {
                writer.append(sBody+"\n");
                //writer.append('\n');
            }
            writer.flush();
            writer.close();



            //saving z
            File gpzfile = new File(root, "Z"+sFileName);
            writer = new FileWriter(gpzfile);
            for (String sBody : mDataZ) {
                writer.append(sBody+"\n");
                //writer.append('\n');
            }
            writer.flush();
            writer.close();
            MessageUtil.showMessage(theContext, sFileName + " Saved");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @OnClick(R.id.start_sampling)
    public void startSamplingBtn() {
        mDataX.clear();
        mDataY.clear();
        mDataZ.clear();
        mOutputArea.append("\nSample#"+sampleCount++ +"******\n");
        mSampleOn = true;

    }


    @OnClick(R.id.stop_sampling)
    public void stopSamplingBtn() {
        mSampleOn = false;
        String X = "";
        String Y = "";
        String Z = "";

        for (String sBody : mDataX) {
            X=X+sBody;
        }

        for (String sBody : mDataY) {
            Y=Y+sBody;
        }

        for (String sBody : mDataZ) {
            Z=Z+sBody;
        }


        mDataFinalX.add(X);
        mDataFinalY.add(Y);
        mDataFinalZ.add(Z);

    }
    @OnClick(R.id.finish_sampling)
    public void finishSamplingBtn() {
        Dexter.checkPermissionsOnSameThread(new MultiplePermissionsListener() {
            @Override
            public void onPermissionsChecked(MultiplePermissionsReport report) {
                if (report.areAllPermissionsGranted()) {
                    //final Calendar t = Calendar.getInstance();
                    mOutputArea.append("\n-------------------------------------SESSION#"+sessionCOunt+"-------------------------------------");
                    String fileName = "Sample Session#" + sessionCOunt++ + ".csv";
                    generateNoteOnSD(fileName, mDataFinalX, mDataFinalY, mDataFinalZ);
                    mDataFinalX.clear();
                    mDataFinalY.clear();
                    mDataFinalZ.clear();

                }
            }

            @Override
            public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                token.continuePermissionRequest();
            }
        }, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE);

    }
}

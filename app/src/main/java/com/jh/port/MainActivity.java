package com.jh.port;

import android.graphics.drawable.RippleDrawable;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    private String TAG = "SerialPort";

    private FileInputStream inputStream;
    private FileOutputStream outputStream;
    private SerialPort serialPort;
    StringBuffer stringBuffer = new StringBuffer();
    Handler handler=new Handler();
    DataRecived dataRecived =new DataRecived();


    private EditText name;
    private EditText speed;
    private EditText flag;
    private EditText times;

    private Button openBtn;
    private Button closeBtn;

    Thread thread;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        name =(EditText) findViewById(R.id.name);
        speed=(EditText) findViewById(R.id.speed);
        flag=(EditText) findViewById(R.id.flag);
        times=(EditText) findViewById(R.id.times);

        openBtn=(Button)findViewById(R.id.openBtn);
        closeBtn=(Button)findViewById(R.id.closeBtn);

        openBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nameStr=name.getText().toString();
                int speedC=Integer.parseInt(speed.getText().toString());
                int flagC=Integer.parseInt(flag.getText().toString());

                openPort(nameStr,speedC,flagC);
            }
        });

        closeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closePort();
            }
        });


    }


    private class DataRecived implements  Runnable{
        @Override
        public void run() {

            Log.d(TAG, "DataRecived run: ");

            if (stringBuffer.length() != 0){
                String msg=stringBuffer.toString();

                Log.d(TAG, msg);

                stringBuffer.delete(0,stringBuffer.length());
            }

        }
    }
    private void openPort(String name,int speed,int flag){
        Log.d(TAG, "name: "+name +"|||| speed"+speed +"|||||"+flag);

        try {
            serialPort = new SerialPort(new File("dev/"+name), speed, flag);

        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (serialPort != null) {
            inputStream = (FileInputStream) serialPort.getInputStream();
            outputStream = (FileOutputStream) serialPort.getOutputStream();
        } else {
            Toast.makeText(this, "open failed !!", Toast.LENGTH_SHORT).show();
        }


        //接收

        thread = new Thread() {
            @Override
            public void run() {
                int i=0;int count=1000;
                Log.d(TAG, "will recive");
                while (inputStream != null) {

                    int size;

                    try {
                        int len = inputStream.available();

                       i ++;


                        if (i >count ){
                            Log.d(TAG, "loop: " + len);

                            count=Integer.parseInt(times.getText().toString());

                            i=0;
                        }


                        if (len > 0) {
                            byte[] buffer = new byte[len];

                            size = inputStream.read(buffer);

                            stringBuffer.append(new String(buffer,0,size));

                            handler.post(dataRecived);
                        }

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                super.run();
            }

        };

        thread.start();


    }

    private void closePort(){

        if (serialPort == null) return;
        serialPort.close();
        serialPort = null;

        inputStream=null;

        thread.stop();
    }
}



package com.example.shail.piledandroidcontrol;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.Toast;
import android.widget.ToggleButton;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

public class PiLED extends AppCompatActivity
{
    Switch LED1;
    ToggleButton LED2;
    Button LED3;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pi_led);

        LED1 = (Switch) findViewById(R.id.led1Button);
        LED2 = (ToggleButton) findViewById(R.id.led2Button);
        LED3 = (Button) findViewById(R.id.led3Button);

        /*******************************************************/
             /*  Set an onclick/onchange listener for every button  */
        /*******************************************************/

        LED1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
        {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
            {
                if (isChecked)
                {
                    new Background_get().execute("led1=1");
                }
                else
                {
                    new Background_get().execute("led1=0");
                }
            }
        });

        LED2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
        {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
            {
                if(isChecked)
                {
                    new Background_get().execute("led2=1");
                }
                else
                {
                    new Background_get().execute("led2=0");
                }
            }
        });

        LED3.setOnTouchListener(new View.OnTouchListener()
        {
            @Override
            public boolean onTouch(View v, MotionEvent event)
            {
                Background_get bg = new Background_get();
                if (event.getAction() == MotionEvent.ACTION_DOWN)
                {
//                    new Background_get().execute("led3=1");
//                    new Background_get().onPostExecute();
                    bg.execute("led3=1");
                    bg.onPostExecute();

                }
                else if (event.getAction() == MotionEvent.ACTION_UP)
                {
                    bg.execute("led3=0");
                    bg.onPostExecute();
                }
                return true;
            }
        });
    }

    /*****************************************************/
         /*  This is a background process for connecting */
         /*   to the raspberry server and sending        */
         /*    the GET request with the added data       */
    /*****************************************************/

    private class Background_get extends AsyncTask<String, Void, String>
    {
        HttpURLConnection connection;
        CharSequence serverURL;

        @Override
        protected String doInBackground(String... params)
        {
            try
            {
                serverURL = "http://192.168.0.16/"; //home
//                CharSequence serverURL = "http://192.168.2.82/"; //home
                URL url = new URL(serverURL + "?" + params[0]); //home
                connection = (HttpURLConnection) url.openConnection();

                connection.getInputStream();
                Log.d("Bhavya", ":=: openedConnection");

                boolean status = (connection != null);
                connection.disconnect();
                return status ? "true" : "false";

            } catch (IOException e)
            {
                e.printStackTrace();
            }
            return null;
        }

        protected void onPostExecute()
        {
                CharSequence text;
                if (connection == null)
                {
                    text = "Could not connect to server: " + serverURL;
                }
                else
                {
                    text = "Connected to server: " + serverURL;
                }
                Context context = getApplicationContext();
                int toastDuration = Toast.LENGTH_SHORT;
                Toast.makeText(context, text, toastDuration).show();
        }

    }
}

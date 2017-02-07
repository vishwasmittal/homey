package com.cryptonites.homey.cfd;

// this project will control the home appliances over the voice command of user

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.speech.RecognizerIntent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.cryptonites.homey.cfd.RetrofitPackage.ApiClient;
import com.cryptonites.homey.cfd.RetrofitPackage.LuisApi;
import com.cryptonites.homey.cfd.modelclasses.Entity;
import com.cryptonites.homey.cfd.modelclasses.LuisResponse;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    //TODO: hide this subscription key
    private String apiSubscriptionKey = LuisApiKey.key;

    private String receivedResponseFromPi;
    private Appliance fan, tv, light;
    private Button fanBtn, tvBtn, lightBtn;
    private EditText editTextOfSpeech;
    private ImageButton speechButton, sendButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fan = new Appliance("fan");
        tv = new Appliance("tv");
        light = new Appliance("light");

        // referencing buttons
        speechButton = (ImageButton) findViewById(R.id.speech_button);
        sendButton = (ImageButton) findViewById(R.id.send_button);
        editTextOfSpeech = (EditText) findViewById(R.id.edit_text_of_speech);
        fanBtn = (Button) findViewById(R.id.fan_button);
        fanBtn.setText("Fan: OFF");
        tvBtn = (Button) findViewById(R.id.tv_button);
        tvBtn.setText("TV: OFF");
        lightBtn = (Button) findViewById(R.id.light_button);
        lightBtn.setText("Light: OFF");

        //setting onClick listeners
        fanBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Appliance app = new Appliance(fan);
                app.setState(!app.getState());
                ApplianceControl(app, fanBtn);
            }
        });

        lightBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Appliance app = new Appliance(light);
                app.setState(!app.getState());
                ApplianceControl(app, lightBtn);
            }
        });

        tvBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Appliance app = new Appliance(tv);
                app.setState(!app.getState());
                ApplianceControl(app, tvBtn);
            }
        });


        speechButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onSpeechButtonClick();
            }
        });

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                connectToLuis(editTextOfSpeech.getText().toString());
            }
        });
    }


    public void ApplianceControl(Appliance appliance, Button applButton) {

        //this will call the server to switch the appliance
        piConnect(appliance.getJsonObject().toString(), appliance, applButton);
    }

    public void ApplianceControl() {
        Toast.makeText(getApplicationContext(), "My Responses are limited, please try again", Toast.LENGTH_SHORT).show();
    }

    private LuisResponse luisResponse;          //this is the result obtained after invoking luis

    //______________________contact luis to get the result analysed_________________________________
    private void connectToLuis(String voiceInput) {

        if (!voiceInput.matches("")) {
//        voiceInput = "switch on tv";
            String query = voiceInput.replace(" ", "%20");

//            Toast.makeText(getApplicationContext(), query, Toast.LENGTH_SHORT).show();
            final LuisApi luisApi = ApiClient.getClient().create(LuisApi.class);

            Call<LuisResponse> call = luisApi.getResponse("luis/v2.0/apps/06fd0339-ce18-40ec-ab34-551fbd428eec?subscription-key=" +
                    apiSubscriptionKey + "&q=" + query + "&verbose=true");
            call.enqueue(new Callback<LuisResponse>() {
                @Override
                public void onResponse(Call<LuisResponse> call, Response<LuisResponse> response) {
                    luisResponse = response.body();
                    Log.e("main Activity", luisResponse.toString());
                    Log.e("Retrofit", "Got It!!!");
                    if (luisResponse.getTopScoringIntent().getIntent().matches("none")) {
                        ApplianceControl();
                    } else if (luisResponse.getTopScoringIntent().getIntent().matches("start")) {
                        String requestedApplName = new String();
                        Button requestedApplButton;
                        String entityString = "";
                        for (Entity e : luisResponse.getEntities()) {
                            entityString += e.getType() + " ";

                        }
                        Log.e("main Activity", entityString);
                        if (entityString.contains("appliance") && entityString.contains("switching")) {
                            for (Entity e : luisResponse.getEntities()) {
                                if (e.getType().contains("appliance")) {
                                    requestedApplName = e.getEntity();
                                }
                            }
                            Appliance appl = new Appliance(requestedApplName, true);
                            switch (requestedApplName) {
                                case "fan":
                                    requestedApplButton = (Button) findViewById(R.id.fan_button);
                                    break;
                                case "tv":
                                    requestedApplButton = (Button) findViewById(R.id.tv_button);
                                    break;
                                case "light":
                                    requestedApplButton = (Button) findViewById(R.id.light_button);
                                    break;
                                default:
                                    requestedApplButton = null;
                            }
                            ApplianceControl(appl, requestedApplButton);
                        } else {
                            ApplianceControl();
                        }
                    } else {
                        String requestedApplName = new String();
                        Button requestedApplButton = new Button(getApplicationContext());
                        String entityString = "";
                        for (Entity e : luisResponse.getEntities()) {
                            entityString += e.getType() + " ";

                        }
                        Log.e("main Activity", entityString);
                        if (entityString.contains("appliance") && entityString.contains("switching")) {
                            for (Entity e : luisResponse.getEntities()) {
                                if (e.getType().contains("appliance")) {
                                    requestedApplName = e.getEntity();
                                }
                            }
                            Appliance appl = new Appliance(requestedApplName, false);
                            switch (requestedApplName) {
                                case "fan":
                                    requestedApplButton = (Button) findViewById(R.id.fan_button);
                                    break;
                                case "tv":
                                    requestedApplButton = (Button) findViewById(R.id.tv_button);
                                    break;
                                case "light":
                                    requestedApplButton = (Button) findViewById(R.id.light_button);
                                    break;
                                default:
                                    requestedApplButton = null;
                            }
                            ApplianceControl(appl, requestedApplButton);
                        } else {
                            ApplianceControl();
                        }
                    }
                }

                @Override
                public void onFailure(Call<LuisResponse> call, Throwable t) {
                    Toast.makeText(getApplicationContext(), "Can't analyse the command", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Toast.makeText(getApplicationContext(), "Please Enter Some Command", Toast.LENGTH_SHORT).show();
        }
    }


    // _____________________________________Thread Handling_____________________________________________
    Button extraButton;
    Appliance extraAppliance;
    private Handler handleIf = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            Toast.makeText(getApplicationContext(), "State Changed", Toast.LENGTH_SHORT).show();
            extraButton.setText((extraAppliance.getName() + ": ON"));
        }
    };

    private Handler handleElse = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            Toast.makeText(getApplicationContext(), receivedResponseFromPi, Toast.LENGTH_SHORT).show();
            extraButton.setText((extraAppliance.getName() + ": OFF"));
        }
    };

    private Handler handleException = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            Toast.makeText(getApplicationContext(), receivedResponseFromPi, Toast.LENGTH_SHORT).show();
        }
    };

    // setting the thread to communicate with pi.......................................................
    public String piConnect(final String applianceJson, final Appliance appliance, Button applButton) {
        extraButton = applButton;
        Runnable r = new Runnable() {
            @Override
            public void run() {

                try { //192.168.1     9999
//                    String hostName = "192.168.43.89";
                    String hostName = "192.168.0.2";
                    Socket client = new Socket(hostName, 8000);
                    Log.e("MainActivity", "connected to server");
                    PrintWriter sendAppliance = new PrintWriter(client.getOutputStream());
                    sendAppliance.write(applianceJson);
                    sendAppliance.flush();
                    InputStreamReader getResponse = new InputStreamReader(client.getInputStream());

                    int receivedChar;
                    ArrayList inputList = new ArrayList();
                    while ((receivedChar = getResponse.read()) != -1) {
                        if ((char) receivedChar == '\n') {
                            break;
                        } else {
                            inputList.add((char) receivedChar);
                        }
                    }

                    char[] inputCharList = new char[inputList.size()];
                    for (int i = 0; i < inputList.size(); ++i) {
                        inputCharList[i] = (char) inputList.get(i);
                    }
                    Log.e("MainActivity", "read all characters from buffer");
                    receivedResponseFromPi = new String(inputCharList);               //this is the server response to the request made by client

                    sendAppliance.close();
                    getResponse.close();
                    client.close();
                    Log.e("MainActivity", "connection closed");
                    extraAppliance = appliance;

                    //since now the list is small thus, the checking by manual brute force, for long list, use brute on list
                    if (extraAppliance.getName().matches("fan")) {
                        fan = extraAppliance;
                    } else if (extraAppliance.getName().matches("light")) {
                        light = extraAppliance;
                    } else {
                        tv = extraAppliance;
                    }

                    String s = receivedResponseFromPi;

                    if (s != null) {
                        if (s.contains("true")) {
                            handleIf.sendEmptyMessage(0);
                        } else {
                            handleElse.sendEmptyMessage(0);
                        }
                    }

                } catch (UnknownHostException e) {
                    e.printStackTrace();
                    Log.e("thread", "Unknown host exception");
                    receivedResponseFromPi = "No Response";
                    handleException.sendEmptyMessage(0);
                } catch (IOException e) {
                    e.printStackTrace();
                    Log.e("thread", "IO exception");
                    receivedResponseFromPi = "No Response";
                    handleException.sendEmptyMessage(0);
                }

            }
        };
        receivedResponseFromPi = "";
        Thread piThread = new Thread(r);
        piThread.start();
        return receivedResponseFromPi;
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    public void onSpeechButtonClick() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "At your command!");

        try {
            startActivityForResult(intent, 100);
        } catch (ActivityNotFoundException a) {
            Toast.makeText(MainActivity.this, "Sorry! Your device does not support voice recognition...", Toast.LENGTH_SHORT).show();
        }
    }

    public void onActivityResult(int request_code, int result_code, Intent i) {
        super.onActivityResult(request_code, result_code, i);

        switch (request_code) {
            case 100:
                if (result_code == RESULT_OK && i != null) {
                    ArrayList<String> result = i.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    editTextOfSpeech.setText(result.get(0));
                    connectToLuis(editTextOfSpeech.getText().toString());
                }
                break;
        }
    }


}// End of class

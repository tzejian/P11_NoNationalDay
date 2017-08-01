package com.example.a127107.p11_nonationalday;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    ListView listView ;
    final int code = 123;
    int checkCode;
    int score;
    String[] values = new String[] { "Singapore is expensive",
            "Singapore has high inflation",
            "Singapore is small",
            "Singapore is 52 years old"

    };


    @Override
    protected void onStart() {
        super.onStart();
        //dialog



        SharedPreferences prefs = this.getBaseContext().getSharedPreferences("code", Context.MODE_PRIVATE);
        boolean isLoggedIn = prefs.getBoolean("isLoggedIn", false);
        if (isLoggedIn) {
            LayoutInflater inflater = (LayoutInflater)
                    getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            LinearLayout passPhrase =
                    (LinearLayout) inflater.inflate(R.layout.parsephase, null);

            final EditText etPassphrase = (EditText) passPhrase
                    .findViewById(R.id.editTextPassPhrase);

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Please Login")
                    .setView(passPhrase)
                    .setNegativeButton("No access code", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Toast.makeText(MainActivity.this, "No code", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .setPositiveButton("Done", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int id) {
                            checkCode = Integer.parseInt(etPassphrase.getText().toString());
                            if(code == checkCode){
                                Toast.makeText(MainActivity.this, "Welcome to NoNationalDay", Toast.LENGTH_SHORT).show();
                                dialog.dismiss();
                                SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
                                SharedPreferences.Editor prefEdit = prefs.edit();
                                prefEdit.putBoolean("isLoggedIn", true );
                                prefEdit.apply();
                            }else{
                                Intent intent = new Intent(getApplicationContext(), byebye.class);
                                startActivity(intent);
                            }

                        }
                    });

            AlertDialog alertDialog = builder.create();
            alertDialog.show();


        }


    }
    protected void onStop(){
        super.onStop();
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor prefEdit = prefs.edit();
        prefEdit.putString("accessCode", accessCode);
        prefEdit.commit();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Get ListView object from xml
        listView = (ListView) findViewById(R.id.lv);

        // Defined Array values to show in ListView



        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, android.R.id.text1, values);


        // Assign adapter to ListView
        listView.setAdapter(adapter);








    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.options, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Tally against the respective action item clicked
        //  and implement the appropriate action
        if (item.getItemId() == R.id.action_send) {
            String [] list = new String[] { "Email", "SMS" };
            new AlertDialog.Builder(this)
                    .setTitle("Select the way you want to send")
                    .setItems(list, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if (which == 0) {
                                Intent email = new Intent(Intent.ACTION_SEND);
                                email.putExtra(Intent.EXTRA_SUBJECT, "Fun facts about Singapore!");

                                email.putExtra(Intent.EXTRA_TEXT, values);
                                email.setType("message/rfc822");

                                startActivity(Intent.createChooser(email,
                                        "Choose an Email client :"));

                            }else {

                                Intent intent = new Intent(Intent.ACTION_MAIN);
                                intent.addCategory(Intent.CATEGORY_APP_MESSAGING);
                                intent.putExtra("sms", values);
                                startActivity(intent);
                            }

                        }
                    })



                    .show();
        } else if (item.getItemId() == R.id.action_quiz) {
            LayoutInflater inflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            LinearLayout quiz = (LinearLayout)inflater.inflate(R.layout.quiz, null);
            final RadioGroup rg1 = (RadioGroup)quiz.findViewById(R.id.radioGroup1);
            final RadioGroup rg2 = (RadioGroup)quiz.findViewById(R.id.radioGroup2);
            final RadioGroup rg3 = (RadioGroup)quiz.findViewById(R.id.radioGroup3);


            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Please Enter")
                    .setView(quiz)
                    .setNegativeButton("Im a Fool", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                        }
                    })
                    .setPositiveButton("Done", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int id) {


                            final int selectedButtonId = rg1.getCheckedRadioButtonId();


                            final int selectedButtonId2 = rg2.getCheckedRadioButtonId();


                            final int selectedButtonId3 = rg3.getCheckedRadioButtonId();



                            if(selectedButtonId == (R.id.radio1No)) {
                                score++;
                            }if(selectedButtonId2 == (R.id.radio2Yes)){
                                score++;
                            }if (selectedButtonId3 == (R.id.radio3Yes)) {
                                score++;
                            }
                            Toast.makeText(MainActivity.this, "Score: " + score, Toast.LENGTH_SHORT).show();
                            score = 0;
                        }

                    });

            AlertDialog alertDialog = builder.create();
            alertDialog.show();
        } else if (item.getItemId() == R.id.action_quit) {
            new AlertDialog.Builder(this)
                    .setTitle("Are you sure you want to quit ")
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener()
                    {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                        }

                    })
                    .setNegativeButton("No", null)
                    .show();
        }
        return super.onOptionsItemSelected(item);
    }

}

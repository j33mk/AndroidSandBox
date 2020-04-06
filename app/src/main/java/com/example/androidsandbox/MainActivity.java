package com.example.androidsandbox;

import android.database.Cursor;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Calendar;


public class MainActivity extends AppCompatActivity {
    private Button mSqlDbBtn;
    private Button btnGetData;
    private TextView txtData;
    private NearByDeviceDBHelper myDb;
    //    private UserProfileDBHelper myDb;
    private Button btnGetHelp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mSqlDbBtn = findViewById(R.id.btnSqlDb);
        btnGetData = findViewById(R.id.btnGetData);
        btnGetHelp = findViewById(R.id.btnHelp);
        txtData = findViewById(R.id.txtData);
        myDb = new NearByDeviceDBHelper(this);
        txtData.setMovementMethod(new ScrollingMovementMethod());
        mSqlDbBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    String currentDateTime = java.text.DateFormat.getDateTimeInstance().format(Calendar.getInstance().getTime());
                    //you can encrypt any text of your choice. I am encrypting my phone number using device wifi mac address
                    String encPhoneNumber = new AESEncryptionHelper(SystemUtilHelper.getMacAddr().replace(":", "")).encrypt("03349499395");
                    boolean isInserted = myDb.insertData("FFxkkdlsile", encPhoneNumber, currentDateTime);
                    if (isInserted == true)
                        Toast.makeText(MainActivity.this, "Data Inserted", Toast.LENGTH_LONG).show();
                    else {
                        Toast.makeText(MainActivity.this, "Data not Inserted", Toast.LENGTH_LONG).show();
                    }
                } catch (Exception ex) {
                    Toast.makeText(MainActivity.this, ex.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
        btnGetData.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Cursor res = myDb.getAllData();
                        if (res.getCount() == 0) {
                            // show message
                            Toast.makeText(MainActivity.this, "No data found in database", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        StringBuffer buffer = new StringBuffer();
                        while (res.moveToNext()) {
                            buffer.append("Id :" + res.getString(0) + "\n");
                            buffer.append("NearByDevice :" + res.getString(1) + "\n");
                            buffer.append("PhoneNumber :" + res.getString(2) + "\n");
                            try {
                                String decryptedPhoneNumber = new AESEncryptionHelper(SystemUtilHelper.getMacAddr().replace(":", "")).decrypt(res.getString(2));
                                buffer.append("DecryptedPhone: " + decryptedPhoneNumber + "\n");
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            buffer.append("DiscoveredAt :" + res.getString(3) + "\n\n");
                        }
                        txtData.setText("");
                        // Show all data
                        txtData.setText(buffer.toString());

                    }
                }
        );
        btnGetHelp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //this mac address is used to encrypt data in your application
                try {
                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                    builder.setCancelable(true);
                    builder.setTitle("Hey Izhar!!");
                    builder.setMessage("I am using Wifi mac address to encrypt the phone number in the database. Note that all over app the same database name will " +
                            "be used which is covid19.db when you create the instance of the UserProfileDBHelper or NearByDeviceDBHelper class so this will " +
                            "automatically check database or create one if don't exists also it will create respective tables. " +
                            "The UserProfile Table stores the current user data while nearby devices table stores any devices which are discovered.." +
                            "If you have still any doubt or questions ping me");
                    builder.show();

                } catch (Exception ex) {
                    Toast.makeText(MainActivity.this, ex.getMessage(), Toast.LENGTH_SHORT).show();
                }

            }
        });


    }

}

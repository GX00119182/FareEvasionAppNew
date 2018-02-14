package com.example.gavin_000.fareevasionappnew;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.provider.MediaStore;
import android.provider.SyncStateContract;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.example.gavin_000.fareevasionappnew.models.Offender;

import java.io.ByteArrayOutputStream;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    TextView tvOpenCamera;

    private final int CAMERA_RESULT = 101;
    //ImageView imageViewPic;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tvOpenCamera = (TextView)findViewById(R.id.tvOpenCamera);
        //imageViewPic = (ImageView)findViewById(R.id.imageViewPic);
        tvOpenCamera.setOnClickListener(new View.OnClickListener()
        {
            @Override

            public void onClick(View v)
            {
                if(ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED)
                {
                    dispatchTakenPictureIntent();
                }
                else
                {
                    if(shouldShowRequestPermissionRationale(Manifest.permission.CAMERA))
                    {
                        Toast.makeText(getApplicationContext(),"Permission needed", Toast.LENGTH_LONG).show();
                    }
                    requestPermissions(new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, CAMERA_RESULT);
                }
            }

        });

        //--------------DB STUFF HERE-------------------------------

        DBHandler db = new DBHandler(this);

         // Inserting Offenders
        System.out.println("Insert: "+ "Inserting offender  now..");
        db.addOffender(new Offender(1,"Sean","McKenna","24 Richmond Avenue, Swords, Dublin", "0871234567", "seanMcer@gmail.com", "Kylemore"));
        db.addOffender(new Offender(2,"Shauna","McGrory","15 Richmond Avenue, Swords, Dublin", "0874434567", "shaunaMcG@gmail.com", "Abbey Street"));

        // Reading all offenders
        System.out.println("Reading: "+ "Reading offenders");
        List<Offender> offenders = db.getAllOffenders();

        for (Offender offender1 : offenders)
        {
            String log = "Id: " + offender1.getId() + " , FirstName: " + offender1.getFname() +  " , LastName: " + offender1.getLname() +
                    " ,Address: " + offender1.getAddress() + ", Phone Number: " + offender1.getPhoneNo() + ", Email: " + offender1.getEmail()
                    + ", Stop Name: " + offender1.getStopName();
            // Writing offenders to log
            System.out.println("Offender:" + log);
        }
        //----------------------------------------------------------
    }

    private void dispatchTakenPictureIntent()
    {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if(intent.resolveActivity(getPackageManager()) != null)
        {
            startActivityForResult(intent, CAMERA_RESULT);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if(resultCode == RESULT_OK)
        {
            if(requestCode == CAMERA_RESULT)
            {
                Bundle extras = data.getExtras();
                Bitmap bitmap =  (Bitmap) extras.get("data");
                //imageViewPic.setImageBitmap(bitmap);
            }
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults)
    {
        if(requestCode == CAMERA_RESULT)
        {
            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED)
            {
                dispatchTakenPictureIntent();
            }
            else{
                Toast.makeText(getApplicationContext(),"Permission needed", Toast.LENGTH_LONG).show();
            }
        }
        else
        {
            super.onRequestPermissionsResult(requestCode, permissions,grantResults);
        }
    }





}

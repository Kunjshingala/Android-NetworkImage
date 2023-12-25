package com.example.networkimages;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;

import com.example.networkimages.databinding.ActivityMainBinding;

import java.io.IOException;
import java.io.InputStream;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;
    Handler mainHandler = new Handler();

    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        binding.btnClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                binding.inputUrl.setText("");
                //image clear
                binding.networkImage.setImageBitmap(null);
            }
        });

        binding.btnFeatch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String url = binding.inputUrl.getText().toString();
                new FetchImage(url).start();
            }
        });

    }

    class FetchImage extends Thread {
        String URL;
        Bitmap bitmap;

        FetchImage(String URL) {
            this.URL = URL;
        }

        @Override
        public void run() {

            mainHandler.post(new Runnable() {

                @Override
                public void run() {
                    progressDialog = new ProgressDialog(MainActivity.this);
                    progressDialog.setMessage("Getting your Pic..");
                    progressDialog.show();
                }
            });

            try {
                InputStream inputStream = new java.net.URL(URL).openStream();
                bitmap = BitmapFactory.decodeStream(inputStream);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            mainHandler.post(new Runnable() {
                @Override
                public void run() {

                    if (progressDialog.isShowing()) {
                        progressDialog.dismiss();
                    }
                    binding.networkImage.setImageBitmap(bitmap);

//                    if (bitmap.isRecycled()){
//                        progressDialog.dismiss();
//                        binding.networkImage.setImageBitmap(bitmap);
//                    }else {
//                        progressDialog.show();
//                    }
                }
            });
        }
    }
}
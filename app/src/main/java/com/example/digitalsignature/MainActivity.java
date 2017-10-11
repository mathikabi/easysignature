package com.example.digitalsignature;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.io.File;

import static com.example.digitalsignature.Signature.myColorCode;

public class MainActivity extends AppCompatActivity implements View.OnClickListener,ColorChange {

    Button btnStartSignature, btnClear, btnSave, btnCancel;
    ImageView imgBlue, imgRed, imgYellow;
    ImageView imageView;
    LinearLayout llyPenSheet;
    Signature mSignature;
    int i = 0;
    ColorChange colorChange;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        colorChange =this;

    }

    private void initView() {
        btnStartSignature = (Button) findViewById(R.id.btn_start_signature);
        btnCancel = (Button) findViewById(R.id.btn_cancel);
        btnSave = (Button) findViewById(R.id.btn_save);
        btnClear = (Button) findViewById(R.id.btn_clear);
        llyPenSheet = (LinearLayout) findViewById(R.id.linearLayout);
        imageView = (ImageView) findViewById(R.id.img_signature);
        imgBlue = (ImageView) findViewById(R.id.img_blue);
        imgYellow = (ImageView) findViewById(R.id.img_yellow);
        imgRed = (ImageView) findViewById(R.id.img_red);
        btnSave.setOnClickListener(this);
        btnStartSignature.setOnClickListener(this);
        btnClear.setOnClickListener(this);
        btnCancel.setOnClickListener(this);
        imgRed.setOnClickListener(this);
        imgYellow.setOnClickListener(this);
        imgBlue.setOnClickListener(this);
        llyPenSheet.setVisibility(View.GONE);

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_start_signature:
                llyPenSheet.setVisibility(View.VISIBLE);
                dialog_action();

                break;
            case R.id.btn_save:
                btnClear.setVisibility(View.INVISIBLE);
                btnCancel.setVisibility(View.INVISIBLE);
                imgRed.setVisibility(View.INVISIBLE);
                imgYellow.setVisibility(View.INVISIBLE);
                imgBlue.setVisibility(View.INVISIBLE);
                btnSave.setVisibility(View.INVISIBLE);
                btnStartSignature.setVisibility(View.INVISIBLE);
                Bitmap b = ScreenshotUtils.getScreenShot(llyPenSheet);
                if (b != null) {

                    btnClear.setVisibility(View.VISIBLE);
                    btnCancel.setVisibility(View.VISIBLE);
                    imgRed.setVisibility(View.VISIBLE);
                    imgYellow.setVisibility(View.VISIBLE);
                    imgBlue.setVisibility(View.VISIBLE);
                    btnSave.setVisibility(View.VISIBLE);
                    btnStartSignature.setVisibility(View.VISIBLE);


                    llyPenSheet.setVisibility(View.GONE);
                    imageView.setVisibility(View.VISIBLE);
                    imageView.setImageBitmap(b);
                    File saveFile = ScreenshotUtils.getMainDirectoryName(getApplicationContext());//get the path to save screenshot
                    final File file = ScreenshotUtils.store(b, "MMS" + ".jpg", saveFile);

                    // final File file = Signature.save(llyPenSheet, StoredPath,b);
                    shareScreenshot(file);
                    Log.v("log_tag", "Panel Saved");
                    //  mSignature.save(llyPenSheet, StoredPath,b);
                    //dialog.dismiss();
                    Toast.makeText(getApplicationContext(), "Successfully Saved", Toast.LENGTH_SHORT).show();
                    // Calling the same class
                    // recreate();
                } else {
                    Toast.makeText(this, "Velaikku Agalaaa", Toast.LENGTH_SHORT).show();
                }

                break;
            case R.id.btn_clear:
                mSignature.clear();
                imageView.setImageResource(android.R.color.transparent);
                imageView.setVisibility(View.GONE);
                llyPenSheet.setVisibility(View.VISIBLE);
                Log.v("log_tag", "Panel Cleared");
                btnSave.setEnabled(false);
                mSignature.setVisibility(View.VISIBLE);
                break;
            case R.id.btn_cancel:

                Log.v("log_tag", "Panel Canceled");
                // Calling the same class
                recreate();
                break;

            case R.id.img_blue:
                i = 1;
                colorChange.onColorChange(i);
                break;
            case R.id.img_yellow:
                i = 2;
                colorChange.onColorChange(i);
                break;
            case R.id.img_red:
                i = 3;
                colorChange.onColorChange(i);
                break;
            default:
        }
    }

    private void interfaceConnector(int code){
    }

    // Function for Digital Signature
    public void dialog_action() {

        mSignature = new Signature(getApplicationContext(), null, btnSave);
        mSignature.setBackgroundColor(Color.WHITE);
        // Dynamically generating Layout through java code
        llyPenSheet.addView(mSignature, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        btnSave.setEnabled(false);

    }

    private void shareScreenshot(File file) {
        Uri uri = Uri.fromFile(file);//Convert file path into Uri for sharing
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_SEND);
        intent.setType("image/*");
        intent.putExtra(android.content.Intent.EXTRA_SUBJECT, "");
        intent.putExtra(android.content.Intent.EXTRA_TEXT, "test");
        intent.putExtra(Intent.EXTRA_STREAM, uri);//pass uri here
        startActivity(Intent.createChooser(intent, "test"));
    }


    @Override
    public void onColorChange(int id) {
        myColorCode(id);
    }
}

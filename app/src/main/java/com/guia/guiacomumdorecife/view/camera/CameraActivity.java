/*
 * Copyright 2016 nekocode
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.guia.guiacomumdorecife.view.camera;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.view.TextureView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.guia.guiacomumdorecife.R;
import com.mikhaellopez.circularimageview.CircularImageView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author nekocode (nekocode.cn@gmail.com)
 */

public class CameraActivity extends AppCompatActivity {
    private static final int REQUEST_CAMERA_PERMISSION = 101;
    private CameraRenderer renderer;
    private TextureView textureView;

    @BindView(R.id.preview_imagem)
    CircularImageView imagemPreview;
    @BindView(R.id.flash_btn)
    ImageButton flashButton;

    private String recentPath;
    private boolean flashOn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);
        ButterKnife.bind(this);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                REQUEST_CAMERA_PERMISSION);
        } else {
            setupCameraPreviewView();
        }

        setarImagemPreview();

        flashOn = false;

    }

    private void setarImagemPreview () {

        File path = new File(Environment
                .getExternalStoragePublicDirectory(Environment
                        .DIRECTORY_PICTURES) + "/" + getTitle()
                .toString());

        if(path.listFiles() != null) {
            if (path.listFiles().length > 0) {
                if (path.listFiles()[0].getPath().toLowerCase()
                        .endsWith(".png")) {
                    recentPath = path.listFiles()[path.listFiles()
                            .length - 1].getPath();
                    getBitmap();
                }
            }
        }

    }

    public void getBitmap() {
        imagemPreview.setVisibility(View.VISIBLE);
        try {
            File f= new File(recentPath);
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inPreferredConfig = Bitmap.Config.ARGB_8888;

            Bitmap bitmap = BitmapFactory.decodeStream(new
                    FileInputStream(f), null, options);
            imagemPreview.setImageBitmap(bitmap);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @OnClick(R.id.btn_imagem)
    public void onImagem () {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        Uri photoURI = FileProvider.getUriForFile(this, getApplicationContext().getPackageName() + ".my.package.name.provider", new File(recentPath));
        intent.setDataAndType(photoURI, "image/*");
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

        startActivity(intent);
    }

    @OnClick(R.id.flash_btn)
    public void onFlash () {
        if (flashOn) {
            renderer.flash(false);
            flashOn = false;
            flashButton.setImageDrawable(getResources().getDrawable(R
                    .drawable.flash_off));
        } else {
            renderer.flash(true);
            flashOn = true;
            flashButton.setImageDrawable(getResources().getDrawable(R
                    .drawable.flash_on));
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CAMERA_PERMISSION: {
                boolean granted = true;
                for (int i = 0; i < grantResults.length; i++) {
                    if (grantResults[i] != PackageManager
                            .PERMISSION_GRANTED) {
                        granted = false;
                    }
                }
                if (granted) {
                    setupCameraPreviewView();
                } else {
                    finish();
                }
            }
        }
    }

    void setupCameraPreviewView() {
        renderer = new CameraRenderer(this);
        textureView = findViewById(R.id.textura_camera);
        assert textureView != null;
        textureView.setSurfaceTextureListener(renderer);

        textureView.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                renderer.onSurfaceTextureSizeChanged(null, v.getWidth(), v.getHeight());
            }
        });
    }

    public void capturar(View v) {

        Runnable r = new Runnable()
        {
            @Override
            public void run()
            {
                SimpleDateFormat format = new SimpleDateFormat
                        ("ddMMyyyy_HHmmss", Locale.ENGLISH);

                String fileName = getTitle().toString() + "_" +  format
                        .format(new Date()) +
                        ".png";
                File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + "/" + getTitle().toString() + "/");

                if (!file.exists()) {
                    file.mkdir();
                }

                File imageFile = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + "/" + getTitle().toString() + "/", fileName);

                Bitmap bitmap = textureView.getBitmap();
                OutputStream outputStream;

                try {
                    outputStream = new FileOutputStream(imageFile);
                    bitmap.compress(Bitmap.CompressFormat.PNG, 90, outputStream);
                    outputStream.flush();
                    outputStream.close();
                    recentPath = imageFile.getPath();
                    getBitmap();

                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };

        Thread t = new Thread(r);
        t.start();

    }

    @Override
    protected void onResume() {
        super.onResume();
        if(flashOn) {
            renderer.flash(true);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(flashOn) {
            renderer.flash(false);
        }
    }
}

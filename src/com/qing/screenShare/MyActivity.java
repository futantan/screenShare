package com.qing.screenShare;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.os.Environment;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

public class MyActivity extends Activity {

    Button button;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        button = (Button) findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                acquireScreenshot(MyActivity.this);
            }
        });

    }

    public void acquireScreenshot(Context mContext) {
        DisplayMetrics metrics = new DisplayMetrics();
        WindowManager WM = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
        Display display = WM.getDefaultDisplay();
        display.getMetrics(metrics);
        int height = metrics.heightPixels; // 屏幕高
        int width = metrics.widthPixels; // 屏幕的宽
        int pixelformat = display.getPixelFormat();
        PixelFormat localPixelFormat1 = new PixelFormat();
        PixelFormat.getPixelFormatInfo(pixelformat, localPixelFormat1);
        int deepth = localPixelFormat1.bytesPerPixel;// 位深

        byte[] arrayOfByte = new byte[height * width * deepth];
        long tmp = System.currentTimeMillis();
        try {
            InputStream localInputStream = readAsRoot(new File(
                    "/dev/graphics/fb0"));
            System.out.println(localInputStream.toString());
            DataInputStream localDataInputStream = new DataInputStream(
                    localInputStream);
            android.util.Log.e("mytest", "-----read start-------");
            localDataInputStream.readFully(arrayOfByte);
            android.util.Log.e("mytest", "-----read end-------time = " + (System.currentTimeMillis() - tmp));
            localInputStream.close();
            android.util.Log.e("mytest", "-----1-------");
            File toFile = new File(Environment.getExternalStorageDirectory() + "/a.png");
            android.util.Log.e("mytest", toFile.getAbsolutePath());
            FileOutputStream out = new FileOutputStream(toFile);
            android.util.Log.e("mytest", "-----2-------");
            int[] tmpColor = new int[width * height];
            int r, g, b;
            tmp = System.currentTimeMillis();
            android.util.Log.e("mytest", "-----bitmap start-------");
            for (int j = 0; j < width * height * deepth; j += deepth) {
                b = arrayOfByte[j] & 0xff;
                g = arrayOfByte[j + 1] & 0xff;
                r = arrayOfByte[j + 2] & 0xff;
                tmpColor[j / deepth] = (r << 16) | (g << 8) | b | (0xff000000);
            }
            Bitmap tmpMap = Bitmap.createBitmap(tmpColor, width, height,
                    Bitmap.Config.ARGB_8888);
            android.util.Log.e("mytest", "-----bitmap end-------time = " + (System.currentTimeMillis() - tmp));

            tmp = System.currentTimeMillis();
            android.util.Log.e("mytest", "-----compress start-------");
            tmpMap.compress(Bitmap.CompressFormat.PNG, 100, out);
            android.util.Log.e("mytest", "-----compress end-------time = " + (System.currentTimeMillis() - tmp));
            out.close();

        } catch (Exception e) {
            android.util.Log.e("mytest", "Exception");
            e.printStackTrace();
        }

    }

    public static InputStream readAsRoot(File paramFile) throws Exception {
        Process localProcess = Runtime.getRuntime().exec("su");
        String str = "cat " + paramFile.getAbsolutePath() + "\n";
        localProcess.getOutputStream().write(str.getBytes());
        return localProcess.getInputStream();
    }
}

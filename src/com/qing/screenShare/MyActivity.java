package com.qing.screenShare;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.os.Environment;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import util.LogUtil;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

public class MyActivity extends Activity {

    Button button1;
    Button button2;
    Button button3;
    Button button4;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        button1 = (Button) findViewById(R.id.button1);
        button2 = (Button) findViewById(R.id.button2);
        button3 = (Button) findViewById(R.id.button3);
        button4 = (Button) findViewById(R.id.button4);


        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getScreenShot(MyActivity.this);
            }
        });

        button3.setOnClickListener(new StartServiceListener());
        button4.setOnClickListener(new StopServiceListener());

    }

    /**
     * 获取屏幕截图
     *
     * @param mContext 环境
     */
    public void getScreenShot(Context mContext) {
        DisplayMetrics metrics = new DisplayMetrics();
        WindowManager WM = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
        Display display = WM.getDefaultDisplay();
        display.getMetrics(metrics);
        int height = metrics.heightPixels; // 屏幕高
        int width = metrics.widthPixels; // 屏幕的宽
        int pixelFormat = display.getPixelFormat();
        PixelFormat localPixelFormat1 = new PixelFormat();
        PixelFormat.getPixelFormatInfo(pixelFormat, localPixelFormat1);
        int depth = localPixelFormat1.bytesPerPixel;// 位深


        byte[] arrayOfByte = new byte[height * width * depth];
        long tmp = System.currentTimeMillis();
        try {
            InputStream localInputStream = readAsRoot(new File(
                    "/dev/graphics/fb0"));
            DataInputStream localDataInputStream = new DataInputStream(
                    localInputStream);
            LogUtil.log("-----read start-------");
            localDataInputStream.readFully(arrayOfByte);
            LogUtil.log("-----read end-------time = " + (System.currentTimeMillis() - tmp));
            localInputStream.close();
            LogUtil.log("-----1-------");
            File toFile = new File(Environment.getExternalStorageDirectory() + "/a.png");
            LogUtil.log(toFile.getAbsolutePath());
            FileOutputStream out = new FileOutputStream(toFile);
            LogUtil.log("-----2-------");
            int[] tmpColor = new int[width * height];
            int r, g, b;
            tmp = System.currentTimeMillis();
            LogUtil.log("-----bitmap start-------");
            for (int j = 0; j < width * height * depth; j += depth) {
                b = arrayOfByte[j] & 0xff;
                g = arrayOfByte[j + 1] & 0xff;
                r = arrayOfByte[j + 2] & 0xff;
                tmpColor[j / depth] = (r << 16) | (g << 8) | b | (0xff000000);
            }
            Bitmap tmpMap = Bitmap.createBitmap(tmpColor, width, height,
                    Bitmap.Config.ARGB_8888);
            LogUtil.log("-----bitmap end-------time = " + (System.currentTimeMillis() - tmp));

            tmp = System.currentTimeMillis();
            LogUtil.log("-----compress start-------");
            tmpMap.compress(Bitmap.CompressFormat.PNG, 100, out);
            LogUtil.log("-----compress end-------time = " + (System.currentTimeMillis() - tmp));
            out.close();

        } catch (Exception e) {
            LogUtil.log("Exception");
            e.printStackTrace();
        }

    }

    public static InputStream readAsRoot(File paramFile) throws Exception {
        Process localProcess = Runtime.getRuntime().exec("su");
        String str = "cat " + paramFile.getAbsolutePath() + "\n";
        localProcess.getOutputStream().write(str.getBytes());
        return localProcess.getInputStream();
    }

    class StartServiceListener implements View.OnClickListener {

        @Override
        public void onClick(View view) {
            Intent intent = new Intent();
            intent.setClass(MyActivity.this, ScreenShootService.class);
            startService(intent);
        }
    }

    class StopServiceListener implements View.OnClickListener {

        @Override
        public void onClick(View view) {
            Intent intent = new Intent();
            intent.setClass(MyActivity.this, ScreenShootService.class);
            stopService(intent);
        }
    }
}

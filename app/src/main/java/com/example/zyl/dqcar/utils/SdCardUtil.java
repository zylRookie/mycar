package com.example.zyl.dqcar.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import static com.example.zyl.dqcar.utils.ClippingPicture.calculateInSampleSize;


/**
 * Author : zhouyx
 * Date   : 2016/04/13
 * Sd卡工具类
 */
public class SdCardUtil {


    public static final String PROJECT_FILE_PATH = Environment
            .getExternalStorageDirectory().getPath() + "/" + "chameleon" + "/"; // 项目路径
    public static String TEMP = "file:///" + PROJECT_FILE_PATH + "camera.jpg";

    /**
     * 判断是否有sd卡
     */
    public static boolean checkSdState() {
        String status = Environment.getExternalStorageState();
        return Environment.MEDIA_MOUNTED.equals(status);
    }

    /**
     * 获取Sd卡路径
     */
    public static String getSd() {
        if (!checkSdState()) {
            return "";
        }
        return Environment.getExternalStorageDirectory().getPath();
    }

    /**
     * 获取相册路径
     */
    public static String getDCIM() {
        if (!checkSdState()) {
            return "";
        }
        String path = getSd() + "/dcim/";
        if (new File(path).exists()) {
            return path;
        }
        path = getSd() + "/DCIM/";
        File file = new File(path);
        if (!file.exists()) {
            if (!file.mkdirs()) {
                return "";
            }
        }
        return path;
    }

    /**
     * 获取DCIM目录下的Camera目录
     */
    public static String getCamera() {
        if (!checkSdState()) {
            return "";
        }
        String path = getDCIM() + "/Camera/";
        File file = new File(path);
        if (!file.exists()) {
            if (!file.mkdirs()) {
                return "";
            }
        }
        return path;
    }

    /**
     * 获取app缓存目录
     */
    public static String getCacheDir(Context context) {
        if (!checkSdState()) {
            return "";
        }
        return getSd() + "/Android/data/" + context.getPackageName() + "/cache/";
    }

    /**
     * 获取app目录
     */
    public static String getPrjFileDir(Context context) {
        if (!checkSdState()) {
            return "";
        }
        String path = getSd() + File.separator + DeviceUtil.getApplicationName(context) + File.separator;
        File projectDir = new File(path);
        if (!projectDir.exists()) {
            if (!projectDir.mkdirs()) {
                return "";
            }
        }
        return path;
    }

    /**
     * 相册目录下的图片路径
     */
    public static String getSysImgPath() {
        if (!checkSdState()) {
            return "";
        }
        return getCamera() + "IMG_" + System.currentTimeMillis() + ".jpg";
    }

    /**
     * app目录下的图片路径
     */
    public static String getAppImgPath(Context context) {
        String prj = getPrjFileDir(context);
        if ("".equals(prj)) {
            return "";
        }
        return prj + "IMG_" + System.currentTimeMillis() + ".jpg";
    }

    public static Bitmap getSmallBitmap(String filePath) {
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(filePath, options);
        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, 480, 800);
        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeFile(filePath, options);
    }

    public static String getImgPath(Bitmap smallBitmap){
        ByteArrayOutputStream logoStream = new ByteArrayOutputStream();
        boolean res = smallBitmap.compress(Bitmap.CompressFormat.PNG,100,logoStream);
        //将图像读取到logoStream中
        byte[] logoBuf = logoStream.toByteArray();
        //将图像保存到byte[]中
        Bitmap temp = BitmapFactory.decodeByteArray(logoBuf,0,logoBuf.length);
        //将图像从byte[]中读取生成Bitmap 对象 temp.txt
        String cathPath = saveMyBitmap("upLoadImage",temp);
        return cathPath;

    }



    //将图像保存到SD卡中
    public static String saveMyBitmap(String bitName, Bitmap mBitmap){
        String cathPath ="/sdcard/" + bitName + ".jpg" ;
        File f = new File("/sdcard/" + bitName + ".jpg");
        try {
            f.createNewFile();
        } catch (IOException e) {
            // TODO Auto-generated catch block
        }
        FileOutputStream fOut = null;
        try {
            fOut = new FileOutputStream(f);
        } catch (Exception e) {
            e.printStackTrace();
        }
        mBitmap.compress(Bitmap.CompressFormat.PNG, 100, fOut);
        try {
            fOut.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            fOut.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return cathPath;
    }
    public static String saveBitmap(String bitName,Bitmap mBitmap)  {
        String cathPath ="/sdcard/" + bitName + ".jpg" ;
        File f = new File( "/sdcard/"+bitName + ".jpg");
        FileOutputStream fOut = null;
        try {
            fOut = new FileOutputStream(f);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        mBitmap.compress(Bitmap.CompressFormat.JPEG, 100, fOut);
        try {
            fOut.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            fOut.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return cathPath;
    }

}
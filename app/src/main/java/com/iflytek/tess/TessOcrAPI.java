package com.iflytek.tess;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.util.Log;

import com.googlecode.tesseract.android.TessBaseAPI;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

public class TessOcrAPI {
    private static final String TAG = "TessOcrAPI";
    final static String sdcardPath = Environment.getExternalStorageDirectory().getAbsolutePath();

    /**
     * @param context  复制字库到sdcard
     * @param fileId   res id
     * @param tessName res name
     * @return 字符复制成功或已经纯在
     */
    private static boolean fileCopySdcard(Context context, int fileId, String tessName) {
        try {  // 获取字库名称
            String fileNameType = tessName + ".trainedData";
            File tessDataDir = new File(sdcardPath + File.separator + "tessData");
            if (!tessDataDir.exists() && !tessDataDir.mkdirs()) return false;
            File tessData = new File(sdcardPath + File.separator + "tessData" + File.separator + fileNameType);
            if (!tessData.exists()) {   // 检测字库是否存在
                Log.i(TAG, fileNameType + ": 正在拷贝 ");
                InputStream input = context.getResources().openRawResource(fileId);
                File file = new File(sdcardPath + File.separator + "tessData", fileNameType);
                FileOutputStream output = new FileOutputStream(file);
                byte[] buff = new byte[1024];
                for (int len; (len = input.read(buff)) != -1; ) output.write(buff, 0, len);
                input.close();
                output.close();
                Log.i(TAG, fileNameType + ": 拷贝完成 ");
            } else Log.i(TAG, fileNameType + ": 已经存在");
            return true;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return false;
    }

    /**
     * @param context 分析图片中的文字
     * @param bitmap  传入图片
     * @param fileId  res id
     * @return 识别图片后的文字
     */
    public static String tessOcr(Context context, Bitmap bitmap, int fileId) {
        TessBaseAPI tessApi = new TessBaseAPI();
        String tessName = context.getResources().getResourceEntryName(fileId);
        if (!fileCopySdcard(context, fileId, tessName)) return "";// 复制文件
        tessApi.setDebug(true);
        boolean success = tessApi.init(sdcardPath, tessName);
        if (success) Log.i(TAG, "OCR Engine pass...");
        else Log.i(TAG, "OCR Engine fail...");

        tessApi.setImage(bitmap);
        String resultText = tessApi.getUTF8Text();// 识别结果
        tessApi.end();
        return "识别结果:" + resultText;
    }

    /**
     * @param name sdcard 图片名称或路径
     * @return 获取sdcard 下的 bitmap
     */
    public static Bitmap getSdcardBitmap(String name) {
        String sdPath = Environment.getExternalStorageDirectory().getAbsolutePath();
        String filepath = sdPath + File.separator + name;

        File file = new File(filepath);
        if (file.exists()) {
            return BitmapFactory.decodeFile(filepath);
        }
        return null;
    }
}

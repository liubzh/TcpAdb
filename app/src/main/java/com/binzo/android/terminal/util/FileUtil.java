package com.binzo.android.terminal.util;

import android.content.Context;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by liubingzhao on 2018/1/19.
 */

public class FileUtil {

    public static String readFile(Context mContext, String file) {
        return readFile(mContext, file, "UTF-8");
    }

    public static String readAsset(Context mContext, String file, String code) {
        int len = 0;
        byte[] buf = null;
        String result = "";
        try {
            InputStream in = mContext.getAssets().open(file);
            len = in.available();
            buf = new byte[len];
            in.read(buf, 0, len);

            result = new String(buf, code);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public static boolean copyAssetToFile(Context mContext, String assetFilePath, String toFilePath) {
        InputStream in = null;
        OutputStream out = null;
        try {
            in = mContext.getAssets().open(assetFilePath);
            out = new FileOutputStream(toFilePath);

            byte[] buffer = new byte[1024];
            int length = -1;
            while ((length = in.read(buffer, 0, 1024)) != -1) {
                out.write(buffer, 0, length);
            }
            out.flush();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (in != null) {
                    in.close();
                }
                if (out != null) {
                    out.flush();
                    out.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    public static String readFile(Context mContext, String file, String code) {
        int len = 0;
        byte[] buf = null;
        String result = "";
        try {
            InputStream in = new FileInputStream(file);
            len = in.available();
            buf = new byte[len];
            in.read(buf, 0, len);

            result = new String(buf, code);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public static void writeFile(Context mContext, String file, String content) {
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(file);
            fos.write(content.getBytes());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}

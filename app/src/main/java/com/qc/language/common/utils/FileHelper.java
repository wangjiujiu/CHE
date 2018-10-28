package com.qc.language.common.utils;

import android.os.Environment;

import java.io.File;
import java.io.IOException;

/**
 * Created by beckett on 2018/10/14.
 */
public class FileHelper {



    public static  File createFile(String name) {
        String dirPath = Environment.getExternalStorageDirectory().getPath() + "/QCYY/AudioRecord/";
        File file = new File(dirPath);
        if (!file.exists()) {
            file.mkdirs();
        }
        String filePath = dirPath + name;
        File objFile = new File(filePath);
        try {
            objFile.createNewFile();
            return objFile;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

}

package com.equationl.ffmpeg;

import android.content.Context;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

class FileUtils {
    private static final String FFMPEG_FILE_NAME = "ffmpeg";

    static File getFFmpeg(Context context) {
        File folder = context.getFilesDir();
        return new File(folder, FFMPEG_FILE_NAME);
    }

    static void copyFile(File inputFile, File outputFile) throws IOException{
        InputStream input = new FileInputStream(inputFile);
        OutputStream output = new FileOutputStream(outputFile);
        byte[] buffer = new byte[1024];
        int bytesRead;
        while ((bytesRead = input.read(buffer, 0, buffer.length)) >= 0) {
            output.write(buffer, 0, bytesRead);
        }
        output.flush();
        output.close();
        input.close();
    }
}

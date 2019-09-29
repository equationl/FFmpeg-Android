package com.equationl.ffmpeg.example;

import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

import java.io.File;
import java.io.IOException;

import com.equationl.ffmpeg.ExecuteBinaryResponseHandler;
import com.equationl.ffmpeg.FFbinaryInterface;
import com.equationl.ffmpeg.FFdownloadFileInterface;
import com.equationl.ffmpeg.FFmpeg;
import com.equationl.ffmpeg.FFtask;
import timber.log.Timber;

/**
 * Created by Brian on 11-12-17.
 */
public class ExampleActivity extends AppCompatActivity {
    private Handler handler = new Handler();
    FFmpeg fFmpeg;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        fFmpeg = FFmpeg.getInstance(this);

        fFmpeg.setFdownloadFileInterface(new FFdownloadFileInterface() {
            @Override
            public void startDownload(String prefix, FFbinaryInterface fFbinaryInterface) {
                Timber.i("start download ffmpeg!");
                //Download the file then call FFmpegFileDownloadSuccessCallback with this file

                //EXAMPLE:
                File dir = Environment.getExternalStorageDirectory();//FIXME need permission
                File file = new File(dir, "ffmpeg");
                //END EXAMPLE

                fFbinaryInterface.FFmpegFileDownloadSuccessCallback(file);  //download finish
            }

            @Override
            public void copyFileFail(IOException e) {
                Timber.e(e);
            }

            @Override
            public void allFinish() {
                versionFFmpeg();
            }

            @Override
            public void verifyFileFail() {
                Timber.e("check file fail!");
            }
        });

        if (fFmpeg.isSupported() && fFmpeg.isFFmpegExist()) {
            // ffmpeg is supported
            versionFFmpeg();
            //ffmpegTestTaskQuit();
        } else {
            // ffmpeg is not supported
            Timber.e("ffmpeg not supported or not exist!");
        }

    }

    private void versionFFmpeg() {
        fFmpeg.execute(new String[]{"-version"}, new ExecuteBinaryResponseHandler() {
            @Override
            public void onSuccess(String message) {
                Timber.d(message);
            }

            @Override
            public void onProgress(String message) {
                Timber.d(message);
            }
        });

    }

    private void ffmpegTestTaskQuit() {
        String[] command = {"-i", "input.mp4", "output.mov"};

        final FFtask task = FFmpeg.getInstance(this).execute(command, new ExecuteBinaryResponseHandler() {
            @Override
            public void onStart() {
                Timber.d("on start");
            }

            @Override
            public void onFinish() {
                Timber.d("on finish");
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Timber.d("RESTART RENDERING");
                        ffmpegTestTaskQuit();
                    }
                }, 5000);
            }

            @Override
            public void onSuccess(String message) {
                Timber.d(message);
            }

            @Override
            public void onProgress(String message) {
                Timber.d(message);
            }

            @Override
            public void onFailure(String message) {
                Timber.d(message);
            }
        });

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Timber.d("STOPPING THE RENDERING!");
                task.sendQuitSignal();
            }
        }, 8000);
    }
}

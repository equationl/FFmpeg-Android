package com.equationl.ffmpeg.example;

import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

import java.io.File;
import java.io.IOException;

import com.equationl.ffmpeg.ExecuteBinaryResponseHandler;
import com.equationl.ffmpeg.FFcheckFileListener;
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

        fFmpeg.setFFcheckFileListener(new FFcheckFileListener() {
            @Override
            public void onCopyFileFail(IOException e) {
                Timber.e(e);
            }

            @Override
            public void onAllFinish() {

            }

            @Override
            public void onVerifyFileFail() {
                Timber.e("check file fail!");
            }
        });

        if (fFmpeg.isSupported()) {
            //check if support
            if (fFmpeg.isFFmpegExist()) {
                //check if ffmpeg file exist
                //all good, begin use
                versionFFmpeg();
            }
            else {
                //begin download ffmpeg
                String prefix = fFmpeg.getPrefix();
                File downFile = downloadFile(prefix);
                //apply download file to application
                fFmpeg.setFFmpegFile(downFile);
                //just for case, check again
                if (fFmpeg.isFFmpegExist()) {
                    //all good, use it
                    versionFFmpeg();
                }
            }
        }
        else {
            //well, not support
            Timber.i("ffmpeg not support!");
        }

    }

    private File downloadFile(String prefix) {
        //For example
        File dir = Environment.getExternalStorageDirectory();//FIXME need permission
        return new File(dir, "ffmpeg");
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

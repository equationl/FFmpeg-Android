package com.equationl.ffmpeg.example;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.equationl.ffmpeg.ExecuteBinaryResponseHandler;
import com.equationl.ffmpeg.FFmpeg;
import com.equationl.ffmpeg.FFtask;

import java.io.File;

/*
* Create by equationl
* fork from https://github.com/equationl/video_shot_pro/blob/master/app/src/main/java/com/equationl/videoshotpro/CommandActivity.java
* */
public class MainActivity extends AppCompatActivity {
    Button btn;
    TextView textview;
    EditText edittext;
    ScrollView sv;
    FFmpeg ffmpeg;
    FFtask fftask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btn = findViewById(R.id.command_button);
        textview = findViewById(R.id.command_text);
        edittext = findViewById(R.id.command_editText);
        sv = findViewById(R.id.command_scroll);

        try {
            //Get ffmpeg
            ffmpeg = getFFmpeg();
            //ffmpeg not support this device
            if (ffmpeg == null) {
                Toast.makeText(this, R.string.toast_ffmpeg_not_support, Toast.LENGTH_LONG).show();
                finish();
            }
        } catch (Exception e) {
            //ffmpeg is not initialized
            //So just init it
            ffmpeg = FFmpeg.getInstance(this);
        }

        btn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String text = edittext.getText().toString();
                textview.setText("");
                if (!ffmpeg.isCommandRunning(fftask)) {
                    String[] cmd = text.split(" ");
                    fftask = ffmpeg.execute(cmd, new ExecuteBinaryResponseHandler() {
                        @Override
                        public void onStart() {
                            btn.setClickable(false);
                            btn.setEnabled(false);
                        }
                        @Override
                        public void onFailure(String message) {
                            textview.setText(String.format("%s\n执行失败", message));
                            //sv.fullScroll(ScrollView.FOCUS_DOWN);
                            scrollToBottom(sv, textview);
                            btn.setClickable(true);
                        }
                        @Override
                        public void onSuccess(String message) {
                            textview.setText(String.format("%s\n执行成功", message));
                            //sv.fullScroll(ScrollView.FOCUS_DOWN);
                            scrollToBottom(sv, textview);
                            btn.setClickable(true);
                        }
                        @Override
                        public void onProgress(String message) {
                            textview.setText(String.format("%s\n%s\n", textview.getText().toString(), message));
                            //sv.fullScroll(ScrollView.FOCUS_DOWN);
                            scrollToBottom(sv, textview);
                        }
                        @Override
                        public void onFinish() {
                            //btn.setClickable(true);
                            btn.setEnabled(true);
                        }
                    });
                }
            }
        });
    }


    /**
     *  作者：gundumw100
     *  原文：https://blog.csdn.net/gundumw100/article/details/69983948
     * */
    public static void scrollToBottom(final View scroll, final View inner) {
        Handler mHandler = new Handler();
        mHandler.post(new Runnable() {
            public void run() {
                if (scroll == null || inner == null) {
                    return;
                }
                int offset = inner.getMeasuredHeight() - scroll.getHeight();
                if (offset < 0) {
                    offset = 0;
                }
                scroll.scrollTo(0, offset);
            }
        });
    }

    /**
     * Get FFmpeg
     *
     * @return
     * <p>if ffmpeg could use and binary file exist, return ffmmpeg object</p>
     * <p>if ffmpeg can't use, return null</p>
     * <p>if ffmpeg binary file not exist, toast to download then throws exception</p>
     * */
    FFmpeg getFFmpeg() throws Exception{
        //init ffmpeg
        FFmpeg fFmpeg = FFmpeg.getInstance(this);
        //Check if device support
        if (fFmpeg.isSupported()) {
            //Check if ffmpeg binary file exist
            if (fFmpeg.isFFmpegExist()) {
                return fFmpeg;
            }
            else {
                //If not exist, just download it
                String prefix = fFmpeg.getPrefix();
                showNeedDownloadDialog(prefix);
                throw new Exception("ffmpeg not exist");
            }
        }
        else {
            return null;
        }
    }

    void showNeedDownloadDialog(String prefix) {
        new AlertDialog.Builder(this)
                .setTitle(R.string.downloadActivity_dialog_askDownload_title)
                .setMessage(R.string.downloadActivity_dialog_askDownload_message)
                .setCancelable(false)
                .setNegativeButton(R.string.downloadActivity_dialog_askDownload_btn_cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Toast.makeText(MainActivity.this, R.string.downloadActivity_toast_downloadData_not_download, Toast.LENGTH_LONG).show();
                    }
                })
                .setPositiveButton(R.string.downloadActivity_dialog_askDownload_btn_ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //Go to DownloadActivity to download file
                        /*Intent intent = new Intent(context, DownloadActivity.class);
                        intent.putExtra("prefix", prefix);
                        context.startActivity(intent); */

                        //Fixme You need to implement download file by yourself
                        //You can find all the binary file at https://github.com/formatBCE/FFmpeg-Android/tree/master/android-ffmpeg/src/main/assets
                        //You need put it on your own server and download by yourself

                        //For this sample, I will use local file instead download file
                        File file = new File("/sdcard/ffmpeg");   //Fixme need permission

                        //Download finish, set it to ffmpeg
                        ffmpeg.setFFmpegFile(file);
                    }
                })
                .show();
    }
}

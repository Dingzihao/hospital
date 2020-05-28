package com.dzh.hospital.util;

import android.app.Application;
import android.content.Context;
import android.speech.tts.TextToSpeech;

import com.blankj.utilcode.util.ToastUtils;
import com.dzh.hospital.App;

import java.util.Locale;

public class ChineseToSpeech {
    private TextToSpeech textToSpeech;

    public ChineseToSpeech(Context context) {
        this.textToSpeech = new TextToSpeech(context, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status == TextToSpeech.SUCCESS) {
                    textToSpeech.setPitch(1.0f);
                    textToSpeech.setSpeechRate(0.3f);
                    int result = textToSpeech.setLanguage(Locale.CHINA);
                    if (result == TextToSpeech.LANG_MISSING_DATA
                            || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                        ToastUtils.showShort("不支持朗读功能");
                    }
                }
            }
        });
    }

    public void speech(String text) {
        textToSpeech.speak(text, TextToSpeech.QUEUE_FLUSH, null);
    }

    public void destroy() {
        if (textToSpeech != null) {
            textToSpeech.stop();
            textToSpeech.shutdown();
        }
    }

}

package com.dzh.hospital.util;

import android.content.Context;
import android.speech.tts.TextToSpeech;

import com.blankj.utilcode.util.ToastUtils;

import java.util.Locale;

/**
 * @author 丁子豪
 * @desc 文字转语音
 * @data on 2020/5/28 15:11
 */
public class ChineseToSpeech {
    private TextToSpeech textToSpeech;

    public ChineseToSpeech(Context context) {
        this.textToSpeech = new TextToSpeech(context, status -> {
            if (status == TextToSpeech.SUCCESS) {
                textToSpeech.setPitch(1.2f);
                textToSpeech.setSpeechRate(0.3f);
                int result = textToSpeech.setLanguage(Locale.CHINA);
                if (result == TextToSpeech.LANG_MISSING_DATA
                        || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                    ToastUtils.showShort("不支持朗读功能");
                }
            }
        });
    }

    public void speech(String text) {
        textToSpeech.speak(text, TextToSpeech.QUEUE_ADD, null);
    }

    public void destroy() {
        if (textToSpeech != null) {
            textToSpeech.stop();
            textToSpeech.shutdown();
        }
    }

}

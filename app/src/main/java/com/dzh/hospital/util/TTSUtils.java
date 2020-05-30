package com.dzh.hospital.util;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import com.baidu.tts.client.SpeechError;
import com.baidu.tts.client.SpeechSynthesizer;
import com.baidu.tts.client.SpeechSynthesizerListener;
import com.baidu.tts.client.TtsMode;

import java.io.IOException;

public class TTSUtils implements SpeechSynthesizerListener {

    private static final String TAG = "TTSUtils";
    private static volatile TTSUtils instance = null;
    private SpeechSynthesizer mSpeechSynthesizer;

    private static final String SAMPLE_DIR = Environment.getExternalStorageDirectory().getAbsolutePath() + "/baiduTTS/";
//    private static final String SAMPLE_DIR = "/sdcard/baiduTTS/";
    private static final String SPEECH_FEMALE_MODEL_NAME = "bd_etts_common_speech_f7_mand_eng_high_am-mgc_v3.6.0_20190117.dat";
    private static final String TEXT_MODEL_NAME = "bd_etts_common_text_txt_all_mand_eng_middle_big_v3.4.2_20190710.dat";

    private static final String APIKEY = "jlWuumfM9ZcvP362L9xEGEYG";
    private static final String SECRETKEY = "KjBw0rCvT9apX4Xac4E3VmYkoDOgvgP2";
    private static final String APPID = "20097709";
    private static final String SN = "97a73e88-737af206-04e4-00f4-247a9-00";

    private TTSUtils() {
    }

    public static TTSUtils getInstance() {
        if (instance == null) {
            synchronized (TTSUtils.class) {
                if (instance == null) {
                    instance = new TTSUtils();
                }
            }
        }
        return instance;
    }

    public void init(Context context) {
        // 获取语音合成对象实例
        mSpeechSynthesizer = SpeechSynthesizer.getInstance();
        // 设置context
        mSpeechSynthesizer.setContext(context);
        // 设置语音合成状态监听器
        mSpeechSynthesizer.setSpeechSynthesizerListener(this);
        mSpeechSynthesizer.setApiKey(APIKEY, SECRETKEY);
        // 设置离线语音合成授权，需要填入从百度语音官网申请的app_id
        mSpeechSynthesizer.setAppId(APPID);
        // 设置语音合成文本模型文件
        mSpeechSynthesizer.setParam(SpeechSynthesizer.PARAM_TTS_TEXT_MODEL_FILE, SAMPLE_DIR + TEXT_MODEL_NAME);
        // 设置语音合成声音模型文件
        mSpeechSynthesizer.setParam(SpeechSynthesizer.PARAM_TTS_SPEECH_MODEL_FILE, SAMPLE_DIR + SPEECH_FEMALE_MODEL_NAME);
        mSpeechSynthesizer.setParam(SpeechSynthesizer.PARAM_MIX_MODE, SpeechSynthesizer.MIX_MODE_DEFAULT);

        // 发音人（在线引擎），可用参数为0,1,2,3。。。（服务器端会动态增加，各值含义参考文档，以文档说明为准。0--普通女声，1--普通男声，2--特别男声，3--情感男声。。。）
        // 5. 以下setParam 参数选填。不填写则默认值生效
        // 设置在线发声音人： 0 普通女声（默认） 1 普通男声  3 情感男声<度逍遥> 4 情感儿童声<度丫丫>
        mSpeechSynthesizer.setParam(SpeechSynthesizer.PARAM_SPEAKER, "0");
        // 设置合成的音量，0-15 ，默认 5
        mSpeechSynthesizer.setParam(SpeechSynthesizer.PARAM_VOLUME, "9");
        // 设置合成的语速，0-15 ，默认 5
        mSpeechSynthesizer.setParam(SpeechSynthesizer.PARAM_SPEED, "5");
        // 设置合成的语调，0-15 ，默认 5
        mSpeechSynthesizer.setParam(SpeechSynthesizer.PARAM_PITCH, "5");
        // 纯离线sdk这个参数必填；离在线sdk没有此参数        // 初始化tts
        mSpeechSynthesizer.setParam(SpeechSynthesizer.PARAM_AUTH_SN, SN);

        OfflineResource offlineResource = createOfflineResource(context,OfflineResource.VOICE_FEMALE);
        // 声学模型文件路径 (离线引擎使用), 请确认下面两个文件存在
        mSpeechSynthesizer.setParam(SpeechSynthesizer.PARAM_TTS_TEXT_MODEL_FILE, offlineResource.getTextFilename());
        mSpeechSynthesizer.setParam(SpeechSynthesizer.PARAM_TTS_SPEECH_MODEL_FILE, offlineResource.getModelFilename());

        mSpeechSynthesizer.initTts(TtsMode.OFFLINE);
    }

    protected OfflineResource createOfflineResource(Context context,String voiceType) {
        OfflineResource offlineResource = null;
        try {
            offlineResource = new OfflineResource(context, voiceType);
        } catch (IOException e) {
            // IO 错误自行处理
            e.printStackTrace();
            Log.d(TAG,"【error】:copy files from assets failed." + e.getMessage());
        }
        return offlineResource;
    }

    //需要合成的msg长度不能超过1024个GBK字节。
    public void speak(String msg) {
        int result = mSpeechSynthesizer.speak(msg);
        if (result < 0) {
            Log.e(TAG, "error,please look up error code = " + result + " in doc or URL:http://yuyin.baidu.com/docs/tts/122 ");
        }
    }

    public void pause() {
        mSpeechSynthesizer.pause();
    }

    public void resume() {
        mSpeechSynthesizer.resume();
    }

    public void stop() {
        mSpeechSynthesizer.stop();
    }

    public void release() {
        if (null != mSpeechSynthesizer) {
            mSpeechSynthesizer.release();
        }
    }

    @Override
    public void onSynthesizeStart(String s) {
        // 监听到合成开始，在此添加相关操作
    }

    @Override
    public void onSynthesizeDataArrived(String s, byte[] bytes, int i, int i1) {

    }


    @Override
    public void onSynthesizeFinish(String s) {
        // 监听到合成结束，在此添加相关操作
    }

    @Override
    public void onSpeechStart(String s) {
        // 监听到合成并播放开始，在此添加相关操作
    }

    @Override
    public void onSpeechProgressChanged(String s, int i) {
        // 监听到播放进度有变化，在此添加相关操作
    }

    @Override
    public void onSpeechFinish(String s) {
        // 监听到播放结束，在此添加相关操作
    }

    @Override
    public void onError(String s, SpeechError speechError) {
        // 监听到出错，在此添加相关操作
    }
}

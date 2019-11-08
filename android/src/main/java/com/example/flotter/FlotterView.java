package com.example.flotter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.View;

import com.airbnb.lottie.LottieAnimationView;

import java.util.Map;

import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.MethodChannel;
import io.flutter.plugin.common.PluginRegistry;
import io.flutter.plugin.platform.PlatformView;

public class FlotterView implements PlatformView {
    private final String TAG = "TAG";
    private final LottieAnimationView animationView;

    private float maxFrame;
    private MethodChannel methodChannel;
    private boolean isReady = false;

    public FlotterView(Context context, int id, final Object args, final PluginRegistry.Registrar registrar) {
        super();

        Map<String, Object> obj = (Map<String, Object>) args;
        animationView = new LottieAnimationView(context);
        methodChannel = new MethodChannel(registrar.messenger(), "flotterView-" + obj.get("animationId"));
        methodChannel.setMethodCallHandler(new MethodChannel.MethodCallHandler() {
            @Override
            public void onMethodCall(MethodCall methodCall, MethodChannel.Result result) {
                Map<String, Object> params = (Map<String, Object>) methodCall.arguments;
                Log.e(TAG, "---" + params);
                switch (methodCall.method) {
                    case "initialize":
                        String data = registrar.lookupKeyForAsset(params.get("animationData").toString());
                        int loopMode = (int) params.get("loopMode");
                        initialize(data, loopMode);
                        result.success(isReady);
                        break;
                    case "play":
                        play();
                        break;
                    case "playFrom":
                        /// 暂未实现
                        float from = Float.valueOf(params.get("from").toString());
                        float to = Float.valueOf(params.get("to").toString());
                        int loop = (int) params.get("loopMode");
                        playFrom(from, to, loop);
                        break;
                    case "pause":
                        pause();
                        break;
                    case "reverse":
                        reverse();
                        break;
                    case "stop":
                        stop();
                        break;
                }
            }
        });

    }

    /**
     * FlotterAnimationView functions
     *
     * @param animationData
     * @param loopMode
     */
    private void initialize(String animationData, int loopMode) {

        try {
            int loop = 0;
            switch (loopMode) {
                case FlotterLoopMode.playOnce:
                    loop = 0;
                    break;
                case FlotterLoopMode.loop:
                    loop = -1;
                    break;
                case FlotterLoopMode.autoReverse:
                    loop = 1;
                    break;
                case FlotterLoopMode.autoReverseLoop:
                    loop = 2;
                    break;
                default:
                    loop = 0;
                    break;
            }
            animationView.setAnimation(animationData);
            animationView.setRepeatCount(loop);
            animationView.setRepeatMode(loopMode);
            isReady = true;
        } catch (Exception e) {
            isReady = false;

        }
    }

    private void play() {
        if (isReady && !animationView.isAnimating()) {
            animationView.setMinAndMaxFrame(0, (int) maxFrame);
            animationView.setMinAndMaxProgress(0, 1);
            animationView.playAnimation();
        }
    }

    private void pause() {
        if (isReady && animationView.isAnimating()) {
            animationView.pauseAnimation();
        }
    }

    @SuppressLint("WrongConstant")
    private void stop() {
        if (isReady) {
            animationView.cancelAnimation();
            animationView.setProgress(0.0f);
            final int mode = animationView.getRepeatMode();
            animationView.setRepeatMode(1);
            animationView.setRepeatMode(mode);
        }
    }

    @SuppressLint("WrongConstant")
    private void reverse() {
        if (isReady) {
            animationView.playAnimation();
            animationView.setRepeatMode(1);
        }
    }

    private void playFrom(float fromProgress, float toProgress, int loopMode) {
        if (isReady && !animationView.isAnimating()) {
            int loop = 0;
            switch (loopMode) {
                case FlotterLoopMode.playOnce:
                    loop = 0;
                    break;
                case FlotterLoopMode.loop:
                    loop = -1;
                    break;
                case FlotterLoopMode.autoReverse:
                    loop = 1;
                    break;
                case FlotterLoopMode.autoReverseLoop:
                    loop = 2;
                    break;
                default:
                    loop = 0;
                    break;
            }
            animationView.playAnimation();
            animationView.setMaxProgress(toProgress);
            animationView.setMinProgress(fromProgress);
            animationView.setRepeatCount(loop);
            animationView.setRepeatMode(loopMode);
        }
    }

    @Override
    public View getView() {
        return animationView;
    }

    @Override
    public void dispose() {
    }

}

package com.example.flotter;

import android.content.Context;

import io.flutter.plugin.common.BinaryMessenger;
import io.flutter.plugin.common.PluginRegistry;
import io.flutter.plugin.common.StandardMessageCodec;
import io.flutter.plugin.platform.PlatformView;
import io.flutter.plugin.platform.PlatformViewFactory;

public class FlotterViewFactory extends PlatformViewFactory {
    private final PluginRegistry.Registrar registrar;

    public FlotterViewFactory(PluginRegistry.Registrar messenger) {
        super(StandardMessageCodec.INSTANCE);
        this.registrar = messenger;
    }

    @Override
    public PlatformView create(Context context, int id, Object o) {
        return new FlotterView(context,id,o,registrar);
    }
}

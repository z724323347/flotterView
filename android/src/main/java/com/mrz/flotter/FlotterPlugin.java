package com.mrz.flotter;

import io.flutter.plugin.common.PluginRegistry;

/**
 * FlotterPlugin
 */
public class FlotterPlugin {
    /**
     * Plugin registration.
     */
    public static void registerWith(PluginRegistry.Registrar registrar) {
        registrar.platformViewRegistry().registerViewFactory("FlotterAnimation_view",
                new FlotterViewFactory(registrar));
    }


}

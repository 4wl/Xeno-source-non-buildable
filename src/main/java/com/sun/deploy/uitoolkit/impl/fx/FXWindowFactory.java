/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.sun.deploy.uitoolkit.PluginWindowFactory
 *  com.sun.deploy.uitoolkit.Window
 *  sun.plugin2.main.client.ModalityInterface
 *  sun.plugin2.message.Pipe
 */
package com.sun.deploy.uitoolkit.impl.fx;

import com.sun.deploy.uitoolkit.PluginWindowFactory;
import com.sun.deploy.uitoolkit.Window;
import com.sun.deploy.uitoolkit.impl.fx.FXPluginToolkit;
import com.sun.deploy.uitoolkit.impl.fx.FXWindow;
import java.util.concurrent.Callable;
import sun.plugin2.main.client.ModalityInterface;
import sun.plugin2.message.Pipe;

public class FXWindowFactory
extends PluginWindowFactory {
    public Window createWindow() {
        return new FXWindow(0L, null);
    }

    public Window createWindow(final long l, final String string, boolean bl, ModalityInterface modalityInterface) {
        try {
            return FXPluginToolkit.callAndWait(new Callable<FXWindow>(){

                @Override
                public FXWindow call() throws Exception {
                    return new FXWindow(l, string);
                }
            });
        }
        catch (Exception exception) {
            throw new RuntimeException(exception);
        }
    }

    public Window createWindow(long l, String string, boolean bl, ModalityInterface modalityInterface, Pipe pipe, int n) {
        return this.createWindow(l, string, bl, modalityInterface);
    }
}


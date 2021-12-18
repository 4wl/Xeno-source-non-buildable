/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  javafx.stage.Stage
 */
package com.sun.deploy.uitoolkit.impl.fx;

import javafx.stage.Stage;

public interface AppletStageManager {
    public Stage getAppletStage();

    public Stage getPreloaderStage();

    public Stage getErrorStage();

    public void setSize(int var1, int var2);
}


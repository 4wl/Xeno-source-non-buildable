/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  javafx.collections.ObservableList
 *  javafx.stage.Stage
 */
package com.sun.javafx.stage;

import javafx.collections.ObservableList;
import javafx.stage.Stage;

public class StageHelper {
    private static StageAccessor stageAccessor;

    public static ObservableList<Stage> getStages() {
        if (stageAccessor == null) {
            try {
                Class.forName(Stage.class.getName(), true, Stage.class.getClassLoader());
            }
            catch (ClassNotFoundException classNotFoundException) {
                // empty catch block
            }
        }
        return stageAccessor.getStages();
    }

    public static void initSecurityDialog(Stage stage, boolean bl) {
        stageAccessor.initSecurityDialog(stage, bl);
    }

    public static void setStageAccessor(StageAccessor stageAccessor) {
        if (StageHelper.stageAccessor != null) {
            System.out.println("Warning: Stage accessor already set: " + StageHelper.stageAccessor);
            Thread.dumpStack();
        }
        StageHelper.stageAccessor = stageAccessor;
    }

    public static interface StageAccessor {
        public ObservableList<Stage> getStages();

        public void initSecurityDialog(Stage var1, boolean var2);
    }
}


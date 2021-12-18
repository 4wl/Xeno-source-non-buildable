/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.sun.deploy.uitoolkit.ui.AbstractDialog
 *  com.sun.deploy.uitoolkit.ui.ModalityHelper
 *  sun.plugin2.applet.Plugin2Manager
 *  sun.plugin2.main.client.ModalityInterface
 */
package com.sun.deploy.uitoolkit.impl.fx.ui;

import com.sun.deploy.uitoolkit.ui.AbstractDialog;
import com.sun.deploy.uitoolkit.ui.ModalityHelper;
import sun.plugin2.applet.Plugin2Manager;
import sun.plugin2.main.client.ModalityInterface;

public class FXModalityHelper
implements ModalityHelper {
    public void reactivateDialog(AbstractDialog abstractDialog) {
        System.out.println("FXModalityHelper.reactivateDialog");
    }

    public boolean installModalityListener(ModalityInterface modalityInterface) {
        System.out.println("FXModalityHelper.installModalityListener" + (Object)modalityInterface);
        return false;
    }

    public void pushManagerShowingSystemDialog() {
        System.out.println("FXModalityHelper.pushManagerShowingSystemDialog");
    }

    public Plugin2Manager getManagerShowingSystemDialog() {
        System.out.println("FXModalityHelper.getManagerShowingSystemDialog");
        return null;
    }

    public void popManagerShowingSystemDialog() {
        System.out.println("FXModalityHelper.popManagerShowingSystemDialog");
    }
}


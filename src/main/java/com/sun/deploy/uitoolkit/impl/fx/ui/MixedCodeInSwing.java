/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.sun.deploy.ui.AppInfo
 */
package com.sun.deploy.uitoolkit.impl.fx.ui;

import com.sun.deploy.ui.AppInfo;
import java.awt.Component;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.Window;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import javax.swing.SwingUtilities;
import sun.awt.AppContext;
import sun.awt.SunToolkit;

public class MixedCodeInSwing {
    private static boolean haveAppContext = false;
    private static Class sysUtils;
    private static Class tClass;
    private static Constructor cMethod;
    private static Method setContentMethod;
    private static Method getDialogMethod;
    private static Method setVisibleMethod;
    private static Method getAnswerMethod;
    private static Method disposeMethod;
    private static Method createSysThreadMethod;

    public static int show(Object object, AppInfo appInfo, String string, String string2, String string3, String string4, String string5, String string6, boolean bl, boolean bl2, String string7) {
        Helper helper = new Helper(null, appInfo, string, string2, string3, string4, string5, string6, bl, bl2, string7);
        try {
            Thread thread = (Thread)createSysThreadMethod.invoke(null, helper);
            thread.start();
            thread.join();
        }
        catch (Exception exception) {
            exception.printStackTrace();
        }
        return helper.getAnswer();
    }

    private static void placeWindow(Window window) {
        Rectangle rectangle = MixedCodeInSwing.getMouseScreenBounds();
        Rectangle rectangle2 = window.getBounds();
        window.setLocation((rectangle.width - rectangle2.width) / 2, (rectangle.height - rectangle2.height) / 2);
    }

    public static Rectangle getMouseScreenBounds() {
        Point point = MouseInfo.getPointerInfo().getLocation();
        GraphicsDevice[] arrgraphicsDevice = GraphicsEnvironment.getLocalGraphicsEnvironment().getScreenDevices();
        for (int i = 0; i < arrgraphicsDevice.length; ++i) {
            Rectangle rectangle = arrgraphicsDevice[i].getDefaultConfiguration().getBounds();
            if (point.x < rectangle.x || point.y < rectangle.y || point.x > rectangle.x + rectangle.width || point.y > rectangle.y + rectangle.height) continue;
            return rectangle;
        }
        return new Rectangle(new Point(0, 0), Toolkit.getDefaultToolkit().getScreenSize());
    }

    static {
        try {
            tClass = Class.forName("com.sun.deploy.ui.DialogTemplate", true, null);
            cMethod = tClass.getDeclaredConstructor(AppInfo.class, Component.class, String.class, String.class, Boolean.TYPE);
            cMethod.setAccessible(true);
            setContentMethod = tClass.getDeclaredMethod("setMixedCodeContent", String.class, Boolean.TYPE, String.class, String.class, String.class, String.class, Boolean.TYPE, Boolean.TYPE, Boolean.TYPE, String.class);
            setContentMethod.setAccessible(true);
            getDialogMethod = tClass.getDeclaredMethod("getDialog", new Class[0]);
            getDialogMethod.setAccessible(true);
            setVisibleMethod = tClass.getDeclaredMethod("setVisible", Boolean.TYPE);
            setVisibleMethod.setAccessible(true);
            disposeMethod = tClass.getDeclaredMethod("disposeDialog", new Class[0]);
            disposeMethod.setAccessible(true);
            getAnswerMethod = tClass.getDeclaredMethod("getUserAnswer", new Class[0]);
            getAnswerMethod.setAccessible(true);
            sysUtils = Class.forName("sun.plugin.util.PluginSysUtil", false, null);
            createSysThreadMethod = sysUtils.getMethod("createPluginSysThread", Runnable.class);
        }
        catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    static class Helper
    implements Runnable {
        private AppInfo appInfo;
        private String title;
        private String masthead;
        private String message;
        private String info;
        private String okBtnStr;
        private String cancelBtnStr;
        private boolean useWarning;
        private boolean isJsDialog;
        private int userAnswer = -1;
        private String showAlways;

        Helper(Component component, AppInfo appInfo, String string, String string2, String string3, String string4, String string5, String string6, boolean bl, boolean bl2, String string7) {
            this.appInfo = appInfo == null ? new AppInfo() : appInfo;
            this.title = string;
            this.masthead = string2;
            this.message = string3;
            this.info = string4;
            this.okBtnStr = string5;
            this.cancelBtnStr = string6;
            this.useWarning = bl;
            this.isJsDialog = bl2;
            this.showAlways = string7;
        }

        /*
         * WARNING - Removed try catching itself - possible behaviour change.
         */
        @Override
        public void run() {
            Class<MixedCodeInSwing> class_ = MixedCodeInSwing.class;
            synchronized (MixedCodeInSwing.class) {
                if (!haveAppContext) {
                    AppContext appContext = SunToolkit.createNewAppContext();
                    haveAppContext = true;
                }
                // ** MonitorExit[var1_1] (shouldn't be in output)
                try {
                    SwingUtilities.invokeAndWait(new Runnable(){

                        @Override
                        public void run() {
                            try {
                                Object t = cMethod.newInstance(new Object[]{Helper.this.appInfo, null, Helper.this.title, Helper.this.masthead, false});
                                setContentMethod.invoke(t, null, false, Helper.this.message, Helper.this.info, Helper.this.okBtnStr, Helper.this.cancelBtnStr, true, Helper.this.useWarning, Helper.this.isJsDialog, Helper.this.showAlways);
                                setVisibleMethod.invoke(t, true);
                                Helper.this.userAnswer = (Integer)getAnswerMethod.invoke(t, new Object[0]);
                                disposeMethod.invoke(t, new Object[0]);
                            }
                            catch (Throwable throwable) {
                                throwable.printStackTrace();
                            }
                        }
                    });
                }
                catch (InterruptedException interruptedException) {
                    interruptedException.printStackTrace();
                }
                catch (InvocationTargetException invocationTargetException) {
                    invocationTargetException.printStackTrace();
                }
                catch (Throwable throwable) {
                    throwable.printStackTrace();
                }
                return;
            }
        }

        int getAnswer() {
            return this.userAnswer;
        }
    }
}


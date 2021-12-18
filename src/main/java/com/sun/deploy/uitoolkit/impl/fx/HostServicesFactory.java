/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  javafx.application.Application
 */
package com.sun.deploy.uitoolkit.impl.fx;

import com.sun.javafx.applet.HostServicesImpl;
import com.sun.javafx.application.HostServicesDelegate;
import java.io.File;
import java.net.URI;
import javafx.application.Application;
import netscape.javascript.JSObject;

public class HostServicesFactory {
    public static HostServicesDelegate getInstance(Application application) {
        HostServicesDelegate hostServicesDelegate = HostServicesImpl.getInstance(application);
        if (hostServicesDelegate == null) {
            hostServicesDelegate = StandaloneHostService.getInstance(application);
        }
        return hostServicesDelegate;
    }

    private HostServicesFactory() {
        throw new InternalError();
    }

    private static class StandaloneHostService
    extends HostServicesDelegate {
        private static HostServicesDelegate instance = null;
        private Class appClass = null;
        static final String[] browsers = new String[]{"google-chrome", "firefox", "opera", "konqueror", "mozilla"};

        /*
         * WARNING - Removed try catching itself - possible behaviour change.
         */
        public static HostServicesDelegate getInstance(Application application) {
            Class<StandaloneHostService> class_ = StandaloneHostService.class;
            synchronized (StandaloneHostService.class) {
                if (instance == null) {
                    instance = new StandaloneHostService(application);
                }
                // ** MonitorExit[var1_1] (shouldn't be in output)
                return instance;
            }
        }

        private StandaloneHostService(Application application) {
            this.appClass = application.getClass();
        }

        @Override
        public String getCodeBase() {
            String string;
            String string2;
            String string3 = this.appClass.getName();
            int n = string3.lastIndexOf(".");
            if (n >= 0) {
                string3 = string3.substring(n + 1);
            }
            if (!(string2 = this.appClass.getResource(string3 = string3 + ".class").toString()).startsWith("jar:file:") || string2.indexOf("!") == -1) {
                return "";
            }
            String string4 = string2.substring(4, string2.lastIndexOf("!"));
            File file = null;
            try {
                file = new File(new URI(string4).getPath());
            }
            catch (Exception exception) {
                // empty catch block
            }
            if (file != null && (string = file.getParent()) != null) {
                return this.toURIString(string);
            }
            return "";
        }

        private String toURIString(String string) {
            try {
                return new File(string).toURI().toString();
            }
            catch (Exception exception) {
                exception.printStackTrace();
                return "";
            }
        }

        @Override
        public String getDocumentBase() {
            return this.toURIString(System.getProperty("user.dir"));
        }

        @Override
        public void showDocument(String string) {
            String string2 = System.getProperty("os.name");
            try {
                if (string2.startsWith("Mac OS")) {
                    Class.forName("com.apple.eio.FileManager").getDeclaredMethod("openURL", String.class).invoke(null, string);
                } else if (string2.startsWith("Windows")) {
                    Runtime.getRuntime().exec("rundll32 url.dll,FileProtocolHandler " + string);
                } else {
                    String string3 = null;
                    for (String string4 : browsers) {
                        if (string3 != null) continue;
                        if (Runtime.getRuntime().exec(new String[]{"which", string4}).getInputStream().read() == -1) continue;
                        Runtime.getRuntime().exec(new String[]{string3 = string4, string});
                    }
                    if (string3 == null) {
                        throw new Exception("No web browser found");
                    }
                }
            }
            catch (Exception exception) {
                exception.printStackTrace();
            }
        }

        @Override
        public JSObject getWebContext() {
            return null;
        }
    }
}


/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  javafx.application.Application
 *  javafx.application.Preloader
 *  javafx.application.Preloader$ErrorNotification
 *  javafx.application.Preloader$PreloaderNotification
 *  javafx.application.Preloader$ProgressNotification
 *  javafx.application.Preloader$StateChangeNotification
 *  javafx.application.Preloader$StateChangeNotification$Type
 *  javafx.stage.Stage
 */
package com.sun.javafx.application;

import com.sun.javafx.application.ParametersImpl;
import com.sun.javafx.application.PlatformImpl;
import com.sun.javafx.jmx.MXExtension;
import com.sun.javafx.runtime.SystemProperties;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.security.AccessController;
import java.util.ArrayList;
import java.util.Base64;
import java.util.LinkedList;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;
import java.util.jar.Attributes;
import java.util.jar.JarFile;
import java.util.jar.Manifest;
import javafx.application.Application;
import javafx.application.Preloader;
import javafx.stage.Stage;

public class LauncherImpl {
    public static final String LAUNCH_MODE_CLASS = "LM_CLASS";
    public static final String LAUNCH_MODE_JAR = "LM_JAR";
    private static final boolean trace = false;
    private static boolean verbose = false;
    private static final String MF_MAIN_CLASS = "Main-Class";
    private static final String MF_JAVAFX_MAIN = "JavaFX-Application-Class";
    private static final String MF_JAVAFX_PRELOADER = "JavaFX-Preloader-Class";
    private static final String MF_JAVAFX_CLASS_PATH = "JavaFX-Class-Path";
    private static final String MF_JAVAFX_FEATURE_PROXY = "JavaFX-Feature-Proxy";
    private static final String MF_JAVAFX_ARGUMENT_PREFIX = "JavaFX-Argument-";
    private static final String MF_JAVAFX_PARAMETER_NAME_PREFIX = "JavaFX-Parameter-Name-";
    private static final String MF_JAVAFX_PARAMETER_VALUE_PREFIX = "JavaFX-Parameter-Value-";
    private static final boolean simulateSlowProgress = false;
    private static AtomicBoolean launchCalled = new AtomicBoolean(false);
    private static final AtomicBoolean toolkitStarted = new AtomicBoolean(false);
    private static volatile RuntimeException launchException = null;
    private static Preloader currentPreloader = null;
    private static Class<? extends Preloader> savedPreloaderClass = null;
    private static ClassLoader savedMainCcl = null;
    private static volatile boolean error = false;
    private static volatile Throwable pConstructorError = null;
    private static volatile Throwable pInitError = null;
    private static volatile Throwable pStartError = null;
    private static volatile Throwable pStopError = null;
    private static volatile Throwable constructorError = null;
    private static volatile Throwable initError = null;
    private static volatile Throwable startError = null;
    private static volatile Throwable stopError = null;
    private static Method notifyMethod = null;

    public static void launchApplication(Class<? extends Application> class_, String[] arrstring) {
        String string;
        Class<Object> class_2 = savedPreloaderClass;
        if (class_2 == null && (string = AccessController.doPrivileged(() -> System.getProperty("javafx.preloader"))) != null) {
            try {
                class_2 = Class.forName(string, false, class_.getClassLoader());
            }
            catch (Exception exception) {
                System.err.printf("Could not load preloader class '" + string + "', continuing without preloader.", new Object[0]);
                exception.printStackTrace();
            }
        }
        LauncherImpl.launchApplication(class_, class_2, arrstring);
    }

    public static void launchApplication(Class<? extends Application> class_, Class<? extends Preloader> class_2, String[] arrstring) {
        if (launchCalled.getAndSet(true)) {
            throw new IllegalStateException("Application launch must not be called more than once");
        }
        if (!Application.class.isAssignableFrom(class_)) {
            throw new IllegalArgumentException("Error: " + class_.getName() + " is not a subclass of javafx.application.Application");
        }
        if (class_2 != null && !Preloader.class.isAssignableFrom(class_2)) {
            throw new IllegalArgumentException("Error: " + class_2.getName() + " is not a subclass of javafx.application.Preloader");
        }
        CountDownLatch countDownLatch = new CountDownLatch(1);
        Thread thread = new Thread(() -> {
            try {
                LauncherImpl.launchApplication1(class_, class_2, arrstring);
            }
            catch (RuntimeException runtimeException) {
                launchException = runtimeException;
            }
            catch (Exception exception) {
                launchException = new RuntimeException("Application launch exception", exception);
            }
            catch (Error error) {
                launchException = new RuntimeException("Application launch error", error);
            }
            finally {
                countDownLatch.countDown();
            }
        });
        thread.setName("JavaFX-Launcher");
        thread.start();
        try {
            countDownLatch.await();
        }
        catch (InterruptedException interruptedException) {
            throw new RuntimeException("Unexpected exception: ", interruptedException);
        }
        if (launchException != null) {
            throw launchException;
        }
    }

    public static void launchApplication(String string, String string2, String[] arrstring) {
        Object object;
        Object object2;
        if (verbose) {
            System.err.println("Java 8 launchApplication method: launchMode=" + string2);
        }
        String string3 = null;
        String string4 = null;
        String[] arrstring2 = arrstring;
        ClassLoader classLoader = null;
        verbose = Boolean.getBoolean("javafx.verbose");
        if (string2.equals(LAUNCH_MODE_JAR)) {
            String string5;
            object2 = LauncherImpl.getJarAttributes(string);
            if (object2 == null) {
                LauncherImpl.abort(null, "Can't get manifest attributes from jar", new Object[0]);
            }
            if ((object = ((Attributes)object2).getValue(MF_JAVAFX_CLASS_PATH)) != null) {
                if (((String)object).trim().length() == 0) {
                    object = null;
                } else {
                    if (verbose) {
                        System.err.println("WARNING: Application jar uses deprecated JavaFX-Class-Path attribute. Please use Class-Path instead.");
                    }
                    classLoader = LauncherImpl.setupJavaFXClassLoader(new File(string), (String)object);
                }
            }
            if ((string5 = ((Attributes)object2).getValue(MF_JAVAFX_FEATURE_PROXY)) != null && "auto".equals(string5.toLowerCase())) {
                LauncherImpl.trySetAutoProxy();
            }
            if (arrstring.length == 0) {
                arrstring2 = LauncherImpl.getAppArguments((Attributes)object2);
            }
            if ((string3 = ((Attributes)object2).getValue(MF_JAVAFX_MAIN)) == null && (string3 = ((Attributes)object2).getValue(MF_MAIN_CLASS)) == null) {
                LauncherImpl.abort(null, "JavaFX jar manifest requires a valid JavaFX-Appliation-Class or Main-Class entry", new Object[0]);
            }
            string3 = string3.trim();
            string4 = ((Attributes)object2).getValue(MF_JAVAFX_PRELOADER);
            if (string4 != null) {
                string4 = string4.trim();
            }
        } else if (string2.equals(LAUNCH_MODE_CLASS)) {
            string3 = string;
            string4 = System.getProperty("javafx.preloader");
        } else {
            LauncherImpl.abort(new IllegalArgumentException("The launchMode argument must be one of LM_CLASS or LM_JAR"), "Invalid launch mode: %1$s", string2);
        }
        if (string3 == null) {
            LauncherImpl.abort(null, "No main JavaFX class to launch", new Object[0]);
        }
        if (classLoader != null) {
            try {
                object2 = classLoader.loadClass(LauncherImpl.class.getName());
                object = ((Class)object2).getMethod("launchApplicationWithArgs", String.class, String.class, new String[0].getClass());
                Thread.currentThread().setContextClassLoader(classLoader);
                ((Method)object).invoke(null, string3, string4, arrstring2);
            }
            catch (Exception exception) {
                LauncherImpl.abort(exception, "Exception while launching application", new Object[0]);
            }
        } else {
            LauncherImpl.launchApplicationWithArgs(string3, string4, arrstring2);
        }
    }

    public static void launchApplicationWithArgs(String string, String string2, String[] arrstring) {
        try {
            LauncherImpl.startToolkit();
        }
        catch (InterruptedException interruptedException) {
            LauncherImpl.abort(interruptedException, "Toolkit initialization error", string);
        }
        Class class_ = null;
        Class class_2 = null;
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        AtomicReference atomicReference = new AtomicReference();
        AtomicReference atomicReference2 = new AtomicReference();
        PlatformImpl.runAndWait(() -> {
            Class<?> class_ = null;
            try {
                class_ = Class.forName(string, true, classLoader);
            }
            catch (ClassNotFoundException classNotFoundException) {
                LauncherImpl.abort(classNotFoundException, "Missing JavaFX application class %1$s", string);
            }
            atomicReference.set(class_);
            if (string2 != null) {
                try {
                    class_ = Class.forName(string2, true, classLoader);
                }
                catch (ClassNotFoundException classNotFoundException) {
                    LauncherImpl.abort(classNotFoundException, "Missing JavaFX preloader class %1$s", string2);
                }
                if (!Preloader.class.isAssignableFrom(class_)) {
                    LauncherImpl.abort(null, "JavaFX preloader class %1$s does not extend javafx.application.Preloader", class_.getName());
                }
                atomicReference2.set(class_.asSubclass(Preloader.class));
            }
        });
        class_ = (Class)atomicReference2.get();
        class_2 = (Class)atomicReference.get();
        savedPreloaderClass = class_;
        ReflectiveOperationException reflectiveOperationException = null;
        try {
            Method method = class_2.getMethod("main", new String[0].getClass());
            if (verbose) {
                System.err.println("Calling main(String[]) method");
            }
            savedMainCcl = Thread.currentThread().getContextClassLoader();
            method.invoke(null, new Object[]{arrstring});
            return;
        }
        catch (IllegalAccessException | NoSuchMethodException reflectiveOperationException2) {
            reflectiveOperationException = reflectiveOperationException2;
            savedPreloaderClass = null;
        }
        catch (InvocationTargetException invocationTargetException) {
            invocationTargetException.printStackTrace();
            LauncherImpl.abort(null, "Exception running application %1$s", class_2.getName());
            return;
        }
        if (!Application.class.isAssignableFrom(class_2)) {
            LauncherImpl.abort(reflectiveOperationException, "JavaFX application class %1$s does not extend javafx.application.Application", class_2.getName());
        }
        Class<Application> class_3 = class_2.asSubclass(Application.class);
        if (verbose) {
            System.err.println("Launching application directly");
        }
        LauncherImpl.launchApplication(class_3, class_, arrstring);
    }

    private static URL fileToURL(File file) throws IOException {
        return file.getCanonicalFile().toURI().toURL();
    }

    private static ClassLoader setupJavaFXClassLoader(File file, String string) {
        try {
            URL[] arruRL;
            File file2 = file.getParentFile();
            ArrayList<URL> arrayList = new ArrayList<URL>();
            String string2 = string;
            if (string2 != null) {
                while (string2.length() > 0) {
                    String string3;
                    int n = string2.indexOf(" ");
                    if (n < 0) {
                        string3 = string2;
                        URL[] arruRL2 = arruRL = file2 == null ? new File(string3) : new File(file2, string3);
                        if (arruRL.exists()) {
                            arrayList.add(LauncherImpl.fileToURL((File)arruRL));
                            break;
                        }
                        if (!verbose) break;
                        System.err.println("Class Path entry \"" + string3 + "\" does not exist, ignoring");
                        break;
                    }
                    if (n > 0) {
                        string3 = string2.substring(0, n);
                        URL[] arruRL3 = arruRL = file2 == null ? new File(string3) : new File(file2, string3);
                        if (arruRL.exists()) {
                            arrayList.add(LauncherImpl.fileToURL((File)arruRL));
                        } else if (verbose) {
                            System.err.println("Class Path entry \"" + string3 + "\" does not exist, ignoring");
                        }
                    }
                    string2 = string2.substring(n + 1);
                }
            }
            if (!arrayList.isEmpty()) {
                ArrayList<URL> arrayList2 = new ArrayList<URL>();
                string2 = System.getProperty("java.class.path");
                if (string2 != null) {
                    while (string2.length() > 0) {
                        int n = string2.indexOf(File.pathSeparatorChar);
                        if (n < 0) {
                            arruRL = string2;
                            arrayList2.add(LauncherImpl.fileToURL(new File((String)arruRL)));
                            break;
                        }
                        if (n > 0) {
                            arruRL = string2.substring(0, n);
                            arrayList2.add(LauncherImpl.fileToURL(new File((String)arruRL)));
                        }
                        string2 = string2.substring(n + 1);
                    }
                }
                URL uRL = LauncherImpl.class.getProtectionDomain().getCodeSource().getLocation();
                arrayList2.add(uRL);
                arrayList2.addAll(arrayList);
                arruRL = arrayList2.toArray(new URL[0]);
                if (verbose) {
                    System.err.println("===== URL list");
                    for (int i = 0; i < arruRL.length; ++i) {
                        System.err.println("" + arruRL[i]);
                    }
                    System.err.println("=====");
                }
                return new URLClassLoader(arruRL, null);
            }
        }
        catch (Exception exception) {
            // empty catch block
        }
        return null;
    }

    private static void trySetAutoProxy() {
        block9: {
            URL[] arruRL;
            if (System.getProperty("http.proxyHost") != null || System.getProperty("https.proxyHost") != null || System.getProperty("ftp.proxyHost") != null || System.getProperty("socksProxyHost") != null) {
                if (verbose) {
                    System.out.println("Explicit proxy settings detected. Skip autoconfig.");
                    System.out.println("  http.proxyHost=" + System.getProperty("http.proxyHost"));
                    System.out.println("  https.proxyHost=" + System.getProperty("https.proxyHost"));
                    System.out.println("  ftp.proxyHost=" + System.getProperty("ftp.proxyHost"));
                    System.out.println("  socksProxyHost=" + System.getProperty("socksProxyHost"));
                }
                return;
            }
            if (System.getProperty("javafx.autoproxy.disable") != null) {
                if (verbose) {
                    System.out.println("Disable autoproxy on request.");
                }
                return;
            }
            String string = System.getProperty("java.home");
            File file = new File(string, "lib");
            File file2 = new File(file, "deploy.jar");
            try {
                arruRL = new URL[]{file2.toURI().toURL()};
            }
            catch (MalformedURLException malformedURLException) {
                return;
            }
            try {
                URLClassLoader uRLClassLoader = new URLClassLoader(arruRL);
                Class<?> class_ = Class.forName("com.sun.deploy.services.ServiceManager", true, uRLClassLoader);
                Class[] arrclass = new Class[]{Integer.TYPE};
                Method method = class_.getDeclaredMethod("setService", arrclass);
                String string2 = System.getProperty("os.name");
                String string3 = string2.startsWith("Win") ? "STANDALONE_TIGER_WIN32" : (string2.contains("Mac") ? "STANDALONE_TIGER_MACOSX" : "STANDALONE_TIGER_UNIX");
                Object[] arrobject = new Object[1];
                Class<?> class_2 = Class.forName("com.sun.deploy.services.PlatformType", true, uRLClassLoader);
                arrobject[0] = class_2.getField(string3).get(null);
                method.invoke(null, arrobject);
                Class<?> class_3 = Class.forName("com.sun.deploy.net.proxy.DeployProxySelector", true, uRLClassLoader);
                Method method2 = class_3.getDeclaredMethod("reset", new Class[0]);
                method2.invoke(null, new Object[0]);
                if (verbose) {
                    System.out.println("Autoconfig of proxy is completed.");
                }
            }
            catch (Exception exception) {
                if (!verbose) break block9;
                System.err.println("Failed to autoconfig proxy due to " + exception);
            }
        }
    }

    private static String decodeBase64(String string) throws IOException {
        return new String(Base64.getDecoder().decode(string));
    }

    private static String[] getAppArguments(Attributes attributes) {
        LinkedList<String> linkedList = new LinkedList<String>();
        try {
            int n = 1;
            String string = MF_JAVAFX_ARGUMENT_PREFIX;
            while (attributes.getValue(string + n) != null) {
                linkedList.add(LauncherImpl.decodeBase64(attributes.getValue(string + n)));
                ++n;
            }
            String string2 = MF_JAVAFX_PARAMETER_NAME_PREFIX;
            String string3 = MF_JAVAFX_PARAMETER_VALUE_PREFIX;
            n = 1;
            while (attributes.getValue(string2 + n) != null) {
                String string4 = LauncherImpl.decodeBase64(attributes.getValue(string2 + n));
                String string5 = null;
                if (attributes.getValue(string3 + n) != null) {
                    string5 = LauncherImpl.decodeBase64(attributes.getValue(string3 + n));
                }
                linkedList.add("--" + string4 + "=" + (string5 != null ? string5 : ""));
                ++n;
            }
        }
        catch (IOException iOException) {
            if (verbose) {
                System.err.println("Failed to extract application parameters");
            }
            iOException.printStackTrace();
        }
        return linkedList.toArray(new String[0]);
    }

    private static void abort(Throwable throwable, String string, Object ... arrobject) {
        String string2 = String.format(string, arrobject);
        if (string2 != null) {
            System.err.println(string2);
        }
        System.exit(1);
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    private static Attributes getJarAttributes(String string) {
        JarFile jarFile = null;
        try {
            jarFile = new JarFile(string);
            Manifest manifest = jarFile.getManifest();
            if (manifest == null) {
                LauncherImpl.abort(null, "No manifest in jar file %1$s", string);
            }
            Attributes attributes = manifest.getMainAttributes();
            return attributes;
        }
        catch (IOException iOException) {
            LauncherImpl.abort(iOException, "Error launching jar file %1%s", string);
        }
        finally {
            try {
                jarFile.close();
            }
            catch (IOException iOException) {}
        }
        return null;
    }

    private static void startToolkit() throws InterruptedException {
        if (toolkitStarted.getAndSet(true)) {
            return;
        }
        if (SystemProperties.isDebug()) {
            MXExtension.initializeIfAvailable();
        }
        CountDownLatch countDownLatch = new CountDownLatch(1);
        PlatformImpl.startup(() -> countDownLatch.countDown());
        countDownLatch.await();
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    private static void launchApplication1(Class<? extends Application> class_, Class<? extends Preloader> class_2, String[] arrstring) throws Exception {
        boolean bl;
        PlatformImpl.FinishListener finishListener;
        block33: {
            Object object;
            LauncherImpl.startToolkit();
            if (savedMainCcl != null && (object = Thread.currentThread().getContextClassLoader()) != null && object != savedMainCcl) {
                PlatformImpl.runLater(() -> LauncherImpl.lambda$launchApplication1$158((ClassLoader)object));
            }
            object = new AtomicBoolean(false);
            final AtomicBoolean atomicBoolean = new AtomicBoolean(false);
            AtomicBoolean atomicBoolean2 = new AtomicBoolean(false);
            AtomicBoolean atomicBoolean3 = new AtomicBoolean(false);
            final CountDownLatch countDownLatch = new CountDownLatch(1);
            CountDownLatch countDownLatch2 = new CountDownLatch(1);
            finishListener = new PlatformImpl.FinishListener((AtomicBoolean)object, countDownLatch2, atomicBoolean2){
                final /* synthetic */ AtomicBoolean val$pStartCalled;
                final /* synthetic */ CountDownLatch val$pShutdownLatch;
                final /* synthetic */ AtomicBoolean val$exitCalled;
                {
                    this.val$pStartCalled = atomicBoolean2;
                    this.val$pShutdownLatch = countDownLatch2;
                    this.val$exitCalled = atomicBoolean3;
                }

                @Override
                public void idle(boolean bl) {
                    if (!bl) {
                        return;
                    }
                    if (atomicBoolean.get()) {
                        countDownLatch.countDown();
                    } else if (this.val$pStartCalled.get()) {
                        this.val$pShutdownLatch.countDown();
                    }
                }

                @Override
                public void exitCalled() {
                    this.val$exitCalled.set(true);
                    countDownLatch.countDown();
                }
            };
            PlatformImpl.addListener(finishListener);
            try {
                String string;
                AtomicReference atomicReference = new AtomicReference();
                if (class_2 != null) {
                    PlatformImpl.runAndWait(() -> {
                        try {
                            Constructor constructor = class_2.getConstructor(new Class[0]);
                            atomicReference.set(constructor.newInstance(new Object[0]));
                            ParametersImpl.registerParameters((Application)atomicReference.get(), new ParametersImpl(arrstring));
                        }
                        catch (Throwable throwable) {
                            System.err.println("Exception in Preloader constructor");
                            pConstructorError = throwable;
                            error = true;
                        }
                    });
                }
                if ((currentPreloader = (Preloader)atomicReference.get()) != null && !error && !atomicBoolean2.get()) {
                    try {
                        currentPreloader.init();
                    }
                    catch (Throwable throwable) {
                        System.err.println("Exception in Preloader init method");
                        pInitError = throwable;
                        error = true;
                    }
                }
                if (currentPreloader != null && !error && !atomicBoolean2.get()) {
                    PlatformImpl.runAndWait(() -> LauncherImpl.lambda$launchApplication1$160((AtomicBoolean)object));
                    if (!error && !atomicBoolean2.get()) {
                        LauncherImpl.notifyProgress(currentPreloader, 0.0);
                    }
                }
                AtomicReference atomicReference2 = new AtomicReference();
                if (!error && !atomicBoolean2.get()) {
                    if (currentPreloader != null) {
                        LauncherImpl.notifyProgress(currentPreloader, 1.0);
                        LauncherImpl.notifyStateChange(currentPreloader, Preloader.StateChangeNotification.Type.BEFORE_LOAD, null);
                    }
                    PlatformImpl.runAndWait(() -> {
                        try {
                            Constructor constructor = class_.getConstructor(new Class[0]);
                            atomicReference2.set(constructor.newInstance(new Object[0]));
                            ParametersImpl.registerParameters((Application)atomicReference2.get(), new ParametersImpl(arrstring));
                            PlatformImpl.setApplicationName(class_);
                        }
                        catch (Throwable throwable) {
                            System.err.println("Exception in Application constructor");
                            constructorError = throwable;
                            error = true;
                        }
                    });
                }
                Application application = (Application)atomicReference2.get();
                if (!error && !atomicBoolean2.get()) {
                    if (currentPreloader != null) {
                        LauncherImpl.notifyStateChange(currentPreloader, Preloader.StateChangeNotification.Type.BEFORE_INIT, application);
                    }
                    try {
                        application.init();
                    }
                    catch (Throwable throwable) {
                        System.err.println("Exception in Application init method");
                        initError = throwable;
                        error = true;
                    }
                }
                if (!error && !atomicBoolean2.get()) {
                    if (currentPreloader != null) {
                        LauncherImpl.notifyStateChange(currentPreloader, Preloader.StateChangeNotification.Type.BEFORE_START, application);
                    }
                    PlatformImpl.runAndWait(() -> {
                        try {
                            atomicBoolean.set(true);
                            Stage stage = new Stage();
                            stage.impl_setPrimary(true);
                            application.start(stage);
                        }
                        catch (Throwable throwable) {
                            System.err.println("Exception in Application start method");
                            startError = throwable;
                            error = true;
                        }
                    });
                }
                if (!error) {
                    countDownLatch.await();
                }
                if (atomicBoolean.get()) {
                    PlatformImpl.runAndWait(() -> {
                        try {
                            application.stop();
                        }
                        catch (Throwable throwable) {
                            System.err.println("Exception in Application stop method");
                            stopError = throwable;
                            error = true;
                        }
                    });
                }
                if (!error) break block33;
                if (pConstructorError != null) {
                    throw new RuntimeException("Unable to construct Preloader instance: " + class_, pConstructorError);
                }
                if (pInitError != null) {
                    throw new RuntimeException("Exception in Preloader init method", pInitError);
                }
                if (pStartError != null) {
                    throw new RuntimeException("Exception in Preloader start method", pStartError);
                }
                if (pStopError != null) {
                    throw new RuntimeException("Exception in Preloader stop method", pStopError);
                }
                if (constructorError != null) {
                    String string2 = "Unable to construct Application instance: " + class_;
                    if (!LauncherImpl.notifyError(string2, constructorError)) {
                        throw new RuntimeException(string2, constructorError);
                    }
                    break block33;
                }
                if (initError != null) {
                    String string3 = "Exception in Application init method";
                    if (!LauncherImpl.notifyError(string3, initError)) {
                        throw new RuntimeException(string3, initError);
                    }
                    break block33;
                }
                if (startError != null) {
                    String string4 = "Exception in Application start method";
                    if (!LauncherImpl.notifyError(string4, startError)) {
                        throw new RuntimeException(string4, startError);
                    }
                    break block33;
                }
                if (stopError == null || LauncherImpl.notifyError(string = "Exception in Application stop method", stopError)) break block33;
                throw new RuntimeException(string, stopError);
            }
            catch (Throwable throwable) {
                boolean bl2;
                PlatformImpl.removeListener(finishListener);
                boolean bl3 = bl2 = System.getSecurityManager() != null;
                if (error && bl2) {
                    System.err.println("Workaround until RT-13281 is implemented: keep toolkit alive");
                } else {
                    PlatformImpl.tkExit();
                }
                throw throwable;
            }
        }
        PlatformImpl.removeListener(finishListener);
        boolean bl4 = bl = System.getSecurityManager() != null;
        if (error && bl) {
            System.err.println("Workaround until RT-13281 is implemented: keep toolkit alive");
        } else {
            PlatformImpl.tkExit();
        }
    }

    private static void notifyStateChange(Preloader preloader, Preloader.StateChangeNotification.Type type2, Application application) {
        PlatformImpl.runAndWait(() -> preloader.handleStateChangeNotification(new Preloader.StateChangeNotification(type2, application)));
    }

    private static void notifyProgress(Preloader preloader, double d) {
        PlatformImpl.runAndWait(() -> preloader.handleProgressNotification(new Preloader.ProgressNotification(d)));
    }

    private static boolean notifyError(String string, Throwable throwable) {
        AtomicBoolean atomicBoolean = new AtomicBoolean(false);
        PlatformImpl.runAndWait(() -> {
            if (currentPreloader != null) {
                try {
                    Preloader.ErrorNotification errorNotification = new Preloader.ErrorNotification(null, string, throwable);
                    boolean bl = currentPreloader.handleErrorNotification(errorNotification);
                    atomicBoolean.set(bl);
                }
                catch (Throwable throwable2) {
                    throwable2.printStackTrace();
                }
            }
        });
        return atomicBoolean.get();
    }

    private static void notifyCurrentPreloader(Preloader.PreloaderNotification preloaderNotification) {
        PlatformImpl.runAndWait(() -> {
            if (currentPreloader != null) {
                currentPreloader.handleApplicationNotification(preloaderNotification);
            }
        });
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public static void notifyPreloader(Application application, Preloader.PreloaderNotification preloaderNotification) {
        if (launchCalled.get()) {
            LauncherImpl.notifyCurrentPreloader(preloaderNotification);
            return;
        }
        Class<LauncherImpl> class_ = LauncherImpl.class;
        synchronized (LauncherImpl.class) {
            if (notifyMethod == null) {
                try {
                    Class<?> class_2 = Class.forName("com.sun.deploy.uitoolkit.impl.fx.FXPreloader");
                    notifyMethod = class_2.getMethod("notifyCurrentPreloader", Preloader.PreloaderNotification.class);
                }
                catch (Exception exception) {
                    exception.printStackTrace();
                    // ** MonitorExit[var2_2] (shouldn't be in output)
                    return;
                }
            }
            // ** MonitorExit[var2_2] (shouldn't be in output)
            try {
                notifyMethod.invoke(null, new Object[]{preloaderNotification});
            }
            catch (Exception exception) {
                exception.printStackTrace();
            }
            return;
        }
    }

    private LauncherImpl() {
        throw new InternalError();
    }

    private static /* synthetic */ void lambda$launchApplication1$160(AtomicBoolean atomicBoolean) {
        try {
            atomicBoolean.set(true);
            Stage stage = new Stage();
            stage.impl_setPrimary(true);
            currentPreloader.start(stage);
        }
        catch (Throwable throwable) {
            System.err.println("Exception in Preloader start method");
            pStartError = throwable;
            error = true;
        }
    }

    private static /* synthetic */ void lambda$launchApplication1$158(ClassLoader classLoader) {
        Thread.currentThread().setContextClassLoader(classLoader);
    }
}


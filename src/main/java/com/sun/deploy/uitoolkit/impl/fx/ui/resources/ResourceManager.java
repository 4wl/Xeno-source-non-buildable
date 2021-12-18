/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.sun.deploy.trace.Trace
 *  javafx.scene.image.Image
 *  javafx.scene.image.ImageView
 */
package com.sun.deploy.uitoolkit.impl.fx.ui.resources;

import com.sun.deploy.trace.Trace;
import java.net.URL;
import java.security.AccessController;
import java.security.PrivilegedExceptionAction;
import java.text.MessageFormat;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class ResourceManager {
    private static ResourceBundle rbFX;
    private static ResourceBundle rbJRE;
    private static final String UNDERSCORE = "_";
    private static final String ESC_UNDERSCORE = "__";
    private static final String AMPERSAND = "&";
    private static final String ESC_AMPERSAND = "&&";
    private static Pattern p_start;
    private static Pattern p_other;
    private static Pattern p_underscore;
    private static Pattern p_escampersand;

    static void reset() {
        rbFX = ResourceBundle.getBundle("com.sun.deploy.uitoolkit.impl.fx.ui.resources.Deployment");
        try {
            rbJRE = ResourceBundle.getBundle("com.sun.deploy.resources.Deployment");
        }
        catch (MissingResourceException missingResourceException) {
            Trace.ignoredException((Exception)missingResourceException);
            rbJRE = rbFX;
        }
    }

    public static String getMessage(String string) {
        return ResourceManager.convertMnemonics(ResourceManager.getString(string));
    }

    private static String escapeUnderscore(String string) {
        return p_underscore.matcher(string).replaceAll(ESC_UNDERSCORE);
    }

    private static String unescapeAmpersand(String string) {
        return p_escampersand.matcher(string).replaceAll(AMPERSAND);
    }

    private static String convertMnemonics(String string) {
        Matcher matcher;
        String string2 = p_start.matcher(string).find() ? UNDERSCORE + string.substring(1) : ((matcher = p_other.matcher(string)).find() ? ResourceManager.escapeUnderscore(string.substring(0, matcher.start() + 1)) + UNDERSCORE + string.substring(matcher.end() - 1) : ResourceManager.escapeUnderscore(string));
        return ResourceManager.unescapeAmpersand(string2);
    }

    public static String getFormattedMessage(String string, Object[] arrobject) {
        try {
            return new MessageFormat(ResourceManager.getMessage(string)).format(arrobject);
        }
        catch (MissingResourceException missingResourceException) {
            Trace.ignoredException((Exception)missingResourceException);
            return string;
        }
    }

    public static String getString(String string) {
        try {
            return rbFX.containsKey(string) ? rbFX.getString(string) : rbJRE.getString(string);
        }
        catch (MissingResourceException missingResourceException) {
            return string;
        }
    }

    public static String getString(String string, Object ... arrobject) {
        return MessageFormat.format(ResourceManager.getString(string), arrobject);
    }

    public static ImageView getIcon(final String string) {
        try {
            return AccessController.doPrivileged(new PrivilegedExceptionAction<ImageView>(){

                @Override
                public ImageView run() {
                    return ResourceManager.getIcon_(string);
                }
            });
        }
        catch (Exception exception) {
            Trace.ignoredException((Exception)exception);
            return null;
        }
    }

    public static ImageView getIcon_(String string) {
        String string2 = ResourceManager.getString(string);
        URL uRL = rbFX.getClass().getResource(string2);
        String string3 = rbFX.getClass().getName();
        if (uRL == null || string.equals("about.java.image")) {
            uRL = rbJRE.getClass().getResource(string2);
            string3 = rbJRE.getClass().getName();
        }
        return ResourceManager.getIcon(uRL);
    }

    public static ImageView getIcon(URL uRL) {
        Image image = new Image(uRL.toString());
        return new ImageView(image);
    }

    static {
        p_start = Pattern.compile("^&[^&]");
        p_other = Pattern.compile("[^&]&[^&]");
        p_underscore = Pattern.compile(UNDERSCORE);
        p_escampersand = Pattern.compile(ESC_AMPERSAND);
        ResourceManager.reset();
    }
}


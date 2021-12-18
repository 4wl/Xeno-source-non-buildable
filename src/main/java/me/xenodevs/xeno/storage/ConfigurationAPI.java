/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  org.apache.commons.io.Charsets
 *  org.apache.commons.io.FileUtils
 */
package me.xenodevs.xeno.storage;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import me.xenodevs.xeno.storage.Configuration;
import org.apache.commons.io.Charsets;
import org.apache.commons.io.FileUtils;
import org.json.JSONObject;

public class ConfigurationAPI {
    public static Configuration loadExistingConfiguration(File file) throws IOException {
        JSONObject jsonObject = new JSONObject(FileUtils.readFileToString((File)file, (Charset)Charsets.UTF_8));
        return new Configuration(file, jsonObject.toMap());
    }

    public static Configuration newConfiguration(File file) {
        return new Configuration(file);
    }
}


/*
 * Decompiled with CFR 0.150.
 */
package me.xenodevs.xeno.storage;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import org.json.JSONException;
import org.json.JSONObject;

public class Configuration {
    private File file;
    public Map<String, Object> options;

    public Configuration(File file, Map<String, Object> options) {
        this.file = file;
        this.options = options;
    }

    public Configuration(File file) {
        this.file = file;
        this.options = new HashMap<String, Object>();
    }

    public Configuration() {
        this.file = null;
        this.options = new HashMap<String, Object>();
    }

    public Object get(String key) {
        return this.options.get(key);
    }

    public double getDouble(String key, Double defaultValue) {
        try {
            String val = String.valueOf(this.options.get(key));
            double dVal = Double.parseDouble(val);
            return dVal;
        }
        catch (NumberFormatException e) {
            return defaultValue;
        }
    }

    public float getFloat(String key) {
        return ((Float)this.options.get(key)).floatValue();
    }

    public boolean getBoolean(String key) {
        return (Boolean)this.options.get(key);
    }

    public void setBool(String key, Boolean value) {
        this.options.put(key, value);
    }

    public void set(String key, Object value) {
        this.options.put(key, value);
    }

    public void save() throws IOException {
        JSONObject jsonObject = new JSONObject(this.options);
        this.file.createNewFile();
        FileWriter fileWriter = new FileWriter(this.file);
        try {
            fileWriter.write(jsonObject.toString(4));
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        catch (JSONException e) {
            e.printStackTrace();
        }
        fileWriter.flush();
        fileWriter.close();
    }
}


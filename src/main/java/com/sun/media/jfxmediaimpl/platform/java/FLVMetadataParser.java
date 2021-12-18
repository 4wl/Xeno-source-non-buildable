/*
 * Decompiled with CFR 0.150.
 */
package com.sun.media.jfxmediaimpl.platform.java;

import com.sun.media.jfxmedia.locator.Locator;
import com.sun.media.jfxmedia.logging.Logger;
import com.sun.media.jfxmediaimpl.MetadataParserImpl;
import java.io.IOException;
import java.nio.charset.Charset;

final class FLVMetadataParser
extends MetadataParserImpl {
    private int dataSize = 0;
    private static final String CHARSET_UTF_8 = "UTF-8";

    public FLVMetadataParser(Locator locator) {
        super(locator);
    }

    @Override
    protected void parse() {
        block5: {
            try {
                if (this.getNextByte() != 70 || this.getNextByte() != 76 || this.getNextByte() != 86) break block5;
                this.skipBytes(2);
                int n = this.getInteger();
                this.skipBytes(n - 9);
                int n2 = 0;
                for (n2 = 0; n2 < 10; ++n2) {
                    this.skipBytes(4);
                    byte by = this.getNextByte();
                    this.dataSize = this.getU24();
                    this.skipBytes(7);
                    if (by == 18) {
                        int n3 = this.getStreamPosition() + this.dataSize;
                        if (!this.parseDataTag()) {
                            if (this.getStreamPosition() >= n3) continue;
                            this.skipBytes(n3 - this.getStreamPosition());
                            continue;
                        }
                        break;
                    }
                    this.skipBytes(this.dataSize);
                }
            }
            catch (IOException iOException) {
                // empty catch block
            }
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    private boolean parseDataTag() throws IOException {
        int n;
        if (this.dataSize < 14) {
            return false;
        }
        byte[] arrby = new byte[14];
        for (n = 0; n < 14; ++n) {
            arrby[n] = this.getNextByte();
        }
        if (arrby[0] != 2) {
            return false;
        }
        n = (arrby[1] & 0xFF) << 8 | arrby[2] & 0xFF;
        if (n != 10) {
            return false;
        }
        if (!Charset.isSupported(CHARSET_UTF_8)) {
            return false;
        }
        String string = new String(arrby, 3, n, Charset.forName(CHARSET_UTF_8));
        if (!string.equals("onMetaData")) {
            return false;
        }
        if (arrby[13] != 8) {
            if (Logger.canLog(3)) {
                Logger.logMsg(3, "FLV metadata must be in an ECMA array");
            }
            return false;
        }
        this.startRawMetadata(this.dataSize);
        if (null == this.rawMetaBlob) {
            if (Logger.canLog(1)) {
                Logger.logMsg(1, "Unable to allocate buffer for FLV metadata");
            }
            return false;
        }
        this.stuffRawMetadata(arrby, 0, 14);
        this.readRawMetadata(this.dataSize - 14);
        this.setParseRawMetadata(true);
        this.skipBytes(14);
        try {
            int n2 = this.getInteger();
            int n3 = 0;
            boolean bl = false;
            boolean bl2 = false;
            do {
                Object object;
                String string2 = this.getString(this.getShort(), Charset.forName(CHARSET_UTF_8));
                FlvDataValue flvDataValue = this.readDataValue(false);
                ++n3;
                String string3 = this.convertTag(string2);
                if (Logger.canLog(1) && !string2.equals("")) {
                    Logger.logMsg(1, n3 + ": \"" + string2 + "\" -> " + (null == string3 ? "(unsupported)" : "\"" + string3 + "\""));
                }
                if (string3 != null && (object = this.convertValue(string2, flvDataValue.obj)) != null) {
                    this.addMetadataItem(string3, object);
                }
                if (n3 < n2) continue;
                if (this.getStreamPosition() < this.dataSize) {
                    if (bl2 || !Logger.canLog(3)) continue;
                    Logger.logMsg(3, "FLV Source has malformed metadata, invalid ECMA element count");
                    bl2 = true;
                    continue;
                }
                bl = true;
            } while (!bl);
        }
        catch (Exception exception) {
            if (Logger.canLog(3)) {
                Logger.logMsg(3, "Exception while processing FLV metadata: " + exception);
            }
        }
        finally {
            if (null != this.rawMetaBlob) {
                this.setParseRawMetadata(false);
                this.addRawMetadata("FLV");
                this.disposeRawMetadata();
            }
            this.done();
        }
        return true;
    }

    private FlvDataValue readDataValue(boolean bl) throws IOException {
        FlvDataValue flvDataValue = new FlvDataValue();
        if (bl) {
            this.skipBytes(this.getShort());
        }
        flvDataValue.type = this.getNextByte();
        switch (flvDataValue.type) {
            case 0: {
                flvDataValue.obj = this.getDouble();
                break;
            }
            case 1: {
                boolean bl2 = this.getNextByte() != 0;
                flvDataValue.obj = bl2;
                break;
            }
            case 2: {
                flvDataValue.obj = this.getString(this.getShort(), Charset.forName(CHARSET_UTF_8));
                break;
            }
            case 3: {
                this.skipObject();
                break;
            }
            case 4: {
                this.getString(this.getShort(), Charset.forName(CHARSET_UTF_8));
                break;
            }
            case 5: {
                break;
            }
            case 6: {
                break;
            }
            case 7: {
                this.skipBytes(2);
                break;
            }
            case 8: {
                this.skipArray();
                break;
            }
            case 9: {
                flvDataValue.scriptDataObjectEnd = true;
                break;
            }
            case 10: {
                this.skipStrictArray();
                break;
            }
            case 11: {
                flvDataValue.obj = this.getDouble();
                this.skipBytes(2);
                break;
            }
            case 12: {
                flvDataValue.obj = this.getString(this.getInteger(), Charset.forName(CHARSET_UTF_8));
                break;
            }
        }
        return flvDataValue;
    }

    private void skipObject() throws IOException {
        FlvDataValue flvDataValue;
        do {
            flvDataValue = this.readDataValue(true);
        } while (!flvDataValue.scriptDataObjectEnd);
    }

    private void skipArray() throws IOException {
        int n = this.getInteger();
        for (int i = 0; i < n; ++i) {
            this.readDataValue(true);
        }
    }

    private void skipStrictArray() throws IOException {
        long l = this.getInteger();
        int n = 0;
        while ((long)n < l) {
            this.readDataValue(false);
            ++n;
        }
    }

    private String convertTag(String string) {
        if (string.equals("duration")) {
            return "duration";
        }
        if (string.equals("width")) {
            return "width";
        }
        if (string.equals("height")) {
            return "height";
        }
        if (string.equals("framerate")) {
            return "framerate";
        }
        if (string.equals("videocodecid")) {
            return "video codec";
        }
        if (string.equals("audiocodecid")) {
            return "audio codec";
        }
        if (string.equals("creationdate")) {
            return "creationdate";
        }
        return null;
    }

    private static class FlvDataValue {
        static final byte NUMBER = 0;
        static final byte BOOLEAN = 1;
        static final byte STRING = 2;
        static final byte OBJECT = 3;
        static final byte MOVIE_CLIP = 4;
        static final byte NULL = 5;
        static final byte UNDEFINED = 6;
        static final byte REFERENCE = 7;
        static final byte ECMA_ARRAY = 8;
        static final byte END_OF_DATA = 9;
        static final byte STRICT_ARRAY = 10;
        static final byte DATE = 11;
        static final byte LONG_STRING = 12;
        boolean scriptDataObjectEnd = false;
        Object obj;
        byte type;

        private FlvDataValue() {
        }
    }
}


/*
 * Decompiled with CFR 0.150.
 */
package com.sun.media.jfxmediaimpl.platform.java;

import com.sun.media.jfxmedia.locator.Locator;
import com.sun.media.jfxmedia.logging.Logger;
import com.sun.media.jfxmediaimpl.MetadataParserImpl;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.UnsupportedCharsetException;
import java.util.Arrays;

final class ID3MetadataParser
extends MetadataParserImpl {
    private static final int ID3_VERSION_MIN = 2;
    private static final int ID3_VERSION_MAX = 4;
    private static final String CHARSET_UTF_8 = "UTF-8";
    private static final String CHARSET_ISO_8859_1 = "ISO-8859-1";
    private static final String CHARSET_UTF_16 = "UTF-16";
    private static final String CHARSET_UTF_16BE = "UTF-16BE";
    private int COMMCount = 0;
    private int TXXXCount = 0;
    private int version = 3;
    private boolean unsynchronized = false;

    public ID3MetadataParser(Locator locator) {
        super(locator);
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Override
    protected void parse() {
        block32: {
            try {
                int n;
                if (!Charset.isSupported(CHARSET_ISO_8859_1)) {
                    throw new UnsupportedCharsetException(CHARSET_ISO_8859_1);
                }
                byte[] arrby = this.getBytes(10);
                this.version = arrby[3] & 0xFF;
                if (arrby[0] != 73 || arrby[1] != 68 || arrby[2] != 51 || this.version < 2 || this.version > 4) break block32;
                int n2 = arrby[5] & 0xFF;
                if ((n2 & 0x80) == 128) {
                    this.unsynchronized = true;
                }
                int n3 = 0;
                int n4 = 21;
                for (n = 6; n < 10; ++n) {
                    n3 += (arrby[n] & 0x7F) << n4;
                    n4 -= 7;
                }
                this.startRawMetadata(n3 + 10);
                this.stuffRawMetadata(arrby, 0, 10);
                this.readRawMetadata(n3);
                this.setParseRawMetadata(true);
                this.skipBytes(10);
                while (this.getStreamPosition() < n3) {
                    Object object;
                    Object object2;
                    String string;
                    byte[] arrby2;
                    Object object3;
                    byte[] arrby3;
                    if (2 == this.version) {
                        arrby3 = this.getBytes(3);
                        n = this.getU24();
                    } else {
                        arrby3 = this.getBytes(4);
                        n = this.getFrameSize();
                        this.skipBytes(2);
                    }
                    if (0 == arrby3[0]) {
                        if (Logger.canLog(1)) {
                            Logger.logMsg(1, "ID3MetadataParser", "parse", "ID3 parser: zero padding detected at " + this.getStreamPosition() + ", terminating");
                        }
                        break;
                    }
                    String string2 = new String(arrby3, Charset.forName(CHARSET_ISO_8859_1));
                    if (Logger.canLog(1)) {
                        Logger.logMsg(1, "ID3MetadataParser", "parse", this.getStreamPosition() + "\\" + n3 + ": frame ID " + string2 + ", size " + n);
                    }
                    if (string2.equals("APIC") || string2.equals("PIC")) {
                        object3 = this.getBytes(n);
                        if (this.unsynchronized) {
                            object3 = this.unsynchronizeBuffer((byte[])object3);
                        }
                        if ((arrby2 = string2.equals("PIC") ? this.getImageFromPIC((byte[])object3) : this.getImageFromAPIC((byte[])object3)) == null) continue;
                        this.addMetadataItem("image", arrby2);
                        continue;
                    }
                    if (string2.startsWith("T") && !string2.equals("TXXX")) {
                        object3 = this.getEncoding();
                        arrby2 = this.getBytes(n - 1);
                        if (this.unsynchronized) {
                            arrby2 = this.unsynchronizeBuffer(arrby2);
                        }
                        string = new String(arrby2, (String)object3);
                        String[] arrstring = this.getTagFromFrameID(string2);
                        if (arrstring == null) continue;
                        for (int i = 0; i < arrstring.length; ++i) {
                            object2 = this.convertValue(arrstring[i], string);
                            if (object2 == null) continue;
                            this.addMetadataItem(arrstring[i], object2);
                        }
                        continue;
                    }
                    if (string2.equals("COMM") || string2.equals("COM")) {
                        String[] arrstring;
                        String string3;
                        object3 = this.getEncoding();
                        arrby2 = this.getBytes(3);
                        if (this.unsynchronized) {
                            arrby2 = this.unsynchronizeBuffer(arrby2);
                        }
                        string = new String(arrby2, Charset.forName(CHARSET_ISO_8859_1));
                        arrby2 = this.getBytes(n - 4);
                        if (this.unsynchronized) {
                            arrby2 = this.unsynchronizeBuffer(arrby2);
                        }
                        if ((string3 = new String(arrby2, (String)object3)) == null) continue;
                        int n5 = string3.indexOf(0);
                        object2 = "";
                        if (n5 == 0) {
                            object = this.isTwoByteEncoding((String)object3) ? string3.substring(2) : string3.substring(1);
                        } else {
                            object2 = string3.substring(0, n5);
                            object = this.isTwoByteEncoding((String)object3) ? string3.substring(n5 + 2) : string3.substring(n5 + 1);
                        }
                        if ((arrstring = this.getTagFromFrameID(string2)) == null) continue;
                        for (int i = 0; i < arrstring.length; ++i) {
                            this.addMetadataItem(arrstring[i] + "-" + this.COMMCount, (String)object2 + "[" + string + "]=" + (String)object);
                            ++this.COMMCount;
                        }
                        continue;
                    }
                    if (string2.equals("TXX") || string2.equals("TXXX")) {
                        object3 = this.getEncoding();
                        arrby2 = this.getBytes(n - 1);
                        if (this.unsynchronized) {
                            arrby2 = this.unsynchronizeBuffer(arrby2);
                        }
                        if (null == (string = new String(arrby2, (String)object3))) continue;
                        int n6 = string.indexOf(0);
                        String string4 = n6 != 0 ? string.substring(0, n6) : "";
                        Object object4 = object2 = this.isTwoByteEncoding((String)object3) ? string.substring(n6 + 2) : string.substring(n6 + 1);
                        object = this.getTagFromFrameID(string2);
                        if (object == null) continue;
                        for (int i = 0; i < ((String[])object).length; ++i) {
                            if (string4.equals("")) {
                                this.addMetadataItem(object[i] + "-" + this.TXXXCount, object2);
                            } else {
                                this.addMetadataItem(object[i] + "-" + this.TXXXCount, string4 + "=" + (String)object2);
                            }
                            ++this.TXXXCount;
                        }
                        continue;
                    }
                    this.skipBytes(n);
                }
            }
            catch (Exception exception) {
                if (Logger.canLog(3)) {
                    Logger.logMsg(3, "ID3MetadataParser", "parse", "Exception while processing ID3v2 metadata: " + exception);
                }
            }
            finally {
                if (null != this.rawMetaBlob) {
                    this.setParseRawMetadata(false);
                    this.addRawMetadata("ID3");
                    this.disposeRawMetadata();
                }
                this.done();
            }
        }
    }

    private int getFrameSize() throws IOException {
        if (this.version == 4) {
            byte[] arrby = this.getBytes(4);
            int n = 0;
            int n2 = 21;
            for (int i = 0; i < 4; ++i) {
                n += (arrby[i] & 0x7F) << n2;
                n2 -= 7;
            }
            return n;
        }
        return this.getInteger();
    }

    private String getEncoding() throws IOException {
        byte by = this.getNextByte();
        if (by == 0) {
            return CHARSET_ISO_8859_1;
        }
        if (by == 1) {
            return CHARSET_UTF_16;
        }
        if (by == 2) {
            return CHARSET_UTF_16BE;
        }
        if (by == 3) {
            return CHARSET_UTF_8;
        }
        throw new IllegalArgumentException();
    }

    private boolean isTwoByteEncoding(String string) {
        if (string.equals(CHARSET_ISO_8859_1) || string.equals(CHARSET_UTF_8)) {
            return false;
        }
        if (string.equals(CHARSET_UTF_16) || string.equals(CHARSET_UTF_16BE)) {
            return true;
        }
        throw new IllegalArgumentException();
    }

    private String[] getTagFromFrameID(String string) {
        if (string.equals("TPE2") || string.equals("TP2")) {
            return new String[]{"album artist"};
        }
        if (string.equals("TALB") || string.equals("TAL")) {
            return new String[]{"album"};
        }
        if (string.equals("TPE1") || string.equals("TP1")) {
            return new String[]{"artist"};
        }
        if (string.equals("COMM") || string.equals("COM")) {
            return new String[]{"comment"};
        }
        if (string.equals("TCOM") || string.equals("TCM")) {
            return new String[]{"composer"};
        }
        if (string.equals("TLEN") || string.equals("TLE")) {
            return new String[]{"duration"};
        }
        if (string.equals("TCON") || string.equals("TCO")) {
            return new String[]{"genre"};
        }
        if (string.equals("TIT2") || string.equals("TT2")) {
            return new String[]{"title"};
        }
        if (string.equals("TRCK") || string.equals("TRK")) {
            return new String[]{"track number", "track count"};
        }
        if (string.equals("TPOS") || string.equals("TPA")) {
            return new String[]{"disc number", "disc count"};
        }
        if (string.equals("TYER") || string.equals("TDRC")) {
            return new String[]{"year"};
        }
        if (string.equals("TXX") || string.equals("TXXX")) {
            return new String[]{"text"};
        }
        return null;
    }

    private byte[] getImageFromPIC(byte[] arrby) {
        int n;
        for (n = 5; 0 != arrby[n] && n < arrby.length; ++n) {
        }
        if (n == arrby.length) {
            return null;
        }
        String string = new String(arrby, 1, 3, Charset.forName(CHARSET_ISO_8859_1));
        if (Logger.canLog(1)) {
            Logger.logMsg(1, "ID3MetadataParser", "getImageFromPIC", "PIC type: " + string);
        }
        if (string.equalsIgnoreCase("PNG") || string.equalsIgnoreCase("JPG")) {
            return Arrays.copyOfRange(arrby, n + 1, arrby.length);
        }
        if (Logger.canLog(3)) {
            Logger.logMsg(3, "ID3MetadataParser", "getImageFromPIC", "Unsupported picture type found \"" + string + "\"");
        }
        return null;
    }

    private byte[] getImageFromAPIC(byte[] arrby) {
        int n;
        int n2;
        int n3;
        boolean bl = false;
        boolean bl2 = false;
        int n4 = arrby.length - 10;
        int n5 = 0;
        for (n3 = 0; n3 < n4; ++n3) {
            if (arrby[n3] != 105 || arrby[n3 + 1] != 109 || arrby[n3 + 2] != 97 || arrby[n3 + 3] != 103 || arrby[n3 + 4] != 101 || arrby[n3 + 5] != 47) continue;
            if (arrby[n3 += 6] == 106 && arrby[n3 + 1] == 112 && arrby[n3 + 2] == 101 && arrby[n3 + 3] == 103) {
                bl = true;
                n5 = n3 + 4;
                break;
            }
            if (arrby[n3] != 112 || arrby[n3 + 1] != 110 || arrby[n3 + 2] != 103) continue;
            bl2 = true;
            n5 = n3 + 3;
            break;
        }
        if (bl) {
            n3 = 0;
            n2 = arrby.length - 1;
            for (n = n5; n < n2; ++n) {
                if (-1 != arrby[n] || -40 != arrby[n + 1]) continue;
                n3 = 1;
                n5 = n;
                break;
            }
            if (n3 != 0) {
                return Arrays.copyOfRange(arrby, n5, arrby.length);
            }
        }
        if (bl2) {
            n3 = 0;
            n2 = arrby.length - 7;
            for (n = n5; n < n2; ++n) {
                if (-119 != arrby[n] || 80 != arrby[n + 1] || 78 != arrby[n + 2] || 71 != arrby[n + 3] || 13 != arrby[n + 4] || 10 != arrby[n + 5] || 26 != arrby[n + 6] || 10 != arrby[n + 7]) continue;
                n3 = 1;
                n5 = n;
                break;
            }
            if (n3 != 0) {
                return Arrays.copyOfRange(arrby, n5, arrby.length);
            }
        }
        return null;
    }

    private byte[] unsynchronizeBuffer(byte[] arrby) {
        byte[] arrby2 = new byte[arrby.length];
        int n = 0;
        for (int i = 0; i < arrby.length; ++i) {
            if ((arrby[i] & 0xFF) == 255 && arrby[i + 1] == 0 && arrby[i + 2] == 0 || (arrby[i] & 0xFF) == 255 && arrby[i + 1] == 0 && (arrby[i + 2] & 0xE0) == 224) {
                arrby2[n] = arrby[i];
                arrby2[++n] = arrby[i + 2];
                ++n;
                i += 2;
                continue;
            }
            arrby2[n] = arrby[i];
            ++n;
        }
        return Arrays.copyOf(arrby2, n);
    }
}


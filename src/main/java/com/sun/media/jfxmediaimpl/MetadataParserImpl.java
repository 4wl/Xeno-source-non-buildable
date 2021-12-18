/*
 * Decompiled with CFR 0.150.
 */
package com.sun.media.jfxmediaimpl;

import com.sun.media.jfxmedia.MetadataParser;
import com.sun.media.jfxmedia.events.MetadataListener;
import com.sun.media.jfxmedia.locator.ConnectionHolder;
import com.sun.media.jfxmedia.locator.Locator;
import java.io.EOFException;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

public abstract class MetadataParserImpl
extends Thread
implements MetadataParser {
    private String[] FLV_VIDEO_CODEC_NAME = new String[]{"Unsupported", "JPEG Video (Unsupported)", "Sorenson H.263 Video", "Flash Screen Video", "On2 VP6 Video", "On2 VP6-Alpha Video", "Unsupported", "H.264 Video", "Unsupported", "Unsupported", "Unsupported", "Unsupported", "Unsupported", "Unsupported", "Unsupported", "Unsupported"};
    private final List<WeakReference<MetadataListener>> listeners = new ArrayList<WeakReference<MetadataListener>>();
    private Map<String, Object> metadata = new HashMap<String, Object>();
    private Locator locator = null;
    private ConnectionHolder connectionHolder = null;
    private ByteBuffer buffer = null;
    private Map<String, ByteBuffer> rawMetaMap = null;
    protected ByteBuffer rawMetaBlob = null;
    private boolean parsingRawMetadata = false;
    private int length = 0;
    private int index = 0;
    private int streamPosition = 0;

    public MetadataParserImpl(Locator locator) {
        this.locator = locator;
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Override
    public void addListener(MetadataListener metadataListener) {
        List<WeakReference<MetadataListener>> list = this.listeners;
        synchronized (list) {
            if (metadataListener != null) {
                this.listeners.add(new WeakReference<MetadataListener>(metadataListener));
            }
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Override
    public void removeListener(MetadataListener metadataListener) {
        List<WeakReference<MetadataListener>> list = this.listeners;
        synchronized (list) {
            if (metadataListener != null) {
                ListIterator<WeakReference<MetadataListener>> listIterator = this.listeners.listIterator();
                while (listIterator.hasNext()) {
                    MetadataListener metadataListener2 = (MetadataListener)listIterator.next().get();
                    if (metadataListener2 != null && metadataListener2 != metadataListener) continue;
                    listIterator.remove();
                }
            }
        }
    }

    @Override
    public void startParser() throws IOException {
        this.start();
    }

    @Override
    public void stopParser() {
        if (this.connectionHolder != null) {
            this.connectionHolder.closeConnection();
        }
    }

    @Override
    public void run() {
        try {
            this.connectionHolder = this.locator.createConnectionHolder();
            this.parse();
        }
        catch (IOException iOException) {
            // empty catch block
        }
    }

    protected abstract void parse();

    protected void addMetadataItem(String string, Object object) {
        this.metadata.put(string, object);
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    protected void done() {
        List<WeakReference<MetadataListener>> list = this.listeners;
        synchronized (list) {
            if (!this.metadata.isEmpty()) {
                ListIterator<WeakReference<MetadataListener>> listIterator = this.listeners.listIterator();
                while (listIterator.hasNext()) {
                    MetadataListener metadataListener = (MetadataListener)listIterator.next().get();
                    if (metadataListener != null) {
                        metadataListener.onMetadata(this.metadata);
                        continue;
                    }
                    listIterator.remove();
                }
            }
        }
    }

    protected int getStreamPosition() {
        if (this.parsingRawMetadata) {
            return this.rawMetaBlob.position();
        }
        return this.streamPosition;
    }

    protected void startRawMetadata(int n) {
        this.rawMetaBlob = ByteBuffer.allocate(n);
    }

    private void adjustRawMetadataSize(int n) {
        if (this.rawMetaBlob.remaining() < n) {
            int n2 = this.rawMetaBlob.position();
            int n3 = n2 + n;
            ByteBuffer byteBuffer = ByteBuffer.allocate(n3);
            this.rawMetaBlob.position(0);
            byteBuffer.put(this.rawMetaBlob.array(), 0, n2);
            this.rawMetaBlob = byteBuffer;
        }
    }

    protected void readRawMetadata(int n) throws IOException {
        byte[] arrby = this.getBytes(n);
        this.adjustRawMetadataSize(n);
        if (null != arrby) {
            this.rawMetaBlob.put(arrby);
        }
    }

    protected void stuffRawMetadata(byte[] arrby, int n, int n2) {
        if (null != this.rawMetaBlob) {
            this.adjustRawMetadataSize(n2);
            this.rawMetaBlob.put(arrby, n, n2);
        }
    }

    protected void disposeRawMetadata() {
        this.parsingRawMetadata = false;
        this.rawMetaBlob = null;
    }

    protected void setParseRawMetadata(boolean bl) {
        if (null == this.rawMetaBlob) {
            this.parsingRawMetadata = false;
            return;
        }
        if (bl) {
            this.rawMetaBlob.position(0);
        }
        this.parsingRawMetadata = bl;
    }

    protected void addRawMetadata(String string) {
        if (null == this.rawMetaBlob) {
            return;
        }
        if (null == this.rawMetaMap) {
            this.rawMetaMap = new HashMap<String, ByteBuffer>();
            this.metadata.put("raw metadata", Collections.unmodifiableMap(this.rawMetaMap));
        }
        this.rawMetaMap.put(string, this.rawMetaBlob.asReadOnlyBuffer());
    }

    protected void skipBytes(int n) throws IOException, EOFException {
        if (this.parsingRawMetadata) {
            this.rawMetaBlob.position(this.rawMetaBlob.position() + n);
            return;
        }
        for (int i = 0; i < n; ++i) {
            this.getNextByte();
        }
    }

    protected byte getNextByte() throws IOException, EOFException {
        if (this.parsingRawMetadata) {
            return this.rawMetaBlob.get();
        }
        if (this.buffer == null) {
            this.buffer = this.connectionHolder.getBuffer();
            this.length = this.connectionHolder.readNextBlock();
        }
        if (this.index >= this.length) {
            this.length = this.connectionHolder.readNextBlock();
            if (this.length < 1) {
                throw new EOFException();
            }
            this.index = 0;
        }
        byte by = this.buffer.get(this.index);
        ++this.index;
        ++this.streamPosition;
        return by;
    }

    protected byte[] getBytes(int n) throws IOException, EOFException {
        byte[] arrby = new byte[n];
        if (this.parsingRawMetadata) {
            this.rawMetaBlob.get(arrby);
            return arrby;
        }
        for (int i = 0; i < n; ++i) {
            arrby[i] = this.getNextByte();
        }
        return arrby;
    }

    protected long getLong() throws IOException, EOFException {
        if (this.parsingRawMetadata) {
            return this.rawMetaBlob.getLong();
        }
        long l = 0L;
        l |= (long)(this.getNextByte() & 0xFF);
        l <<= 8;
        l |= (long)(this.getNextByte() & 0xFF);
        l <<= 8;
        l |= (long)(this.getNextByte() & 0xFF);
        l <<= 8;
        l |= (long)(this.getNextByte() & 0xFF);
        l <<= 8;
        l |= (long)(this.getNextByte() & 0xFF);
        l <<= 8;
        l |= (long)(this.getNextByte() & 0xFF);
        l <<= 8;
        l |= (long)(this.getNextByte() & 0xFF);
        l <<= 8;
        return l |= (long)(this.getNextByte() & 0xFF);
    }

    protected int getInteger() throws IOException, EOFException {
        if (this.parsingRawMetadata) {
            return this.rawMetaBlob.getInt();
        }
        int n = 0;
        n |= this.getNextByte() & 0xFF;
        n <<= 8;
        n |= this.getNextByte() & 0xFF;
        n <<= 8;
        n |= this.getNextByte() & 0xFF;
        n <<= 8;
        return n |= this.getNextByte() & 0xFF;
    }

    protected short getShort() throws IOException, EOFException {
        if (this.parsingRawMetadata) {
            return this.rawMetaBlob.getShort();
        }
        short s = 0;
        s = (short)(s | this.getNextByte() & 0xFF);
        s = (short)(s << 8);
        s = (short)(s | this.getNextByte() & 0xFF);
        return s;
    }

    protected double getDouble() throws IOException, EOFException {
        if (this.parsingRawMetadata) {
            return this.rawMetaBlob.getDouble();
        }
        long l = this.getLong();
        return Double.longBitsToDouble(l);
    }

    protected String getString(int n, Charset charset) throws IOException, EOFException {
        byte[] arrby = this.getBytes(n);
        return new String(arrby, 0, n, charset);
    }

    protected int getU24() throws IOException, EOFException {
        int n = 0;
        n |= this.getNextByte() & 0xFF;
        n <<= 8;
        n |= this.getNextByte() & 0xFF;
        n <<= 8;
        return n |= this.getNextByte() & 0xFF;
    }

    protected Object convertValue(String string, Object object) {
        if (string.equals("duration") && object instanceof Double) {
            Double d = (Double)object * 1000.0;
            return d.longValue();
        }
        if (string.equals("duration") && object instanceof String) {
            String string2 = (String)object;
            return Long.valueOf(string2.trim());
        }
        if (string.equals("width") || string.equals("height")) {
            Double d = (Double)object;
            return d.intValue();
        }
        if (string.equals("framerate")) {
            return object;
        }
        if (string.equals("videocodecid")) {
            int n = ((Double)object).intValue();
            if (n < this.FLV_VIDEO_CODEC_NAME.length) {
                return this.FLV_VIDEO_CODEC_NAME[n];
            }
            return null;
        }
        if (string.equals("audiocodecid")) {
            return "MPEG 1 Audio";
        }
        if (string.equals("creationdate")) {
            return ((String)object).trim();
        }
        if (string.equals("track number") || string.equals("disc number")) {
            String[] arrstring = ((String)object).split("/");
            if (arrstring.length == 2) {
                return Integer.valueOf(arrstring[0].trim());
            }
        } else if (string.equals("track count") || string.equals("disc count")) {
            String[] arrstring = ((String)object).split("/");
            if (arrstring.length == 2) {
                return Integer.valueOf(arrstring[1].trim());
            }
        } else {
            if (string.equals("album")) {
                return object;
            }
            if (string.equals("artist")) {
                return object;
            }
            if (string.equals("genre")) {
                return object;
            }
            if (string.equals("title")) {
                return object;
            }
            if (string.equals("album artist")) {
                return object;
            }
            if (string.equals("comment")) {
                return object;
            }
            if (string.equals("composer")) {
                return object;
            }
            if (string.equals("year")) {
                String string3 = (String)object;
                return Integer.valueOf(string3.trim());
            }
        }
        return null;
    }
}


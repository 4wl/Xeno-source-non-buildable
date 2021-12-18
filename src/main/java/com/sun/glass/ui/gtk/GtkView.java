/*
 * Decompiled with CFR 0.150.
 */
package com.sun.glass.ui.gtk;

import com.sun.glass.ui.Pixels;
import com.sun.glass.ui.View;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;

final class GtkView
extends View {
    private boolean imEnabled = false;
    private boolean isInPreeditMode = false;
    private final StringBuilder preedit = new StringBuilder();
    private ByteBuffer attributes;
    private int lastCaret;

    GtkView() {
    }

    private native void enableInputMethodEventsImpl(long var1, boolean var3);

    @Override
    protected void _enableInputMethodEvents(long l, boolean bl) {
        this.enableInputMethodEventsImpl(l, bl);
        if (this.imEnabled) {
            this.preedit.setLength(0);
        }
        this.imEnabled = bl;
    }

    @Override
    protected int _getNativeFrameBuffer(long l) {
        return 0;
    }

    @Override
    protected native long _create(Map var1);

    @Override
    protected native long _getNativeView(long var1);

    @Override
    protected native int _getX(long var1);

    @Override
    protected native int _getY(long var1);

    @Override
    protected native void _setParent(long var1, long var3);

    @Override
    protected native boolean _close(long var1);

    @Override
    protected native void _scheduleRepaint(long var1);

    @Override
    protected void _begin(long l) {
    }

    @Override
    protected void _end(long l) {
    }

    @Override
    protected void _uploadPixels(long l, Pixels pixels) {
        Buffer buffer = pixels.getPixels();
        if (buffer.isDirect()) {
            this._uploadPixelsDirect(l, buffer, pixels.getWidth(), pixels.getHeight());
        } else if (buffer.hasArray()) {
            if (pixels.getBytesPerComponent() == 1) {
                ByteBuffer byteBuffer = (ByteBuffer)buffer;
                this._uploadPixelsByteArray(l, byteBuffer.array(), byteBuffer.arrayOffset(), pixels.getWidth(), pixels.getHeight());
            } else {
                IntBuffer intBuffer = (IntBuffer)buffer;
                this._uploadPixelsIntArray(l, intBuffer.array(), intBuffer.arrayOffset(), pixels.getWidth(), pixels.getHeight());
            }
        } else {
            this._uploadPixelsDirect(l, pixels.asByteBuffer(), pixels.getWidth(), pixels.getHeight());
        }
    }

    private native void _uploadPixelsDirect(long var1, Buffer var3, int var4, int var5);

    private native void _uploadPixelsByteArray(long var1, byte[] var3, int var4, int var5, int var6);

    private native void _uploadPixelsIntArray(long var1, int[] var3, int var4, int var5, int var6);

    @Override
    protected native boolean _enterFullscreen(long var1, boolean var3, boolean var4, boolean var5);

    @Override
    protected native void _exitFullscreen(long var1, boolean var3);

    @Override
    protected void _finishInputMethodComposition(long l) {
        if (this.imEnabled && this.isInPreeditMode) {
            this.preedit.setLength(0);
            this.notifyInputMethod(this.preedit.toString(), null, null, null, 0, 0, 0);
        }
    }

    private void notifyPreeditMode(boolean bl) {
        this.isInPreeditMode = bl;
    }

    protected void notifyInputMethodDraw(String string, int n, int n2, int n3, byte[] arrby) {
        Object object;
        int[] arrn = null;
        byte[] arrby2 = null;
        if (this.attributes == null) {
            this.attributes = ByteBuffer.allocate(32);
        }
        if (n2 > 0) {
            this.preedit.replace(n, n + n2, "");
        }
        if (string != null) {
            this.preedit.insert(n, string);
        } else if (arrby == null) {
            this.preedit.setLength(0);
        }
        if (this.attributes.capacity() < this.preedit.length()) {
            object = ByteBuffer.allocate((int)((double)this.preedit.length() * 1.5));
            ((ByteBuffer)object).put(this.attributes);
            this.attributes = object;
        }
        this.attributes.limit(this.preedit.length());
        if (arrby != null && this.attributes.limit() >= n + arrby.length) {
            this.attributes.position(n);
            this.attributes.put(arrby);
        }
        if (this.attributes.limit() > 0) {
            object = new ArrayList();
            ArrayList<Byte> arrayList = new ArrayList<Byte>();
            this.attributes.rewind();
            byte by = this.attributes.get();
            ((ArrayList)object).add(0);
            arrayList.add(by);
            int n4 = 1;
            while (this.attributes.hasRemaining()) {
                byte by2 = this.attributes.get();
                if (by != by2) {
                    ((ArrayList)object).add(n4);
                    arrayList.add(by2);
                }
                by = by2;
                ++n4;
            }
            ((ArrayList)object).add(this.attributes.limit());
            arrn = new int[((ArrayList)object).size()];
            n4 = 0;
            Iterator iterator = ((ArrayList)object).iterator();
            while (iterator.hasNext()) {
                Integer number = (Integer)iterator.next();
                arrn[n4++] = number;
            }
            arrby2 = new byte[arrayList.size()];
            n4 = 0;
            for (Byte by2 : arrayList) {
                arrby2[n4++] = by2;
            }
        }
        this.notifyInputMethod(this.preedit.toString(), arrn, arrn, arrby2, 0, n3, 0);
        this.lastCaret = n3;
    }

    protected void notifyInputMethodCaret(int n, int n2, int n3) {
        switch (n2) {
            case 0: {
                this.lastCaret += n;
                break;
            }
            case 1: {
                this.lastCaret -= n;
                break;
            }
            case 10: {
                this.lastCaret = n;
                break;
            }
        }
        this.notifyInputMethod(this.preedit.toString(), null, null, null, 0, this.lastCaret, 0);
    }
}


/*
 * Decompiled with CFR 0.150.
 */
package com.sun.webkit.dom;

import com.sun.webkit.Disposer;
import com.sun.webkit.DisposerRecord;
import com.sun.webkit.dom.KeyboardEventImpl;
import com.sun.webkit.dom.MouseEventImpl;
import com.sun.webkit.dom.MutationEventImpl;
import com.sun.webkit.dom.NodeImpl;
import com.sun.webkit.dom.UIEventImpl;
import com.sun.webkit.dom.WheelEventImpl;
import org.w3c.dom.events.Event;
import org.w3c.dom.events.EventTarget;

public class EventImpl
implements Event {
    private final long peer;
    private static final int TYPE_WheelEvent = 1;
    private static final int TYPE_MouseEvent = 2;
    private static final int TYPE_KeyboardEvent = 3;
    private static final int TYPE_UIEvent = 4;
    private static final int TYPE_MutationEvent = 5;
    public static final int NONE = 0;
    public static final int CAPTURING_PHASE = 1;
    public static final int AT_TARGET = 2;
    public static final int BUBBLING_PHASE = 3;
    public static final int MOUSEDOWN = 1;
    public static final int MOUSEUP = 2;
    public static final int MOUSEOVER = 4;
    public static final int MOUSEOUT = 8;
    public static final int MOUSEMOVE = 16;
    public static final int MOUSEDRAG = 32;
    public static final int CLICK = 64;
    public static final int DBLCLICK = 128;
    public static final int KEYDOWN = 256;
    public static final int KEYUP = 512;
    public static final int KEYPRESS = 1024;
    public static final int DRAGDROP = 2048;
    public static final int FOCUS = 4096;
    public static final int BLUR = 8192;
    public static final int SELECT = 16384;
    public static final int CHANGE = 32768;

    EventImpl(long l) {
        this.peer = l;
        Disposer.addRecord(this, new SelfDisposer(l));
    }

    static Event create(long l) {
        if (l == 0L) {
            return null;
        }
        switch (EventImpl.getCPPTypeImpl(l)) {
            case 2: {
                return new MouseEventImpl(l);
            }
            case 3: {
                return new KeyboardEventImpl(l);
            }
            case 1: {
                return new WheelEventImpl(l);
            }
            case 4: {
                return new UIEventImpl(l);
            }
            case 5: {
                return new MutationEventImpl(l);
            }
        }
        return new EventImpl(l);
    }

    long getPeer() {
        return this.peer;
    }

    public boolean equals(Object object) {
        return object instanceof EventImpl && this.peer == ((EventImpl)object).peer;
    }

    public int hashCode() {
        long l = this.peer;
        return (int)(l ^ l >> 17);
    }

    static long getPeer(Event event) {
        return event == null ? 0L : ((EventImpl)event).getPeer();
    }

    private static native void dispose(long var0);

    private static native int getCPPTypeImpl(long var0);

    static Event getImpl(long l) {
        return EventImpl.create(l);
    }

    @Override
    public String getType() {
        return EventImpl.getTypeImpl(this.getPeer());
    }

    static native String getTypeImpl(long var0);

    @Override
    public EventTarget getTarget() {
        return (EventTarget)((Object)NodeImpl.getImpl(EventImpl.getTargetImpl(this.getPeer())));
    }

    static native long getTargetImpl(long var0);

    @Override
    public EventTarget getCurrentTarget() {
        return (EventTarget)((Object)NodeImpl.getImpl(EventImpl.getCurrentTargetImpl(this.getPeer())));
    }

    static native long getCurrentTargetImpl(long var0);

    @Override
    public short getEventPhase() {
        return EventImpl.getEventPhaseImpl(this.getPeer());
    }

    static native short getEventPhaseImpl(long var0);

    @Override
    public boolean getBubbles() {
        return EventImpl.getBubblesImpl(this.getPeer());
    }

    static native boolean getBubblesImpl(long var0);

    @Override
    public boolean getCancelable() {
        return EventImpl.getCancelableImpl(this.getPeer());
    }

    static native boolean getCancelableImpl(long var0);

    @Override
    public long getTimeStamp() {
        return EventImpl.getTimeStampImpl(this.getPeer());
    }

    static native long getTimeStampImpl(long var0);

    public boolean getDefaultPrevented() {
        return EventImpl.getDefaultPreventedImpl(this.getPeer());
    }

    static native boolean getDefaultPreventedImpl(long var0);

    public EventTarget getSrcElement() {
        return (EventTarget)((Object)NodeImpl.getImpl(EventImpl.getSrcElementImpl(this.getPeer())));
    }

    static native long getSrcElementImpl(long var0);

    public boolean getReturnValue() {
        return EventImpl.getReturnValueImpl(this.getPeer());
    }

    static native boolean getReturnValueImpl(long var0);

    public void setReturnValue(boolean bl) {
        EventImpl.setReturnValueImpl(this.getPeer(), bl);
    }

    static native void setReturnValueImpl(long var0, boolean var2);

    public boolean getCancelBubble() {
        return EventImpl.getCancelBubbleImpl(this.getPeer());
    }

    static native boolean getCancelBubbleImpl(long var0);

    public void setCancelBubble(boolean bl) {
        EventImpl.setCancelBubbleImpl(this.getPeer(), bl);
    }

    static native void setCancelBubbleImpl(long var0, boolean var2);

    @Override
    public void stopPropagation() {
        EventImpl.stopPropagationImpl(this.getPeer());
    }

    static native void stopPropagationImpl(long var0);

    @Override
    public void preventDefault() {
        EventImpl.preventDefaultImpl(this.getPeer());
    }

    static native void preventDefaultImpl(long var0);

    @Override
    public void initEvent(String string, boolean bl, boolean bl2) {
        EventImpl.initEventImpl(this.getPeer(), string, bl, bl2);
    }

    static native void initEventImpl(long var0, String var2, boolean var3, boolean var4);

    public void stopImmediatePropagation() {
        EventImpl.stopImmediatePropagationImpl(this.getPeer());
    }

    static native void stopImmediatePropagationImpl(long var0);

    private static class SelfDisposer
    implements DisposerRecord {
        private final long peer;

        SelfDisposer(long l) {
            this.peer = l;
        }

        @Override
        public void dispose() {
            EventImpl.dispose(this.peer);
        }
    }
}


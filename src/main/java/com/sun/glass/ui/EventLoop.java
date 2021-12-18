/*
 * Decompiled with CFR 0.150.
 */
package com.sun.glass.ui;

import com.sun.glass.ui.Application;
import java.util.ArrayDeque;
import java.util.Deque;

public final class EventLoop {
    private static final Deque<EventLoop> stack = new ArrayDeque<EventLoop>();
    private State state = State.IDLE;
    private Object returnValue;

    EventLoop() {
        Application.checkEventThread();
    }

    public State getState() {
        Application.checkEventThread();
        return this.state;
    }

    public Object enter() {
        Application.checkEventThread();
        if (!this.state.equals((Object)State.IDLE)) {
            throw new IllegalStateException("The event loop object isn't idle");
        }
        this.state = State.ACTIVE;
        stack.push(this);
        try {
            Object object = Application.enterNestedEventLoop();
            assert (object == this) : "Internal inconsistency - wrong EventLoop";
            assert (stack.peek() == this) : "Internal inconsistency - corrupted event loops stack";
            assert (this.state.equals((Object)State.LEAVING)) : "The event loop isn't leaving";
            Object object2 = this.returnValue;
            return object2;
        }
        finally {
            this.returnValue = null;
            this.state = State.IDLE;
            stack.pop();
            if (!stack.isEmpty() && EventLoop.stack.peek().state.equals((Object)State.LEAVING)) {
                Application.invokeLater(() -> {
                    EventLoop eventLoop = stack.peek();
                    if (eventLoop != null && eventLoop.state.equals((Object)State.LEAVING)) {
                        Application.leaveNestedEventLoop(eventLoop);
                    }
                });
            }
        }
    }

    public void leave(Object object) {
        Application.checkEventThread();
        if (!this.state.equals((Object)State.ACTIVE)) {
            throw new IllegalStateException("The event loop object isn't active");
        }
        this.state = State.LEAVING;
        this.returnValue = object;
        if (stack.peek() == this) {
            Application.leaveNestedEventLoop(this);
        }
    }

    public static enum State {
        IDLE,
        ACTIVE,
        LEAVING;

    }
}


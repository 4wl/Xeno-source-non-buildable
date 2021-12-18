/*
 * Decompiled with CFR 0.150.
 */
package com.sun.webkit.network;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

abstract class FormDataElement {
    private InputStream inputStream;

    FormDataElement() {
    }

    void open() throws IOException {
        this.inputStream = this.createInputStream();
    }

    long getSize() {
        if (this.inputStream == null) {
            throw new IllegalStateException();
        }
        return this.doGetSize();
    }

    InputStream getInputStream() {
        if (this.inputStream == null) {
            throw new IllegalStateException();
        }
        return this.inputStream;
    }

    void close() throws IOException {
        if (this.inputStream != null) {
            this.inputStream.close();
            this.inputStream = null;
        }
    }

    protected abstract InputStream createInputStream() throws IOException;

    protected abstract long doGetSize();

    private static FormDataElement fwkCreateFromByteArray(byte[] arrby) {
        return new ByteArrayElement(arrby);
    }

    private static FormDataElement fwkCreateFromFile(String string) {
        return new FileElement(string);
    }

    private static final class FileElement
    extends FormDataElement {
        private final File file;

        private FileElement(String string) {
            this.file = new File(string);
        }

        @Override
        protected InputStream createInputStream() throws IOException {
            return new BufferedInputStream(new FileInputStream(this.file));
        }

        @Override
        protected long doGetSize() {
            return this.file.length();
        }
    }

    private static final class ByteArrayElement
    extends FormDataElement {
        private final byte[] byteArray;

        private ByteArrayElement(byte[] arrby) {
            this.byteArray = arrby;
        }

        @Override
        protected InputStream createInputStream() {
            return new ByteArrayInputStream(this.byteArray);
        }

        @Override
        protected long doGetSize() {
            return this.byteArray.length;
        }
    }
}


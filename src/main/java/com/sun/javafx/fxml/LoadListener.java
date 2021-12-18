/*
 * Decompiled with CFR 0.150.
 */
package com.sun.javafx.fxml;

public interface LoadListener {
    public void readImportProcessingInstruction(String var1);

    public void readLanguageProcessingInstruction(String var1);

    public void readComment(String var1);

    public void beginInstanceDeclarationElement(Class<?> var1);

    public void beginUnknownTypeElement(String var1);

    public void beginIncludeElement();

    public void beginReferenceElement();

    public void beginCopyElement();

    public void beginRootElement();

    public void beginPropertyElement(String var1, Class<?> var2);

    public void beginUnknownStaticPropertyElement(String var1);

    public void beginScriptElement();

    public void beginDefineElement();

    public void readInternalAttribute(String var1, String var2);

    public void readPropertyAttribute(String var1, Class<?> var2, String var3);

    public void readUnknownStaticPropertyAttribute(String var1, String var2);

    public void readEventHandlerAttribute(String var1, String var2);

    public void endElement(Object var1);
}


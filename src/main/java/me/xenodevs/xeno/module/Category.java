/*
 * Decompiled with CFR 0.150.
 */
package me.xenodevs.xeno.module;

public enum Category {
    COMBAT("Combat"),
    MOVEMENT("Movement"),
    RENDER("Render"),
    PLAYER("Player"),
    CLIENT("Client"),
    MISC("Misc"),
    HUD("HUD");

    public String name;

    private Category(String string2) {
        this.name = string2;
    }
}


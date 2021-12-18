/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.minecraft.util.math.BlockPos
 */
package me.xenodevs.xeno.utils.block.hole;

import net.minecraft.util.math.BlockPos;

public class Hole {
    public Type type;
    public Facing facing;
    public BlockPos hole;
    public BlockPos offset;

    public Hole(Type type2, Facing facing, BlockPos hole, BlockPos offset) {
        this.type = type2;
        this.facing = facing;
        this.hole = hole;
        this.offset = offset;
    }

    public Hole(Type type2, Facing facing, BlockPos hole) {
        this.type = type2;
        this.facing = facing;
        this.hole = hole;
    }

    public Facing opposite() {
        if (this.facing == Facing.West) {
            return Facing.East;
        }
        if (this.facing == Facing.East) {
            return Facing.West;
        }
        if (this.facing == Facing.North) {
            return Facing.South;
        }
        if (this.facing == Facing.South) {
            return Facing.North;
        }
        return Facing.None;
    }

    public static enum Type {
        Obsidian,
        Bedrock,
        Double;

    }

    public static enum Facing {
        West,
        South,
        North,
        East,
        None;

    }
}


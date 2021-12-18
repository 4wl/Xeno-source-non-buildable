/*
 * Decompiled with CFR 0.150.
 */
package com.sun.openpisces;

import java.util.Arrays;

final class Helpers {
    private Helpers() {
        throw new Error("This is a non instantiable class");
    }

    static boolean within(float f, float f2, float f3) {
        float f4 = f2 - f;
        return f4 <= f3 && f4 >= -f3;
    }

    static boolean within(double d, double d2, double d3) {
        double d4 = d2 - d;
        return d4 <= d3 && d4 >= -d3;
    }

    static int quadraticRoots(float f, float f2, float f3, float[] arrf, int n) {
        int n2 = n;
        if (f != 0.0f) {
            float f4 = f2 * f2 - 4.0f * f * f3;
            if (f4 > 0.0f) {
                float f5 = (float)Math.sqrt(f4);
                if (f2 >= 0.0f) {
                    arrf[n2++] = 2.0f * f3 / (-f2 - f5);
                    arrf[n2++] = (-f2 - f5) / (2.0f * f);
                } else {
                    arrf[n2++] = (-f2 + f5) / (2.0f * f);
                    arrf[n2++] = 2.0f * f3 / (-f2 + f5);
                }
            } else if (f4 == 0.0f) {
                float f6 = -f2 / (2.0f * f);
                arrf[n2++] = f6;
            }
        } else if (f2 != 0.0f) {
            float f7 = -f3 / f2;
            arrf[n2++] = f7;
        }
        return n2 - n;
    }

    static int cubicRootsInAB(float f, float f2, float f3, float f4, float[] arrf, int n, float f5, float f6) {
        int n2;
        double d;
        double d2;
        double d3;
        double d4;
        double d5;
        double d6;
        double d7;
        if (f == 0.0f) {
            int n3 = Helpers.quadraticRoots(f2, f3, f4, arrf, n);
            return Helpers.filterOutNotInAB(arrf, n, n3, f5, f6) - n;
        }
        if ((d7 = (d6 = 0.5 * (0.07407407407407407 * (double)(f2 /= f) * (d5 = (double)(f2 * f2)) - 0.3333333333333333 * (double)f2 * (double)(f3 /= f) + (double)(f4 /= f))) * d6 + (d4 = (d3 = 0.3333333333333333 * (-0.3333333333333333 * d5 + (double)f3)) * d3 * d3)) < 0.0) {
            d2 = 0.3333333333333333 * Math.acos(-d6 / Math.sqrt(-d4));
            d = 2.0 * Math.sqrt(-d3);
            arrf[n + 0] = (float)(d * Math.cos(d2));
            arrf[n + 1] = (float)(-d * Math.cos(d2 + 1.0471975511965976));
            arrf[n + 2] = (float)(-d * Math.cos(d2 - 1.0471975511965976));
            n2 = 3;
        } else {
            d2 = Math.sqrt(d7);
            d = Math.cbrt(d2 - d6);
            double d8 = -Math.cbrt(d2 + d6);
            arrf[n] = (float)(d + d8);
            n2 = 1;
            if (Helpers.within(d7, 0.0, 1.0E-8)) {
                arrf[n + 1] = -(arrf[n] / 2.0f);
                n2 = 2;
            }
        }
        float f7 = 0.33333334f * f2;
        for (int i = 0; i < n2; ++i) {
            int n4 = n + i;
            arrf[n4] = arrf[n4] - f7;
        }
        return Helpers.filterOutNotInAB(arrf, n, n2, f5, f6) - n;
    }

    static float[] widenArray(float[] arrf, int n, int n2) {
        if (arrf.length >= n + n2) {
            return arrf;
        }
        return Arrays.copyOf(arrf, 2 * (n + n2));
    }

    static int[] widenArray(int[] arrn, int n, int n2) {
        if (arrn.length >= n + n2) {
            return arrn;
        }
        return Arrays.copyOf(arrn, 2 * (n + n2));
    }

    static float evalCubic(float f, float f2, float f3, float f4, float f5) {
        return f5 * (f5 * (f5 * f + f2) + f3) + f4;
    }

    static float evalQuad(float f, float f2, float f3, float f4) {
        return f4 * (f4 * f + f2) + f3;
    }

    static int filterOutNotInAB(float[] arrf, int n, int n2, float f, float f2) {
        int n3 = n;
        for (int i = n; i < n + n2; ++i) {
            if (!(arrf[i] >= f) || !(arrf[i] < f2)) continue;
            arrf[n3++] = arrf[i];
        }
        return n3;
    }

    static float polyLineLength(float[] arrf, int n, int n2) {
        assert (n2 % 2 == 0 && arrf.length >= n + n2) : "";
        float f = 0.0f;
        for (int i = n + 2; i < n + n2; i += 2) {
            f += Helpers.linelen(arrf[i], arrf[i + 1], arrf[i - 2], arrf[i - 1]);
        }
        return f;
    }

    static float linelen(float f, float f2, float f3, float f4) {
        float f5 = f3 - f;
        float f6 = f4 - f2;
        return (float)Math.sqrt(f5 * f5 + f6 * f6);
    }

    static void subdivide(float[] arrf, int n, float[] arrf2, int n2, float[] arrf3, int n3, int n4) {
        switch (n4) {
            case 6: {
                Helpers.subdivideQuad(arrf, n, arrf2, n2, arrf3, n3);
                break;
            }
            case 8: {
                Helpers.subdivideCubic(arrf, n, arrf2, n2, arrf3, n3);
                break;
            }
            default: {
                throw new InternalError("Unsupported curve type");
            }
        }
    }

    static void isort(float[] arrf, int n, int n2) {
        for (int i = n + 1; i < n + n2; ++i) {
            float f = arrf[i];
            for (int j = i - 1; j >= n && arrf[j] > f; --j) {
                arrf[j + 1] = arrf[j];
            }
            arrf[j + 1] = f;
        }
    }

    static void subdivideCubic(float[] arrf, int n, float[] arrf2, int n2, float[] arrf3, int n3) {
        float f = arrf[n + 0];
        float f2 = arrf[n + 1];
        float f3 = arrf[n + 2];
        float f4 = arrf[n + 3];
        float f5 = arrf[n + 4];
        float f6 = arrf[n + 5];
        float f7 = arrf[n + 6];
        float f8 = arrf[n + 7];
        if (arrf2 != null) {
            arrf2[n2 + 0] = f;
            arrf2[n2 + 1] = f2;
        }
        if (arrf3 != null) {
            arrf3[n3 + 6] = f7;
            arrf3[n3 + 7] = f8;
        }
        f = (f + f3) / 2.0f;
        f2 = (f2 + f4) / 2.0f;
        f7 = (f7 + f5) / 2.0f;
        f8 = (f8 + f6) / 2.0f;
        float f9 = (f3 + f5) / 2.0f;
        float f10 = (f4 + f6) / 2.0f;
        f3 = (f + f9) / 2.0f;
        f4 = (f2 + f10) / 2.0f;
        f5 = (f7 + f9) / 2.0f;
        f6 = (f8 + f10) / 2.0f;
        f9 = (f3 + f5) / 2.0f;
        f10 = (f4 + f6) / 2.0f;
        if (arrf2 != null) {
            arrf2[n2 + 2] = f;
            arrf2[n2 + 3] = f2;
            arrf2[n2 + 4] = f3;
            arrf2[n2 + 5] = f4;
            arrf2[n2 + 6] = f9;
            arrf2[n2 + 7] = f10;
        }
        if (arrf3 != null) {
            arrf3[n3 + 0] = f9;
            arrf3[n3 + 1] = f10;
            arrf3[n3 + 2] = f5;
            arrf3[n3 + 3] = f6;
            arrf3[n3 + 4] = f7;
            arrf3[n3 + 5] = f8;
        }
    }

    static void subdivideCubicAt(float f, float[] arrf, int n, float[] arrf2, int n2, float[] arrf3, int n3) {
        float f2 = arrf[n + 0];
        float f3 = arrf[n + 1];
        float f4 = arrf[n + 2];
        float f5 = arrf[n + 3];
        float f6 = arrf[n + 4];
        float f7 = arrf[n + 5];
        float f8 = arrf[n + 6];
        float f9 = arrf[n + 7];
        if (arrf2 != null) {
            arrf2[n2 + 0] = f2;
            arrf2[n2 + 1] = f3;
        }
        if (arrf3 != null) {
            arrf3[n3 + 6] = f8;
            arrf3[n3 + 7] = f9;
        }
        f2 += f * (f4 - f2);
        f3 += f * (f5 - f3);
        f8 = f6 + f * (f8 - f6);
        f9 = f7 + f * (f9 - f7);
        float f10 = f4 + f * (f6 - f4);
        float f11 = f5 + f * (f7 - f5);
        f4 = f2 + f * (f10 - f2);
        f5 = f3 + f * (f11 - f3);
        f6 = f10 + f * (f8 - f10);
        f7 = f11 + f * (f9 - f11);
        f10 = f4 + f * (f6 - f4);
        f11 = f5 + f * (f7 - f5);
        if (arrf2 != null) {
            arrf2[n2 + 2] = f2;
            arrf2[n2 + 3] = f3;
            arrf2[n2 + 4] = f4;
            arrf2[n2 + 5] = f5;
            arrf2[n2 + 6] = f10;
            arrf2[n2 + 7] = f11;
        }
        if (arrf3 != null) {
            arrf3[n3 + 0] = f10;
            arrf3[n3 + 1] = f11;
            arrf3[n3 + 2] = f6;
            arrf3[n3 + 3] = f7;
            arrf3[n3 + 4] = f8;
            arrf3[n3 + 5] = f9;
        }
    }

    static void subdivideQuad(float[] arrf, int n, float[] arrf2, int n2, float[] arrf3, int n3) {
        float f = arrf[n + 0];
        float f2 = arrf[n + 1];
        float f3 = arrf[n + 2];
        float f4 = arrf[n + 3];
        float f5 = arrf[n + 4];
        float f6 = arrf[n + 5];
        if (arrf2 != null) {
            arrf2[n2 + 0] = f;
            arrf2[n2 + 1] = f2;
        }
        if (arrf3 != null) {
            arrf3[n3 + 4] = f5;
            arrf3[n3 + 5] = f6;
        }
        f = (f + f3) / 2.0f;
        f2 = (f2 + f4) / 2.0f;
        f5 = (f5 + f3) / 2.0f;
        f6 = (f6 + f4) / 2.0f;
        f3 = (f + f5) / 2.0f;
        f4 = (f2 + f6) / 2.0f;
        if (arrf2 != null) {
            arrf2[n2 + 2] = f;
            arrf2[n2 + 3] = f2;
            arrf2[n2 + 4] = f3;
            arrf2[n2 + 5] = f4;
        }
        if (arrf3 != null) {
            arrf3[n3 + 0] = f3;
            arrf3[n3 + 1] = f4;
            arrf3[n3 + 2] = f5;
            arrf3[n3 + 3] = f6;
        }
    }

    static void subdivideQuadAt(float f, float[] arrf, int n, float[] arrf2, int n2, float[] arrf3, int n3) {
        float f2 = arrf[n + 0];
        float f3 = arrf[n + 1];
        float f4 = arrf[n + 2];
        float f5 = arrf[n + 3];
        float f6 = arrf[n + 4];
        float f7 = arrf[n + 5];
        if (arrf2 != null) {
            arrf2[n2 + 0] = f2;
            arrf2[n2 + 1] = f3;
        }
        if (arrf3 != null) {
            arrf3[n3 + 4] = f6;
            arrf3[n3 + 5] = f7;
        }
        f2 += f * (f4 - f2);
        f3 += f * (f5 - f3);
        f6 = f4 + f * (f6 - f4);
        f7 = f5 + f * (f7 - f5);
        f4 = f2 + f * (f6 - f2);
        f5 = f3 + f * (f7 - f3);
        if (arrf2 != null) {
            arrf2[n2 + 2] = f2;
            arrf2[n2 + 3] = f3;
            arrf2[n2 + 4] = f4;
            arrf2[n2 + 5] = f5;
        }
        if (arrf3 != null) {
            arrf3[n3 + 0] = f4;
            arrf3[n3 + 1] = f5;
            arrf3[n3 + 2] = f6;
            arrf3[n3 + 3] = f7;
        }
    }

    static void subdivideAt(float f, float[] arrf, int n, float[] arrf2, int n2, float[] arrf3, int n3, int n4) {
        switch (n4) {
            case 8: {
                Helpers.subdivideCubicAt(f, arrf, n, arrf2, n2, arrf3, n3);
                break;
            }
            case 6: {
                Helpers.subdivideQuadAt(f, arrf, n, arrf2, n2, arrf3, n3);
            }
        }
    }
}


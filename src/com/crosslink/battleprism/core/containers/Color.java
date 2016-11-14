package com.crosslink.battleprism.core.containers;

/**
 * Created with IntelliJ IDEA.
 * User: Joseph
 * Date: 1/30/14
 * Time: 8:16 PM
 * To change this template use File | Settings | File Templates.
 */
public class Color {
    public byte r, g, b, a;

    public Color(int r, int g, int b)
    {
        this.r = (byte) r;
        this.g = (byte) g;
        this.b = (byte) b;
        this.a = (byte) 0xFF; // Java doesn't have byte literals because it's an abortion.
    }

    public Color(int r, int g, int b, int a)
    {
        this.r = (byte) r;
        this.g = (byte) g;
        this.b = (byte) b;
        this.a = (byte) a;
    }

    @Override
    public String toString() {
        return String.format("{Red:%s Green:%s Blue:%s Alpha:%s}", (int)r, (int)g, (int)b, (int)a);
    }

    public static Color white = new Color(255, 255, 255, 255);
    public static Color black = new Color(0, 0, 0, 255);
    public static Color grey = new Color(128, 128, 128, 255);
    public static Color red = new Color(255, 0, 0, 255);
    public static Color green = new Color(0, 255, 0, 255);
    public static Color blue = new Color(0, 0, 255, 255);
    public static Color highlight = new Color(255, 255, 96, 255);
}

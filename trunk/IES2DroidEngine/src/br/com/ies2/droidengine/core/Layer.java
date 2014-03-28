package br.com.ies2.droidengine.core;

import android.graphics.Canvas;

public abstract class Layer {

    private float x;
    private float y;
    private String name;
    private boolean isVisible = true;

    public Layer(String name) {
        this.name = name;
        x = 0.0f;
        y = 0.0f;
    }

    public Layer(String name, float x, float y) {
        this(name);
        this.x = x;
        this.y = y;
    }

    public abstract void draw(Canvas canvas);

    public void setPosition(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    public float getX() {
        return this.x;
    }

    public float getY() {
        return this.y;
    }

    public final void setVisisble(boolean isVisible) {
        this.isVisible = isVisible;
    }

    public final boolean isVisible() {
        return this.isVisible;
    }

}

package br.com.ies2.droidengine.utils;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import br.com.ies2.droidengine.core.Layer;

public class Rectangle extends Layer {
    private float width;
    private float height;
    private Paint squarePaint;

    public Rectangle(String name, float x, float y, float width, float height) {
        super(name, x, y);
        this.width = width;
        this.height = height;
        squarePaint = new Paint();
        setColor(0, 0, 0);
    }

    public void setColor(int r, int g, int b) {
        int c = Color.rgb(r, g, b);
        squarePaint.setColor(c);
    }

    @Override
    public void draw(Canvas canvas) {
        canvas.drawRect(getX(), getY(), width + getX(), height + getY(), squarePaint);

    }

    public void move(float x, float y) {
        setPosition(getX() + x, getY() + y);
    }

    public void changePosition(float x, float y) {
        setPosition(x, y);
    }
}

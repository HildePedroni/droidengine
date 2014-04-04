package br.com.ies2.testedroidengine;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;

import com.larvalabs.svgandroid.SVG;
import com.larvalabs.svgandroid.SVGParser;

public class SvgView extends ImageView {

    public SvgView(Context context, AttributeSet attrs) {
        super(context, attrs);

        if (this.getDrawable() == null) {
            // Get defined attributes
            TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.SvgView, 0, 0);
            // Getting a file name
            CharSequence cs = a.getText(R.styleable.SvgView_android_src);
            String file = cs.toString();

            if (file.endsWith(".svg")) {
                int id = a.getResourceId(R.styleable.SvgView_android_src, -1);
                if (id != -1) {
                    try {
                        SVG svg = SVGParser.getSVGFromResource(getResources(), id);
                        Drawable drawable = svg.createPictureDrawable();
                        setLayerType(View.LAYER_TYPE_SOFTWARE, null);
                        setImageDrawable(drawable);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }

    }
}

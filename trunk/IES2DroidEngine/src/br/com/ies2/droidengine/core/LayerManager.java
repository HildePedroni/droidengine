package br.com.ies2.droidengine.core;

import java.util.ArrayList;
import java.util.List;

import android.graphics.Canvas;

public class LayerManager {

    private List<Layer> layers;

    protected LayerManager() {
        layers = new ArrayList<Layer>();
    }

    public void draw(Canvas canvas) {
        for (Layer layer : layers) {
            if (layer.isVisible()) {
                layer.draw(canvas);
            }
        }
    }

    public void layerUpdate(long gameTime) {
        for (Layer layer : layers) {
            layer.layerUpdate(gameTime);
        }
    }

    public void add(Layer layer) {
        layers.add(layer);
    }

    public void addAtIndex(Layer layer, int index) throws Exception {
        layers.add(index, layer);
    }

    /**
     * Return null if the index is not valid
     * 
     * @param index
     * @return
     */
    public Layer getLayerByIndex(int index) {
        if (index <= layers.size() || index < 0) {
            return layers.get(index);
        } else {
            return null;
        }
    }

    /**
     * Return null if there is no layer with name
     * 
     * @param name
     * @return Layer
     */
    public Layer getLayerByName(String name) {
        for (Layer layer : layers) {
            if (layer.getName().equals(name)) {
                return layer;
            }
        }
        return null;
    }
}

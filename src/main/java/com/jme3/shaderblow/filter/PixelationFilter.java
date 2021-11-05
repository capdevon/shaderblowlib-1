package com.jme3.shaderblow.filter;

import com.jme3.asset.AssetManager;
import com.jme3.material.Material;
import com.jme3.post.Filter;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;

/**
 * Pixelation post processing filter.
 *
 * @author Cord Rehn - jordansg57@gmail.com
 */
public class PixelationFilter extends Filter {

    private float pixelWidth = 20;
    private float pixelHeight = 20;
    private float screenWidth;
    private float screenHeight;

    public PixelationFilter() {
        super("PixelationFilter");
    }

    @Override
    protected void initFilter(AssetManager manager, RenderManager renderManager, ViewPort vp, int w, int h) {
        material = new Material(manager, "MatDefs/Filters/Pixelation/Pixelation.j3md");
        screenWidth = w;
        screenHeight = h;

        // set the material parameters to whatever the fields actually are
        // since this initFilter() method doesn't get called
        // till right before the first render, but by then every AppStates'
        // update() methods have run once and if we are changing these
        // parameters in that time, they have already changed but since the material was
        // null at that point the material's value wasn't set!... just trust me on this one...
        setPixelWidth(pixelWidth);
        setPixelHeight(pixelHeight);
    }

    @Override
    protected Material getMaterial() {
        return material;
    }

    public float getPixelWidth() {
        return pixelWidth;
    }

    public void setPixelWidth(float x) {
        pixelWidth = x;
        if (material != null) {
            material.setFloat("ResX", pixelWidth / screenWidth);
        }
    }

    public float getPixelHeight() {
        return pixelHeight;
    }

    public void setPixelHeight(float y) {
        pixelHeight = y;
        if (material != null) {
            material.setFloat("ResY", pixelHeight / screenHeight);
        }
    }

}

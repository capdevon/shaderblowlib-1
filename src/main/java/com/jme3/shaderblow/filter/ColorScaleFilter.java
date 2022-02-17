/*
 * Copyright (c) 2009-2012 ShaderBlow
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are
 * met:
 *
 * * Redistributions of source code must retain the above copyright
 *   notice, this list of conditions and the following disclaimer.
 *
 * * Redistributions in binary form must reproduce the above copyright
 *   notice, this list of conditions and the following disclaimer in the
 *   documentation and/or other materials provided with the distribution.
 *
 * * Neither the name of 'ShaderBlow' nor the names of its contributors
 *   may be used to endorse or promote products derived from this software
 *   without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED
 * TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package com.jme3.shaderblow.filter;

import java.io.IOException;

import com.jme3.asset.AssetManager;
import com.jme3.export.InputCapsule;
import com.jme3.export.JmeExporter;
import com.jme3.export.JmeImporter;
import com.jme3.export.OutputCapsule;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.post.Filter;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;

/**
 * The ColorScale filter apply a color to the render image. You can use this
 * filter to tint the render according to one particular color without change
 * any material (underwater scene, night scene, fire scene) or to achieve
 * fade-in/fade-out effect.
 *
 * <pre>
 * Features:
 * 	- Allow to set the color to apply. Default is red.
 * 	- Allow to set intensity of the color. Default is 0.7f. Frag shader clamps color intensity between 0 and 1.
 * </pre>
 *
 * @author H
 */
public class ColorScaleFilter extends Filter {

    /**
     * Default values
     */
    private ColorRGBA filterColor = ColorRGBA.Red.clone();
    private float colorDensity = 0.7f;
    private boolean overlay;
    private boolean multiply;

    /**
     * Create a ColorScaleFilter.
     */
    public ColorScaleFilter() {
        super("ColorScaleFilter");
    }

    /**
     * Create a ColorScaleFilter.
     *
     * @param filterColor Allow to set the color to apply. Default is red.
     * @param colorDensity Allow to set intensity of the color. Frag shader
     * clamps color intensity between 0 and 1.
     */
    public ColorScaleFilter(ColorRGBA filterColor, float colorDensity) {
        this();
        this.filterColor = filterColor;
        this.colorDensity = colorDensity;
    }

    @Override
    protected void initFilter(AssetManager manager, RenderManager renderManager, ViewPort vp, int w, int h) {
        material = new Material(manager, "MatDefs/Filters/ColorScale/ColorScale.j3md");
        material.setColor("FilterColor", filterColor);
        material.setFloat("ColorDensity", colorDensity);
        material.setBoolean("Overlay", overlay);
        material.setBoolean("Multiply", multiply);
    }

    @Override
    protected Material getMaterial() {
        return material;
    }

    public boolean isOverlay() {
        return overlay;
    }

    public void setOverlay(boolean overlay) {
        this.overlay = overlay;
        if (material != null) {
            material.setBoolean("Overlay", overlay);
        }
    }

    public boolean isMultiply() {
        return multiply;
    }

    public void setMultiply(boolean multiply) {
        this.multiply = multiply;
        if (material != null) {
            material.setBoolean("Multiply", multiply);
        }
    }

    public float getColorDensity() {
        return colorDensity;
    }

    public void setColorDensity(float colorDensity) {
        this.colorDensity = colorDensity;
        if (material != null) {
            material.setFloat("ColorDensity", colorDensity);
        }
    }

    public ColorRGBA getFilterColor() {
        return filterColor;
    }

    public void setFilterColor(ColorRGBA filterColor) {
        this.filterColor = filterColor;
        if (material != null) {
            material.setColor("FilterColor", filterColor);
        }
    }

    @Override
    public void write(JmeExporter ex) throws IOException {
        super.write(ex);
        OutputCapsule oc = ex.getCapsule(this);
        oc.write(filterColor, "FilterColor", ColorRGBA.Red);
        oc.write(colorDensity, "ColorDensity", 0.7f);
        oc.write(multiply, "Multiply", false);
        oc.write(overlay, "Overlay", false);
    }

    @Override
    public void read(JmeImporter im) throws IOException {
        super.read(im);
        InputCapsule ic = im.getCapsule(this);
        filterColor = (ColorRGBA) ic.readSavable("FilterColor", ColorRGBA.Red);
        colorDensity = ic.readFloat("ColorDensity", 0.7f);
        multiply = ic.readBoolean("Multiply", false);
        overlay = ic.readBoolean("Overlay", false);
    }

}

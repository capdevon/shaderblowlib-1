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

import java.util.Random;

import com.jme3.asset.AssetManager;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.post.Filter;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;

/**
 * Old Film filter simulate the effect of a classic looking film effect. It's a
 * port of http://devmaster.net/posts/2989/shader-effects-old-film.
 *
 * <pre>
 * Features:
 * 	- Allow to set the <strong>filter's color</strong>. Default is sepia
 * 	(ColorRGBA(112f / 255f, 66f / 255f, 20f / 255f, 1.0f)).
 * 	- Allow to set the <strong>color's density</strong>. Default is 0.7. Shader clamps this value between 0 to 1.
 * 	The color image gets grayscale when color's densite is set to 0.
 * 	- Allow to set the <strong>noise's density</strong>. Default is 0.4. Shader clamps this value between 0 to 1.
 * 	- Allow to set the <strong>scratches' density</strong>. Default is 0.3. Shader clamps this value between 0 to 1.
 * 	- Allow to set the <strong>vignetting's diameter</strong>. Default is 0.9. Shader clamps this value between 0 to
 * 	1.4. Vignetting effect is made using two circles. The inner circle represents the region untouched by vignetting.
 * 	The region between the inner and outer circle represent the area where vignetting starts to take place, which is
 * 	a gradual fade to black from the inner to outer ring. Any part of the frame outside of the outer ring would be
 * 	completely black.
 * </pre>
 *
 * <strong>NOTE</strong>: I chose to clamp this value inside the frag shader
 * code instead of using java code because I thought this way is faster (better
 * from preformace point of view). You can clamp this values using java code if
 * you want.
 *
 * @author H
 */
public class OldFilmFilter extends Filter {

    /**
     * Material parameter's name constants
     */
    private static final String M_PARAM_NAME_RANDOM_VALUE = "RandomValue";
    private static final String M_PARAM_NAME_OUTER_VIGNETTING = "OuterVignetting";
    private static final String M_PARAM_NAME_INNER_VIGNETTING = "InnerVignetting";
    private static final String M_PARAM_NAME_SCRATCH_DENSITY = "ScratchDensity";
    private static final String M_PARAM_NAME_NOISE_DENSITY = "NoiseDensity";
    private static final String M_PARAM_NAME_COLOR_DENSITY = "ColorDensity";
    private static final String M_PARAM_NAME_FILTER_COLOR = "FilterColor";

    /**
     * Default values
     */
    private static final ColorRGBA DEAFULT_COLOR = new ColorRGBA(112f / 255f, 66f / 255f, 20f / 255f, 1.0f);
    private static final float DEFAULT_COLOR_DENSITY = 0.7f;
    private static final float DEFAULT_NOISE_DENSITY = 0.4f;
    private static final float DEFAULT_SCRATCH_DENSITY = 0.3f;
    private static final float DEFAULT_VIGNETTING_VALUE = 0.9f;
    private static final float DEFAULT_VIGNETTING_FADE_REGION_WIDTH = 0.4f;

    private ColorRGBA filterColor = DEAFULT_COLOR;
    private float colorDensity = DEFAULT_COLOR_DENSITY;
    private float noiseDensity = DEFAULT_NOISE_DENSITY;
    private float scratchDensity = DEFAULT_SCRATCH_DENSITY;
    private float vignettingValue = DEFAULT_VIGNETTING_VALUE;
    private final Random rand = new Random();

    /**
     * Default Constructor.
     */
    public OldFilmFilter() {
        super("OldFilmFilter");
    }

    /**
     * Constructor.
     *
     * @param filterColor     Allow to set the <strong>filter's color</strong>.
     * @param colorDensity    Allow to set the <strong>color's density</strong>.
     *                        Shader clamps this value between 0 to 1. The color
     *                        image gets grayscale when color's density is set to 0.
     * @param noiseDensity    Allow to set the <strong>noise's density</strong>.
     *                        Shader clamps this value between 0 to 1.
     * @param scratchDensity  Allow to set the <strong>scratches' density</strong>.
     *                        Shader clamps this value between 0 to 1.
     * @param vignettingValue Allow to set the <strong>vignetting's
     *                        diameter</strong>. Shader clamps this value between 0
     *                        to 1.4. Vignetting effect is made using two circles.
     *                        The inner circle represents the region untouched by
     *                        vignetting. The region between the inner and outer
     *                        circle represent the area where vignetting starts to
     *                        take place, which is a gradual fade to black from the
     *                        inner to outer ring. Any part of the frame outside of
     *                        the outer ring would be completely black.
     */
    public OldFilmFilter(ColorRGBA filterColor, float colorDensity, float noiseDensity, float scratchDensity, float vignettingValue) {
        this();
        this.filterColor = filterColor;
        this.colorDensity = colorDensity;
        this.noiseDensity = noiseDensity;
        this.scratchDensity = scratchDensity;
        this.vignettingValue = vignettingValue;
    }

    @Override
    protected void initFilter(AssetManager manager, RenderManager renderManager, ViewPort vp, int w, int h) {
        material = new Material(manager, "MatDefs/Filters/OldFilm/OldFilm.j3md");
        material.setColor(M_PARAM_NAME_FILTER_COLOR, filterColor);
        material.setFloat(M_PARAM_NAME_COLOR_DENSITY, colorDensity);
        material.setFloat(M_PARAM_NAME_NOISE_DENSITY, noiseDensity);
        material.setFloat(M_PARAM_NAME_SCRATCH_DENSITY, scratchDensity);
        material.setFloat(M_PARAM_NAME_INNER_VIGNETTING, vignettingValue - DEFAULT_VIGNETTING_FADE_REGION_WIDTH);
        material.setFloat(M_PARAM_NAME_OUTER_VIGNETTING, vignettingValue);
        material.setFloat(M_PARAM_NAME_RANDOM_VALUE, rand.nextFloat());
    }

    @Override
    protected void preFrame(float tpf) {
        material.setFloat(M_PARAM_NAME_RANDOM_VALUE, rand.nextFloat());
    }

    @Override
    protected Material getMaterial() {
        return material;
    }

    public void setColorDensity(float colorDensity) {
        this.colorDensity = colorDensity;
        if (material != null) {
            material.setFloat(M_PARAM_NAME_COLOR_DENSITY, colorDensity);
        }
    }

    public float getColorDensity() {
        return colorDensity;
    }

    public void setFilterColor(ColorRGBA filterColor) {
        this.filterColor = filterColor;
        if (material != null) {
            material.setColor(M_PARAM_NAME_FILTER_COLOR, filterColor);
        }
    }

    public ColorRGBA getFilterColor() {
        return filterColor;
    }

    public void setNoiseDensity(float noiseDensity) {
        this.noiseDensity = noiseDensity;
        if (material != null) {
            material.setFloat(M_PARAM_NAME_NOISE_DENSITY, noiseDensity);
        }
    }

    public float getNoiseDensity() {
        return noiseDensity;
    }

    public void setScratchDensity(float scratchDensity) {
        this.scratchDensity = scratchDensity;
        if (material != null) {
            material.setFloat(M_PARAM_NAME_SCRATCH_DENSITY, scratchDensity);
        }
    }

    public float getScratchDensity() {
        return scratchDensity;
    }

    public void setVignettingValue(float vignettingValue) {
        this.vignettingValue = vignettingValue;
        if (material != null) {
            material.setFloat(M_PARAM_NAME_INNER_VIGNETTING, vignettingValue - DEFAULT_VIGNETTING_FADE_REGION_WIDTH);
            material.setFloat(M_PARAM_NAME_OUTER_VIGNETTING, vignettingValue);
        }
    }

    public float getVignettingValue() {
        return vignettingValue;
    }
}

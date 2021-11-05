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
package com.jme3.shaderblow.test;

import com.jme3.app.SimpleApplication;
import com.jme3.light.AmbientLight;
import com.jme3.light.DirectionalLight;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.post.FilterPostProcessor;
import com.jme3.scene.Geometry;
import com.jme3.scene.Spatial;
import com.jme3.scene.shape.Box;
import com.jme3.shaderblow.filter.BasicSSAO;
import com.jme3.system.AppSettings;
import com.jme3.util.TangentBinormalGenerator;

/**
 *
 * @author ShaderBlow
 */
public class TestBasicSSAO extends SimpleApplication {

    /**
     *
     * @param args
     */
    public static void main(String[] args) {
        TestBasicSSAO app = new TestBasicSSAO();
        AppSettings newSetting = new AppSettings(true);
        newSetting.setResolution(1024, 768);
        newSetting.setGammaCorrection(false);
        newSetting.setVSync(false);
        app.setSettings(newSetting);
        app.start();
    }

    @Override
    public void simpleInitApp() {

        flyCam.setMoveSpeed(15);
        viewPort.setBackgroundColor(ColorRGBA.Gray);

        final Geometry cube = new Geometry("Box", new Box(1, 1, 1));
        final Material mat = new Material(assetManager, "Common/MatDefs/Light/Lighting.j3md");
        cube.setMaterial(mat);
        cube.setLocalTranslation(0, 2, 1);
        rootNode.attachChild(cube);

        loadModel(new Vector3f(0, 0, 0));
        loadModel(new Vector3f(-2f, 0, 0));

        addLighting();
        setupFilters();
    }

    private Spatial loadModel(Vector3f position) {
        final Spatial model = assetManager.loadModel("Models/LightBlow/jme_lightblow.mesh.xml");
        final Material mat = new Material(assetManager, "Common/MatDefs/Light/Lighting.j3md");
        model.setMaterial(mat);
        model.setLocalTranslation(position);
        TangentBinormalGenerator.generate(model);
        rootNode.attachChild(model);
        return model;
    }

    private void addLighting() {
        DirectionalLight sun = new DirectionalLight();
        sun.setDirection(new Vector3f(-0.8f, -0.6f, -0.08f).normalizeLocal());
        sun.setColor(new ColorRGBA(1, 1, 1, 1));
        rootNode.addLight(sun);

        AmbientLight ambient = new AmbientLight();
        ambient.setColor(new ColorRGBA(1.5f, 1.5f, 1.5f, 1.0f));
        rootNode.addLight(ambient);
    }

    private void setupFilters() {
        // BasicSSAO ssao = new BasicSSAO();
        // ssao.scaleSettings(0.25f); // or whatever works for your model scale

        // In vars: reflection-radius, intensity, scale, bias
        final BasicSSAO ssao = new BasicSSAO(0.15f, 5.5f, 0.5f, 0.025f);
        // Add in detail pass - this doubles the number of samples taken and halves
        // performance. But, allows for
        // smoothing artifacting while keeping detail
        ssao.setUseDetailPass(true);
        // Add distance falloff and set distance/rate of falloff
        ssao.setUseDistanceFalloff(true);
        ssao.setFalloffStartDistance(50f);
        ssao.setFalloffRate(4.0f);

        FilterPostProcessor fpp = new FilterPostProcessor(assetManager);
        fpp.addFilter(ssao);
        viewPort.addProcessor(fpp);
    }

    @Override
    public void simpleUpdate(final float tpf) {
    }
}

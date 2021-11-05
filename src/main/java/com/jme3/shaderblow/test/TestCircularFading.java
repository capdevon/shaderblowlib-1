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
import com.jme3.input.KeyInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.material.Material;
import com.jme3.post.FilterPostProcessor;
import com.jme3.scene.Spatial;
import com.jme3.shaderblow.filter.CircularFadingFilter;
import com.jme3.shaderblow.test.SceneHelper;
import com.jme3.util.TangentBinormalGenerator;

/**
 *
 * @author shaderblow
 */
public class TestCircularFading extends SimpleApplication {

    private FilterPostProcessor fpp;
    private CircularFadingFilter circleFadingFilter;

    /**
     *
     * @param args
     */
    public static void main(final String[] args) {
        TestCircularFading app = new TestCircularFading();
        app.start();
    }

    @Override
    public void simpleInitApp() {

        // assetManager.registerLocator("assets", FileLocator.class);
        flyCam.setMoveSpeed(10);

        initInputs();

        SceneHelper.buildSkybox(assetManager, rootNode);
        SceneHelper.buildFloor(assetManager, rootNode);
        SceneHelper.buildLights(rootNode);

        final Spatial char_boy2 = assetManager.loadModel("Models/LightBlow/jme_lightblow.mesh.xml");
        final Material mat2 = assetManager.loadMaterial("Materials/MatCap/MatCapBump1.j3m");
        char_boy2.setMaterial(mat2);
        char_boy2.setLocalTranslation(2, 0, 0);
        TangentBinormalGenerator.generate(char_boy2);
        rootNode.attachChild(char_boy2);

        fpp = new FilterPostProcessor(assetManager);
        fpp.setNumSamples(4);

        circleFadingFilter = new CircularFadingFilter(getCamera(), char_boy2.getWorldTranslation());
        circleFadingFilter.setFadingSpeed(0.5f);
        fpp.addFilter(circleFadingFilter);

        viewPort.addProcessor(fpp);
    }

    private void initInputs() {
        inputManager.addMapping("toggle", new KeyTrigger(KeyInput.KEY_SPACE));
        inputManager.addListener(acl, "toggle");
    }

    final ActionListener acl = new ActionListener() {
        @Override
        public void onAction(final String name, final boolean keyPressed, final float tpf) {
            if (name.equals("toggle") && keyPressed) {
                boolean enabled = circleFadingFilter.isEnabled();
                circleFadingFilter.setEnabled(!enabled);
            }
        }
    };
}

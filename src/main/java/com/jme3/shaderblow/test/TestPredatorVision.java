/*
 * Copyright (c) 2009-2013 ShaderBlow
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

import com.jme3.animation.AnimChannel;
import com.jme3.animation.AnimControl;
import com.jme3.animation.SkeletonControl;
import com.jme3.app.SimpleApplication;
import com.jme3.asset.TextureKey;
import com.jme3.input.KeyInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.light.DirectionalLight;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.post.FilterPostProcessor;
import com.jme3.renderer.Caps;
import com.jme3.scene.Spatial;
import com.jme3.shaderblow.filter.PredatorVisionFilter;
import com.jme3.shaderblow.lightblow.CartoonEdgeProcessor;
import com.jme3.texture.Texture;
import com.jme3.util.SkyFactory;
import com.jme3.util.TangentBinormalGenerator;

/**
 * @author wezrule
 */
public class TestPredatorVision extends SimpleApplication implements ActionListener {

    private PredatorVisionFilter predatorFilter;

    /**
     *
     * @param args
     */
    public static void main(final String[] args) {
        TestPredatorVision app = new TestPredatorVision();
        app.start();
    }

    @Override
    public void simpleInitApp() {

        flyCam.setMoveSpeed(10);
        viewPort.setBackgroundColor(ColorRGBA.Gray);

        buildScene();
        createSky();
        addLighting();

        FilterPostProcessor fpp = new FilterPostProcessor(assetManager);
        predatorFilter = new PredatorVisionFilter();
        predatorFilter.setScreenWidth(0.5f);
        fpp.addFilter(predatorFilter);
        viewPort.addProcessor(fpp);

        inputManager.addMapping("toggle", new KeyTrigger(KeyInput.KEY_SPACE));
        inputManager.addListener(this, "toggle");
    }

    @Override
    public void onAction(String name, boolean isPressed, float tpf) {
        if (name.equals("toggle") && isPressed) {
            boolean enabled = predatorFilter.isEnabled();
            predatorFilter.setEnabled(!enabled);
        }
    }

    private void addLighting() {
        if (renderer.getCaps().contains(Caps.GLSL100)) {
            final CartoonEdgeProcessor cartoonEdgeProcess = new CartoonEdgeProcessor();
            viewPort.addProcessor(cartoonEdgeProcess);
        }

        // Required for toon edge effect
        final DirectionalLight sun = new DirectionalLight();
        sun.setDirection(new Vector3f(-0.8f, -0.6f, -0.08f).normalizeLocal());
        sun.setColor(new ColorRGBA(1, 1, 1, 1));
        rootNode.addLight(sun);
    }

    private void createSky() {
        TextureKey skyhi = new TextureKey("Textures/Water256.dds", true);
        skyhi.setGenerateMips(true);
        Texture texlow = assetManager.loadTexture(skyhi);
        rootNode.attachChild(SkyFactory.createSky(assetManager, texlow, SkyFactory.EnvMapType.CubeMap));
    }

    // Copied from the glass test case
    private void buildScene() {

        final Spatial char_boy1 = loadModel("Materials/Glass/Glass1.j3m", new Vector3f(0, 0, 0));
        playAnimation(char_boy1, "Action", true);

        final Spatial char_boy2 = loadModel("Materials/Glass/Glass1_bump.j3m", new Vector3f(1, 0, 0));
        playAnimation(char_boy2, "Action", true);

        final Spatial char_boy3 = loadModel("Materials/Glass/Glass2_low.j3m", new Vector3f(-1, 0, 0));
        playAnimation(char_boy3, null, true);

        final Spatial char_boy4 = loadModel("Materials/Glass/Glass3_color.j3m", new Vector3f(-2, 0, 0));
        playAnimation(char_boy4, null, true);

        final Spatial char_boy5 = loadModel("Materials/Glass/Glass4_specular.j3m", new Vector3f(-3, 0, 0));
        playAnimation(char_boy5, null, true);
    }

    private Spatial loadModel(String matDef, Vector3f position) {
        Spatial model = assetManager.loadModel("Models/LightBlow/jme_lightblow.mesh.xml");
        Material mat = assetManager.loadMaterial(matDef);
        model.setMaterial(mat);
        model.setLocalTranslation(position);
        TangentBinormalGenerator.generate(model);
        rootNode.attachChild(model);

        return model;
    }

    private void playAnimation(Spatial model, String name, boolean hardwareSkinning) {
        if (name != null) {
            AnimControl animControl = model.getControl(AnimControl.class);
            AnimChannel channel = animControl.createChannel();
            channel.setAnim(name);
        }

        SkeletonControl skControl = model.getControl(SkeletonControl.class);
        skControl.setHardwareSkinningPreferred(hardwareSkinning);
    }
}

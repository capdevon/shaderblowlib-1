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

import com.jme3.anim.AnimComposer;
import com.jme3.anim.SkinningControl;
import com.jme3.app.SimpleApplication;
import com.jme3.asset.TextureKey;
import com.jme3.input.KeyInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.light.DirectionalLight;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.post.FilterPostProcessor;
import com.jme3.renderer.Caps;
import com.jme3.scene.Spatial;
import com.jme3.shaderblow.filter.NightVisionFilter;
import com.jme3.shaderblow.lightblow.CartoonEdgeProcessor;
import com.jme3.texture.Texture;
import com.jme3.util.SkyFactory;
import com.jme3.util.TangentBinormalGenerator;

/**
 * @author wezrule
 */
public class TestNightVision extends SimpleApplication {

    private NightVisionFilter nightVisFilter;

    /**
     *
     * @param args
     */
    public static void main(String[] args) {
        TestNightVision app = new TestNightVision();
        app.start();
    }

    @Override
    public void simpleInitApp() {

        flyCam.setMoveSpeed(10);
        cam.setLocation(new Vector3f(-2.68017f, 2.3739557f, 9.298212f));
        cam.setRotation(new Quaternion(0.006584827f, 0.993395f, -0.077767275f, 0.084114805f));

        SceneHelper.buildFloor(assetManager, rootNode);
        buildScene();
        createSky();
        addLighting();
        initInputs();
    }

    private void initInputs() {
        inputManager.addMapping("toggle", new KeyTrigger(KeyInput.KEY_SPACE));
        inputManager.addMapping("random-color", new KeyTrigger(KeyInput.KEY_R));
        inputManager.addListener(acl, "toggle", "random-color");
    }

    final ActionListener acl = new ActionListener() {
        @Override
        public void onAction(String name, boolean isPressed, float tpf) {
            if (name.equals("toggle") && isPressed) {
                boolean enabled = nightVisFilter.isEnabled();
                nightVisFilter.setEnabled(!enabled);
                
            } else if (name.equals("random-color") && isPressed) {
                if (nightVisFilter.isEnabled()) {
                    nightVisFilter.setColor(ColorRGBA.randomColor());
                }
            }
        }
    };

    private void addLighting() {
        if (renderer.getCaps().contains(Caps.GLSL100)) {
            final CartoonEdgeProcessor cartoonEdgeProcess = new CartoonEdgeProcessor();
            viewPort.addProcessor(cartoonEdgeProcess);
        }

        nightVisFilter = new NightVisionFilter();
        nightVisFilter.setColor(new ColorRGBA(0.0f, 1.0f, 0.0f, 1.0f));
        nightVisFilter.setNoiseTexture(assetManager.loadTexture("Textures/NightVision/Noise.png"));
        nightVisFilter.setMaskTexture(assetManager.loadTexture("Textures/NightVision/BinocularsMask.png"));

        FilterPostProcessor fpp = new FilterPostProcessor(assetManager);
        fpp.addFilter(nightVisFilter);
        viewPort.addProcessor(fpp);

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
        playAnimation(char_boy3, "Action", true);

        final Spatial char_boy4 = loadModel("Materials/Glass/Glass3_color.j3m", new Vector3f(-2, 0, 0));
        playAnimation(char_boy4, "Action", true);

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
            AnimComposer composer = model.getControl(AnimComposer.class);
            composer.setCurrentAction(name);
        }

        SkinningControl skControl = model.getControl(SkinningControl.class);
        skControl.setHardwareSkinningPreferred(hardwareSkinning);
    }

}

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

import com.jme3.animation.AnimChannel;
import com.jme3.animation.AnimControl;
import com.jme3.animation.SkeletonControl;
import com.jme3.app.SimpleApplication;
import com.jme3.asset.TextureKey;
import com.jme3.light.DirectionalLight;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.renderer.Caps;
import com.jme3.scene.Spatial;
import com.jme3.shaderblow.lightblow.CartoonEdgeProcessor;
import com.jme3.texture.Texture;
import com.jme3.util.SkyFactory;
import com.jme3.util.TangentBinormalGenerator;

/**
 *
 * @author capdevon/shaderblow
 */
public class TestMatCap extends SimpleApplication {

    /**
     *
     * @param args
     */
    public static void main(final String[] args) {
        final TestMatCap app = new TestMatCap();
        app.start();
    }

    @Override
    public void simpleInitApp() {

        flyCam.setMoveSpeed(10);
        viewPort.setBackgroundColor(ColorRGBA.Gray);

        buildScene();
        createSky();
        addLighting();
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

    private void buildScene() {

        final Spatial char_boy1 = loadModel("Materials/MatCap/MatCap1.j3m", new Vector3f(0, 0, 0));
        playAnimation(char_boy1, "Action", true);

        final Spatial char_boy2 = loadModel("Materials/MatCap/MatCapBump1.j3m", new Vector3f(1, 0, 0));
        playAnimation(char_boy2, "Action", true);

        final Spatial char_boy3 = loadModel("Materials/MatCap/MatCap2.j3m", new Vector3f(-1, 0, 0));
        playAnimation(char_boy3, "Action", true);

        final Spatial char_boy4 = loadModel("Materials/MatCap/MatCapBump2.j3m", new Vector3f(-2, 0, 0));
        playAnimation(char_boy4, "Action", true);
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

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
import com.jme3.asset.TextureKey;
import com.jme3.material.Material;
import com.jme3.material.RenderState;
import com.jme3.renderer.queue.RenderQueue.Bucket;
import com.jme3.scene.Spatial;
import com.jme3.texture.Texture;
import com.jme3.util.SkyFactory;
import com.jme3.util.TangentBinormalGenerator;

/**
 *
 * @author ShaderBlow
 */
public class TestFakeParticleBlow extends SimpleApplication {

    /**
     *
     * @param args
     */
    public static void main(String[] args) {
        TestFakeParticleBlow app = new TestFakeParticleBlow();
        app.start();
    }

    @Override
    public void simpleInitApp() {

        // assetManager.registerLocator("assets", FileLocator.class);
        final Spatial fire = assetManager.loadModel("Models/FakeParticleBlow/FakeParticleBlow.j3o");
        fire.setMaterial(createParticleMaterial("Materials/FakeParticleBlow/FakeParticleBlow.j3m"));
        TangentBinormalGenerator.generate(fire);
        fire.setQueueBucket(Bucket.Transparent);
        rootNode.attachChild(fire);

        final Spatial fire2 = assetManager.loadModel("Models/FakeParticleBlow/FakeParticleBlow.j3o");
        fire2.setMaterial(createParticleMaterial("Materials/FakeParticleBlow/FakeParticleBlow_2.j3m"));
        TangentBinormalGenerator.generate(fire2);
        fire2.setQueueBucket(Bucket.Transparent);
        fire2.setLocalTranslation(2, 0, 0);
        rootNode.attachChild(fire2);

        flyCam.setMoveSpeed(5);

        createSky();
    }

    private Material createParticleMaterial(String matDef) {
        final Material mat = assetManager.loadMaterial(matDef);
        mat.getAdditionalRenderState().setFaceCullMode(RenderState.FaceCullMode.Off);
        mat.getAdditionalRenderState().setDepthTest(true);
        mat.getAdditionalRenderState().setDepthWrite(false);
        return mat;
    }

    private void createSky() {
        TextureKey skyhi = new TextureKey("Textures/Water256.dds", true);
        skyhi.setGenerateMips(true);
        Texture texlow = assetManager.loadTexture(skyhi);
        rootNode.attachChild(SkyFactory.createSky(assetManager, texlow, SkyFactory.EnvMapType.CubeMap));
    }

}

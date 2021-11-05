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
import com.jme3.material.RenderState.BlendMode;
import com.jme3.material.RenderState.FaceCullMode;
import com.jme3.math.ColorRGBA;
import com.jme3.renderer.queue.RenderQueue.Bucket;
import com.jme3.scene.Geometry;
import com.jme3.scene.shape.Quad;
import com.jme3.texture.Texture;
import com.jme3.texture.Texture.WrapMode;
import com.jme3.util.SkyFactory;
import com.jme3.util.TangentBinormalGenerator;

/**
 *
 * @author ShaderBlow
 */
public class TestFakeParticleBlow2 extends SimpleApplication {

    /**
     *
     * @param args
     */
    public static void main(final String[] args) {
        TestFakeParticleBlow2 app = new TestFakeParticleBlow2();
        app.start();
    }

    @Override
    public void simpleInitApp() {
        // assetManager.registerLocator("assets", FileLocator.class);

        flyCam.setMoveSpeed(10);

        createSky();

        // FakeParticleBlow
        final Quad quad = new Quad(3, 3); // Create an spatial. A plane in this case
        final Geometry geom = new Geometry("Particle", quad);
        Material mat = createFakeParticleBlow();
        geom.setMaterial(mat); // Assign the material to the spatial
        TangentBinormalGenerator.generate(geom);
        geom.setQueueBucket(Bucket.Transparent); // Remember to set the queue bucket to transparent for the spatial

        rootNode.attachChild(geom);
    }

    private Material createFakeParticleBlow() {
        // Create the material
        final Material mat = new Material(assetManager, "MatDefs/FakeParticleBlow/FakeParticleBlow.j3md");

        // Create the mask texture to use
        final Texture maskTex = assetManager.loadTexture("Textures/FakeParticleBlow/mask.png");
        mat.setTexture("MaskMap", maskTex);

        // Create the texture to use for the spatial.
        final Texture aniTex = assetManager.loadTexture("Textures/FakeParticleBlow/particles.png");
        aniTex.setWrap(WrapMode.MirroredRepeat); // NOTE: Set WrapMode = MirroredRepeat in order to animate the texture
        mat.setTexture("AniTexMap", aniTex); // Set texture

        mat.setFloat("TimeSpeed", 2); // Set animation speed
        mat.setColor("BaseColor", ColorRGBA.Green.clone()); // Set base color to apply to the texture
        // mat.setBoolean("Animation_X", true); // Enable X axis animation
        mat.setBoolean("Animation_Y", true); // Enable Y axis animation
        mat.setBoolean("Change_Direction", true); // Change direction of the texture animation

        mat.getAdditionalRenderState().setDepthTest(true);
        mat.getAdditionalRenderState().setDepthWrite(false);
        mat.getAdditionalRenderState().setFaceCullMode(FaceCullMode.Off); // Allow to see both sides of a face
        mat.getAdditionalRenderState().setBlendMode(BlendMode.Additive);

        final ColorRGBA fogColor = ColorRGBA.Black.clone();
        fogColor.a = 10; // fogColor's alpha value is used to calculate the intensity of the fog (distance to apply fog)
        mat.setColor("FogColor", fogColor); // Set fog color to apply to the spatial.

        // mat.setTexture("FogSkyBox", texlow);
        
        return mat;
    }

    private void createSky() {
        TextureKey skyhi = new TextureKey("Textures/Water256.dds", true);
        skyhi.setGenerateMips(true);
        Texture texlow = assetManager.loadTexture(skyhi);
        rootNode.attachChild(SkyFactory.createSky(assetManager, texlow, SkyFactory.EnvMapType.CubeMap));
    }
}

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
import com.jme3.asset.plugins.FileLocator;
import com.jme3.light.AmbientLight;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.shape.Box;
import com.jme3.texture.Texture;

/**
 * @author thetoucher
 */
public class TestDissolve extends SimpleApplication {

    // speed of animation
    private final float m_speed = .125f;
    private float count = 0;
    private int dir = 1;

    // reusable params
    private final Vector2f DSParams = new Vector2f(0, 0); // standard
    private final Vector2f DSParamsInv = new Vector2f(0, 1); // inverted
    private final Vector2f DSParamsBurn = new Vector2f(0, 0); // used for offset organic burn map

    /**
     *
     * @param args
     */
    public static void main(final String[] args) {
        final TestDissolve app = new TestDissolve();
        app.start();
    }

    @Override
    public void simpleInitApp() {

        assetManager.registerLocator("assets", FileLocator.class);

        viewPort.setBackgroundColor(ColorRGBA.Gray);
        cam.setLocation(new Vector3f(0, 1.5f, 10f));
        flyCam.setMoveSpeed(5);
        // flyCam.setEnabled(false);

        runTest();
        addLighting();
    }

    private void runTest() {
        Texture texture;
        Material mat;

        // linear dissolve
        addTestCube(-3f, 3f, assetManager.loadTexture("Textures/Dissolve/linear.png"), DSParams);

        // organic dissolve
        addTestCube(0, 3f, assetManager.loadTexture("Textures/Dissolve/burnMap.png"), DSParamsInv);

        // pixel dissolve
        texture = assetManager.loadTexture("Textures/Dissolve/pixelMap.png");
        texture.setMagFilter(Texture.MagFilter.Nearest); // this is needed to retain the crisp pixelated look
        addTestCube(3f, 3f, texture, DSParams);

        // organic growth
        mat = addTestCube(-3f, 0, assetManager.loadTexture("Textures/Dissolve/growMap.png"), DSParamsInv).getMaterial();
        mat.setColor("Ambient", ColorRGBA.Green);
        mat.setTexture("DiffuseMap", assetManager.loadTexture("Textures/Dissolve/growMap.png"));

        addTestCube(-3f, 0, assetManager.loadTexture("Textures/Dissolve/growMap.png"), DSParams);

        // texture mask
        mat = addTestCube(0, 0, assetManager.loadTexture("Textures/Dissolve/streetBurn.png"), DSParams).getMaterial();
        mat.setTexture("DiffuseMap", assetManager.loadTexture("Textures/Dissolve/streetClean.png"));
        mat.setColor("Ambient", ColorRGBA.White);

        mat = addTestCube(0f, 0f, assetManager.loadTexture("Textures/Dissolve/streetBurn.png"), DSParamsInv).getMaterial();
        mat.setTexture("DiffuseMap", assetManager.loadTexture("Textures/Dissolve/street.png"));
        mat.setColor("Ambient", ColorRGBA.White);

        // organic burn
        addTestCube(3f, 0, assetManager.loadTexture("Textures/Dissolve/burnMap.png"), DSParamsBurn).getMaterial().setColor("Ambient", ColorRGBA.Red);
        addTestCube(3f, 0, assetManager.loadTexture("Textures/Dissolve/burnMap.png"), DSParams);
    }

    private void addLighting() {
        final AmbientLight ambient = new AmbientLight();
        ambient.setColor(ColorRGBA.White);
        rootNode.addLight(ambient);
    }

    private Geometry addTestCube(float xPos, float yPos, Texture map, Vector2f DSParams) {
        // Create a material instance using ShaderBlow's Lighting.j3md
        Material mat = new Material(assetManager, "MatDefs/Dissolve/Lighting.j3md");
        mat.setColor("Ambient", ColorRGBA.Blue);
        mat.setColor("Diffuse", ColorRGBA.White);
        mat.setColor("Specular", ColorRGBA.Black);
        mat.setBoolean("UseMaterialColors", true);
        mat.setTexture("DissolveMap", map); // Set mask texture map
        mat.setVector2("DissolveParams", DSParams); // Set params

        Box box = new Box(1, 1, 1);
        Geometry geom = new Geometry("Box", box);
        geom.setMaterial(mat);
        geom.setLocalTranslation(new Vector3f(xPos, yPos, 0));
        rootNode.attachChild(geom);

        return geom;
    }

    @Override
    public void simpleUpdate(final float tpf) {

        count += tpf * m_speed * dir;

        // animation ossolation
        if (count > 1f) {
            dir = -1;
        } else if (count < 0) {
            dir = 1;
        }

        // update the dissolve amounts
        DSParams.setX(count);
        DSParamsInv.setX(count);
        DSParamsBurn.setX(count - .05f);
    }

}

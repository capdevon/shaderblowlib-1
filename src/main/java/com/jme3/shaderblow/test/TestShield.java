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
import com.jme3.collision.CollisionResult;
import com.jme3.collision.CollisionResults;
import com.jme3.font.BitmapText;
import com.jme3.input.MouseInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.MouseButtonTrigger;
import com.jme3.light.DirectionalLight;
import com.jme3.material.Material;
import com.jme3.material.RenderState;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Ray;
import com.jme3.math.Vector3f;
import com.jme3.renderer.queue.RenderQueue.Bucket;
import com.jme3.scene.Geometry;
import com.jme3.scene.shape.Box;
import com.jme3.scene.shape.Sphere;
import com.jme3.shaderblow.forceshield.ForceShieldControl;

/**
 * 
 * @author shaderblow
 */
public class TestShield extends SimpleApplication implements ActionListener {

    private ForceShieldControl forceShieldControl;

    /**
     *
     * @param args
     */
    public static void main(String[] args) {
        new TestShield().start();
    }

    @Override
    public void simpleInitApp() {

        initCrossHairs();

        DirectionalLight sun = new DirectionalLight();
        sun.setDirection(new Vector3f(1, -1, 0));
        rootNode.addLight(sun);

        Box box = new Box(1, 1, 1);
        Geometry cube = new Geometry("ship", box);
        cube.setLocalScale(0.5f, 0.5f, 0.5f);
        Material mat1 = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        mat1.setColor("Color", ColorRGBA.randomColor());
        cube.setMaterial(mat1);
        rootNode.attachChild(cube);

        // Create spatial to be the shield
        Sphere sphere = new Sphere(30, 30, 1.2f);
        Geometry shield = new Geometry("forceshield", sphere);
        shield.setQueueBucket(Bucket.Transparent); // Remember to set the queue bucket to transparent for the spatial

        // Create ForceShield
        Material forceMat = new Material(assetManager, "MatDefs/ForceShield/ForceShield.j3md");
        forceMat.getAdditionalRenderState().setBlendMode(RenderState.BlendMode.Alpha);
        forceMat.getAdditionalRenderState().setDepthWrite(false);
        forceMat.setFloat("MaxDistance", 1);
        forceMat.setTexture("ColorMap", assetManager.loadTexture("Textures/ForceShield/fs_texture.png"));
//        forceMat.setColor("Color", new ColorRGBA(1, 0, 0, 3));
//        forceMat.setFloat("MinAlpha", 0.1f);

        forceShieldControl = new ForceShieldControl(forceMat);
        shield.addControl(forceShieldControl); // Add the control to the spatial
        forceShieldControl.setEffectSize(1.2f); // Set the effect size
        forceShieldControl.setColor(new ColorRGBA(1, 0, 0, 3)); // Set effect color
        forceShieldControl.setVisibility(0.03f); // Set shield visibility.
        forceShieldControl.setMaxTime(0.5f);
//        forceShieldControl.setTexture(assetManager.loadTexture("Textures/ForceShield/fs_texture.png")); // Set a texture to the shield
//        forceShieldControl.setEnabled(false); // Enable, disable animation.

        rootNode.attachChild(shield);

        viewPort.setBackgroundColor(ColorRGBA.Gray);
        flyCam.setMoveSpeed(10);

        inputManager.addMapping("FIRE", new MouseButtonTrigger(MouseInput.BUTTON_LEFT));
        inputManager.addListener(this, "FIRE");
    }

    @Override
    public void onAction(String name, boolean isPressed, float arg) {
        if (name.equals("FIRE") && isPressed) {

            Ray ray = new Ray(cam.getLocation(), cam.getDirection());
            CollisionResults results = new CollisionResults();
            rootNode.collideWith(ray, results);

            if (results.size() > 0) {
                CollisionResult closest = results.getClosestCollision();
                Vector3f point = closest.getContactPoint();

                System.out.println("Hit at " + point);
                forceShieldControl.registerHit(point);
            }
        }
    }

    private void initCrossHairs() {
        guiNode.detachAllChildren();
        guiFont = assetManager.loadFont("Interface/Fonts/Default.fnt");
        BitmapText ch = new BitmapText(guiFont, false);
        ch.setSize(guiFont.getCharSet().getRenderedSize() * 2);
        ch.setText("+"); // crosshairs
        ch.setLocalTranslation(
                // center
                settings.getWidth() / 2 - guiFont.getCharSet().getRenderedSize() / 3 * 2,
                settings.getHeight() / 2 + ch.getLineHeight() / 2, 0);
        guiNode.attachChild(ch);
    }

}

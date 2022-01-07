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
import com.jme3.math.FastMath;
import com.jme3.math.Ray;
import com.jme3.math.Vector3f;
import com.jme3.renderer.queue.RenderQueue.Bucket;
import com.jme3.scene.Geometry;
import com.jme3.scene.Mesh;
import com.jme3.scene.Node;
import com.jme3.scene.shape.Box;
import com.jme3.scene.shape.Sphere;
import com.jme3.shaderblow.forceshield.ForceShieldControl;
import com.jme3.system.AppSettings;

/**
 *
 * @author shaderblow
 */
public class TestShield extends SimpleApplication implements ActionListener {

    private Node shipNode;
    private float angle = 0;

    /**
     *
     * @param args
     */
    public static void main(String[] args) {
        TestShield app = new TestShield();
        AppSettings settings = new AppSettings(true);
        settings.setTitle("TestShield");
        settings.setResolution(1280, 720);
        app.setSettings(settings);
        app.setPauseOnLostFocus(false);
        app.setShowSettings(false);
        app.start();
    }

    @Override
    public void simpleInitApp() {

        initCrossHairs();
        addLighting();
        createShip();
        configInputs();

        viewPort.setBackgroundColor(ColorRGBA.DarkGray);
        flyCam.setMoveSpeed(10);
    }

    @Override
    public void simpleUpdate(float tpf) {
        angle += tpf;
        angle %= FastMath.TWO_PI;
        float x = FastMath.cos(angle) * 2;
        float y = FastMath.sin(angle) * 2;
        shipNode.setLocalTranslation(x, 0, y);
        shipNode.rotate(0, tpf, 0);
    }

    private void createShip() {

        shipNode = new Node("ShipNode");

        Geometry ship = createMesh("ShipMesh", new Box(1, 1, 1), ColorRGBA.Blue);
        ship.setLocalScale(0.5f, 0.5f, 0.5f);
        shipNode.attachChild(ship);

        Geometry shield = createShield();
        shipNode.attachChild(shield);

        rootNode.attachChild(shipNode);
    }

    private Geometry createMesh(String name, Mesh mesh, ColorRGBA color) {
        Geometry geo = new Geometry(name, mesh);
        Material mat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        mat.setColor("Color", color);
        geo.setMaterial(mat);
        return geo;
    }

    private Geometry createShield() {
        Sphere sphere = new Sphere(30, 30, 1.2f);
        Geometry shield = new Geometry("ForceShield", sphere);
        shield.setQueueBucket(Bucket.Transparent); // Remember to set the queue bucket to transparent for the spatial

        Material forceMat = createForceShieldMAT();
        ForceShieldControl forceShieldControl = new ForceShieldControl(forceMat);
        shield.addControl(forceShieldControl); // Add the control to the spatial
        forceShieldControl.setEffectSize(1.2f); // Set the effect size
        forceShieldControl.setColor(ColorRGBA.Cyan); // Set effect color
        forceShieldControl.setVisibility(0.03f); // Set shield visibility.
        forceShieldControl.setMaxTime(0.5f);
//        forceShieldControl.setTexture(assetManager.loadTexture("Textures/ForceShield/fs_texture.png")); // Set a texture to the shield
//        forceShieldControl.setEnabled(false); // Enable, disable animation.

        return shield;
    }

    private Material createForceShieldMAT() {
        // Create ForceShield
        Material mat = new Material(assetManager, "MatDefs/ForceShield/ForceShield.j3md");
        mat.getAdditionalRenderState().setBlendMode(RenderState.BlendMode.Alpha);
        mat.getAdditionalRenderState().setDepthWrite(false);
        mat.setFloat("MaxDistance", 1);
        mat.setTexture("ColorMap", assetManager.loadTexture("Textures/ForceShield/fs_texture.png"));
//        mat.setColor("Color", new ColorRGBA(1, 0, 0, 3));
//        mat.setFloat("MinAlpha", 0.1f);
        return mat;
    }

    private void addLighting() {
        DirectionalLight sun = new DirectionalLight();
        sun.setDirection(new Vector3f(1, -1, 0));
        rootNode.addLight(sun);
    }

    private void configInputs() {
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
                Geometry geo    = closest.getGeometry();
                Vector3f point  = closest.getContactPoint();
                float dist      = closest.getDistance();
                String target   = geo.getName();
                System.out.println("Hit #" + target + " at " + point + ", " + dist + " WU away.");

                ForceShieldControl control = geo.getControl(ForceShieldControl.class);
                if (control != null) {
                    control.registerHit(point);
                }
            }
        }
    }

    private void initCrossHairs() {
        guiFont = assetManager.loadFont("Interface/Fonts/Default.fnt");
        BitmapText ch = new BitmapText(guiFont);
        ch.setSize(guiFont.getCharSet().getRenderedSize() * 2);
        ch.setText("+"); // crosshairs
        float x = settings.getWidth() / 2 - guiFont.getCharSet().getRenderedSize() / 3 * 2;
        float y = settings.getHeight() / 2 + ch.getLineHeight() / 2;
        // center
        ch.setLocalTranslation(x, y, 0);
        guiNode.attachChild(ch);
    }

}

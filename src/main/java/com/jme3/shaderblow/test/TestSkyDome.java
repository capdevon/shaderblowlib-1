/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jme3.shaderblow.test;

import com.jme3.app.SimpleApplication;
import com.jme3.light.AmbientLight;
import com.jme3.light.DirectionalLight;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.renderer.queue.RenderQueue.Bucket;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.shape.Box;
import com.jme3.shaderblow.skydome.SkyDomeControl;

/**
 *
 * @author mifth
 */
public class TestSkyDome extends SimpleApplication {

    /**
     *
     * @param args
     */
    public static void main(final String[] args) {
        TestSkyDome app = new TestSkyDome();
        app.start();
    }

    @Override
    public void simpleInitApp() {

        flyCam.setMoveSpeed(30);
        viewPort.setBackgroundColor(new ColorRGBA(0.5f, 0.5f, 1f, 1f));

        addGeometry();
        addSkyDome();
    }

    private void addSkyDome() {
        SkyDomeControl skyDome = new SkyDomeControl(assetManager, cam,
                "Models/SkyDome/SkyDome.j3o",
                "Textures/SkyDome/SkyNight_L.png",
                "Textures/SkyDome/Moon_L.png",
                "Textures/SkyDome/Clouds_L.png",
                "Textures/SkyDome/Fog_Alpha.png");

        Node sky = new Node();
        sky.setQueueBucket(Bucket.Sky);
        sky.addControl(skyDome);
        sky.setCullHint(Spatial.CullHint.Never);

// Either add a reference to the control for the existing JME fog filter or use the one I posted…
// But… REMEMBER!  If you use JME’s… the sky dome will have fog rendered over it.
// Sorta pointless at that point
//        FogFilter fog = new FogFilter(ColorRGBA.Blue, 0.5f, 10f);
//        skyDome.setFogFilter(fog, viewPort);
// Set some fog colors… or not (defaults are cool)
        skyDome.setFogColor(ColorRGBA.Blue);
        skyDome.setFogNightColor(new ColorRGBA(0.5f, 0.5f, 1f, 1f));
        skyDome.setDaySkyColor(new ColorRGBA(0.5f, 0.5f, 0.9f, 1f));

// Enable the control to modify the fog filter
        skyDome.setControlFog(true);

// Add the directional light you use for sun… or not
        DirectionalLight sun = new DirectionalLight();
        sun.setDirection(new Vector3f(-0.8f, -0.6f, -0.08f).normalizeLocal());
        sun.setColor(new ColorRGBA(1, 1, 1, 1));
        rootNode.addLight(sun);
        skyDome.setSun(sun);

        AmbientLight ambient = new AmbientLight();
        ambient.setColor(new ColorRGBA(0.7f, 0.7f, 1f, 1.0f));
        rootNode.addLight(ambient);

// Set some sunlight day/night colors… or not
        skyDome.setSunDayLight(new ColorRGBA(1, 1, 1, 1));
        skyDome.setSunNightLight(new ColorRGBA(0.5f, 0.5f, 0.9f, 1f));

// Enable the control to modify your sunlight
        skyDome.setControlSun(true);

// Enable the control
        skyDome.setEnabled(true);

// Add the skydome to the root… or where ever
        rootNode.attachChild(sky);
    }

    private void addGeometry() {
        Box box = new Box(1, 1, 1);
        Geometry cube = new Geometry("Box", box);
        Material mat = new Material(assetManager, "Common/MatDefs/Light/Lighting.j3md");
        cube.setMaterial(mat);
        rootNode.attachChild(cube);

        for (int i = 0; i < 100; i++) {
            Geometry geo = cube.clone(false);
            geo.setName("instance" + i);
            geo.setLocalTranslation(getRandomPosition());
            geo.rotate(0, (float) Math.random() * (float) Math.PI, 0);
            rootNode.attachChild(geo);
        }
    }

    private Vector3f getRandomPosition() {
        float x = (float) Math.random() * 100.0f - 50f;
        float y = (float) Math.random() * 10.0f;
        float z = (float) Math.random() * 100.0f - 50f;
        return new Vector3f(x, y, z);
    }

    @Override
    public void simpleUpdate(float tpf) {
    }
}

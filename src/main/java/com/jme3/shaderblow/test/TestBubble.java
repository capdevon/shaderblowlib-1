package com.jme3.shaderblow.test;

import com.jme3.app.SimpleApplication;
import com.jme3.input.ChaseCamera;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.renderer.queue.RenderQueue;
import com.jme3.scene.Geometry;
import com.jme3.scene.Spatial;
import com.jme3.scene.shape.Sphere;
import com.jme3.util.SkyFactory;

/**
 *
 * @author shaderblow
 */
public class TestBubble extends SimpleApplication {

    /**
     *
     * @param args
     */
    public static void main(String[] args) {
        TestBubble app = new TestBubble();
        app.start();
    }

    @Override
    public void simpleInitApp() {
        Sphere sphere = new Sphere(200, 200, 1);
        Geometry geom = new Geometry("Sphere", sphere);
        Material mat = new Material(assetManager, "MatDefs/Bubble/Bubble.j3md");
        mat.setTexture("ColorMap", assetManager.loadTexture("Textures/Bubble/rainbow.png"));
        mat.setFloat("Shininess", 20f);
        mat.setColor("SpecularColor", ColorRGBA.Blue);
        mat.setBoolean("UseSpecularNormal", true);
        geom.setMaterial(mat);
        geom.setQueueBucket(RenderQueue.Bucket.Transparent);
        rootNode.attachChild(geom);

        Spatial sky = SkyFactory.createSky(assetManager, "Textures/Sky/Bright/BrightSky.dds", SkyFactory.EnvMapType.CubeMap);
        rootNode.attachChild(sky);

        flyCam.setEnabled(false);
        ChaseCamera chaseCam = new ChaseCamera(cam, geom, inputManager);
    }

    @Override
    public void simpleUpdate(float tpf) {

    }

}

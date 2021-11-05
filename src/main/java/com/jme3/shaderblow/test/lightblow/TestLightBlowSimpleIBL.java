package com.jme3.shaderblow.test.lightblow;

import com.jme3.app.SimpleApplication;
import com.jme3.light.DirectionalLight;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.shape.Box;
import com.jme3.scene.shape.Sphere;
import com.jme3.util.TangentBinormalGenerator;

/**
 *
 * @author ShaderBlow
 */
public class TestLightBlowSimpleIBL extends SimpleApplication {

    /**
     *
     * @param args
     */
    public static void main(final String[] args) {
        final TestLightBlowSimpleIBL app = new TestLightBlowSimpleIBL();
        app.start();
    }

    @Override
    public void simpleInitApp() {

        createModels();

        // Add a light Source
        final DirectionalLight sun = new DirectionalLight();
        sun.setDirection(new Vector3f(-0.8f, -0.6f, -0.08f).normalizeLocal());
        sun.setColor(new ColorRGBA(1.0f, 1.0f, 1.0f, 1));
        rootNode.addLight(sun);

        flyCam.setMoveSpeed(40);
        viewPort.setBackgroundColor(ColorRGBA.Gray);
    }

    public void createModels() {
        final Material mat = assetManager.loadMaterial("Materials/LightBlow/Simple_IBL/Simple_IBL.j3m");

        final Geometry sphere = new Geometry("Sphere", new Sphere(20, 20, 5));
        sphere.setMaterial(mat);
        TangentBinormalGenerator.generate(sphere);
        sphere.setLocalTranslation(0, 0, -20);
        sphere.rotate(1.6f, 0, 0);
        rootNode.attachChild(sphere);

        final Geometry cube = new Geometry("Cube", new Box(3, 3, 3));
        cube.setMaterial(mat);
        TangentBinormalGenerator.generate(cube);
        cube.setLocalTranslation(-8, 0, -20);
        cube.rotate(1.6f, 0, 0);
        rootNode.attachChild(cube);
    }

}

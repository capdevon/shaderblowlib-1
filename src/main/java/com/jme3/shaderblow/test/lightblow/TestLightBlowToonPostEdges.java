package com.jme3.shaderblow.test.lightblow;

import com.jme3.app.SimpleApplication;
import com.jme3.light.AmbientLight;
import com.jme3.light.DirectionalLight;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.renderer.Caps;
import com.jme3.scene.Spatial;
import com.jme3.shaderblow.lightblow.CartoonEdgeProcessor;
import com.jme3.util.TangentBinormalGenerator;

/**
 *
 * @author ShaderBlow
 */
public class TestLightBlowToonPostEdges extends SimpleApplication {

    /**
     *
     * @param args
     */
    public static void main(String[] args) {
        TestLightBlowToonPostEdges app = new TestLightBlowToonPostEdges();
        app.start();

    }

    @Override
    public void simpleInitApp() {

        viewPort.setBackgroundColor(ColorRGBA.Gray);
        flyCam.setMoveSpeed(5);

        buildScene();
        addLigthing();
        setupFilters();
    }

    private void buildScene() {
        String dir = "Materials/LightBlow/Toon_System/";

        loadModel(dir + "Toon_Base.j3m", new Vector3f(0, 0, 0));
        loadModel(dir + "Toon_Base_Specular.j3m", new Vector3f(-2, 0, 0));
    }

    private Spatial loadModel(String matDef, Vector3f position) {
        Spatial model = assetManager.loadModel("Models/ToonBlow/toon.obj");
        Material mat = assetManager.loadMaterial(matDef);
        model.setMaterial(mat);
        TangentBinormalGenerator.generate(model);
        model.setLocalTranslation(position);
        rootNode.attachChild(model);
        return model;
    }

    private void setupFilters() {
        if (renderer.getCaps().contains(Caps.GLSL100)) {
            CartoonEdgeProcessor cartoonEdgeProcess = new CartoonEdgeProcessor();
            viewPort.addProcessor(cartoonEdgeProcess);
        }
    }

    private void addLigthing() {
        DirectionalLight sun = new DirectionalLight();
        sun.setDirection(new Vector3f(-0.8f, -0.6f, -0.08f).normalizeLocal());
        sun.setColor(new ColorRGBA(1, 1, 1, 1));
        rootNode.addLight(sun);

        AmbientLight ambient = new AmbientLight();
        ambient.setColor(new ColorRGBA(1.5f, 1.5f, 1.5f, 1.0f));
        rootNode.addLight(ambient);
    }

    @Override
    public void simpleUpdate(float tpf) {
    }

}

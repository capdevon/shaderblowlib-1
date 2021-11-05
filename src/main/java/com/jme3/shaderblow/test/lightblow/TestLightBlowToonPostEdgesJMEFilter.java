package com.jme3.shaderblow.test.lightblow;

import com.jme3.app.SimpleApplication;
import com.jme3.light.AmbientLight;
import com.jme3.light.DirectionalLight;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.post.FilterPostProcessor;
import com.jme3.post.filters.CartoonEdgeFilter;
import com.jme3.renderer.Caps;
import com.jme3.scene.Spatial;
import com.jme3.util.TangentBinormalGenerator;

/**
 *
 * @author ShaderBlow
 */
public class TestLightBlowToonPostEdgesJMEFilter extends SimpleApplication {

    /**
     *
     * @param args
     */
    public static void main(String[] args) {
        TestLightBlowToonPostEdgesJMEFilter app = new TestLightBlowToonPostEdgesJMEFilter();
        app.start();
    }

    @Override
    public void simpleInitApp() {

        viewPort.setBackgroundColor(ColorRGBA.Gray);
        flyCam.setMoveSpeed(10);

        buildScene();
        addLighting();
        setupFilters();
    }

    private void buildScene() {
        String dir = "Materials/LightBlow/Toon_System/";

        loadModel(dir + "Toon_Base.j3m", new Vector3f(0, 0, 0));
        loadModel(dir + "Toon_Base_Specular.j3m", new Vector3f(-2, 0, 0));
    }

    private Spatial loadModel(String matDef, Vector3f position) {
        final Spatial model = assetManager.loadModel("Models/ToonBlow/toon.obj");
        final Material mat = assetManager.loadMaterial(matDef);
        model.setMaterial(mat);
        TangentBinormalGenerator.generate(model);
        model.setLocalTranslation(position);
        rootNode.attachChild(model);

        return model;
    }

    private void setupFilters() {
        if (renderer.getCaps().contains(Caps.GLSL120)) {
            FilterPostProcessor fpp = new FilterPostProcessor(assetManager);
            CartoonEdgeFilter toon = new CartoonEdgeFilter();
            fpp.addFilter(toon);
            viewPort.addProcessor(fpp);
        }
    }

    private void addLighting() {
        final DirectionalLight sun = new DirectionalLight();
        sun.setDirection(new Vector3f(-0.8f, -0.6f, -0.08f).normalizeLocal());
        sun.setColor(new ColorRGBA(1, 1, 1, 1));
        rootNode.addLight(sun);

        final AmbientLight ambient = new AmbientLight();
        ambient.setColor(new ColorRGBA(1.5f, 1.5f, 1.5f, 1.0f));
        rootNode.addLight(ambient);
    }

    @Override
    public void simpleUpdate(final float tpf) {

    }

}

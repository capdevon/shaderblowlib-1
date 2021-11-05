package com.jme3.shaderblow.test.lightblow;

import com.jme3.app.SimpleApplication;
import com.jme3.asset.TextureKey;
import com.jme3.light.AmbientLight;
import com.jme3.light.DirectionalLight;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.scene.Spatial;
import com.jme3.texture.Texture;
import com.jme3.util.SkyFactory;
import com.jme3.util.TangentBinormalGenerator;

/**
 *
 * @author ShaderBlow
 */
public class TestLightBlowMultiplyColorSystem extends SimpleApplication {

    /**
     *
     * @param args
     */
    public static void main(String[] args) {
        TestLightBlowMultiplyColorSystem app = new TestLightBlowMultiplyColorSystem();
        app.start();
    }

    @Override
    public void simpleInitApp() {

        flyCam.setMoveSpeed(5);

        buildScene();
        createSky();
        addLighting();
    }

    private void buildScene() {

        final String dir = "Materials/LightBlow/MultiplyColor_System/";

        loadModel(dir + "MultiplyColor_Base.j3m", new Vector3f(0, 0, 0));
        loadModel(dir + "MultiplyColor_1.j3m", new Vector3f(-2f, 0, 0));
        loadModel(dir + "MultiplyColor_2.j3m", new Vector3f(-4f, 0, 0));
        loadModel(dir + "MultiplyColor_3.j3m", new Vector3f(-6f, 0, 0));
    }

    private Spatial loadModel(String matDef, Vector3f position) {
        Spatial model = assetManager.loadModel("Models/LightBlow/jme_lightblow.mesh.xml");
        Material mat = assetManager.loadMaterial(matDef);
        model.setMaterial(mat);
        model.setLocalTranslation(position);
        TangentBinormalGenerator.generate(model);
        rootNode.attachChild(model);

        return model;
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

    private void createSky() {
        TextureKey skyhi = new TextureKey("Textures/Water256.dds", true);
        skyhi.setGenerateMips(true);
        Texture texlow = assetManager.loadTexture(skyhi);
        rootNode.attachChild(SkyFactory.createSky(assetManager, texlow, SkyFactory.EnvMapType.CubeMap));
    }

}

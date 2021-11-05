package com.jme3.shaderblow.test.lightblow;

import com.jme3.app.SimpleApplication;
import com.jme3.asset.TextureKey;
import com.jme3.light.DirectionalLight;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.scene.Spatial;
import com.jme3.system.AppSettings;
import com.jme3.texture.Texture;
import com.jme3.util.SkyFactory;
import com.jme3.util.TangentBinormalGenerator;

/**
 *
 * @author ShaderBlow
 */
public class TestLightBlowLightingSystem extends SimpleApplication {

    /**
     *
     * @param args
     */
    public static void main(String[] args) {
        TestLightBlowLightingSystem app = new TestLightBlowLightingSystem();
        AppSettings newSetting = new AppSettings(true);
        newSetting.setResolution(1024, 768);
        newSetting.setGammaCorrection(false);
        newSetting.setVSync(false);
        app.setSettings(newSetting);
        app.start();
    }

    @Override
    public void simpleInitApp() {

        flyCam.setMoveSpeed(5f);

        buildScene();
        createSky();
        addLighting();
    }

    private void buildScene() {

        final String dir = "Materials/LightBlow/Lighting_System/";

        loadModel(dir + "LightBlow_Base.j3m", new Vector3f(0, 0, 0));
        loadModel(dir + "LightBlow_HemiLighting_1.j3m", new Vector3f(-2f, 0, 0));
        loadModel(dir + "LightBlow_HemiLighting_2.j3m", new Vector3f(-4f, 0, 0));
        loadModel(dir + "LightBlow_Base_Specular.j3m", new Vector3f(4f, 0, 0));
        loadModel(dir + "LightBlow_Linear.j3m", new Vector3f(2f, 0, 0));
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
        DirectionalLight sun = new DirectionalLight();
        sun.setDirection(new Vector3f(-0.8f, -0.6f, -0.08f).normalizeLocal());
        sun.setColor(new ColorRGBA(1, 1, 1, 1));
        rootNode.addLight(sun);
    }

    private void createSky() {
        TextureKey skyhi = new TextureKey("Textures/Water256.dds", true);
        skyhi.setGenerateMips(true);
        Texture texlow = assetManager.loadTexture(skyhi);
        rootNode.attachChild(SkyFactory.createSky(assetManager, texlow, SkyFactory.EnvMapType.CubeMap));
    }

}

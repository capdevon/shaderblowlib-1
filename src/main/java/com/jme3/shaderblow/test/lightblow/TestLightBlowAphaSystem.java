package com.jme3.shaderblow.test.lightblow;

import com.jme3.app.SimpleApplication;
import com.jme3.asset.TextureKey;
import com.jme3.light.DirectionalLight;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.renderer.queue.RenderQueue.Bucket;
import com.jme3.scene.Spatial;
import com.jme3.texture.Texture;
import com.jme3.util.SkyFactory;
import com.jme3.util.TangentBinormalGenerator;

/**
 *
 * @author ShaderBlow
 */
public class TestLightBlowAphaSystem extends SimpleApplication {

    /**
     *
     * @param args
     */
    public static void main(String[] args) {
        TestLightBlowAphaSystem app = new TestLightBlowAphaSystem();
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
        String dir = "Materials/LightBlow/Alpha_System/";

        loadModel(dir + "LightBlow_AlphaDiffuseMap.j3m", new Vector3f(0, 0, 0));
        loadModel(dir + "LightBlow_AlphaDiffuseMap_Threshould.j3m", new Vector3f(-2, 0, 0));
        loadModel(dir + "LightBlow_AlphaNormalMap.j3m", new Vector3f(-4, 0, 0));
        loadModel(dir + "LightBlow_AlphaNormalMap_Threshould.j3m", new Vector3f(-6, 0, 0));
    }

    private Spatial loadModel(String matDef, Vector3f position) {
        Spatial model = assetManager.loadModel("Models/LightBlow/jme_lightblow.mesh.xml");
        Material mat = assetManager.loadMaterial(matDef);
        model.setMaterial(mat);
        model.setLocalTranslation(position);
        TangentBinormalGenerator.generate(model);
        model.setQueueBucket(Bucket.Transparent);
        rootNode.attachChild(model);

        return model;
    }

    private void addLighting() {
        final DirectionalLight sun = new DirectionalLight();
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

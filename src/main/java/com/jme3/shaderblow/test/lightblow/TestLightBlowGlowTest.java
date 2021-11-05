package com.jme3.shaderblow.test.lightblow;

import com.jme3.app.SimpleApplication;
import com.jme3.asset.TextureKey;
import com.jme3.light.DirectionalLight;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.post.FilterPostProcessor;
import com.jme3.post.filters.BloomFilter;
import com.jme3.scene.Spatial;
import com.jme3.texture.Texture;
import com.jme3.util.SkyFactory;
import com.jme3.util.TangentBinormalGenerator;

/**
 *
 * @author ShaderBlow
 */
public class TestLightBlowGlowTest extends SimpleApplication {

    /**
     *
     * @param args
     */
    public static void main(final String[] args) {
        final TestLightBlowGlowTest app = new TestLightBlowGlowTest();
        app.start();
    }

    @Override
    public void simpleInitApp() {
        buildScene();
        createSky();
        addLighting();
    }

    private void buildScene() {

        final String dir = "Materials/LightBlow/Shading_System/";

        loadModel(dir + "LightBlow_ibl.j3m", new Vector3f(0, 0, 0));
        loadModel(dir + "LightBlow_reflection.j3m", new Vector3f(-1.5f, 0, 0));
        loadModel(dir + "LightBlow_reflection_additive.j3m", new Vector3f(-2.5f, 0, 0));
        loadModel(dir + "LightBlow_ref_a_nor.j3m", new Vector3f(-4f, 0, 0));
        loadModel(dir + "LightBlow_minnaert.j3m", new Vector3f(-6f, 0, 0));
        loadModel(dir + "LightBlow_rim.j3m", new Vector3f(-8f, 0, 0));
        loadModel(dir + "LightBlow_rim_2.j3m", new Vector3f(-10f, 0, 0));
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

        FilterPostProcessor fpp = new FilterPostProcessor(assetManager);
        // fpp.setNumSamples(4);
        final BloomFilter bloom = new BloomFilter(BloomFilter.GlowMode.Objects);
        bloom.setDownSamplingFactor(2);
        bloom.setBlurScale(1.37f);
        bloom.setExposurePower(2.1f);
        bloom.setExposureCutOff(0.3f);
        bloom.setBloomIntensity(7.8f);

        fpp.addFilter(bloom);
        viewPort.addProcessor(fpp);
    }

    private void createSky() {
        TextureKey skyhi = new TextureKey("Textures/Water256.dds", true);
        skyhi.setGenerateMips(true);
        Texture texlow = assetManager.loadTexture(skyhi);
        rootNode.attachChild(SkyFactory.createSky(assetManager, texlow, SkyFactory.EnvMapType.CubeMap));
    }

}

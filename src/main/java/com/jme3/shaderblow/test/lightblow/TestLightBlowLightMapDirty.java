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
public class TestLightBlowLightMapDirty extends SimpleApplication {

    /**
     *
     * @param args
     */
    public static void main(String[] args) {
        TestLightBlowLightMapDirty app = new TestLightBlowLightMapDirty();
        app.start();
    }

    @Override
    public void simpleInitApp() {

        flyCam.setMoveSpeed(10);
        createSky();
        addLighting();

        Spatial model = assetManager.loadModel("Models/LightBlow/lightmap/lightmap.mesh.xml");
        Material mat = assetManager.loadMaterial("Materials/LightBlow/lightmap/lightmap.j3m");
        model.setMaterial(mat);
        TangentBinormalGenerator.generate(model);
        rootNode.attachChild(model);

//        Spatial model2 = assetManager.loadModel("Models/LightBlow/lightmap/lightmap.mesh.xml");
//        Material mat2 = assetManager.loadMaterial("Materials/LightBlow/lightmap/lightmap_dirty.j3m");
//        model2.setMaterial(mat2);
//        TangentBinormalGenerator.generate(model2);
//        model2.move(4f, 0f, 0f);
//        rootNode.attachChild(model2);
    }

    private void addLighting() {
        DirectionalLight sun = new DirectionalLight();
        sun.setDirection(new Vector3f(-0.8f, -0.6f, -0.08f).normalizeLocal());
        sun.setColor(new ColorRGBA(1, 1, 1, 1));
        rootNode.addLight(sun);

        AmbientLight ambient = new AmbientLight();
        ambient.setColor(new ColorRGBA(0.5f, 0.9f, 0.5f, 1));
        rootNode.addLight(ambient);
    }

    private void createSky() {
        TextureKey skyhi = new TextureKey("Textures/Water256.dds", true);
        skyhi.setGenerateMips(true);
        Texture texlow = assetManager.loadTexture(skyhi);
        rootNode.attachChild(SkyFactory.createSky(assetManager, texlow, SkyFactory.EnvMapType.CubeMap));
    }

}

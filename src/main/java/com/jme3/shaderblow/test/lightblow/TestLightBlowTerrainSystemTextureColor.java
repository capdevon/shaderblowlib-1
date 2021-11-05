package com.jme3.shaderblow.test.lightblow;

import com.jme3.app.SimpleApplication;
import com.jme3.light.DirectionalLight;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.scene.Spatial;
import com.jme3.util.SkyFactory;
import com.jme3.util.SkyFactory.EnvMapType;
import com.jme3.util.TangentBinormalGenerator;

/**
 *
 * @author ShaderBlow
 */
public class TestLightBlowTerrainSystemTextureColor extends SimpleApplication {

    /**
     *
     * @param args
     */
    public static void main(String[] args) {
        TestLightBlowTerrainSystemTextureColor app = new TestLightBlowTerrainSystemTextureColor();
        app.start();
    }

    @Override
    public void simpleInitApp() {

        flyCam.setMoveSpeed(5);

        Spatial sky = SkyFactory.createSky(assetManager, "Textures/Sky/Bright/BrightSky.dds", EnvMapType.CubeMap);
        rootNode.attachChild(sky);

        Spatial terrain = assetManager.loadModel("Models/LightBlow/jme_lightblow_terrain.mesh.xml");
        Material mat = assetManager.loadMaterial("Materials/LightBlow/Terrain_System/Terrain_System_Texture.j3m");
        terrain.setMaterial(mat);
        TangentBinormalGenerator.generate(terrain);
        rootNode.attachChild(terrain);

        DirectionalLight sun = new DirectionalLight();
        sun.setDirection(new Vector3f(-0.8f, -0.6f, -0.08f).normalizeLocal());
        sun.setColor(new ColorRGBA(1, 1, 1, 1));
        rootNode.addLight(sun);
    }

}

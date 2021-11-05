package com.jme3.shaderblow.test;

import com.jme3.animation.AnimChannel;
import com.jme3.animation.AnimControl;
import com.jme3.animation.SkeletonControl;
import com.jme3.app.SimpleApplication;
import com.jme3.asset.TextureKey;
import com.jme3.light.DirectionalLight;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.renderer.Caps;
import com.jme3.scene.Spatial;
import com.jme3.shaderblow.lightblow.CartoonEdgeProcessor;
import com.jme3.texture.Texture;
import com.jme3.util.SkyFactory;
import com.jme3.util.SkyFactory.EnvMapType;
import com.jme3.util.TangentBinormalGenerator;

/**
 * @author wezrule
 */
public class TestGlass extends SimpleApplication {

    /**
     *
     * @param args
     */
    public static void main(final String[] args) {
        final TestGlass app = new TestGlass();
        app.start();
    }

    @Override
    public void simpleInitApp() {
        // assetManager.registerLocator("assets", FileLocator.class);

        flyCam.setMoveSpeed(10);
        viewPort.setBackgroundColor(ColorRGBA.Gray);

        buildScene();
        createSky();
        addLighting();
    }

    private void addLighting() {
        if (renderer.getCaps().contains(Caps.GLSL100)) {
            final CartoonEdgeProcessor cartoonEdgeProcess = new CartoonEdgeProcessor();
            viewPort.addProcessor(cartoonEdgeProcess);
        }

        // Required for toon edge effect
        final DirectionalLight sun = new DirectionalLight();
        sun.setDirection(new Vector3f(-0.8f, -0.6f, -0.08f).normalizeLocal());
        sun.setColor(new ColorRGBA(1, 1, 1, 1));
        rootNode.addLight(sun);
    }

    private void createSky() {
        final TextureKey skyhi = new TextureKey("Textures/Water256.dds", true);
        skyhi.setGenerateMips(true);
        final Texture texlow = assetManager.loadTexture(skyhi);
        rootNode.attachChild(SkyFactory.createSky(assetManager, texlow, EnvMapType.CubeMap));
    }

    private void buildScene() {

        final Spatial char_boy1 = loadModel("Materials/Glass/Glass1.j3m", new Vector3f(0, 0, 0));
        playAnimation(char_boy1, "Action", true);

        final Spatial char_boy2 = loadModel("Materials/Glass/Glass1_bump.j3m", new Vector3f(1, 0, 0));
        playAnimation(char_boy2, "Action", true);

        final Spatial char_boy3 = loadModel("Materials/Glass/Glass2_low.j3m", new Vector3f(-1, 0, 0));
        playAnimation(char_boy3, "Action", true);

        final Spatial char_boy4 = loadModel("Materials/Glass/Glass3_color.j3m", new Vector3f(-2, 0, 0));
        playAnimation(char_boy4, "Action", true);

        final Spatial char_boy5 = loadModel("Materials/Glass/Glass4_specular.j3m", new Vector3f(-3, 0, 0));
        playAnimation(char_boy5, "Action", true);
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

    private void playAnimation(Spatial model, String name, boolean hardwareSkinning) {
        if (name != null) {
            AnimControl animControl = model.getControl(AnimControl.class);
            AnimChannel channel = animControl.createChannel();
            channel.setAnim(name);
        }

        SkeletonControl skControl = model.getControl(SkeletonControl.class);
        skControl.setHardwareSkinningPreferred(hardwareSkinning);
    }
}

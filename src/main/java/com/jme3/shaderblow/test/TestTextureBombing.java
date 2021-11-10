package com.jme3.shaderblow.test;

import com.jme3.anim.AnimComposer;
import com.jme3.anim.SkinningControl;
import com.jme3.app.SimpleApplication;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.shape.Box;
import com.jme3.texture.Texture;
import com.jme3.util.TangentBinormalGenerator;

/*
 * @author wezrule
 */
public class TestTextureBombing extends SimpleApplication {

    /**
     *
     * @param args
     */
    public static void main(String[] args) {
        new TestTextureBombing().start();
    }

    @Override
    public void simpleInitApp() {

        flyCam.setMoveSpeed(10);
        viewPort.setBackgroundColor(ColorRGBA.Gray);

        Material textureBombMat = new Material(assetManager, "MatDefs/TextureBombing/TextureBombing.j3md");
        textureBombMat.setTexture("TextureAtlas", assetManager.loadTexture("Textures/TextureBombing/Glyphs.png"));
        textureBombMat.setTexture("NoiseTex", assetManager.loadTexture("Textures/TextureBombing/Noise.png"));

        textureBombMat.setFloat("ScaleFactor", 10f); // Scales the tex coords by 10, increasing the cell count
        textureBombMat.setFloat("NumImages", 10f); // A 10 x 10 texture atlas (100 images)
        textureBombMat.setFloat("Percentage", 1f); // 75% of cells will be filled
        textureBombMat.setBoolean("RandomScale", true); // Should the images be randomly scaled?
        textureBombMat.setBoolean("UseAtlasColors", false); // We have a black and white image, so let the shader generate color
        textureBombMat.setBoolean("RandomRotate", true); // Should they be randomly rotated?
        textureBombMat.setFloat("SamplesPerCell", 1.0f); // Should be >= 1.0f (How many images per cell)
        textureBombMat.setBoolean("Animated", false); // Should the images be animated?

        // Change filters otherwise there are artifacts
        textureBombMat.getTextureParam("TextureAtlas").getTextureValue().setMinFilter(Texture.MinFilter.BilinearNoMipMaps);
        textureBombMat.getTextureParam("NoiseTex").getTextureValue().setMinFilter(Texture.MinFilter.BilinearNoMipMaps);

        // 1.
        Geometry cube = new Geometry("Cube", new Box(1, 1, 1));
        cube.setMaterial(textureBombMat);
        rootNode.attachChild(cube);

        // 2.
        final Spatial char_boy1 = assetManager.loadModel("Models/LightBlow/jme_lightblow.mesh.xml");
        Material matcapMat = textureBombMat.clone();
        matcapMat.setColor("Color", new ColorRGBA(0.3f, 0.7f, 1f, 1.0f));
        char_boy1.setMaterial(matcapMat);
        char_boy1.setLocalTranslation(2.5f, -1, 0);
        TangentBinormalGenerator.generate(char_boy1);

        AnimComposer composer = char_boy1.getControl(AnimComposer.class);
        composer.setCurrentAction("Action");
        SkinningControl skControl = char_boy1.getControl(SkinningControl.class);
        skControl.setHardwareSkinningPreferred(true);

        rootNode.attachChild(char_boy1);

        // 3rd model (same one but with animations)
        Spatial boy3 = char_boy1.clone(true);
        boy3.setLocalTranslation(4, -1, 0);
        Geometry b3_child = (Geometry) ((Node) boy3).getChild("Material");
        b3_child.getMaterial().setBoolean("Animated", true); // Should the images be animated?
        rootNode.attachChild(boy3);

        // 4th model (Using a 9x9 blood atlas)
        Spatial boy4 = char_boy1.clone(true);
        boy4.setLocalTranslation(6, -1, 0);
        Geometry b4_child = (Geometry) ((Node) boy4).getChild("Material");

        Material childMat = b4_child.getMaterial();
        childMat.setColor("Color", ColorRGBA.White);
        childMat.setTexture("ColorMap", assetManager.loadTexture("Textures/matcaps/met2.png"));
        childMat.setFloat("NumImages", 3f); // A 3 x 3 texture atlas (9 images)
        childMat.setFloat("ScaleFactor", 15f); // Scales the tex coords by 10, increasing the cell count
        childMat.setTexture("TextureAtlas", assetManager.loadTexture("Textures/TextureBombing/Blood.png"));
        childMat.getTextureParam("TextureAtlas").getTextureValue().setMinFilter(Texture.MinFilter.BilinearNoMipMaps);
        childMat.getTextureParam("NoiseTex").getTextureValue().setMinFilter(Texture.MinFilter.BilinearNoMipMaps);
        childMat.setFloat("SamplesPerCell", 6.0f); // Should be >= 1.0f (How many images per cell)
        childMat.setBoolean("Animated", false); // Should the images be animated?
        childMat.setBoolean("UseAtlasColors", true);
        rootNode.attachChild(boy4);
    }
}

package com.jme3.shaderblow.test;

import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;

import com.jme3.app.SimpleApplication;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Mesh;
import com.jme3.scene.VertexBuffer;
import com.jme3.scene.VertexBuffer.Format;
import com.jme3.scene.VertexBuffer.Usage;
import com.jme3.util.BufferUtils;

/**
 *
 * @author ShaderBlow
 */
public class TestSimpleSpriteParticle extends SimpleApplication {

    private Mesh mesh;
    private final SimpleSprite[] sprites = new SimpleSprite[30000];

    private class SimpleSprite {

        Vector3f position;
        float size = 1f;
        ColorRGBA color;
        float startX = 1f;
        float startY = 1f;
        float endX = 0f;
        float endY = 0f;
    }

    /**
     *
     * @param args
     */
    public static void main(String[] args) {
        TestSimpleSpriteParticle app = new TestSimpleSpriteParticle();
        app.start();
    }

    @Override
    public void simpleInitApp() {

        flyCam.setMoveSpeed(50);

        mesh = createSpriteMesh(sprites);

        float min = 0;
        float max = 30;
        for (int i = 0; i < sprites.length; i++) {
            sprites[i] = new SimpleSprite();
            sprites[i].position = new Vector3f(
                    min + (float) (Math.random() * max), 
                    min + (float) (Math.random() * max), 
                    min + (float) (Math.random() * max));
            sprites[i].color = ColorRGBA.White;
        }
        updateParticleData(mesh, sprites);

        Geometry geom = new Geometry("Sprite", mesh);
        Material mat = new Material(assetManager, "MatDefs/SimpleSpriteParticle/SimpleSpriteParticle.j3md");
        mat.setTexture("Texture", assetManager.loadTexture("Textures/matcaps/Gold.jpg"));
        geom.setMaterial(mat);

        rootNode.attachChild(geom);
    }

    @Override
    public void simpleUpdate(float tpf) {
        updateParticleData(mesh, sprites);
    }

    private void updateParticleData(Mesh mesh, SimpleSprite[] particles) {
        VertexBuffer pvb = mesh.getBuffer(VertexBuffer.Type.Position);
        FloatBuffer positions = (FloatBuffer) pvb.getData();

        VertexBuffer cvb = mesh.getBuffer(VertexBuffer.Type.Color);
        ByteBuffer colors = (ByteBuffer) cvb.getData();

        VertexBuffer svb = mesh.getBuffer(VertexBuffer.Type.Size);
        FloatBuffer sizes = (FloatBuffer) svb.getData();

        VertexBuffer tvb = mesh.getBuffer(VertexBuffer.Type.TexCoord);
        FloatBuffer texcoords = (FloatBuffer) tvb.getData();

        // update data in vertex buffers
        positions.rewind();
        colors.rewind();
        sizes.rewind();
        texcoords.rewind();

        for (SimpleSprite p : particles) {
            positions.put(p.position.x).put(p.position.y).put(p.position.z);
            colors.putInt(p.color.asIntABGR());
            sizes.put(p.size);
            texcoords.put(p.startX).put(p.startY).put(p.endX).put(p.endY);
        }

        positions.flip();
        colors.flip();
        sizes.flip();
        texcoords.flip();

        // force renderer to re-send data to <span class="domtooltips">GPU<span class="domtooltips_tooltip"
        // style="display: none">A "Graphics Processing Unit" is a specialized circuit designed to rapidly manipulate
        // and alter memory in such a way so as to accelerate the building of images in a frame buffer intended for
        // output to a display. GPUs are used in embedded systems, mobile phones, personal computers, workstations, and
        // game consoles.</span></span>
        pvb.updateData(positions);
        cvb.updateData(colors);
        svb.updateData(sizes);
        tvb.updateData(texcoords);
        mesh.updateBound();
    }

    private void initBuffer(Mesh mesh, VertexBuffer.Type type, Buffer pb, Usage usage, int components, Format format, boolean normalise) {
        VertexBuffer pvb = new VertexBuffer(type);
        pvb.setupData(usage, components, format, pb);
        if (normalise) {
            pvb.setNormalized(true);
        }

        VertexBuffer buf = mesh.getBuffer(type);
        if (buf != null) {
            buf.updateData(pb);
        } else {
            mesh.setBuffer(pvb);
        }
    }

    private Mesh createSpriteMesh(SimpleSprite[] sprites) {
        Mesh spriteMesh = new Mesh();
        int numParticles = sprites.length;
        spriteMesh.setMode(Mesh.Mode.Points);
        initBuffer(spriteMesh, VertexBuffer.Type.Position, BufferUtils.createFloatBuffer(numParticles * 3),
                Usage.Stream, 3, Format.Float, false);
        initBuffer(spriteMesh, VertexBuffer.Type.Color, BufferUtils.createByteBuffer(numParticles * 4), Usage.Stream,
                4, Format.UnsignedByte, true);
        initBuffer(spriteMesh, VertexBuffer.Type.Size, BufferUtils.createFloatBuffer(numParticles), Usage.Stream, 1,
                Format.Float, false);
        initBuffer(spriteMesh, VertexBuffer.Type.TexCoord, BufferUtils.createFloatBuffer(numParticles * 4),
                Usage.Stream, 4, Format.Float, false);
        return spriteMesh;
    }
}

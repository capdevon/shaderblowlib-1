/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jme3.shaderblow.test.electricity;

import com.jme3.asset.AssetManager;
import com.jme3.material.Material;
import com.jme3.material.MaterialDef;
import com.jme3.renderer.RenderManager;
import com.jme3.scene.Geometry;

/**
 *
 * @author cvlad
 */
public class Electricity5Material extends Material {

    private int layers = 10;
    private float width = 1;

    public Electricity5Material(MaterialDef def) {
        super(def);
    }

    public Electricity5Material(AssetManager contentMan, String defName) {
        super(contentMan, defName);
    }

    public void setLayers(int layers) {
        this.layers = layers;
    }

    public int getLayers() {
        return this.layers;
    }

    public void setWidth(float width) {
        this.width = width;
    }

    public float getWidth() {
        return this.width;
    }

    @Override
    public void render(Geometry geom, RenderManager rm) {
        // TODO: copy&paste code from LodControl to get lod working, 
        // since geometry.getLodLevel() has a maximum based on the mesh
        for (int i = 1; i <= layers; i++) {
            setFloat("width", width * i / layers);
            setFloat("layer", ((float) i) / layers);
            super.render(geom, rm);
        }
    }
}

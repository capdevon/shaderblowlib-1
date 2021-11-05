/*
 * Copyright (c) 2009-2012 ShaderBlow
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are
 * met:
 *
 * * Redistributions of source code must retain the above copyright
 *   notice, this list of conditions and the following disclaimer.
 *
 * * Redistributions in binary form must reproduce the above copyright
 *   notice, this list of conditions and the following disclaimer in the
 *   documentation and/or other materials provided with the distribution.
 *
 * * Neither the name of 'ShaderBlow' nor the names of its contributors
 *   may be used to endorse or promote products derived from this software
 *   without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED
 * TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package com.jme3.shaderblow.test;

import com.jme3.app.SimpleApplication;
import com.jme3.font.BitmapText;
import com.jme3.input.KeyInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.AnalogListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.math.ColorRGBA;
import com.jme3.post.FilterPostProcessor;
import com.jme3.shaderblow.filter.ColorScaleFilter;
import com.jme3.shaderblow.test.SceneHelper;

/**
 * 
 * @author shaderblow
 */
public class TestColorScale extends SimpleApplication {

    private FilterPostProcessor fpp;
    private boolean enabled = true;
    private ColorScaleFilter colorScale;

    /**
     *
     * @param args
     */
    public static void main(final String[] args) {
        final TestColorScale app = new TestColorScale();
        app.start();
    }

    @Override
    public void simpleInitApp() {

        // assetManager.registerLocator("assets", FileLocator.class);
        flyCam.setMoveSpeed(15);

        SceneHelper.buildTestModel(assetManager, rootNode);
        SceneHelper.buildSkybox(assetManager, rootNode);
        SceneHelper.buildFloor(assetManager, rootNode);
        SceneHelper.buildLights(rootNode);

        fpp = new FilterPostProcessor(assetManager);
        // fpp.setNumSamples(4);
        colorScale = new ColorScaleFilter(new ColorRGBA(130f / 255f, 26f / 255f, 90f / 255f, 1.0f), 0.7f);
        fpp.addFilter(colorScale);
        viewPort.addProcessor(fpp);
        
        initInputs();

        guiFont = assetManager.loadFont("Interface/Fonts/Default.fnt");
        BitmapText ch = new BitmapText(guiFont, false);
        ch.setSize(guiFont.getCharSet().getRenderedSize());
        ch.setText("Space,Y,H,1,2,3"); // crosshairs
        ch.setColor(new ColorRGBA(1f, 0.8f, 0.1f, 1f));
        ch.setLocalTranslation(settings.getWidth() * 0.3f, settings.getHeight() * 0.1f, 0);
        guiNode.attachChild(ch);
    }

    private void initInputs() {
        inputManager.addMapping("toggle", new KeyTrigger(KeyInput.KEY_SPACE));
        inputManager.addMapping("Overlay", new KeyTrigger(KeyInput.KEY_1));
        inputManager.addMapping("Multiply", new KeyTrigger(KeyInput.KEY_2));
        inputManager.addMapping("Normal", new KeyTrigger(KeyInput.KEY_3));
        inputManager.addMapping("DensityUp", new KeyTrigger(KeyInput.KEY_Y));
        inputManager.addMapping("DensityDown", new KeyTrigger(KeyInput.KEY_H));

        inputManager.addListener(acl, "toggle");
        inputManager.addListener(acl, "Overlay");
        inputManager.addListener(acl, "Multiply");
        inputManager.addListener(acl, "Normal");
        inputManager.addListener(anl, "DensityUp", "DensityDown");
    }

    final ActionListener acl = new ActionListener() {
        @Override
        public void onAction(String name, boolean keyPressed, float tpf) {
            if (name.equals("Overlay") && keyPressed) {
                if (enabled) {
                    colorScale.setMultiply(false);
                    colorScale.setOverlay(true);
                }
            }

            if (name.equals("Multiply") && keyPressed) {
                if (enabled) {
                    colorScale.setMultiply(true);
                    colorScale.setOverlay(false);
                }
            }

            if (name.equals("Normal") && keyPressed) {
                if (enabled) {
                    colorScale.setMultiply(false);
                    colorScale.setOverlay(false);
                }
            }

            if (name.equals("toggle") && keyPressed) {
                if (enabled) {
                    enabled = false;
                    viewPort.removeProcessor(fpp);
                } else {
                    enabled = true;
                    viewPort.addProcessor(fpp);
                }
            }
        }
    };

    final AnalogListener anl = new AnalogListener() {
        @Override
        public void onAnalog(String name, float isPressed, float tpf) {
            if (name.equals("DensityUp")) {
                colorScale.setColorDensity(colorScale.getColorDensity() + 0.001f);
                System.out.println("ColorScale density : " + colorScale.getColorDensity());
            }
            if (name.equals("DensityDown")) {
                colorScale.setColorDensity(colorScale.getColorDensity() - 0.001f);
                System.out.println("ColorScale density : " + colorScale.getColorDensity());
            }
        }
    };
}

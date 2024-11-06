package Man;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
//package project;

import Texture.TextureReader;
import java.awt.event.*;
import java.io.IOException;
import javax.media.opengl.*;

import java.util.BitSet;
import javax.media.opengl.glu.GLU;

public class AnimGLEventListener4 extends AnimListener {

    int animationIndex = 0;
    int alphaIndex = 4;
    int ninjaIndex = 32;
    int healthIndex = 31;
    int healthBarIndex = 30;
    int maxWidth = 100;
    int maxHeight = 100;
    int x = maxWidth/2, y = 0;
    int xN = maxWidth/2, yN = 0;
    int x1 = maxWidth/3, y1 = maxHeight-10;
    int hx = -75, hy = 100 , hdx = -75;
    double starSpeed = 2.0 ;
    boolean throwNinjaStar = false , gameIsRun = true;
    String textureNames[] = {"Man1.png","Man2.png","Man3.png","Man4.png","a.png","b.png","c.png" ,"d.png" ,"e.png","f.png","g.png","h.png","i.png","j.png","k.png","l.png","m.png","n.png","o.png","p.png","q.png","r.png","s.png","t.png","u.png","v.png","w.png","x.png","y.png","z.png" ,"HealthB.png","Health.png","ninja star.png","Back.png"};
    TextureReader.Texture texture[] = new TextureReader.Texture[textureNames.length];
    int textures[] = new int[textureNames.length];

    /*
     5 means gun in array pos
     x and y coordinate for gun
     */
    public void init(GLAutoDrawable gld) {

        GL gl = gld.getGL();
        gl.glClearColor(1.0f, 1.0f, 1.0f, 1.0f);    //This Will Clear The Background Color To Black

        gl.glEnable(GL.GL_TEXTURE_2D);  // Enable Texture Mapping
        gl.glBlendFunc(GL.GL_SRC_ALPHA, GL.GL_ONE_MINUS_SRC_ALPHA);
        gl.glGenTextures(textureNames.length, textures, 0);

        for(int i = 0; i < textureNames.length; i++){
            try {
                texture[i] = TextureReader.readTexture(assetsFolderName + "//" + textureNames[i] , true);
                gl.glBindTexture(GL.GL_TEXTURE_2D, textures[i]);

//                mipmapsFromPNG(gl, new GLU(), texture[i]);
                new GLU().gluBuild2DMipmaps(
                        GL.GL_TEXTURE_2D,
                        GL.GL_RGBA, // Internal Texel Format,
                        texture[i].getWidth(), texture[i].getHeight(),
                        GL.GL_RGBA, // External format from image,
                        GL.GL_UNSIGNED_BYTE,
                        texture[i].getPixels() // Imagedata
                );
            } catch( IOException e ) {
                System.out.println(e);
                e.printStackTrace();
            }
        }
    }

    public void display(GLAutoDrawable gld) {
        y1--;
        GL gl = gld.getGL();
        gl.glClear(GL.GL_COLOR_BUFFER_BIT);       //Clear The Screen And The Depth Buffer
        gl.glLoadIdentity();
        DrawBackground(gl);
        handleKeyPress();
        animationIndex = animationIndex % 4;
        if (throwNinjaStar){
            DrawSprite(gl, xN, yN, ninjaIndex, 0.5f);
            shoot();
        }
        if (y1 < 0){
            throwNinjaStar = false;
            x1 = (int)(Math.random()*maxWidth);
            y1 = maxHeight;
            alphaIndex = (int)(Math.random()*26)+4;
        }
//        DrawGraph(gl);x
        DrawSprite(gl, x, y, animationIndex, 1); // draw ninja star
        if (gameIsRun) {
            DrawSprite(gl, x1, y1, alphaIndex, 0.8f);
        }
        gl.glPushMatrix();
        gl.glScaled(0.3, 0.8, 1);
        DrawHealth(gl,hx ,hy, healthBarIndex, 1);
        DrawHealth(gl,hdx ,hy, healthIndex, 1);
        gl.glPopMatrix();
        if (y1 == 0 ){
           hdx -=10;
        }
        if (hdx <= -175){
            gameIsRun = false ;
        }
        if(y1<0){
            x1=(int)(Math.random()*maxWidth);
            y1=maxHeight;
            alphaIndex = (int)(Math.random()*26)+4; // make range between 4 (a) to (z)
        }
    }

    public void shoot(){
        if (throwNinjaStar){
            double distSquared = squaredDistance(xN , yN , x1 , y1);
            double dis = Math.sqrt(distSquared);
            if (dis > 1){
                xN += starSpeed * -((xN - x1)/dis);
                yN += starSpeed * -((yN - y1)/dis);
            }else {
                throwNinjaStar = false ;
                y1 = maxHeight ;
                x1 = (int)(Math.random() * maxWidth);
                alphaIndex = (int)(Math.random()*26)+4;
            }
        }
    }
    public double squaredDistance(int x, int y, int x1, int y1){
        return Math.pow(x-x1,2)+Math.pow(y-y1,2);
    }


    public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
    }

    public void displayChanged(GLAutoDrawable drawable, boolean modeChanged, boolean deviceChanged) {
    }
    int angle=0;
    public void DrawSprite(GL gl,int x, int y, int index, float scale){
        gl.glEnable(GL.GL_BLEND);
        gl.glBindTexture(GL.GL_TEXTURE_2D, textures[index]);	// Turn Blending On

        gl.glPushMatrix();
        gl.glTranslated( x/(maxWidth/2.0) - 0.9, y/(maxHeight/2.0) - 0.9, 0);
        gl.glScaled(0.1*scale, 0.1*scale, 1);
        if (index==texture.length-2){
            gl.glRotated(angle+=45,0,0,1);
        }
        //System.out.println(x +" " + y);
        gl.glBegin(GL.GL_QUADS);
        // Front Face
        gl.glTexCoord2f(0.0f, 0.0f); // make layer that draw on it .
        gl.glVertex3f(-1.0f, -1.0f, -1.0f); // vertex
        gl.glTexCoord2f(1.0f, 0.0f);
        gl.glVertex3f(1.0f, -1.0f, -1.0f);
        gl.glTexCoord2f(1.0f, 1.0f);
        gl.glVertex3f(1.0f, 1.0f, -1.0f);
        gl.glTexCoord2f(0.0f, 1.0f);
        gl.glVertex3f(-1.0f, 1.0f, -1.0f);
        gl.glEnd();
        gl.glPopMatrix();
        gl.glDisable(GL.GL_BLEND);
    }
    public void DrawHealth(GL gl,int x, int y, int index, float scale){
        gl.glEnable(GL.GL_BLEND);
        gl.glBindTexture(GL.GL_TEXTURE_2D, textures[index]);	// Turn Blending On

        gl.glPushMatrix();
        gl.glTranslated( x/(maxWidth/2.0) - 0.9, y/(maxHeight/2.0) - 0.9, 0);
        gl.glScaled(scale, 0.1*scale, 1);
        //System.out.println(x +" " + y);
        gl.glBegin(GL.GL_QUADS);
        // Front Face
        gl.glTexCoord2f(0.0f, 0.0f); // make layer that draw on it .
        gl.glVertex3f(-1.0f, -1.0f, -1.0f); // vertex
        gl.glTexCoord2f(1.0f, 0.0f);
        gl.glVertex3f(1.0f, -1.0f, -1.0f);
        gl.glTexCoord2f(1.0f, 1.0f);
        gl.glVertex3f(1.0f, 1.0f, -1.0f);
        gl.glTexCoord2f(0.0f, 1.0f);
        gl.glVertex3f(-1.0f, 1.0f, -1.0f);
        gl.glEnd();
        gl.glPopMatrix();
        gl.glDisable(GL.GL_BLEND);
    }

    public void DrawBackground(GL gl){
        gl.glEnable(GL.GL_BLEND);
        gl.glBindTexture(GL.GL_TEXTURE_2D, textures[textures.length-1]);	// Turn Blending On
        gl.glPushMatrix();
        gl.glBegin(GL.GL_QUADS);
        // Front Face
        gl.glTexCoord2f(0.0f, 0.0f);
        gl.glVertex3f(-1.0f, -1.0f, -1.0f);
        gl.glTexCoord2f(1.0f, 0.0f);
        gl.glVertex3f(1.0f, -1.0f, -1.0f);
        gl.glTexCoord2f(1.0f, 1.0f);
        gl.glVertex3f(1.0f, 1.0f, -1.0f);
        gl.glTexCoord2f(0.0f, 1.0f);
        gl.glVertex3f(-1.0f, 1.0f, -1.0f);
        gl.glEnd();
        gl.glPopMatrix();

        gl.glDisable(GL.GL_BLEND);
    }

    /*
     * KeyListener
     */

    public void handleKeyPress() {
        if (gameIsRun) {
            if (isKeyPressed(KeyEvent.VK_LEFT)){
                if (x > 0) {
                    x--;
                }
                animationIndex++;
            }
            if (isKeyPressed(KeyEvent.VK_RIGHT)) {
                if (x < maxWidth - 10) {
                    x++;
                }
                animationIndex++;
            }

//           char c = textureNames[alphaIndex].charAt(0);
//           if (isKeyPressed(KeyEvent.getExtendedKeyCodeForChar(c))){
//           if (!throwNinjaStar) {
//               throwNinjaStar = true;
//               xN = x ; // pos x-axis of ninja star
//               yN = y ; // pos y-axis of ninja star
//            }
//            }
            if (isKeyPressed(KeyEvent.VK_A + - 4 +alphaIndex)) {
                if (!throwNinjaStar) {
                    throwNinjaStar = true;
                    xN = x; // pos x-axis of ninja star
                    yN = y; // pos y-axis of ninja star
                }
            }
        }
    }

    public BitSet keyBits = new BitSet(256);

    @Override
    public void keyPressed(final KeyEvent event) {
        int keyCode = event.getKeyCode();
        keyBits.set(keyCode);
    }

    @Override
    public void keyReleased(final KeyEvent event) {
        int keyCode = event.getKeyCode();
        keyBits.clear(keyCode);
    }

    @Override
    public void keyTyped(final KeyEvent event) {
        // don't care
    }

    public boolean isKeyPressed(final int keyCode) {
        return keyBits.get(keyCode);
    }
}
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package codigo;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Shape;
import java.awt.geom.Area;
import java.awt.geom.Ellipse2D;
import javax.swing.ImageIcon;

/**
 *
 * @author jmartinezdejuan
 */
public class Helicoptero extends Ellipse2D.Double {

    Color colorCopter;
    int yVelocidad = -2;
    Image copterImage;
    boolean sube = false;

    public Helicoptero(int _ancho, int _alto, Color _color) {
        super(100, 100, _ancho, _alto);
        colorCopter = _color;
        pintaCopter(_ancho, _alto);
    }

    public void setSube(boolean b) {
        sube = b;
    }

    public void pintaCopter(int _ancho, int _alto) {
        if (sube) {
            copterImage = (new ImageIcon(new ImageIcon(
                    getClass().getResource("/imagenes/pig_up.png"))
                    .getImage().getScaledInstance(_ancho, _alto, Image.SCALE_DEFAULT)))
                    .getImage();
        } else {
            copterImage = (new ImageIcon(new ImageIcon(
                    getClass().getResource("/imagenes/pig_down.png"))
                    .getImage().getScaledInstance(_ancho, _alto, Image.SCALE_DEFAULT)))
                    .getImage();
        }
    }
    public void restablecePosicion(){
        y=VentanaJuego.ALTOPANTALLA/2;
    }
    public void mueve(Graphics2D g2) {
        this.y = this.y - yVelocidad;
        //pongo un tope para que no se salga por el techo
        if (this.y < 0) {
            this.y = 0;
            yVelocidad = -2;
        }
        g2.drawImage(copterImage, (int) x, (int) y, null);
        if (yVelocidad < -3) {
            yVelocidad = -2;
        }
    }

    public boolean chequeaColisionColumna(Columna c) {
        Area areaCopter = new Area(this);
        Area areaInterCapitel = new Area(c.capitel);
        Area areaInterBase = new Area(c.base);
        if (areaCopter.intersects(c.capitel) || areaCopter.intersects(c.base)) {
            return true;
        }
        areaInterCapitel.intersect(areaCopter);
        if (!areaInterCapitel.isEmpty()) {
            return true;
        }
        areaInterBase.intersect(areaCopter);
        if (!areaInterBase.isEmpty()) {
            return true;
        }

        return false;
    }

    public boolean chequeaColisionBellota(Bellota b) {

        Area areaCopter = new Area(this);
        Area areaBellota = new Area(b);
        if (areaCopter.intersects(b.getBounds2D())) {
//            b.bellotaImg = null;
            return true;
        }
        areaBellota.intersect(areaCopter);
        if (!areaBellota.isEmpty()) {
            return true;
        }
        return false;
    }
    public boolean chequeaColisionJamon(Jamon j) {

        Area areaCopter = new Area(this);
        Area areaJamon = new Area(j);
        if (areaCopter.intersects(j.getBounds2D())) {
            //j.jamonImg = null;
            return true;
        }
        areaJamon.intersect(areaCopter);
        if (!areaJamon.isEmpty()) {
            return true;
        }
        return false;
    }

//        Area areaCopter = new Area(this);
//        Area areaCirculoInf = new Area(c.circuloInferior);
//        Area areaCirculoSup = new Area(c.circuloSuperior);
//        boolean choca = true;
//        areaCopter.intersect(areaCirculoInf);
//        areaCopter.intersect(areaCirculoSup);        
//       
//        if (areaCopter.isEmpty()){
//            choca = false;
//        }
//        return (this.intersects(c.capitel) || 
//                this.intersects(c.base) ||
//                choca
//                );
//    }
}

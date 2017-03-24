/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package codigo;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.geom.Ellipse2D;
import java.util.Random;
import javax.swing.ImageIcon;

/**
 *
 * @author jmartinezdejuan
 */
public class Jamon extends Ellipse2D.Double {

    boolean visible = true;
    int radioBellota = 20;
    private int ancho_pantalla;
    Image jamonImg;

    public Jamon(int _anchoPantalla, int desplazamiento) {
        super(_anchoPantalla*1.5, desplazamiento, 40, 20);
        precargaImagenes();
        ancho_pantalla = _anchoPantalla;

    }

    private void precargaImagenes() {

        jamonImg = (new ImageIcon(new ImageIcon(
                getClass().getResource("/imagenes/jamon.png"))
                .getImage().getScaledInstance((int) (radioBellota * 2), radioBellota, Image.SCALE_DEFAULT)))
                .getImage();
    }

    public void mueve(Graphics2D g2) {
        mueveJamon();
        g2.drawImage(jamonImg, (int) getX(), (int) getY() - radioBellota / 2, null);
    }

    private void mueveJamon() {
        
        if (getX() + radioBellota < 0) {
            Random aleatorio = new Random();
        int desplazamiento = aleatorio.nextInt(400);
            setFrame(ancho_pantalla,
                    100 + desplazamiento,
                    getWidth(),
                    getHeight());
        } else {
            setFrame(getX() - 4,getY(), getWidth(), getHeight());

        }
        

    }
}

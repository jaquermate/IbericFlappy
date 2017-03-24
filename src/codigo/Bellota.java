/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package codigo;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.geom.Ellipse2D;
import java.util.Random;
import javax.swing.ImageIcon;

/**
 *
 * @author jmartinezdejuan
 */
public class Bellota extends Ellipse2D.Double {

    boolean visible = true;

    int radioBellota = 20;
    private int ancho_pantalla;
    Image bellotaImg;

    public Bellota(int _anchoPantalla, int desplazamiento) {
        super(_anchoPantalla, desplazamiento, 20, 20);

        precargaImagenes();
        ancho_pantalla = _anchoPantalla;

    }

    private void precargaImagenes() {

        bellotaImg = (new ImageIcon(new ImageIcon(
                getClass().getResource("/imagenes/bellota.png"))
                .getImage().getScaledInstance(radioBellota, radioBellota, Image.SCALE_DEFAULT)))
                .getImage();
    }

    public void mueve(Graphics2D g2) {
        mueveBellota();
        g2.drawImage(bellotaImg, (int) getX(), (int) getY() - radioBellota / 2, null);
    }

    private void mueveBellota() {

        if (getX() + radioBellota < 0) {
            Random aleatorio = new Random();
            int desplazamiento = aleatorio.nextInt(300); 
            setFrame(ancho_pantalla,
                    100 + desplazamiento,
                    getWidth(),
                    getHeight());
        } else {
            setFrame(getX() - 6, getY(), getWidth(), getHeight());

        }

    }
}

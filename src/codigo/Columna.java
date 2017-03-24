/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package codigo;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;
import java.util.Random;
import javax.swing.ImageIcon;

/**
 *
 * @author jmartinezdejuan
 */
public class Columna {
    
    Rectangle2D capitel, base;
    
    int hueco = 350;
    int altura_columna = 180;
    int ancho_columna = 60;
    private int ancho_pantalla;
    Image col_abajo, col_arriba;
    boolean obstaculo;
    int contador=0;
    int nivel=1;
    
  
        
    public Columna (int _ancho, int _anchoPantalla, boolean _obstaculo){
        obstaculo=_obstaculo;
        Random aleatorio = new Random();
        int desplazamiento = aleatorio.nextInt(100);
        
        capitel = new Rectangle2D.Double(_ancho, -desplazamiento, ancho_columna, altura_columna);
        base = new Rectangle2D.Double(_ancho, 
                                      altura_columna/2 + hueco - desplazamiento + ancho_columna/2, 
                                      ancho_columna, 
                                      altura_columna);
        
        ancho_pantalla = _anchoPantalla;
        precargaImagenes();
    }
    public int getContador(){
        return contador;
    }
    
    private void precargaImagenes(){
         
//        col_abajo = (new ImageIcon(new ImageIcon(
//                getClass().getResource("/imagenes/pipe_bottom.png"))
//                .getImage().getScaledInstance(ancho_columna, altura_columna, Image.SCALE_DEFAULT)))
//                .getImage();
//        col_arriba = (new ImageIcon(new ImageIcon(
//                getClass().getResource("/imagenes/pipe_top.png"))
//                .getImage().getScaledInstance(ancho_columna, altura_columna, Image.SCALE_DEFAULT)))
//                .getImage();       
//        
    }
    
    public void mueve(Graphics2D g2){
        mueveColumna();
        g2.drawImage(col_abajo, (int)base.getX(), (int)base.getY()-ancho_columna/2, null);
        g2.drawImage(col_arriba, (int)capitel.getX(), (int)capitel.getY()-ancho_columna/2, null);
        g2.setColor(Color.PINK);
        g2.fill(base);
        g2.fill(capitel);
      
    }
    
    private void mueveColumna(){
        if (capitel.getX() + ancho_columna < 0){
            Random aleatorio = new Random();
            int desplazamiento = aleatorio.nextInt(100);
            int posAnt=0;
            posAnt=posAnt-desplazamiento;
            capitel.setFrame(ancho_pantalla, 
                            posAnt,
                            capitel.getWidth(), 
                            capitel.getHeight());
            
            
            base.setFrame(ancho_pantalla, 
                            altura_columna/2 + hueco - desplazamiento + ancho_columna/2,
                            base.getWidth(), 
                            base.getHeight());
            contador++;
            if(contador%5==0){
                nivel++;
            }
           
        }
        else {
            capitel.setFrame(capitel.getX()-4, capitel.getY(),capitel.getWidth(), capitel.getHeight());
            base.setFrame(base.getX()-4, base.getY(),base.getWidth(), base.getHeight()); 
        }
            
        
              
        }
    }


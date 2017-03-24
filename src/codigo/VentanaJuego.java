/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package codigo;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;
import java.util.Scanner;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.ImageIcon;
import javax.swing.Timer;

/**
 *
 * @author jmartinezdejuan
 */
public class VentanaJuego extends javax.swing.JFrame {

    Clip sonidoBellota, sonidoJamon, sonidoMuerte, sonidoEstrella, sonidoHomer;
    Bellota miBellota;
    Jamon miJamon;
    boolean gameOver = false;
    boolean godMode = false;
    Image fondo;
    Helicoptero miCopter = new Helicoptero(50, 50, Color.WHITE);
    static int ANCHOPANTALLA = 780;
    static int ALTOPANTALLA = 500;
    static int SEPARACION_COLUMNAS = 60;
    int numColumnas = 17, contadorBellotas = 0, contadorVidas = 10, hScore;
    Columna[] arrayCol = new Columna[100];
    BufferedImage buffer = null;
    Graphics2D bufferGraphics, lienzoGraphics = null;

    Timer temporizador = new Timer(10, new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            bucleDelJuego();
        }
    });

    /**
     * Creates new form VentanaJuego
     */
    public VentanaJuego() {
        load(new File("Best.txt"));
        initComponents();
        principioJuego.setVisible(true);
        principioJuego.setAlwaysOnTop(true);
        contadorBoton.setVisible(true);
        contadorBell.setVisible(true);
        botonContadorVidas.setVisible(true);
        botonGodModeTexto.setVisible(true);
        botonGodModeTexto.setText("");

        for (int i = 0; i < numColumnas; i++) {
            arrayCol[i] = new Columna(ANCHOPANTALLA + i * SEPARACION_COLUMNAS, ANCHOPANTALLA, false);
        }
        precargaImagenes();
        inicializaBuffers();
        finalJuego.setSize(jPanel1.getWidth(), jPanel1.getHeight());
        Random r = new Random();
        miBellota = new Bellota(ANCHOPANTALLA, r.nextInt(400));
        miJamon = new Jamon(ANCHOPANTALLA, r.nextInt(400));
    }

    private void inicializaBuffers() {

        lienzoGraphics = (Graphics2D) jPanel1.getGraphics();
        buffer = (BufferedImage) jPanel1.createImage(ANCHOPANTALLA, ALTOPANTALLA);
        bufferGraphics = buffer.createGraphics();
        jPanel1.setSize(850, 500);
        bufferGraphics.setColor(Color.GRAY);
        bufferGraphics.fillRect(0, 0, ANCHOPANTALLA, ALTOPANTALLA);
        principioJuego.setSize(530, 400);

    }

    private void precargaImagenes() {
        fondo = (new ImageIcon(new ImageIcon(
                getClass().getResource("/imagenes/fondo.png"))
                .getImage().getScaledInstance(ANCHOPANTALLA, ALTOPANTALLA, Image.SCALE_DEFAULT)))
                .getImage();
    }

    public void load(File file) {
        try {
            Scanner reader = new Scanner(file);
            while (reader.hasNext()) {
                 hScore=reader.nextInt();
            }
        } catch (IOException i) {
            System.out.println("Error. " + i);
        }
    }

    public void save() {
        FileWriter out;
        try {
            out = new FileWriter("Best.txt");
            if(contadorBellotas>hScore){
                out.write("" + contadorBellotas);
            }
            out.close();
        } catch (IOException i) {
            System.out.println("Error: " + i.getMessage());
        }
    }

    private void bucleDelJuego() {
        bufferGraphics.drawImage(fondo, 0, 0, null);
        miCopter.pintaCopter((int) miCopter.getWidth(), (int) miCopter.getHeight());
        if (!botonGodMode.isSelected()) {
            for (int ii = 0; ii < numColumnas - 1; ii++) {
                if (miCopter.chequeaColisionColumna(arrayCol[ii])) {
                    temporizador.stop();

                    contadorVidas--;
                    miCopter.restablecePosicion();
                    temporizador.restart();
                    if (contadorVidas == 0) {
                        save();
                        if(contadorBellotas>hScore){                                                     
                           jLabel5.setText("NEW HIGH SCORE: " +Integer.toString(contadorBellotas));
                        }else{
                            jLabel5.setText("SCORE: " +Integer.toString(contadorBellotas));
                        }
                        
                        try {
                            sonidoMuerte = AudioSystem.getClip();
                            sonidoMuerte.open(AudioSystem.getAudioInputStream(getClass().getResource("/sonidos/cerdoMuerto.wav")));
                            sonidoMuerte.loop(0);
                        } catch (Exception e) {
                        }
                        temporizador.stop();
                        finalJuego.setVisible(true);
                    }
                }
            }

            if (miCopter.chequeaColisionJamon(miJamon)) {
                miJamon.setFrame(miJamon.getX(), 1000, miJamon.getWidth(), miJamon.getHeight());
                miJamon.y = 1000;
                try {
                    sonidoJamon = AudioSystem.getClip();
                    sonidoJamon.open(AudioSystem.getAudioInputStream(getClass().getResource("/sonidos/chocaJamon.wav")));
                    sonidoJamon.loop(0);
                } catch (Exception e) {
                }

            }

        }
        if (miCopter.chequeaColisionBellota(miBellota)) {
            miBellota.setFrame(miBellota.getX(), 1000, miBellota.getWidth(), miBellota.getHeight());
            miBellota.y = 1000;
            try {
                sonidoBellota = AudioSystem.getClip();
                sonidoBellota.open(AudioSystem.getAudioInputStream(getClass().getResource("/sonidos/comerBellota.wav")));
                sonidoBellota.loop(0);
            } catch (Exception e) {
            }
            contadorBellotas++;
        }

        contadorBoton.setText(Integer.toString(arrayCol[0].getContador()));
        contadorBell.setText(Integer.toString(contadorBellotas));
        botonContadorVidas.setText(Integer.toString(contadorVidas));
        if (botonGodMode.isSelected()) {
            botonGodModeTexto.setText("activado");
        } else {
            botonGodModeTexto.setText("desactivado");
        }
        bufferGraphics.setColor(Color.GRAY);
        miCopter.mueve(bufferGraphics);
        miBellota.mueve(bufferGraphics);
        miJamon.mueve(bufferGraphics);
        for (int ii = 0; ii < numColumnas - 1; ii++) {
            arrayCol[ii].mueve(bufferGraphics);
        }
        lienzoGraphics.drawImage(buffer, 0, 0, null);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        finalJuego = new javax.swing.JDialog();
        jLabel2 = new javax.swing.JLabel();
        reiniciar = new javax.swing.JButton();
        botonSalir = new javax.swing.JButton();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        botonSalir1 = new javax.swing.JButton();
        principioJuego = new javax.swing.JDialog();
        jLabel3 = new javax.swing.JLabel();
        jToggleButton1 = new javax.swing.JToggleButton();
        jToggleButton2 = new javax.swing.JToggleButton();
        jToggleButton3 = new javax.swing.JToggleButton();
        jPanel1 = new javax.swing.JPanel();
        contadorBoton = new javax.swing.JLabel();
        contadorBell = new javax.swing.JLabel();
        botonContadorVidas = new javax.swing.JLabel();
        botonGodMode = new javax.swing.JToggleButton();
        botonGodModeTexto = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();

        finalJuego.setBounds(new java.awt.Rectangle(0, 0, 100, 500));
        finalJuego.setSize(new java.awt.Dimension(400, 300));

        jLabel2.setFont(new java.awt.Font("Shree Devanagari 714", 1, 75)); // NOI18N
        jLabel2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/platoJamon.png"))); // NOI18N
        jLabel2.setPreferredSize(new java.awt.Dimension(445, 225));
        jLabel2.setSize(new java.awt.Dimension(445, 225));

        reiniciar.setText("Reiniciar");
        reiniciar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                reiniciarActionPerformed(evt);
            }
        });

        botonSalir.setText("Salir");
        botonSalir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botonSalirActionPerformed(evt);
            }
        });

        jLabel6.setText("NO ERES MI TUTOR!!!!!");

        botonSalir1.setText("Spider Cerdo");
        botonSalir1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botonSalir1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout finalJuegoLayout = new javax.swing.GroupLayout(finalJuego.getContentPane());
        finalJuego.getContentPane().setLayout(finalJuegoLayout);
        finalJuegoLayout.setHorizontalGroup(
            finalJuegoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(finalJuegoLayout.createSequentialGroup()
                .addGap(37, 37, 37)
                .addComponent(reiniciar)
                .addGap(32, 32, 32)
                .addComponent(botonSalir)
                .addGap(29, 29, 29)
                .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 169, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(finalJuegoLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 492, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(48, 48, 48)
                .addComponent(botonSalir1)
                .addGap(30, 30, 30)
                .addComponent(jLabel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(29, 29, 29))
        );
        finalJuegoLayout.setVerticalGroup(
            finalJuegoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(finalJuegoLayout.createSequentialGroup()
                .addGroup(finalJuegoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(finalJuegoLayout.createSequentialGroup()
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(finalJuegoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 301, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 18, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, finalJuegoLayout.createSequentialGroup()
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(botonSalir1)
                        .addGap(58, 58, 58)))
                .addGroup(finalJuegoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(finalJuegoLayout.createSequentialGroup()
                        .addGroup(finalJuegoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(reiniciar)
                            .addComponent(botonSalir))
                        .addGap(35, 35, 35))
                    .addGroup(finalJuegoLayout.createSequentialGroup()
                        .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(8, 8, 8))))
        );

        principioJuego.setSize(new java.awt.Dimension(532, 300));

        jLabel3.setFont(new java.awt.Font("Shree Devanagari 714", 1, 50)); // NOI18N
        jLabel3.setText("¿COMENZAR JUEGO?");

        jToggleButton1.setText("Sí");
        jToggleButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jToggleButton1ActionPerformed(evt);
            }
        });

        jToggleButton2.setText("Sí o Sí");
        jToggleButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jToggleButton2ActionPerformed(evt);
            }
        });

        jToggleButton3.setText("Déjame pesao, que lo he abierto sin querer");
        jToggleButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jToggleButton3ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout principioJuegoLayout = new javax.swing.GroupLayout(principioJuego.getContentPane());
        principioJuego.getContentPane().setLayout(principioJuegoLayout);
        principioJuegoLayout.setHorizontalGroup(
            principioJuegoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(principioJuegoLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(principioJuegoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(principioJuegoLayout.createSequentialGroup()
                        .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, 589, Short.MAX_VALUE)
                        .addGap(18, 18, 18))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, principioJuegoLayout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addGroup(principioJuegoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, principioJuegoLayout.createSequentialGroup()
                                .addComponent(jToggleButton1)
                                .addGap(96, 96, 96)
                                .addComponent(jToggleButton2)
                                .addGap(138, 138, 138))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, principioJuegoLayout.createSequentialGroup()
                                .addComponent(jToggleButton3)
                                .addGap(127, 127, 127))))))
        );
        principioJuegoLayout.setVerticalGroup(
            principioJuegoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(principioJuegoLayout.createSequentialGroup()
                .addGap(85, 85, 85)
                .addComponent(jLabel3)
                .addGap(31, 31, 31)
                .addGroup(principioJuegoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jToggleButton2)
                    .addComponent(jToggleButton1))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jToggleButton3)
                .addContainerGap(39, Short.MAX_VALUE))
        );

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setResizable(false);
        addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                formKeyPressed(evt);
            }
        });

        jPanel1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                jPanel1MousePressed(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                jPanel1MouseReleased(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 624, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 590, Short.MAX_VALUE)
        );

        botonGodMode.setText("GodMode");
        botonGodMode.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botonGodModeActionPerformed(evt);
            }
        });

        jLabel1.setText("Bellotas");

        jLabel4.setText("Vidas");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 41, Short.MAX_VALUE)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(botonGodMode, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 104, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel4)
                                    .addComponent(contadorBoton, javax.swing.GroupLayout.PREFERRED_SIZE, 96, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel1)))
                            .addGroup(layout.createSequentialGroup()
                                .addGap(25, 25, 25)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(botonContadorVidas, javax.swing.GroupLayout.DEFAULT_SIZE, 58, Short.MAX_VALUE)
                                    .addComponent(contadorBell, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                        .addGap(17, 17, 17))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(679, 679, 679)
                        .addComponent(botonGodModeTexto, javax.swing.GroupLayout.PREFERRED_SIZE, 96, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(29, 29, 29))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addGap(40, 40, 40)
                .addComponent(contadorBoton, javax.swing.GroupLayout.PREFERRED_SIZE, 72, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(contadorBell, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(botonContadorVidas, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(137, 137, 137)
                .addComponent(botonGodModeTexto, javax.swing.GroupLayout.PREFERRED_SIZE, 72, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(29, 29, 29)
                .addComponent(botonGodMode)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void formKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_formKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_SPACE) {
            miCopter.yVelocidad += 6;
        }
        if (evt.getKeyCode() == KeyEvent.VK_Z) {
            godMode = true;
        }


    }//GEN-LAST:event_formKeyPressed

    private void jToggleButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jToggleButton1ActionPerformed
        principioJuego.setVisible(false);
        temporizador.start();
    }//GEN-LAST:event_jToggleButton1ActionPerformed

    private void jToggleButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jToggleButton2ActionPerformed
        principioJuego.setVisible(false);
        temporizador.start();
    }//GEN-LAST:event_jToggleButton2ActionPerformed

    private void jToggleButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jToggleButton3ActionPerformed
        principioJuego.setVisible(false);
        temporizador.stop();

    }//GEN-LAST:event_jToggleButton3ActionPerformed

    private void jPanel1MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jPanel1MousePressed
        miCopter.yVelocidad += 6;
        miCopter.setSube(true);
    }//GEN-LAST:event_jPanel1MousePressed

    private void jPanel1MouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jPanel1MouseReleased
        miCopter.yVelocidad -= 6;
        miCopter.setSube(false);
    }//GEN-LAST:event_jPanel1MouseReleased

    private void reiniciarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_reiniciarActionPerformed

//    sonidoBellota.stop();
        //  sonidoJamon.stop();
        sonidoHomer.stop();
        sonidoMuerte.stop();
        Helicoptero miCopter = new Helicoptero(50, 50, Color.WHITE);

        contadorBellotas = 0;
        contadorVidas = 10;
        Columna[] arrayCol = new Columna[100];
        for (int i = 0; i < numColumnas; i++) {
            arrayCol[i] = new Columna(ANCHOPANTALLA + i * SEPARACION_COLUMNAS, ANCHOPANTALLA, false);
        }
        lienzoGraphics.drawImage(buffer, 0, 0, null);
        finalJuego.setVisible(false);

        temporizador.restart();


    }//GEN-LAST:event_reiniciarActionPerformed

    private void botonGodModeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botonGodModeActionPerformed
        if (botonGodMode.isSelected()) {
            try {
                sonidoEstrella = AudioSystem.getClip();
                sonidoEstrella.open(AudioSystem.getAudioInputStream(getClass().getResource("/sonidos/estrella.wav")));
                sonidoEstrella.loop(0);
            } catch (Exception e) {
            }
        } else {
            sonidoEstrella.stop();
        }
    }//GEN-LAST:event_botonGodModeActionPerformed

    private void botonSalirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botonSalirActionPerformed
        finalJuego.setVisible(false);
        jPanel1.setVisible(false);
        System.exit(0);
    }//GEN-LAST:event_botonSalirActionPerformed

    private void botonSalir1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botonSalir1ActionPerformed
        sonidoMuerte.stop();
        try {
                sonidoHomer = AudioSystem.getClip();
                sonidoHomer.open(AudioSystem.getAudioInputStream(getClass().getResource("/sonidos/spiderCerdo.wav")));
                sonidoHomer.loop(0);
            } catch (Exception e) {
            }
    }//GEN-LAST:event_botonSalir1ActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(VentanaJuego.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(VentanaJuego.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(VentanaJuego.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(VentanaJuego.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new VentanaJuego().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel botonContadorVidas;
    private javax.swing.JToggleButton botonGodMode;
    private javax.swing.JLabel botonGodModeTexto;
    private javax.swing.JButton botonSalir;
    private javax.swing.JButton botonSalir1;
    private javax.swing.JLabel contadorBell;
    private javax.swing.JLabel contadorBoton;
    private javax.swing.JDialog finalJuego;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JToggleButton jToggleButton1;
    private javax.swing.JToggleButton jToggleButton2;
    private javax.swing.JToggleButton jToggleButton3;
    private javax.swing.JDialog principioJuego;
    private javax.swing.JButton reiniciar;
    // End of variables declaration//GEN-END:variables
}

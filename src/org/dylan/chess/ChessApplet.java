/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dylan.chess;

import javax.swing.JApplet;
import javax.swing.JOptionPane;

/**
 *
 * @author dylan
 */
public class ChessApplet extends JApplet {

    /**
     * Initializes the applet ChessApplet
     */
    @Override
    public void init() {
        try {
            java.awt.EventQueue.invokeAndWait(new Runnable() {
                public void run() {
                    initComponents();
                    ChessBoard cb = new ChessBoard();
                    add(cb);
                    String val = getParameter("val");
                    JOptionPane.showMessageDialog(rootPane, val);
                }
            });
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * This method is called from within the init() method to initialize the
     * form. WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">                          
    private void initComponents() {

        setLayout(new java.awt.BorderLayout());
    }// </editor-fold>                        


    // Variables declaration - do not modify                     
    // End of variables declaration    
}
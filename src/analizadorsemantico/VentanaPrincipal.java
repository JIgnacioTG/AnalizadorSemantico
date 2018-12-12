/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package analizadorsemantico;

import javax.swing.UIManager;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author LENOVO
 */
public class VentanaPrincipal extends javax.swing.JFrame {
    
    // Modelo base de las tablas
    DefaultTableModel dtmVariables = new DefaultTableModel();
    DefaultTableModel dtmErrores = new DefaultTableModel();

    /**
     * Crea la ventana de la aplicación.
     */
    public VentanaPrincipal() {
        initComponents();
        
        // Se dibujan las tablas
        dibujarTablas();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        lblCodigo = new javax.swing.JLabel();
        scrCodigo = new javax.swing.JScrollPane();
        txtCodigo = new javax.swing.JTextArea();
        scrVariables = new javax.swing.JScrollPane();
        tblVariables = new javax.swing.JTable();
        lblVariables = new javax.swing.JLabel();
        lblErrores = new javax.swing.JLabel();
        scrErrores = new javax.swing.JScrollPane();
        tblErrores = new javax.swing.JTable();
        btnAnalizar = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Analizador semántico do-while");

        lblCodigo.setText("Código:");

        txtCodigo.setColumns(20);
        txtCodigo.setRows(5);
        scrCodigo.setViewportView(txtCodigo);

        tblVariables.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Variable", "Tipo", "Valor"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        scrVariables.setViewportView(tblVariables);

        lblVariables.setText("Tabla de variables:");

        lblErrores.setText("Tabla de errores:");

        tblErrores.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Posición", "Descripción"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        scrErrores.setViewportView(tblErrores);

        btnAnalizar.setActionCommand("btnAnalizar");
        btnAnalizar.setLabel("Analizar");
        btnAnalizar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAnalizarActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(scrCodigo)
                            .addComponent(lblCodigo))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(lblVariables)
                                .addGap(0, 0, Short.MAX_VALUE))
                            .addComponent(scrVariables, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(lblErrores)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(scrErrores, javax.swing.GroupLayout.DEFAULT_SIZE, 606, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(btnAnalizar)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblCodigo, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(lblVariables, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(scrVariables, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addComponent(scrCodigo, javax.swing.GroupLayout.DEFAULT_SIZE, 140, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblErrores)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(scrErrores, javax.swing.GroupLayout.DEFAULT_SIZE, 146, Short.MAX_VALUE)
                .addGap(18, 18, 18)
                .addComponent(btnAnalizar)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnAnalizarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAnalizarActionPerformed
        // Se pasa el código al analizador.
        AnalizadorSemantico.analizar(txtCodigo.getText());
        
        // Se obtienen el número de variables y errores existentes en la tabla.
        int numVariables = dtmVariables.getRowCount();
        int numErrores = dtmErrores.getRowCount();
        
        // Se limpia la tabla de variables.
        for (int i = 0; i < numVariables; i++) {
            dtmVariables.removeRow(0);
        }
        
        // Se limpia la tabla de errores.
        for (int i = 0; i < numErrores; i++) {
            dtmErrores.removeRow(0);
        }
        
        // Ahora se llena la tabla de variables.
        for (int i = 0; i < AnalizadorSemantico.obtenerNumVariables(); i++) {
            dtmVariables.addRow(new Object[]{AnalizadorSemantico.obtenerVariables()[i], AnalizadorSemantico.obtenerTipoVar()[i], AnalizadorSemantico.obtenerValoresVar()[i]});
        }
        
        // Se llena la tabla de errores.
        for (int i = 0; i < AnalizadorSemantico.obtenerNumErrores(); i++) {
            dtmErrores.addRow(new Object[]{"Línea " +AnalizadorSemantico.obtenerPosError()[i], AnalizadorSemantico.obtenerTxtError()[i]});
        }
        
        
    }//GEN-LAST:event_btnAnalizarActionPerformed

    private void dibujarTablas() {
        
        // Se añaden las columnas de las tablas.
        dtmVariables.addColumn("Variable");
        dtmVariables.addColumn("Tipo");
        dtmVariables.addColumn("Valor");
        
        dtmErrores.addColumn("Posición");
        dtmErrores.addColumn("Descripción");
        
        // Se agrega el modelo a las tablas con las columnas previamente agregadas.
        tblVariables.setModel(dtmVariables);
        tblErrores.setModel(dtmErrores);
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the System look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            javax.swing.UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(VentanaPrincipal.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                new VentanaPrincipal().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAnalizar;
    private javax.swing.JLabel lblCodigo;
    private javax.swing.JLabel lblErrores;
    private javax.swing.JLabel lblVariables;
    private javax.swing.JScrollPane scrCodigo;
    private javax.swing.JScrollPane scrErrores;
    private javax.swing.JScrollPane scrVariables;
    private javax.swing.JTable tblErrores;
    private javax.swing.JTable tblVariables;
    private javax.swing.JTextArea txtCodigo;
    // End of variables declaration//GEN-END:variables
}

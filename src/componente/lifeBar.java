/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package componente;

/**
 *
 * @author Allan
 */
public class lifeBar extends javax.swing.JPanel {

    /**
     * Creates new form lifeBar
     */
    private int qtdeLife;

    public int getQtdeLife() {
        return qtdeLife;
    }

    public void setQtdeLife(int qtdeLife) {
        this.qtdeLife = qtdeLife;
    }

    
    public lifeBar() {
        initComponents();
    }
    
    public void downlife(){
        qtdeLife--;
        
        switch(qtdeLife){
            case 2:
                life_3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/combattank/img/vida-vazia-35x35.jpg")));
                break;
            case 1:
                life_2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/combattank/img/vida-vazia-35x35.jpg")));
                break;
            case 0:
                life_1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/combattank/img/vida-vazia-35x35.jpg")));
                break;
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        life_2 = new javax.swing.JLabel();
        life_1 = new javax.swing.JLabel();
        life_3 = new javax.swing.JLabel();

        life_2.setBackground(new java.awt.Color(40, 40, 40));
        life_2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/combattank/img/vida-cheia-35x35.jpg"))); // NOI18N
        life_2.setMaximumSize(new java.awt.Dimension(70, 70));
        life_2.setMinimumSize(new java.awt.Dimension(70, 70));
        life_2.setName("life-2"); // NOI18N
        life_2.setPreferredSize(new java.awt.Dimension(50, 50));

        life_1.setBackground(new java.awt.Color(40, 40, 40));
        life_1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/combattank/img/vida-cheia-35x35.jpg"))); // NOI18N
        life_1.setMaximumSize(new java.awt.Dimension(70, 70));
        life_1.setMinimumSize(new java.awt.Dimension(70, 70));
        life_1.setName("life-1"); // NOI18N
        life_1.setPreferredSize(new java.awt.Dimension(50, 50));

        life_3.setBackground(new java.awt.Color(40, 40, 40));
        life_3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/combattank/img/vida-cheia-35x35.jpg"))); // NOI18N
        life_3.setMaximumSize(new java.awt.Dimension(70, 70));
        life_3.setMinimumSize(new java.awt.Dimension(70, 70));
        life_3.setName("life-3"); // NOI18N
        life_3.setPreferredSize(new java.awt.Dimension(50, 50));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(186, Short.MAX_VALUE)
                .addComponent(life_1, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(life_2, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(life_3, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(life_1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addComponent(life_3, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addComponent(life_2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel life_1;
    private javax.swing.JLabel life_2;
    private javax.swing.JLabel life_3;
    // End of variables declaration//GEN-END:variables
}

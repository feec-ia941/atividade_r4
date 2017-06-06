/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lidaPanel;

import edu.memphis.ccrg.lida.framework.ModuleName;
import edu.memphis.ccrg.lida.framework.gui.panels.GuiPanelImpl;
import java.net.URL;
import java.util.logging.Level;
import javax.swing.ImageIcon;
import modules.Environment;

/**
 *
 * @author alciomar
 */
public class ResulActionCreature extends GuiPanelImpl {

    /**
     * Creates new form ResulActionCreature
     */
    private Environment environment;

    public ResulActionCreature() {
        initComponents();
    }

    @Override
    public void initPanel(String[] param) {
        environment = (Environment) agent.getSubmodule(ModuleName.Environment);
        if (environment != null) {
            refresh();
        }
    }

    @Override
    public void refresh() {

        if (this.environment != null &&  environment.getLastAction() != null) {

            jlabelResultText.setText(this.environment.getLastAction());

            String result = environment.getLastAction();

            if (result.equals("goBrinck")) {

                ImageIcon image = new ImageIcon(getClass().getResource("/img/wall.png"));
                Result.setIcon(image);
            } else if (result.equals("gotoJewel")) {
                ImageIcon image = new ImageIcon(getClass().getResource("/img/jewel.png"));
                Result.setIcon(image);
            } else if (result.equals("gotoFood")) {
                ImageIcon image = new ImageIcon(getClass().getResource("/img/food.png"));
                Result.setIcon(image);
            } else if (result.equals("rotate")) {
                ImageIcon image = new ImageIcon(getClass().getResource("/img/Rotate.png"));
                Result.setIcon(image);
            } else if (result.equals("get")) {
                ImageIcon image = new ImageIcon(getClass().getResource("/img/eat.png"));
                Result.setIcon(image);
            }

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

        jlabelResultText = new javax.swing.JLabel();
        Result = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();

        jlabelResultText.setText("Result");

        Result.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);

        jLabel2.setText("By: Alciomar");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(Result, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jlabelResultText)
                .addContainerGap(345, Short.MAX_VALUE))
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                    .addContainerGap(306, Short.MAX_VALUE)
                    .addComponent(jLabel2)
                    .addGap(10, 10, 10)))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(Result, javax.swing.GroupLayout.DEFAULT_SIZE, 288, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jlabelResultText)
                .addGap(6, 6, 6))
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                    .addContainerGap(288, Short.MAX_VALUE)
                    .addComponent(jLabel2)
                    .addContainerGap()))
        );
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel Result;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jlabelResultText;
    // End of variables declaration//GEN-END:variables
}
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package View;

import java.awt.event.KeyEvent;
import javax.swing.JOptionPane;

/**
 *
 * @author Admin
 */
public class FindGroupChat extends javax.swing.JInternalFrame {

    /**
     * Creates new form NhapUserForm
     */
    public FindGroupChat() {
        initComponents();
    }


    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        btnJoinGroup = new javax.swing.JButton();
        txtSelectGroup = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        lstGroupChat = new javax.swing.JList<>();
        btnFindGroup = new javax.swing.JButton();
        txtGroupName = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();

        setClosable(true);
        setIconifiable(true);
        setMaximizable(true);
        setResizable(true);
        setTitle("Tìm Phòng Chat");

        btnJoinGroup.setText("Tham Gia");
        btnJoinGroup.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnJoinGroupActionPerformed(evt);
            }
        });

        txtSelectGroup.setEnabled(false);

        lstGroupChat.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lstGroupChatMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(lstGroupChat);

        btnFindGroup.setText("Tìm");
        btnFindGroup.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFindGroupActionPerformed(evt);
            }
        });

        txtGroupName.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtGroupNameKeyPressed(evt);
            }
        });

        jLabel1.setText("Tên Phòng");

        jLabel2.setText("Nhóm");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1)
                    .addComponent(jLabel2))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(txtSelectGroup, javax.swing.GroupLayout.PREFERRED_SIZE, 232, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(btnJoinGroup)
                        .addContainerGap(393, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                                .addGap(46, 46, 46)
                                .addComponent(jScrollPane1))
                            .addComponent(txtGroupName, javax.swing.GroupLayout.DEFAULT_SIZE, 590, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnFindGroup, javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(45, 45, 45))))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(59, 59, 59)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(btnFindGroup, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel1)
                            .addComponent(txtGroupName, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(32, 32, 32)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 258, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addComponent(jLabel2)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtSelectGroup, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnJoinGroup, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(52, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void lstGroupChatMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lstGroupChatMouseClicked
        txtSelectGroup.setText(this.lstGroupChat.getSelectedValue());
        // TODO add your handling code here:
    }//GEN-LAST:event_lstGroupChatMouseClicked

    private void btnFindGroupActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFindGroupActionPerformed
        if(!txtGroupName.equals("")){
            String msg = "findGroup///"+Login.client.NickName+"///"+txtGroupName.getText()+"///"+"SERVER";
            Login.client.send(msg); 
        }
        else{
            JOptionPane.showMessageDialog(this, "Chưa Nhập!");
        }
    }//GEN-LAST:event_btnFindGroupActionPerformed

    private void btnJoinGroupActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnJoinGroupActionPerformed
        if(!txtSelectGroup.equals(""))
        {
            String msg = "joinGroup///"+Login.client.NickName+"///"+txtSelectGroup.getText()+"///"+"SERVER";
            Login.client.send(msg); 
        }
        else{
            JOptionPane.showMessageDialog(this, "Chưa chọn bạn để kết bạn!");
        }
    }//GEN-LAST:event_btnJoinGroupActionPerformed

    private void txtGroupNameKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtGroupNameKeyPressed
        if(evt.getKeyCode() == KeyEvent.VK_ENTER) {
            btnFindGroup.doClick();
        }        // TODO add your handling code here:
    }//GEN-LAST:event_txtGroupNameKeyPressed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnFindGroup;
    private javax.swing.JButton btnJoinGroup;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JScrollPane jScrollPane1;
    public javax.swing.JList<String> lstGroupChat;
    private javax.swing.JTextField txtGroupName;
    private javax.swing.JTextField txtSelectGroup;
    // End of variables declaration//GEN-END:variables
}

package View;
import Controller.SocketServer;
import java.awt.Color;
import javax.swing.UIManager;

public class ServerFrame extends javax.swing.JFrame {

    public SocketServer server;
    public Thread serverThread;   
    public ServerFrame() {
        initComponents(); 
        txtServer.setEditable(false);
    }
    
    public boolean isWin32(){
        return System.getProperty("os.name").startsWith("Windows");
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        btnOpenServer = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        txtServer = new javax.swing.JTextArea();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Server");

        btnOpenServer.setText("Mo Server");
        btnOpenServer.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnOpenServerActionPerformed(evt);
            }
        });

        txtServer.setColumns(20);
        txtServer.setFont(new java.awt.Font("Consolas", 0, 12)); // NOI18N
        txtServer.setRows(5);
        jScrollPane1.setViewportView(txtServer);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 577, Short.MAX_VALUE)
                .addContainerGap())
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnOpenServer)
                .addGap(249, 249, 249))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(btnOpenServer)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 251, Short.MAX_VALUE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnOpenServerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnOpenServerActionPerformed
        server = new SocketServer(this);
        btnOpenServer.setEnabled(false);
        txtServer.setBackground(Color.green);
    }//GEN-LAST:event_btnOpenServerActionPerformed

    public void RetryStart(int port){
        if(server != null){ server.stop(); }
        server = new SocketServer(this);
    }
    
    @SuppressWarnings("UseSpecificCatch")
    public static void main(String args[]) {

        try{
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        }
        catch(Exception ex){
            System.out.println("Look & Feel Exception");
        }
        
        java.awt.EventQueue.invokeLater(() -> {
            new ServerFrame().setVisible(true);
        });
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnOpenServer;
    private javax.swing.JScrollPane jScrollPane1;
    public javax.swing.JTextArea txtServer;
    // End of variables declaration//GEN-END:variables
}

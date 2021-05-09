package Controller;

import View.*;
import View.SignUp;
import java.io.*;
import java.net.*;
import javax.swing.DefaultListModel;
import javax.swing.JOptionPane;

public class SocketClient implements Runnable{
    
    public int port;
    public String serverAddr;
    public Socket socket;
    public static Login lg=null;
    public SignUp sg;
    public ForgotPassword fgPassword;
    public CreatePassword createPassword;
    public FriendForm friendForm=null;
    public GroupForm groupForm=null;
    public FindFriend findFriend;
    public FindGroupChat findGroupChat;
    public CreateGroupChat createGroupChat;
    public Menu menu;
    public BufferedReader In;
    public BufferedWriter Out;
    boolean flThread=true;
    public static String NickName;
    public static String Email="";
    public SocketClient(Login frame) throws IOException{
        lg = frame; this.serverAddr = lg.serverAddr; this.port = lg.port;
        socket = new Socket(InetAddress.getByName(serverAddr), port);
        Out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        Out.flush();
        In = new BufferedReader(new InputStreamReader(socket.getInputStream()) );
        
//        hist = ui.hist;
    }


    @Override
    @SuppressWarnings({"UseSpecificCatch", "CallToPrintStackTrace"})
    public void run() {
        while( flThread){
            try {
                String msg =  In.readLine();
                String[] ktmsg = msg.split("///");
                System.out.println("Incoming : "+msg);
                switch(ktmsg[0]){
                    case ("login"):{
                        if(ktmsg[2].equals("TRUE")){
                            JOptionPane.showMessageDialog(lg, "Đăng Nhập Thành Công!");
                            NickName=ktmsg[3];
                            Email=lg.Email;
                            lg.setVisible(false);
                            new Menu().setVisible(true);
                        }
                        else{
                            lg.btnLogin.setEnabled(true);
                            if(ktmsg[2].equals("FALSE1")){
                                JOptionPane.showMessageDialog(lg, "Sai Tên Đăng nhập Hoặc Mật Khẩu!");
                            }
                            else{
                            JOptionPane.showMessageDialog(lg, "Tài Khoản Đã Được Đăng Nhập Ở Nơi Khác!");    
                            }
                        }
                        break;
                    }
                    case ("logout"):{
                        menu.setVisible(false);
                        new Login().setVisible(true);
                        break;
                    }
                    case ("signUp"):{
                        if(ktmsg[2].equals("TRUE")){
                            JOptionPane.showMessageDialog(sg, "Thanh Cong");
                            sg.setVisible(false);
                            lg.setVisible(true);
                        }
                        else{
                            System.out.println(ktmsg[2]);
                            if(ktmsg[2].equals("FALSE1")){
                                sg.setVisible(false);
                                lg.setVisible(true);                                
                            }
                            else{
                                JOptionPane.showMessageDialog(sg, "That bai!");
                            }
                            
                        }
                        break;
                    }
                    case ("forgotPassword"):{
                        if(ktmsg[2].equals("FALSE1")){
                            fgPassword.btnKiemTra.setEnabled(true);
                            JOptionPane.showMessageDialog(fgPassword,"Không tìm thấy Email trên");
                        }
                        else{
                            if(ktmsg[2].equals("TRUE1")){
                                fgPassword.btnKiemTra.setEnabled(false);
                                fgPassword.txtEmail.setEnabled(false);
                                fgPassword.btnXacNhan.setEnabled(true);
                                fgPassword.txtCode.setEnabled(true);
                                JOptionPane.showMessageDialog(fgPassword,"Chúng tôi đã Gửi 1 mã\n 6 chữ số đến mail của bạn \nvui lòng nhập vào ô Code");
                            }
                            else{
                                if(ktmsg[2].equals("TRUE2")){
                                    JOptionPane.showMessageDialog(fgPassword,"Thành Công");
                                    fgPassword.setVisible(false);
                                    new CreatePassword().setVisible(true);
                                }
                                else{
                                    fgPassword.btnXacNhan.setEnabled(true);
                                    JOptionPane.showMessageDialog(fgPassword,"Sai Code vui lòng kiểm tra lại");
                                }
                            }
                        }
                        break;
                    }
                    case ("newPass"):{
                        if(ktmsg[2].equals("TRUE")){
                            if(Email.equals("")){
                                JOptionPane.showMessageDialog(createPassword, "Thanh Cong");
                                createPassword.setVisible(false);
                                lg.setVisible(true);
                            }
                            else{
                                JOptionPane.showMessageDialog(createPassword, "Thanh Cong");
                                createPassword.setVisible(false);
                            }
                        }
                        else{
                            JOptionPane.showMessageDialog(createPassword, "That bai!");
                        }
                        break;
                    }
//friend                    
                    case ("friendFormLoad"):
                //load list friend
                       DefaultListModel dm=new DefaultListModel();
                       if(ktmsg[2].equals("FALSE")){
                         dm.addElement("Không có bạn nè nào!");
                         friendForm.lstFriend.setModel(dm);
                         friendForm.lstFriend.enable(false);
                       }
                       else{
                            friendForm.lstFriend.enable(true);
                            String[] listFriend=ktmsg[2].split("@@");
                            for(int i=1;i<listFriend.length;i++)
                                 dm.addElement(listFriend[i]);
                            friendForm.lstFriend.setModel(dm);  
                       }
                        break;
                
                    //load message
                    case ("showMessageFriend"):
                        if(friendForm==null){
                            break;
                        }
                        friendForm.txtChat.setText("");
                    //load list friend
                        if(!ktmsg[2].equals("False")){
                           String[] listMsg=ktmsg[2].split("@@");
                            for(int i=1;i<listMsg.length;i++)
                                friendForm.txtChat.append(listMsg[i]+"\n");
                        }
                        break;
                    case ("findFriend"):
                        DefaultListModel dm3=new DefaultListModel();
                    //load list friend
                         if(ktmsg[2].equals("FALSE")){
                         dm3.addElement("Không có bạn nè nào!");
                         findFriend.lstFriend.setModel(dm3);
                         findFriend.lstFriend.enable(false);
                         break;
                       }
                         findFriend.lstFriend.enable(true);
                        String[] lstFriend=ktmsg[2].split("@@");
                        for(int i=1;i<lstFriend.length;i++)
                            if(!lstFriend[i].equals(NickName)) dm3.addElement(lstFriend[i]);
                        findFriend.lstFriend.setModel(dm3);
                        break;  
                    case ("addFriend"):
                        
                    //load list friend
                        if(ktmsg[2].equals("TRUE")){
                            JOptionPane.showMessageDialog(findFriend, "Thêm Bạn Thành Công");
                        }
                        else{
                            JOptionPane.showMessageDialog(findFriend, "Thêm Bạn thất bại do bạn đã kết bạn với người này hoặc do lỗi");
                        }
                        break;                        
                        
//Group                          
                //groupFormLoad
                    case ("groupFormLoad"):
                //load list friend
                        DefaultListModel dm1=new DefaultListModel();
                        if(ktmsg[2].equals("FALSE")){
                            dm1.addElement("Không có nhóm nào!");
                            groupForm.lstGroup.setModel(dm1);
                            groupForm.lstGroup.enable(false);
                        }
                        else{
                            groupForm.lstGroup.enable(true);
                            String[] listGroup=ktmsg[2].split("@@");
                            for(int i=1;i<listGroup.length;i++)
                                dm1.addElement(listGroup[i]);
                            groupForm.lstGroup.setModel(dm1);                           
                        }
                        break; 
                    case ("showMessageGroup"):
                        if(groupForm==null){
                            break;
                        }
                        groupForm.txtChat.setText("");
                        
                    //load list group
                        if(!ktmsg[2].equals("False")){
                            String[] listMsggr=ktmsg[2].split("@@");
                            for(int i=1;i<listMsggr.length;i++)
                                groupForm.txtChat.append(listMsggr[i]+"\n");
                        }
                        break;                       
                //load find group
                    case ("findGroup"):
                        
                    //load list friend
                        DefaultListModel dm2=new DefaultListModel();
                        if(ktmsg[2].equals("FALSE")){
                            dm2.addElement("Không có Phòng Chat Nào!");
                            findGroupChat.lstGroupChat.setModel(dm2);
                            findGroupChat.lstGroupChat.enable(false);
                        }
                        else{
                            findGroupChat.lstGroupChat.enable(true);
                            String[] lstGroup=ktmsg[2].split("@@");
                            for(int i=1;i<lstGroup.length;i++)
                                dm2.addElement(lstGroup[i]);
                                findGroupChat.lstGroupChat.setModel(dm2);
                        }
                        break;                          
                    case ("joinGroup"):
                        
                    //load list friend
                        if(ktmsg[2].equals("TRUE")){
                            JOptionPane.showMessageDialog(findGroupChat, "Vào Nhóm Thành Công");
                        }
                        else{
                            JOptionPane.showMessageDialog(findGroupChat, "Vào nhóm thất bại do bạn đã ở trong nhóm hoặc do lỗi");
                        }
                        break;                
                    case ("createGroup"):
                        
                    //load list friend
                        if(ktmsg[2].equals("TRUE")){
                            JOptionPane.showMessageDialog(createGroupChat, "Tạo Nhóm Thành Công");
                            createGroupChat.setVisible(false);
                        }
                        else{
                            JOptionPane.showMessageDialog(createGroupChat, "Tạo nhóm thất bại do đã tồn tại nhóm hoặc do lỗi");
                        }
                        break;                
            }
                

        }
            catch(Exception ex) {
                flThread = false;
                
                 for(int i = 1; i < lg.model.size(); i++){
                    lg.model.removeElementAt(i);
                }
                
                lg.clientThread.stop();
                
                System.out.println("Exception SocketClient run()");
                ex.printStackTrace();
            }
        }
    }
    //addform to socket
    public void AddForm( SignUp sg){
        this.sg = sg;
    }
    public void AddForm( ForgotPassword forgotPassword){
        this.fgPassword = forgotPassword;
    }
    public void AddForm( CreatePassword createPassword){
        this.createPassword = createPassword;
    }
    public void AddForm( FriendForm f){
        this.friendForm = f;
    }
    public void AddForm( GroupForm f){
        this.groupForm = f;
    }
    public void AddForm( FindFriend f){
        this.findFriend = f;
    }        
    public void AddForm( FindGroupChat f){
        this.findGroupChat = f;
    }       
    public void AddForm( CreateGroupChat f){
        this.createGroupChat = f;
    }    
    public void AddForm( Menu f){
        this.menu = f;
    }
        
    public void send(String msg){
        try {
            Out.write(msg);
            Out.newLine();
            Out.flush();
            System.out.println("Outgoing : "+msg);
        } 
        catch (IOException ex) {
            System.out.println("Exception SocketClient send()");
        }
    }
    
    public void closeThread(Thread t){
        t = null;
        flThread=false;
    }
}

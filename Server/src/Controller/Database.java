package Controller;

import Model.MailConfig;
import Model.DatabaseConfig;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class Database {
    

    @SuppressWarnings("UseSpecificCatch")
    public boolean checkLogin(String username, String password) throws SQLException{
        
        try {      
            Connection con = null;
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            con = DriverManager.getConnection(DatabaseConfig.CONNECTION_Url,DatabaseConfig.USERNAME,DatabaseConfig.PASSWORD);
            String SQL = "Select * from QLUser where Email=? and MatKhau=?";
            PreparedStatement pstm;
            pstm = con.prepareStatement(SQL);
            pstm.setString(1,username);
            pstm.setString(2,password);
            ResultSet rs = pstm.executeQuery();
            if(rs.next()){
                SocketServer.nickName =rs.getString(2);
                    rs.close();
                    pstm.close();
                    con.close();
                return true;
            }
            else{
                return false;
            }
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(Database.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
   
            
    }
    
    @SuppressWarnings("UseSpecificCatch")
    public boolean addUser(String username, String password,String NickName,String PhoneNumber) throws SQLException{
        
                try {
            Connection con = null;
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            con = DriverManager.getConnection(DatabaseConfig.CONNECTION_Url,DatabaseConfig.USERNAME,DatabaseConfig.PASSWORD);
            String SQL = "Insert into QLUser values(?,?,?,?)";
            PreparedStatement pstm;
            pstm = con.prepareStatement(SQL);
            pstm.setString(1,username);
            pstm.setString(2,password);
            pstm.setString(3,NickName);
            pstm.setString(4,PhoneNumber);
            pstm.executeUpdate();
            pstm.close();
            con.close();            
            return true;
 
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(Database.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
}
    boolean forgotPassWord(String Email, int Code) throws SQLException {
        try {
            Connection con = null;
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            con = DriverManager.getConnection(DatabaseConfig.CONNECTION_Url,DatabaseConfig.USERNAME,DatabaseConfig.PASSWORD);
            String SQL = "Select * from QLUser where Email=?";
            PreparedStatement pstm;
            pstm = con.prepareStatement(SQL);
            pstm.setString(1,Email);
            ResultSet rs = pstm.executeQuery();
            if(rs.next()){
               //GuiEmail
                Properties props = new Properties();
                props.put("mail.smtp.auth", "true");
                props.put("mail.smtp.host", MailConfig.HOST_NAME);
                props.put("mail.smtp.socketFactory.port", MailConfig.SSL_PORT);
                props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
                props.put("mail.smtp.port", MailConfig.SSL_PORT);
 
        // get Session
                Session session = Session.getDefaultInstance(props, new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(MailConfig.APP_EMAIL, MailConfig.APP_PASSWORD);
                    }
                });
            pstm.close();
            con.close();
        // compose message
                try {
                    MimeMessage message = new MimeMessage(session);
                    message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(Email));
                    message.setSubject("Mật Khẩu Khôi Phục Tài Khoản");
                    String msg = "Code khôi Phục Mật Khẩu Là:"+Code;
                    message.setText(msg);

                    // send message
                    Transport.send(message);

                    return true;
                } catch (MessagingException e) {
                    return false;
                }
            }
            else{
                return false;
            }
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(Database.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
        // throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    boolean newPass(String email, String pass) throws SQLException {
        try {
            Connection con = null;
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            con = DriverManager.getConnection(DatabaseConfig.CONNECTION_Url,DatabaseConfig.USERNAME,DatabaseConfig.PASSWORD);
            String SQL = "Update QLUser set MatKhau=? where Email=?";
            PreparedStatement pstm;
            pstm = con.prepareStatement(SQL);
            pstm.setString(1,pass);
            pstm.setString(2,email);
            pstm.executeUpdate();
            pstm.close();
            con.close();            
            return true;
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(Database.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
       // throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    boolean friendFormLoad(String nickName) throws SQLException {
        try {
            Connection con = null;
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            con = DriverManager.getConnection(DatabaseConfig.CONNECTION_Url,DatabaseConfig.USERNAME,DatabaseConfig.PASSWORD);
            String SQL = "Select * from Friend where NickName=?";
            PreparedStatement pstm;
            pstm = con.prepareStatement(SQL);
            pstm.setString(1,nickName);
            ResultSet rs = pstm.executeQuery();
            while(rs.next()){
                //qua client tach chuoi theo @@
                SocketServer.list =SocketServer.list+"@@"+rs.getString(2);     
            }
            rs.close();
            pstm.close();
            con.close();            
            if(SocketServer.list.equals("")){
                return false;
            }
            else{
                return true;
            }
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(Database.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
// throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    boolean friendMessageLoad(String nickName, String friend) throws SQLException {
                       try {
            Connection con = null;
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            con = DriverManager.getConnection(DatabaseConfig.CONNECTION_Url,DatabaseConfig.USERNAME,DatabaseConfig.PASSWORD);
            String SQL = "Select * from MessageFriend where ((NickName=? AND Friend=?) OR (Friend=? AND NickName=?))";
            PreparedStatement pstm;
            pstm = con.prepareStatement(SQL);
            pstm.setString(1,nickName);
            pstm.setString(2,friend);
            pstm.setString(3,nickName);
            pstm.setString(4,friend);
            ResultSet rs = pstm.executeQuery();
            while(rs.next()){
                //qua client tach chuoi theo @@
                SocketServer.message =SocketServer.message+"@@"+rs.getString(2)+"--->"+rs.getString(3)+" :"+rs.getString(4);   
            }
            rs.close();
            pstm.close();
            con.close();
            if(SocketServer.message.equals("")){
                return false;
            }
            else{
                return true;
            }
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(Database.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    boolean sendMessageFriend(String nickName, String mess, String friend) throws SQLException {
        try {
            Connection con = null;
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            con = DriverManager.getConnection(DatabaseConfig.CONNECTION_Url,DatabaseConfig.USERNAME,DatabaseConfig.PASSWORD);
            String SQL = "Insert into MessageFriend values(?,?,?)";
            PreparedStatement pstm;
            pstm = con.prepareStatement(SQL);
            pstm.setString(1,nickName);
            pstm.setString(2,friend);
            pstm.setString(3,mess);
            pstm.executeUpdate();
            pstm.close();
            con.close();
            return true;
 
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(Database.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }

    boolean groupFormLoad(String nickName) throws SQLException {
        try {
            Connection con = null;
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            con = DriverManager.getConnection(DatabaseConfig.CONNECTION_Url,DatabaseConfig.USERNAME,DatabaseConfig.PASSWORD);
            String SQL = "Select * from MemberGroup where NickName=?";
            PreparedStatement pstm;
            pstm = con.prepareStatement(SQL);
            pstm.setString(1,nickName);
            ResultSet rs = pstm.executeQuery();
            while(rs.next()){
                //qua client tach chuoi theo @@
                SocketServer.list =SocketServer.list+"@@"+rs.getString(1);   
            }
            rs.close();
            pstm.close();
            con.close();
            if(SocketServer.list.equals("")){
                return false;
            }
            else{
                return true;
            }
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(Database.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }        
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    boolean GroupMessageLoad(String username, String group){
           try {
            Connection con = null;
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            con = DriverManager.getConnection(DatabaseConfig.CONNECTION_Url,DatabaseConfig.USERNAME,DatabaseConfig.PASSWORD);
            String SQL = "Select * from GroupMessage where GroupName=?";
            PreparedStatement pstm;
            pstm = con.prepareStatement(SQL);
            pstm.setString(1,group);
            ResultSet rs = pstm.executeQuery();
            while(rs.next()){
                //qua client tach chuoi theo @@
                SocketServer.message =SocketServer.message+"@@"+rs.getString(3)+"   :"+rs.getString(4);   
            }
            rs.close();
            pstm.close();
            con.close();
            if(SocketServer.message.equals("")){
                return false;
            }
            else{
                return true;
            }
        } catch (Exception ex) {
            Logger.getLogger(Database.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }   
    }

    boolean sendMessageGroup(String username, String mess, String group){
        try {
            Connection con = null;
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            con = DriverManager.getConnection(DatabaseConfig.CONNECTION_Url,DatabaseConfig.USERNAME,DatabaseConfig.PASSWORD);
            String SQL = "Insert into GroupMessage values(?,?,?)";
            PreparedStatement pstm;
            pstm = con.prepareStatement(SQL);
            pstm.setString(1,group);
            pstm.setString(2,username);
            pstm.setString(3,mess);
            pstm.executeUpdate();
            pstm.close();
            con.close();
            return true;
 
        } catch (Exception ex) {
            Logger.getLogger(Database.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }
    boolean findFriend(String userName) throws SQLException {
        try {
            Connection con = null;
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            con = DriverManager.getConnection(DatabaseConfig.CONNECTION_Url,DatabaseConfig.USERNAME,DatabaseConfig.PASSWORD);
            String SQL = "select * from Fn_findFriend(?)";
            PreparedStatement pstm;
            pstm = con.prepareStatement(SQL);
            pstm.setString(1,userName);
            ResultSet rs = pstm.executeQuery();
            while(rs.next()){
                //qua client tach chuoi theo @@
                SocketServer.message =SocketServer.message+"@@"+rs.getString(1);   
            }
            rs.close();
            pstm.close();
            con.close();
            if(SocketServer.message.equals("")){
                return false;
            }
            else{
                return true;
            }
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(Database.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        } 
    }
    boolean addFriend(String username, String friend) {
        try {
            Connection con = null;
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            con = DriverManager.getConnection(DatabaseConfig.CONNECTION_Url,DatabaseConfig.USERNAME,DatabaseConfig.PASSWORD);
            String SQL = "Insert into Friend values(?,?)";
            PreparedStatement pstm;
            pstm = con.prepareStatement(SQL);
            pstm.setString(1,username);
            pstm.setString(2,friend);
            pstm.executeUpdate();
            pstm.close();
            con.close();
            return true;
 
        } catch (Exception ex) {
            Logger.getLogger(Database.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }        
    }    
    
//Group
    boolean findGroup(String group) throws SQLException {
        try {
            Connection con = null;
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            con = DriverManager.getConnection(DatabaseConfig.CONNECTION_Url,DatabaseConfig.USERNAME,DatabaseConfig.PASSWORD);
            String SQL = "select * from Fn_findGroup(?)";
            PreparedStatement pstm;
            pstm = con.prepareStatement(SQL);
            pstm.setString(1,group);
            ResultSet rs = pstm.executeQuery();
            while(rs.next()){
                //qua client tach chuoi theo @@
                SocketServer.message =SocketServer.message+"@@"+rs.getString(1);   
            }
            rs.close();
            pstm.close();
            con.close();
            if(SocketServer.message.equals("")){
                return false;
            }
            else{
                return true;
            }
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(Database.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        } 
    }

    boolean joinGroup(String username, String group) throws SQLException {
        try {
            Connection con = null;
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            con = DriverManager.getConnection(DatabaseConfig.CONNECTION_Url,DatabaseConfig.USERNAME,DatabaseConfig.PASSWORD);
            String SQL = "Insert into MemberGroup values(?,?)";
            PreparedStatement pstm;
            pstm = con.prepareStatement(SQL);
            pstm.setString(1,group);
            pstm.setString(2,username);
            pstm.executeUpdate();
            pstm.close();
            con.close();
            return true;
 
        } catch (Exception ex) {
            Logger.getLogger(Database.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }

    boolean createGroup(String username, String group) {
        try {
            Connection con = null;
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            con = DriverManager.getConnection(DatabaseConfig.CONNECTION_Url,DatabaseConfig.USERNAME,DatabaseConfig.PASSWORD);
            String SQL = "Insert into Groups values(?)";
            PreparedStatement pstm;
            pstm = con.prepareStatement(SQL);
            pstm.setString(1,group);
            pstm.executeUpdate();
            //insert xong thì join user đó vào group.
            SQL="Insert into MemberGroup values(?,?)";
            pstm = con.prepareStatement(SQL);
            pstm.setString(1,group);
            pstm.setString(2,username);
            pstm.executeUpdate();
            pstm.close();
            con.close();
            return true;
 
        } catch (Exception ex) {
            Logger.getLogger(Database.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }        
    }

    String memberList(String group) throws SQLException {
        String kq="";
           try {
            Connection con = null;
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            con = DriverManager.getConnection(DatabaseConfig.CONNECTION_Url,DatabaseConfig.USERNAME,DatabaseConfig.PASSWORD);
            String SQL = "Select * from MemberGroup where GroupName=?";
            PreparedStatement pstm;
            pstm = con.prepareStatement(SQL);
            pstm.setString(1,group);
            ResultSet rs = pstm.executeQuery();
            while(rs.next()){
                // tach chuoi theo @@
                kq=kq+"@@"+rs.getString(2);   
            }
            rs.close();
            pstm.close();
            con.close();
            if(kq.equals("")){
                return "@@";
            }
            else{
                return kq;
            }
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(Database.class.getName()).log(Level.SEVERE, null, ex);
            return "@@";
        }  
    }
}

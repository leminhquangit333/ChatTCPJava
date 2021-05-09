package Controller;
import Model.Messages;
import static Model.ServerConfig.MAX_User;
import View.ServerFrame;
import java.io.*;
import java.net.*;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
class ServerThread extends Thread { 
	
    public SocketServer server = null;
    public Socket socket = null;
    public int ID = -1;
    public String username = "";
    public String email = "";
    public BufferedReader streamIn  =  null;
    public BufferedWriter streamOut = null;
    public ServerFrame ui;

    public ServerThread(SocketServer _server, Socket _socket){  
    	super();
        server = _server;
        socket = _socket;
        ID     = socket.getPort();
        ui = _server.ui;
    }
    
    public void send(String msg){
        try {
            streamOut.write(msg);
            streamOut.newLine();
            streamOut.flush();
            System.out.println("Outgoing : "+msg);
        } 
        catch (IOException ex) {
            System.out.println("Exception [SocketClient : send(...)]");
        }
    }
    
    public int getID(){  
	    return ID;
    }
   
    @SuppressWarnings("deprecation")
    @Override
	public void run(){  
    	ui.txtServer.append("\nServer Thread " + ID + " running.");
        
        while (true){  
    	    try{  
                String msg = streamIn.readLine();
                String [] ktmsg = msg.split("///");
    	    	server.handle(ID, ktmsg);
                //ktmsg 0: type 1: sender 
            }
            catch(IOException ioe){  
            	System.out.println(ID + " ERROR reading: " + ioe.getMessage());
                server.remove(ID);
                stop();
            }   catch (SQLException ex) {
                    Logger.getLogger(ServerThread.class.getName()).log(Level.SEVERE, null, ex);
                }
        }
    }
    
    public void open() throws IOException {  
        streamOut = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        streamOut.flush();
        streamIn = new BufferedReader(new InputStreamReader(socket.getInputStream()));
    }
    
    public void close() throws IOException {  
    	if (socket != null)    socket.close();
        if (streamIn != null)  streamIn.close();
        if (streamOut != null) streamOut.close();
    }
}

public class SocketServer implements Runnable {
    
    public ServerThread clients[];
    public ServerSocket server = null;
    public Thread       thread = null;
    public int clientCount = 0, port = 20000;
    public ServerFrame ui;
    public Database db;

    public SocketServer(ServerFrame frame){
       
        clients = new ServerThread[50];
        ui = frame;
        db = new Database();
        
	try{  
	    server = new ServerSocket(port);
            port = server.getLocalPort();
	    ui.txtServer.append("Bat Dau Ket Noi Server. IP May Tinh: " + InetAddress.getLocalHost() + ", Port: " + server.getLocalPort());
	    start(); 
        }
	catch(IOException ioe){  
            ui.txtServer.append("Khong The Tim Thay port : " + port + "\nRetrying"); 
            ui.RetryStart(0);
	}
    }
	
    @Override
    public void run(){  
	while (thread != null){  
            try{  
		ui.txtServer.append("\nCho Doi Ket Noi client..."); 
	        addThread(server.accept()); 
	    }
	    catch(IOException ioe){ 
                ui.txtServer.append("\nLoi Ket Noi Server: \n");
                ui.RetryStart(0);
	    }
        }
    }
	
    public void start(){  
    	if (thread == null){  
            thread = new Thread(this); 
	    thread.start();
	}
    }
    
    @SuppressWarnings("deprecation")
    public void stop(){  
        if (thread != null){  
            thread.stop(); 
	    thread = null;
	}
    }
    
    private int findClient(int ID){  
    	for (int i = 0; i < clientCount; i++){
        	if (clients[i].getID() == ID){
                    return i;
                }
	}
	return -1;
    }

    //biến code quên mật khẩu
        int Code;
        public static String nickName="";
        public static String list="";
        public static String message="";
   
    public synchronized void handle(int ID, String[] ktmsg) throws SQLException{ 
        try{
            switch(ktmsg[0]){
    //Systerm            
                case ("login"):{
                    if(findUserThread(ktmsg[1]) == null){
                        if(db.checkLogin(ktmsg[1],ktmsg[2])){
                            clients[findClient(ID)].username = nickName;
                            clients[findClient(ID)].email = ktmsg[1] ;
                            clients[findClient(ID)].send(new Messages("login", "SERVER", "TRUE",nickName).toString());
                        }
                        else{
                            clients[findClient(ID)].send(new Messages("login", "SERVER", "FALSE1",ktmsg[1]).toString());
                        } 
                    }
                    else{
                        clients[findClient(ID)].send(new Messages("login", "SERVER", "FALSE2", ktmsg[1]).toString());
                    }
                    break;
                    }
                case ("logout"):{

                            clients[findClient(ID)].send("logout");
                            remove(ID);
                    break;
                    }
                //
                case ("signUp"):
                    if(ktmsg[1].equals("exit"))
                    {
                        //tat form signUp
                        clients[findClient(ID)].send(new Messages("signUp", "SERVER", "FALSE1",ktmsg[1] ).toString());
                        break;
                    }
                    if(db.addUser(ktmsg[1], ktmsg[2],ktmsg[3],ktmsg[4])){
                           //tendangnhap,pass,nickname,phonenumber

                        clients[findClient(ID)].send(new Messages("signUp", "SERVER", "TRUE",ktmsg[1] ).toString());
                    }
                    else{
                        clients[findClient(ID)].send(new Messages("signUp", "SERVER", "FALSE",ktmsg[1] ).toString());
                    }
                    break;
                 //
                case ("forgotPassword"):
                        if(ktmsg[2].equals("No")){
                             Code=(int) (Math.random()*1000000);
                            if(db.forgotPassWord(ktmsg[1],Code)){
                               //tendangnhap,pass,nickname,phonenumber

                                clients[findClient(ID)].send(new Messages("forgotPassword", "SERVER", "TRUE1",ktmsg[1] ).toString());
                            }
                            else{
                                clients[findClient(ID)].send(new Messages("forgotPassword", "SERVER", "FALSE1",ktmsg[1] ).toString());
                            }
                        }
                        else{
                            if(Integer.valueOf(ktmsg[2])==Code){
                                clients[findClient(ID)].send(new Messages("forgotPassword", "SERVER", "TRUE2",ktmsg[1] ).toString());
                            }
                            else{
                                clients[findClient(ID)].send(new Messages("forgotPassword", "SERVER", "FALSE2",ktmsg[1] ).toString());
                            }
                        }
                          break;
                //
                case ("newPass"):
                        if(db.newPass(ktmsg[1], ktmsg[2])){
                           //tendangnhap,pass,nickname,phonenumber

                            clients[findClient(ID)].send(new Messages("newPass", "SERVER", "TRUE",ktmsg[1] ).toString());
                        }
                        else{
                            clients[findClient(ID)].send(new Messages("newPass", "SERVER", "FALSE",ktmsg[1] ).toString());
                        }
                        break;    
    //friend chat                    
                //Load form friend
                case ("friendFormLoad"):
                    //load list friend
                    System.out.println(nickName+"22222222");
                        if(db.friendFormLoad(clients[findClient(ID)].username)){

                            clients[findClient(ID)].send(new Messages("friendFormLoad", "SERVER", list,clients[findClient(ID)].username).toString());
                            list="";
                        }
                        else{
                            clients[findClient(ID)].send(new Messages("friendFormLoad", "SERVER", "FALSE",ktmsg[1] ).toString());
                        }
                        break; 
                 //load messageFriend
                case ("showMessageFriend"):
                    //load message friend
                    if(ktmsg[2].equals("null")) break;
                        if(db.friendMessageLoad(clients[findClient(ID)].username,ktmsg[2])){
                            //ktmsg[2] friend name

                            clients[findClient(ID)].send(new Messages("showMessageFriend", "SERVER", message,clients[findClient(ID)].username).toString());
                            message="";
                        }
                        else{
                            clients[findClient(ID)].send(new Messages("showMessageFriend", "SERVER", "FALSE",ktmsg[1] ).toString());
                        }
                        break;
                case ("messageFriend"):
                    //load message friend
                    if(ktmsg[3].equals("null")) break;
                        if(db.sendMessageFriend(clients[findClient(ID)].username,ktmsg[2],ktmsg[3])){
                            //ktmsg[3] friend name
                            if(db.friendMessageLoad(clients[findClient(ID)].username,ktmsg[3])){

                               clients[findClient(ID)].send(new Messages("showMessageFriend", "SERVER", message,clients[findClient(ID)].username).toString());
                               if(findUserThread(ktmsg[3])!=null)
                                    findUserThread(ktmsg[3]).send(new Messages("showMessageFriend", "SERVER", message,clients[findClient(ID)].username).toString());
                               message="";
                            }
                        }
                        else{
                            clients[findClient(ID)].send(new Messages("showMessageFriend", "SERVER", "FALSE",clients[findClient(ID)].username ).toString());
                        }
                        break;
                 case ("findFriend"):
                    //load message friend
                     if(ktmsg[2].equals(""))break;
                        if(db.findFriend(ktmsg[2])){
                            //ktmsg[2] group name   
                            clients[findClient(ID)].send(new Messages("findFriend", "SERVER", message,clients[findClient(ID)].username).toString());
                            message="";
                        }
                        else{
                            clients[findClient(ID)].send(new Messages("findFriend", "SERVER", "FALSE",clients[findClient(ID)].username ).toString());
                        }
                    break;   
                 case ("addFriend"):
                    //load message friend
                     if(ktmsg[2].equals(""))break;
                        if(db.addFriend(clients[findClient(ID)].username,ktmsg[2])){
                            //ktmsg[2] friend name   
                            clients[findClient(ID)].send(new Messages("addFriend", "SERVER", "TRUE",clients[findClient(ID)].username).toString());
                        }
                        else{
                            clients[findClient(ID)].send(new Messages("addFriend", "SERVER", "FALSE",clients[findClient(ID)].username ).toString());
                        }
                    break;                 
    //GroupChat
                    //group form load
                case ("groupFormLoad"):
                    //load list friend
                        if(db.groupFormLoad(clients[findClient(ID)].username)){

                            clients[findClient(ID)].send(new Messages("groupFormLoad", "SERVER", list,clients[findClient(ID)].username).toString());
                            list="";
                        }
                        else{
                            clients[findClient(ID)].send(new Messages("groupFormLoad", "SERVER", "FALSE",clients[findClient(ID)].username ).toString());
                        }
                        break;

                    // Group mess
                case ("showMessageGroup"):
                    //load lại form Group
                    if(ktmsg[2].equals("null")) break;
                        if(db.GroupMessageLoad(clients[findClient(ID)].username,ktmsg[2])){
                            //ktmsg[2] Group name

                            clients[findClient(ID)].send(new Messages("showMessageGroup", "SERVER", message,clients[findClient(ID)].username).toString());
                            message="";
                        }
                        else{
                            clients[findClient(ID)].send(new Messages("showMessageGroup", "SERVER", "FALSE",clients[findClient(ID)].username ).toString());
                        }
                         break;
                    //
                case ("messageGroup"):
                    //load message friend
                    if(ktmsg[3].equals("null")) break;
                        if(db.sendMessageGroup(clients[findClient(ID)].username,ktmsg[2],ktmsg[3])){
                            //ktmsg[3] group name
                            if(db.GroupMessageLoad(clients[findClient(ID)].username,ktmsg[3])){

                               clients[findClient(ID)].send(new Messages("showMessageGroup", "SERVER", message,clients[findClient(ID)].username).toString());
                                System.out.println(db.memberList(ktmsg[3]));
                               String[] member = db.memberList(ktmsg[3]).split("@@");
                               if(!member[1].equals("")){
                                    for(int i=1;i<member.length;i++){
                                       if(findUserThread(member[i])!=null)
                                            findUserThread(member[i]).send(new Messages("showMessageGroup", "SERVER", message,member[i]).toString());
                                    }
                               }
                               message="";
                            }
                        }
                        else{
                            clients[findClient(ID)].send(new Messages("showMessageFriend", "SERVER", "FALSE",clients[findClient(ID)].username ).toString());
                        }
                        break;
                case ("findGroup"):
                    //load message friend
                    if(ktmsg[2].equals(""))break;
                        if(db.findGroup(ktmsg[2])){
                            //ktmsg[2] group name   
                            clients[findClient(ID)].send(new Messages("findGroup", "SERVER", message,clients[findClient(ID)].username).toString());
                            message="";
                        }
                        else{
                            clients[findClient(ID)].send(new Messages("findGroup", "SERVER", "FALSE",clients[findClient(ID)].username ).toString());
                        }
                    break;
                case ("joinGroup"):
                    //load message friend
                    if(ktmsg[2].equals(""))break;
                        if(db.joinGroup(ktmsg[1],ktmsg[2])){
                            //ktmsg[1] username ,ktmsg[2] group name   
                            clients[findClient(ID)].send(new Messages("joinGroup", "SERVER", "TRUE",clients[findClient(ID)].username).toString());
                        }
                        else{
                            clients[findClient(ID)].send(new Messages("joinGroup", "SERVER", "FALSE",clients[findClient(ID)].username ).toString());
                        }
                    break;        
                case ("createGroup"):
                        if(db.createGroup(clients[findClient(ID)].username,ktmsg[2])){
                            //ktmsg[2] group name   
                            clients[findClient(ID)].send(new Messages("createGroup", "SERVER","TRUE",clients[findClient(ID)].username).toString());
                        }
                        else{
                            clients[findClient(ID)].send(new Messages("createGroup", "SERVER", "FALSE",clients[findClient(ID)].username ).toString());
                        }
                    break;                
            }
        }
        catch (Exception ex) {
            System.out.println(ex);
        }
    }
    
    public ServerThread findUserThread(String usr){
        for(int i = 0; i < clientCount; i++){
            if(clients[i].username.equals(usr)||clients[i].email.equals(usr)){
                return clients[i];
            }
        }
        return null;
    }
	
    @SuppressWarnings("deprecation")
    public synchronized void remove(int ID){  
    int pos = findClient(ID);
        if (pos >= 0){  
            ServerThread toTerminate = clients[pos];
            ui.txtServer.append("\nXoa client thread " + ID + " at " + pos);
	    if (pos < clientCount-1){
                for (int i = pos+1; i < clientCount; i++){
                    clients[i-1] = clients[i];
	        }
	    }
	    clientCount--;
	    try{  
	      	toTerminate.close(); 
	    }
	    catch(IOException ioe){  
	      	ui.txtServer.append("\nError closing thread: " + ioe); 
	    }
	    toTerminate.stop(); 
	}
    }
    
    private void addThread(Socket socket){  
	if (clientCount <   MAX_User){  
            ui.txtServer.append("\nKet Noi Thanh Cong Client: " + socket);
	    clients[clientCount] = new ServerThread(this, socket);
	    try{  
	      	clients[clientCount].open(); 
	        clients[clientCount].start();  
	        clientCount++; 
	    }
	    catch(IOException ioe){  
	      	ui.txtServer.append("\nError opening thread: " + ioe); 
	    } 
	}
	else{
            ui.txtServer.append("\nClient refused: maximum " + MAX_User + " reached.");
	}
    }
}

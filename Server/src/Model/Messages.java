package Model;

import java.io.Serializable;

public class Messages implements Serializable{
    
    private static final long serialVersionUID = 1L;
    public String type, sender, content, recipient;
    
    public Messages(String type, String sender, String content, String recipient){
        this.type = type; this.sender = sender; this.content = content; this.recipient = recipient;
    }
    
    @Override
    public String toString(){
        return type+"///"+sender+"///"+content+"///"+recipient;
    }
}

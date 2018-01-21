/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pks;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.nio.charset.StandardCharsets;
import javax.swing.JFrame;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.CRC32;

/**
 *
 * @author Mário
 */
public class PKS {

    static final int MAX = 65000;

    public static void main(String[] args) throws SocketException, IOException {

        int port = 1200;
        int offset = 10;
        Begin begin=null;
       // while(true){
        Scanner scan = new Scanner(System.in);
        PrintStream stdout = System.out;
        System.setOut(stdout);
        GUIServer guis=null;
        if(guis!=null) guis.dispose();
        
        
        System.out.println("1 - client\n2 - server");
        if (scan.nextInt() == 1) {                                      //client
            begin = new Begin();
            begin.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            begin.setSize(500, 500);
            begin.setTitle("Program");
            begin.setVisible(true);
            
            
        } else {                                                       //server
            byte[] buffer = new byte[65000];
            DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
            DatagramSocket socket = new DatagramSocket(port);
            guis = new GUIServer();
            guis.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            guis.setSize(800, 500);
            guis.setTitle("Server");
            guis.setVisible(true);

            TextAreaOutputStream out = new TextAreaOutputStream(guis.output1);
            PrintStream outstream = new PrintStream(out);
            System.setOut(outstream);
            String message = null;
            //String part = null;
            byte[] messagebuff=null;
            int recieved=0;
            byte[] Filebuff=null;
            int[] check=null;
            boolean done=true;
            byte resend[]=null;
            byte controlarray[]=null;
            byte[] data=null;
            int counter=0;
            while (!guis.stop) {
                socket.receive(packet);
                counter++;
                System.out.println(counter);
                recieved++;
                Boolean correct=true;
                if(data==null) data=new byte[packet.getLength()];
                    
                    data = packet.getData();
                
                
                
                if (data[0] == 1) {
                    int frag = data[1];
                    int fragcount = data[2];
                    
                    if(frag<0)
                        frag=128+(128+frag);
                    
                    if(fragcount<0)
                        fragcount=128+(128+fragcount);
                    
                    
                    
                    if(messagebuff==null){
                    messagebuff= new byte[((packet.getLength()-offset)*fragcount)];
                }
                    if(check==null) check=new int[fragcount];
                   
                    if(controlarray==null) {controlarray= new byte[packet.getLength()-offset];}
                    System.arraycopy(data, offset, controlarray, 0, packet.getLength()-offset);
                    
                    System.arraycopy(data, offset, messagebuff, frag*(packet.getLength()-offset),(packet.getLength()-offset) );
                    
                    MyCrc crc=new MyCrc();
                    
                   int control =crc.count(controlarray);
                   byte[] CrcArray = new byte[4];   
                   
                   
                CrcArray[0]=(byte)(control >>> 24);
                CrcArray[1]=(byte)(control >>> 16);
                CrcArray[2]=(byte)(control >>> 8);
                CrcArray[3]=(byte)control;
              //  System.out.println(control);
                for(int i=0; i<4; i++){
                    if(!(CrcArray[i]==data[i+3]))
                        correct=false;
                }
                if(correct){
                    check[frag]=1;
                }
                
//       System.out.println(data[1] + " " + (packet.getLength()-offset)*fragcount+ " " + (packet.getLength()-offset)*fragcount );
                    
                    
                    
                
                   
                   
                    //  System.out.println(part);
                    //         socket.receive(packet);
//                System.out.println( new String(packet.getData()) ) ;
                //    socket.send(packet);
                
                
                
                
                if (recieved == fragcount) {
                   // int k=1;
                    for(int j=0; j<fragcount;j++){
                        if(check[j]!=1){
                            done=false;
                            //resend[++k]=(byte)j;
                        }
                    }
                    
                        
                    System.out.println(done);
                     
                  /*  message=new String(messagebuff, "UTF-8");       //
                       
                        System.out.println(message);
                        message = null;
                        recieved=0;
                        messagebuff=null;
                        check=null;
                        controlarray=null;
                        data=null;                               */   //
                        
                        if(!done){
                        data[0]=3;
                        data[1]=(byte)0;
                        
                        socket.send(packet);
                        recieved=0;
                       // while(!done){
                            
                            socket.receive(packet);
                            counter++;
                            System.out.println(counter);
                            data=packet.getData();
                          //  recieved++;
                    //        int n=data[1];
                         //   int total=data[2];
                            
                            System.arraycopy(data, offset, messagebuff, 0,(packet.getLength()-offset) );
                            
                         //   if(recieved==total){
                                message=new String(messagebuff, "UTF-8");
                       
                                System.out.println(message);
                                message = null;
                                recieved=0;
                                messagebuff=null;
                                done=true;
                                controlarray=null;
                                check=null;
                                data=null;
                                recieved=0;
                        //    }
                            
                        
                    }
                    else{
                        
                        data[0]=4;
                        data[1]=0;
                      //  byte[] qwer=new byte[20];
                        
                        message=new String(messagebuff, "UTF-8");
                        socket.send(packet);
                        System.out.println(message);
                        message = null;
                        recieved=0;
                        messagebuff=null;
                        check=null;
                        controlarray=null;
                        data=null;
                        
                       
                    }
                        
                    }
            
                continue;
                }
                if (data[0] == 2) {

                //    DatagramPacket pack = new DatagramPacket(new byte[MAX], 0, MAX);
                
                    
              //  byte[] DataBuff = new byte[packet.getLength() - offset];
                    int frag=data[8];
                    int fragcount=data[9];
                    if(frag<0)
                        frag=128+(128+frag);
                    
                    if(fragcount<0)
                        fragcount=128+(128+fragcount);
                    //System.out.println(fragcount);
                    if(Filebuff==null)
                    Filebuff = new byte[(packet.getLength() - offset)*fragcount +1];
                   // System.out.println(" "+frag +" " + (packet.getLength()-offset)*frag+ " " + (packet.getLength()-offset) );
                    
                    
                   
                   System.arraycopy(data, offset, Filebuff, frag*(packet.getLength()-offset),(packet.getLength()-offset));

                   if(check==null) check=new int[fragcount];
                   
                    if(controlarray==null) {controlarray= new byte[packet.getLength()-offset];}
                    System.arraycopy(data, offset, controlarray, 0, packet.getLength()-offset);
                    
                    MyCrc crc=new MyCrc();
                    
                   int control =crc.count(controlarray);
                   byte[] CrcArray = new byte[4];   
                   
                   CrcArray[0]=(byte)(control >>> 24);
                CrcArray[1]=(byte)(control >>> 16);
                CrcArray[2]=(byte)(control >>> 8);
                CrcArray[3]=(byte)control;
              //  System.out.println(control);
                for(int i=0; i<4; i++){
                    if(!(CrcArray[i]==data[i+4]))
                        done=false;
                }
               
                if(correct){
                    check[frag]=1;
                }
                    
                     if (recieved == fragcount) {
                   // int k=1;
                    for(int j=0; j<fragcount;j++){
                        
                        if(check[j]!=1){
                            done=false;
                            //resend[++k]=(byte)j;
                        }
                    }
                    
                     }       
                if(recieved==fragcount && !done){
                    
                data[0]=3;
                System.out.println(done);
                socket.send(packet);
                socket.receive(packet);
                counter++;
                            System.out.println(counter);
                data=packet.getData();
                System.arraycopy(data, offset, Filebuff, 0,(packet.getLength()-offset));
                done=true;
                }
                    
                    //socket.send(packet);
                    if(recieved==fragcount){
                        
                        System.out.println(done);
                        byte[] extenbuf=new byte[3];
                        System.arraycopy(data, 1, extenbuf, 0, 3);
                        String extension=new String(extenbuf, "UTF-8");
                        data[0]=4;
                        try {
                        Thread.sleep(2000);
                    } catch (InterruptedException ex) {
                        Logger.getLogger(PKS.class.getName()).log(Level.SEVERE, null, ex);
                    }
                        socket.send(packet);
                   
                   System.out.println(extension);
                   FileOutputStream fos = new FileOutputStream(new File("File."+extension), true);
                        recieved=0;
                    
                        fos.write(Filebuff);
                        fos.close();
                        Filebuff=null;
                        check=null;
                        controlarray=null;
                    }

                }

            }
            
            }
    }
    public static void callclient(String string) throws SocketException, IOException {

        GUI gui = new GUI();
        gui.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        gui.setSize(500, 500);
        gui.setTitle("Program");
        gui.setVisible(true);
        gui.setIP(string);
        if (string == "") {
            gui.setIP("localhost");

        }

    }
}

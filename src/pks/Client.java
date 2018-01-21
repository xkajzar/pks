/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pks;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.UUID;
import java.util.zip.CRC32;
/**
 *
 * @author Mário
 */
public class Client {
    String uuid = UUID.randomUUID().toString();	
	int counter=0;
	int port = 1200;
	int offset = 10;
	int a = 0;
        int b=0;
	String IP ;
	
	DatagramSocket socket = null;
	DatagramPacket packet;
	 
			
	public void sendtext(String msg, int frag, boolean mistake)  throws Exception{
		
		a=0;
                b=0;
		int fragcount=0;
                int fragment=0;
		InetAddress host = InetAddress.getByName( IP ) ;
		
		
		
		byte[] sendBuf = msg.getBytes("UTF-8");

		byte[] Buff= new byte[frag+offset];
		
		while(b<sendBuf.length){
                for(int c=0; c<offset; c++){
			Buff[c] = 0;	
		}
		if(sendBuf.length%frag==0)
                    fragcount=sendBuf.length/frag;
                else
                    fragcount=sendBuf.length/frag +1;
		Buff[0]=1;
		Buff[1]=(byte)fragment;
                Buff[2]=(byte)fragcount;
		
		
		for(a=0; a<frag && b<sendBuf.length; a++){
			Buff[a+offset] = sendBuf[b];
                        b++;
		}
                if(a<frag){
                    for(;a<frag; a++){
                        Buff[a+offset]=0;
                    }
                }
                byte[] controlarray= new byte[Buff.length-offset];
                
                System.arraycopy(Buff, offset, controlarray, 0, Buff.length-offset);
                
             /*   CRC32 crc32=new CRC32();
                crc32.update(controlarray);*/
		MyCrc crc= new MyCrc();
                int control=crc.count(controlarray);
		Buff[3]=(byte)(control >>> 24);
                Buff[4]=(byte)(control >>> 16);
                Buff[5]=(byte)(control >>> 8);
                Buff[6]=(byte)control;
		/*System.out.println(control);
                System.out.println("OTHER "+crc32.getValue());*/
                try{
	
		socket = new DatagramSocket();
		packet = new DatagramPacket(Buff, 0, Buff.length, host, port);
	
	if(mistake==true){
            Buff[10]=0;
            Buff[11]=0;
            Buff[12]=0;
            mistake=false;
        }	 
        socket.send( packet ) ;
        counter++;
                            System.out.println(counter);
        socket.setSoTimeout( 2000 ) ;

       // packet.setData( new byte[Buff.length+offset]) ; 
       
      //  socket.receive( packet ) ;

       // System.out.println( new String(packet.getData()) ) ;
        fragment++;
		}
		catch( Exception e )
	      {
	         System.out.println( e ) ;
	      }
	      finally
	      {
	         /*if( socket != null )
	            socket.close();*/
	      }
	
	
	}
                boolean done=false;
                Thread.sleep(500);
                socket.receive(packet);
                //while(!done){
                
                
                byte[] repair= new byte[packet.getLength()];
                repair=packet.getData();
                //int limit=repair[1];
                
                if(repair[0]==3){
                int i=2;
               // while(i<limit){
                    Buff[0]=4;
                    Buff[1]=repair[i];
                    
                    packet = new DatagramPacket(Buff, 0, Buff.length, host, port);
                    System.arraycopy(sendBuf, 0, Buff, offset, frag);
                    socket.send(packet);
                    counter++;
                            System.out.println(counter);
                    
              //  }   
                }
                if(repair[0]==4){
                    done=true;
                }
               // }
        }
	public void sendfile(String path, int Packetsize, String type, boolean mistake) throws Exception{
	
		
		File file = new File(path);
		InetAddress host = InetAddress.getByName( IP ) ;
	
		FileInputStream fis = new FileInputStream(file);
		DataInputStream dis = new DataInputStream(fis);
		
		
		FileOutputStream fos = new FileOutputStream(uuid , true);
		
		
		int count = 0;
		int fragcount= 0;
		int total;
		long filesize = file.length();
		
		if((int)filesize%Packetsize==0){
                   total=(int)filesize/Packetsize;
                }
                else total=(int)filesize/Packetsize+1;
		byte[] sendBuf = new byte[10000000];
		dis.read(sendBuf);
		
		
		
		while(count<=filesize){
                    
		byte[] Buff= new byte[Packetsize];
		byte[] Back = new byte[Packetsize-offset];
		
		
		
		for(a=0; a<offset; a++){
			Buff[a] = 0;	
		}
		
		Buff[0]=2;
		Buff[8]=(byte) fragcount;
		Buff[9]=(byte) total;
		
		byte[] exten = new byte[3];
		exten = type.getBytes("UTF-8");
		
		for(a=0; a<exten.length; a++){
			
			Buff[a+1]=exten[a];
		}
		
		
		
		for(a=0; a<Buff.length-offset; a++){
			Buff[a+offset] = sendBuf[count];
			count++;
		}
		
		
		byte[] controlarray= new byte[Buff.length-offset];
                
                System.arraycopy(Buff, offset, controlarray, 0, Buff.length-offset);
		MyCrc crc= new MyCrc();
                int control=crc.count(controlarray);
		Buff[4]=(byte)(control >>> 24);
                Buff[5]=(byte)(control >>> 16);
                Buff[6]=(byte)(control >>> 8);
                Buff[7]=(byte)control;
		
		
		if(mistake){
                    Buff[10]=0;
                    Buff[11]=0;
                    Buff[12]=0;
                    mistake=false;
                }
		try{
	
		socket = new DatagramSocket();
		packet = new DatagramPacket(Buff, 0, Buff.length, host, port);
	
		Thread.sleep(200);
        socket.send( packet ) ;
        counter++;
                            System.out.println(counter);
        fragcount++;
        
        if(fragcount==total){
            
                socket.receive(packet);
                
                
                Buff=packet.getData();
                System.out.println(Buff[0]);
                if(Buff[0]==3){
                System.arraycopy(sendBuf, 0, Buff, offset, packet.getLength()-offset);
                socket.send(packet);
                counter++;
                            System.out.println(counter);
                }
        }
        //socket.setSoTimeout( 2000 ) ;

       
      //  packet.setData( new byte[Packetsize], 0, Packetsize) ;

      
       //socket.receive( packet ) ;
    
       /* byte[] Back1= packet.getData();
       
       for(a=0; a<Packetsize-offset; a++){				//test
   		Back[a]=Back1[a+offset];
   		}
   		fos.write(Back);*/
       
       // Print the response
        //System.out.println( new String(packet.getData()) ) ;
	
		}
		
		catch( Exception e )
	      {
	         System.out.println( e ) ;
	      }
	      finally
	      {
	         
	      }
	
	
	}
                
                
		fos.close();
		}
		
	public void setIP(String p){
		this.IP=p;
	}
	public void keepalive(){
		DatagramSocket socket = null;
		DatagramPacket packet;
		
	}
	
}

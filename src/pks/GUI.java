package pks;


import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.*;
import java.util.*;

import javax.swing.*;




public class GUI extends JFrame{

	
public Client client1 = new Client();

String IP;

JButton button1;
JTextField text1;

JButton button2;
JTextField text2;
JTextField text3;
JTextField text4;
JCheckBox mistake;

public GUI() throws SocketException, IOException{
	
	setLayout(new FlowLayout());
	
	
	
	
	text1 = new JTextField(20);
	add(text1);
	
	button1 = new JButton("Poslat");
	add(button1);
	
	text2 = new JTextField(20);
	add(text2);
	
	button2 = new JButton("Subor");
	add(button2);
	
	text3 = new JTextField(5);
	add(text3);
	
	text4 = new JTextField(10);
	add(text4);
        mistake=new JCheckBox();
        add(mistake);
	
	button1.addActionListener(new action1());
	button2.addActionListener(new action2());
}



public void setIP(String string){
	this.IP = string;
	client1.setIP(IP);
}


public class action1 implements ActionListener{
	public void actionPerformed(ActionEvent b) {
		try {
			client1.sendtext(text1.getText(), Integer.parseInt(text3.getText()), mistake.isSelected());
			text1.setText(null);
			
		} catch (Exception e) {
			
			e.printStackTrace();
		}
	

}}
public class action2 implements ActionListener{
	public void actionPerformed(ActionEvent b) {
		try {
			client1.sendfile(text2.getText(),Integer.parseInt(text3.getText()), text4.getText(), mistake.isSelected());
			text2.setText(null);
			text3.setText(null);
			text4.setText(null);
		} catch (Exception e) {
			
			e.printStackTrace();
		}
	

}}
/*public void keepalive() throws Exception{
	client1.sendtext("keepalive");
}*/
}

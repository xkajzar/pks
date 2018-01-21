package pks;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.*;
import java.util.*;

import javax.swing.*;




public class Begin extends JFrame{

PKS main = new PKS();
JButton button1;
JTextField text1;
JLabel label1;
JButton button2;


public Begin(){
	
	setLayout(new FlowLayout());
	
	label1 = new JLabel();
	label1.setText("IP address for client");
	add(label1);
	
	text1 = new JTextField(20);
	add(text1);
	
	
	
	button1 = new JButton("IP");
	add(button1);
	
	
	
	
	
	button1.addActionListener(new action1());
	
}





public class action1 implements ActionListener{
	public void actionPerformed(ActionEvent b) {
		try {
			main.callclient(text1.getText());
			
			
		} catch (Exception e) {
			
			e.printStackTrace();
		}
	

}}

	


/*public void keepalivve(){
//	main.Callserver();
}*/
}

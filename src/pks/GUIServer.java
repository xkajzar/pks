package pks;



import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.*;
import java.util.*;

import javax.swing.*;


public class GUIServer extends JFrame{
	

	

boolean stop=false;
 	
	JTextArea output1;
        JButton button1;

	

	public GUIServer() throws SocketException, IOException{
        
		
		setLayout(new FlowLayout());
		
		output1 = new JTextArea(20,50);
		output1.setEditable(false);
		add(output1);
		
		JScrollPane scroll = new JScrollPane (output1, 
				JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);

		add(scroll);
		
              /* button1 = new JButton("Stop");
                add(button1);
              button1.addActionListener((ActionEvent e) -> {
                  stop=true;
                  
                  
                });*/
                
	}


public void getStream(String output) {
	
	output1.setText(output);
}


public JTextArea getOutputArea(){
    return output1;
}

	}



package com.balai.letstalk;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;



public class Login extends Activity 
{

	EditText FirstValue, FirstValue1;
	TextView Result;
	boolean flag;
    BufferedReader in;
    PrintWriter out;
    private EditText et1,et2;
    private boolean connected = false;
    Socket socket;
   
    
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.activity_login);
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.tittle_bar);
		
		et1 = (EditText) findViewById(R.id.Edit01);
	    et2 = (EditText) findViewById(R.id.Edit02);


	   
	    try{
	    	 String serverIpAddress = "10.32.10.236";
         	 int port = 9001;
         	 socket = new Socket();
             socket.connect(new InetSocketAddress(serverIpAddress, port), 10000);
             connected = true;
	    } catch (IOException ec)
	    { 
	    	 connected = false;
  	    	 Result = (TextView) findViewById(R.id.Text011);
  	         Result.append("Connection failed...\n" ); 
  	         Button btn123 = (Button) findViewById(R.id.signin_button);
  	         btn123.setEnabled(false);
  	    }
	    if(connected) 
	    {
	    	 // set listeners
		     et1.addTextChangedListener(mTextWatcher);
		     et2.addTextChangedListener(mTextWatcher);

		    // run once to disable if empty
		     checkFieldsForEmptyValues(); 
		    	    
             Thread cThread = new Thread(new ClientThread());
             cThread.start();
	    }

	}
	
 	 
	    public class ClientThread implements Runnable
	    {
	 
	        public void run()
	        {
	             try {
	            	 
	                if(connected)
	                {
	                 	Result = (TextView) findViewById(R.id.Text011);
	     	   	        Result.append("Succesfully Connected to Server...\n SignIn now.." ); 
	                    try {
	                       in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
	                       out = new PrintWriter(socket.getOutputStream(), true);
	                         
	                    } catch (Exception e) {
	                       Log.e("ClientActivity", "S: Error", e);
	                    }
	                 }
	            	        	 
	                // Process all messages from server, according to the protocol.
	    	        while (connected) 
	    	        {
	    	        	
	   	   	            
	    	            final String line = in.readLine();

			        	Button btn1 = (Button) findViewById(R.id.signin_button);
		     		    btn1.setOnClickListener(new OnClickListener() {
		     		       @Override
		     		       public void onClick(View v) 
		     		       {  
		     		          if (line.startsWith("SUBMITNAME"))
		     		          {	
			    	           	 FirstValue = (EditText) findViewById(R.id.Edit01);
			    		       	 final String message = FirstValue.getText().toString();
			    		       	 out.println(message);
			    		        	 
		     		           }	
		     		        }
		     		    });
		     		  
		     		    if (line.startsWith("PASSWORD"))
		     		    {
	    	            	
	    	            	FirstValue1 = (EditText) findViewById(R.id.Edit02);
    			        	final String message1 = FirstValue1.getText().toString();
    			        	out.println(message1); 
    			        	
	    	            } else if (line.startsWith("NAMEACCEPTED"))
	    	            {
	    	            	 break;
	    	            	
	    	            } 
	    	        }
	    	    
	    	        socket.close();
	    	       
	    	        Intent intent = new Intent(Login.this, ChatRoom.class);
	    	        
	    	        FirstValue = (EditText) findViewById(R.id.Edit01);
		            final String message = FirstValue.getText().toString();
	    	        
		            FirstValue1 = (EditText) findViewById(R.id.Edit02);
		        	final String message1 = FirstValue1.getText().toString();
		        	
		            Bundle extras = new Bundle();
	    	        extras.putString("EXTRA_MESSAGE",message);
	    	        extras.putString("EXTRA_MESSAGE1",message1);
	    	        intent.putExtras(extras);
                    startActivity(intent); 
	    	      
                    finish();
                    
	            } catch (IOException ec)
	    	    { 
	   	    	 Result = (TextView) findViewById(R.id.Text011);
	   	         Result.append("Connection failed...\n" ); 
	   	         Button btn123 = (Button) findViewById(R.id.signin_button);
	   	         btn123.setEnabled(false);
	   	        }
	        }
	    }	
	
  
	    //  create a textWatcher member  
	    private TextWatcher mTextWatcher = new TextWatcher()
	    {
	    	@Override
	    	public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3){
	    	}

	    	@Override
	    	public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
	    	}

	    	@Override
	    	public void afterTextChanged(Editable editable) {
	    		// check Fields For Empty Values
	    		checkFieldsForEmptyValues();
	    	}
	    };

	    void checkFieldsForEmptyValues()
	    {
	    	Button b = (Button) findViewById(R.id.signin_button);
 
	    	String s1 = et1.getText().toString();
	    	String s2 = et2.getText().toString();

	    	if(s1.equals("")|| s2.equals(""))
	    	{
	    		b.setEnabled(false);
	    	}
	    	else
	    	{
	    		b.setEnabled(true);
	    	}
	    }

	
	    @Override
	    public boolean onCreateOptionsMenu(Menu menu)
	    {
	    	// Inflate the menu; this adds items to the action bar if it is present.
	    	getMenuInflater().inflate(R.menu.login, menu);
	    	return true;
	    }

	
 	    public void Exit(View view)
 	    {
 	    	// TODO Auto-generated method stub
 	    	finish();
 	    	System.exit(0);
 	    }
	
		
 	    public void signUp(View view)
 	    { 
 	    	setContentView(R.layout.activity_signup);
 	    	getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.tittle_bar); 		
 	    }
 
	
	
 	    @Override
 	    public void onConfigurationChanged(final Configuration newConfig)
 	    {
 	    	super.onConfigurationChanged(newConfig);
 	    	//Do nothing here
 	    }

}
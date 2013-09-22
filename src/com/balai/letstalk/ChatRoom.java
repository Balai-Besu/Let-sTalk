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
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

public class ChatRoom extends Activity {

	EditText FirstValue ;
	TextView Result;
    BufferedReader in;
    PrintWriter out;
    private EditText et1;
    private boolean connected = false;
    String message1;
    String message;
    Socket socket;
  
    
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.activity_chatroom);
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.mytitle);
		
		
		Bundle extras = getIntent().getExtras();
		message = extras.getString("EXTRA_MESSAGE");
		message1 = extras.getString("EXTRA_MESSAGE1");
		
		Result = (TextView) findViewById(R.id.Text01);
		
		et1 = (EditText) findViewById(R.id.Edit012);
	  
	    // set listeners
	    et1.addTextChangedListener(mTextWatcher);

	    // run once to disable if empty
	    checkFieldsForEmptyValues(); 
	
		
	    try {          
          	 String serverIpAddress = "10.80.114.41";
          	 int port = 9001;
          	 socket = new Socket();
             socket.connect(new InetSocketAddress(serverIpAddress, port), 10000);
             connected = true;
             if(connected)
             { 
                try {
               	  in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                  out = new PrintWriter(socket.getOutputStream(), true);
                    
                } catch (Exception e) {
                    Log.e("ClientActivity", "S: Error", e);
                }
             }
            
             while (true) 
	         {
	            final String line = in.readLine();     	  
     	
	            if (line.startsWith("SUBMITNAME"))
		      	{	            	  
		           out.println(message);  	 
		        } else if (line.startsWith("PASSWORD"))
		        {     
	               out.println(message1); 
     		    } else if (line.startsWith("NAMEACCEPTED"))
	            {
     		       addChatLine("Welcome To Let's Chat ...");  
	               break;
	            } 
	         }  
            
        } catch (IOException ec)
	    { 
        	addChatLine("Connection failed..." ); 
	        
	    }	    
	    //    addChatLine("Testing");  
	    Thread mThread = new Thread(new MessageThread());
        mThread.start();	    
	}
		
    public class MessageThread implements Runnable
    {
       public void run()
       {
          try {       	 
	             while (true) 
	             {
	               	 Button btn1 = (Button) findViewById(R.id.send);
	     		     btn1.setOnClickListener(new OnClickListener() {
		               @Override
		   	           public void onClick(View v) 
	  	 	           {
		               	  FirstValue = (EditText) findViewById(R.id.Edit012);
		              	  final String chatter = FirstValue.getText().toString();
		              	  FirstValue.setText("");
		                  out.println(chatter);
		               }
		             });
		     		    
		     	     ImageButton  btn12 = (ImageButton) findViewById(R.id.logout);
		             btn12.setOnClickListener(new OnClickListener() {

		     		   @Override
		     		   public void onClick(View v) 
		     		   {
		     		    	 out.println("EXIT"); 
		     		    }
		     		 }); 
		     		    
		     		 ImageButton  btn11 = (ImageButton) findViewById(R.id.online);
		     	     btn11.setOnClickListener(new OnClickListener() {

		                @Override
		     	        public void onClick(View v) 
		   		        {
		   		        	 out.println("ONLINE"); 
	    		        }
	     		    }); 
		     		    
	                	
	    	           final String line = in.readLine();     	  
		     	
	    	            if (line.startsWith("SUBMITNAME"))
     		        	{
	    	            	  
	    		          	 out.println(message);
	    		        	 
     		        	} else if (line.startsWith("PASSWORD"))
		     		    {
	    	                  
	    	                 out.println(message1); 
	    	      
		     		    } else if (line.startsWith("NAMEACCEPTED"))
	    	            {
		     		    	addChatLine("Welcome To Let's Chat ...");  
	    	            	
	    	            } else if (line.startsWith("ONLINE")) {
	    	            	 
	    	            	runOnUiThread(new Runnable() {
	    	            	     public void run() {
	    	            	      addChatLine(line.substring(7)); 
	    	            	    }
	    	            	});
	    	            	
	    	            }  else if (line.startsWith("MESSAGE")) {
	    	            	 
	    	            	runOnUiThread(new Runnable() {
	    	            	     public void run() {
	    	            	   addChatLine(line.substring(8)); 
	    	            	    }
	    	            	});
	    	            	//addChatLine("TEST_MESSAGE");  
	    	            } else if (line.startsWith("EXIT"))
	    	            {
	    	            	break;
	    	            }
		     	 	   
	    	        }
	               
	                socket.close();
		    	       
	    	        Intent intent = new Intent(ChatRoom.this, Login.class);
                    startActivity(intent); 
                    finish();
	    	    
	                
	          } catch (IOException ec)
	          { 
	        	  addChatLine("Connection failed..." ); 
	   	        
	          }
	       }
	   }	
	  
	  
	
	
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
	    	Button b = (Button) findViewById(R.id.send);

	    	String s1 = et1.getText().toString();
	    	 
	    	if(s1.equals(""))
	    	{
	    		b.setEnabled(false);
	    	}
	    	else
	    	{
	    		b.setEnabled(true);
	    	}
	    }
	  
	  
	  
	 
	  

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.chat_room, menu);
		return true;
	}
	
	public void addChatLine(String line1) 
	{
        Result.append("\n    " + line1);
        Result.setMovementMethod(new ScrollingMovementMethod());
        
    }
	
	 @Override
	 public void onConfigurationChanged(final Configuration newConfig)
	 {
	   	super.onConfigurationChanged(newConfig);
	   	//Do nothing here
	 }

	
}

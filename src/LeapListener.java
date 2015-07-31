package com.mtbs3d.minecrift.provider;

import com.leapmotion.leap.Listener;
import com.leapmotion.leap.Gesture;
import com.leapmotion.leap.Controller;


public class LeapListener extends Listener {

      private Controller controller; 
	
  
      public LeapListener() {
	this.controller = new Controller(this);
	controller.enableGesture(Gesture.Type.TYPE_CIRCLE, true);
      }
      
      public void onConnect(Controller controller) {
	System.out.println("############################");
	System.out.println("############################");
	System.out.println("############################");
	System.out.println("############################");
	System.out.println("LeapListener::onConnect => The leap has connected!");
	System.out.println("############################");
	System.out.println("############################");
	System.out.println("############################");
	System.out.println("############################");
      }
  
      
      public void onDisconnect(Controller controller) {
	System.out.println("############################");
	System.out.println("############################");
	System.out.println("############################");
	System.out.println("############################");
	System.out.println("LeapListener::onDisConnect => The leap has disconnected!");
	System.out.println("############################");
	System.out.println("############################");
	System.out.println("############################");
	System.out.println("############################");
      }
	
  }
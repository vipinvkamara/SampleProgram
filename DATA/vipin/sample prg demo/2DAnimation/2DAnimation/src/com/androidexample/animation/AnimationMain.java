package com.androidexample.animation;

import com.androidexample.animation.R;
import android.os.Bundle;
import android.app.Activity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class AnimationMain extends Activity {

	
	AnimationView anim_view;
	AnimationView anim_view1;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.animation_main);
        
        // Get AnimationView reference see animation_main.xml
        anim_view = (AnimationView) this.findViewById(R.id.anim_view);
        anim_view.loadAnimation("spark", 18,0,0);
        
        // Get AnimationView reference see animation_main.xml
        anim_view1 = (AnimationView) this.findViewById(R.id.anim_view1);
        
        // You can call this method inside thread
        anim_view1.loadAnimation("spark", 18,0,0);
      
        Button play_button = (Button) this.findViewById(R.id.play_button);
        
        
        play_button.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				
				//Play Both Animation
				anim_view.playAnimation();
		        anim_view1.playAnimation();
				 
			}
		}); 
        
    }
    
    @Override
    protected void onDestroy() {
    	
        super.onDestroy();
        
        // After activity close animation will close
        anim_view=null;
        anim_view1=null;
        
    }
    
}
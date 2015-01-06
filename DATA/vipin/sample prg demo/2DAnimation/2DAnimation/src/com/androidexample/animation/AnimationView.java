/**
 * 
 */
package com.androidexample.animation;

import java.util.ArrayList;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.ImageView;


public class AnimationView extends ImageView
{
	private static final String TAG = "AnimationView";
	private Context mContext = null;
	
	private static final int DELAY = 100; //delay between frames in milliseconds
	private  int drawX = 0;
	private  int drawY = 0;
	
	private boolean mIsPlaying = false;
	private boolean mStartPlaying = false;
	
	private ArrayList<Bitmap> mBitmapList = new ArrayList<Bitmap>();
	
	private int play_frame = 0;
    private long last_tick = 0;
    
    public AnimationView(Context context, AttributeSet attrs)
    {
    	super(context, attrs);
    	mContext = context;
    }
    
    @Override
    protected void onDraw(Canvas c)
    {
    	/******* onDraw method called first time and when postInvalidate() called *****/
        
    	//Log.d(TAG, "onDraw called");
        if (mStartPlaying)
        {
            Log.d(TAG, "starting animation...");
            play_frame = 0;
            mStartPlaying = false;
            mIsPlaying = true;
            
            // Again call onDraw method
            postInvalidate();
        }
        else if (mIsPlaying)
        {
            
            if (play_frame >= mBitmapList.size())
            {
                //mIsPlaying = false;
                play_frame=0;
                
                // Again call onDraw method
                postInvalidate();
            }
            else
            {
                long time = (System.currentTimeMillis() - last_tick);
                
                int draw_x = drawX;
                int draw_y = drawY;
                if (time >= DELAY) //the delay time has passed. set next frame
                {
                    last_tick = System.currentTimeMillis();
                    c.drawBitmap(mBitmapList.get(play_frame), draw_x, draw_y, null);
                    play_frame++;
                    
                    // Again call onDraw method
                    postInvalidate();
                }
                else //still within delay.  redraw current frame
                {
                    c.drawBitmap(mBitmapList.get(play_frame), draw_x, draw_y, null);
                    
                    // Again call onDraw method
                    postInvalidate();
                }
            }
        }
    }
    
    /*ideally this should be in a background thread*/
    public void loadAnimation(String prefix, int nframes,int drawx,int drawy)
    {
    	drawX = drawx;
    	drawY = drawy;
    	mBitmapList.clear();
    	
        for (int x = 0; x < nframes; x++)
        {
        	String name = prefix + "" + x;  // prefix = "spark" see loadAnimation call
        	
        	//Log.d(TAG, "loading animation frame: " + name);
        	
        	// Set Bitmap image
        	int res_id = mContext.getResources().getIdentifier(name, "drawable", mContext.getPackageName());
        	BitmapDrawable d = (BitmapDrawable) mContext.getResources().getDrawable(res_id);
        	mBitmapList.add(d.getBitmap());
        }
    }
    
    public void playAnimation()
    {
    	mStartPlaying = true;
    	
    	// Again call onDraw method
    	postInvalidate();
    }
}

package com.dios.y2onlineshop.screen;

import net.rim.device.api.system.Display;
import net.rim.device.api.system.EncodedImage;
import net.rim.device.api.system.GIFEncodedImage;
import net.rim.device.api.ui.Color;
import net.rim.device.api.ui.Graphics;
import net.rim.device.api.ui.MenuItem;
import net.rim.device.api.ui.UiApplication;
import net.rim.device.api.ui.component.GaugeField;
import net.rim.device.api.ui.decor.Background;
import net.rim.device.api.ui.decor.BackgroundFactory;

public class LoadingScreen extends GeneralScreen {
	private GIFEncodedImage _image;
    private int _currentFrame;
    private int _width, _height, _xPos, _yPos;
    private AnimatorThread _animatorThread;
    private boolean isAnimating;
    private Background oldBackground;
    private GaugeField gaugeField;
    
	public LoadingScreen(){
		super();
		
		initMenu();
	}
	
	public void initMenu(){
		addMenuItem(menuItem);
		addMenuItem(searchRetailItem);
		addMenuItem(searchGrosirItem);
	}
	
	protected void paint(Graphics graphics) {
    	super.paint(graphics);
    		// Draw the animation frame.
    	if(isAnimating)
    	{
    		graphics
    		  .drawImage(_xPos, _yPos, _image
    		    .getFrameWidth(_currentFrame), _image
    		      .getFrameHeight(_currentFrame), _image,
    		    _currentFrame, 0, 0);
    	}
    }


    private class AnimatorThread extends Thread {
    	private LoadingScreen _theField;
    	private boolean _keepGoing = true;
    	private int _totalFrames, _loopCount, _totalLoops;
    	public AnimatorThread(LoadingScreen _theScreen) {
    		_theField = _theScreen;
    		_totalFrames = _image.getFrameCount();
    		_totalLoops = _image.getIterations();

    	}

    	public synchronized void stop() {
    		_keepGoing = false;
    	}

    	public void run() {
    		while (_keepGoing) {
    			// Invalidate the field so that it is redrawn.
    			UiApplication.getUiApplication().invokeAndWait(
    			  new Runnable() {
    				public void run() {
    					_theField.invalidate();
    					if(gaugeField!=null)_theField.add(gaugeField);
    				}
    			});
    			try {
    			  // Sleep for the current frame delay before
    			  // the next frame is drawn.
    			  sleep(_image.getFrameDelay(_currentFrame) * 10);
    			} catch (InterruptedException iex) {
    			} // Couldn't sleep.
    			// Increment the frame.
    			++_currentFrame;
    			if (_currentFrame == _totalFrames) {
    			  // Reset back to frame 0 
    			  // if we have reached the end.
    			  _currentFrame = 0;
    			  ++_loopCount;
    			  // Check if the animation should continue.
    			  if (_loopCount == _totalLoops) {
    			    _keepGoing = false;
    			  }
    			}
    		}
    	}

    }
    public void showLoading() {
    	final LoadingScreen loadingScreen = this;
    	UiApplication.getUiApplication().invokeLater(new Runnable() {

			public void run() {
            	oldBackground = getBackground();
            	isAnimating=true;
        //    	setBackground(BackgroundFactory.createSolidTransparentBackground(Color.WHITE, 100));
            	setBackground(BackgroundFactory.createSolidTransparentBackground(Color.WHITE, 10));
            	EncodedImage encImg =  GIFEncodedImage.getEncodedImageResource("img/loading.gif");
            	GIFEncodedImage img = (GIFEncodedImage) encImg;
        
            	// Store the image and it's dimensions.
            	_image = img;
            	_width = img.getWidth();
            	_height = img.getHeight();
            	_xPos = (Display.getWidth() - _width) >> 1;
            	_yPos = (Display.getHeight() - _height) >> 1;
            	// Start the animation thread.
            	_animatorThread = new AnimatorThread(loadingScreen);
            	_animatorThread.start();
			}
		});
    }
    
    
    public void showLoading(GaugeField gauge) {
    	System.out.println("@@@@@@@@@@@ gauge : " + gauge.getState());
    	gaugeField = gauge;
    	oldBackground = getBackground();
    	isAnimating=true;
//    	setBackground(BackgroundFactory.createSolidTransparentBackground(Color.WHITE, 100));
    	setBackground(BackgroundFactory.createSolidTransparentBackground(Color.WHITE, 10));
    	EncodedImage encImg =  GIFEncodedImage.getEncodedImageResource("img/loading.gif");
    	GIFEncodedImage img = (GIFEncodedImage) encImg;

    	// Store the image and it's dimensions.
    	_image = img;
    	_width = img.getWidth();
    	_height = img.getHeight();
    	_xPos = (Display.getWidth() - _width) >> 1;
    	_yPos = (Display.getHeight() - _height) >> 1;
    	// Start the animation thread.
    	_animatorThread = new AnimatorThread(this);
    	_animatorThread.start();
    }
    
    public void hideLoading() {
    	UiApplication.getUiApplication().invokeLater(new Runnable() {

			public void run() {
            	isAnimating=false;
            	if(_animatorThread != null){
            		_animatorThread.stop();
            	}    	
            	setBackground(oldBackground);
			}
    	});
    }

	public boolean isAnimating() {
		return isAnimating;
	}
    
	private MenuItem menuItem = new MenuItem("Menu", 110, 10)
	{
	   public void run() 
	   {
		   UiApplication.getUiApplication().pushScreen(new UserMenuScreen());
	   }
	};
	
	private MenuItem searchRetailItem = new MenuItem("Pencarian Retail", 110, 10)
	{
	   public void run() 
	   {
		   UiApplication.getUiApplication().pushScreen(new SearchProductScreen(true));
	   }
	};
	
	private MenuItem searchGrosirItem = new MenuItem("Pencarian Grosir", 110, 10)
	{
	   public void run() 
	   {
		   UiApplication.getUiApplication().pushScreen(new SearchProductScreen(false));
	   }
	};
	
}

package com.dios.y2onlineshop.utils;

import net.rim.device.api.system.Bitmap;
import net.rim.device.api.system.Display;

public class DisplayHelper {
	
	public static float intededWidth=480;
	public static float intededHeight=320;
	public static float GetWidthScale()
	{
		return Display.getWidth()/intededWidth;
	}
	public static float GetHeightScale()
	{
		return GetWidthScale();
		//return Display.getHeight()/intededHeight;
	}
	public static Bitmap CreateEmptyTransparentBitmap(int width,int height)
	{
		Bitmap ret = new Bitmap(width, height);
		ret.createAlpha(Bitmap.ALPHA_BITDEPTH_8BPP);
		
		int[] stripe=new int[width]; for (int n=width-1;n>=0;n--) stripe[n]=0;
		for (int y=height-1;y>=0;y--) ret.setARGB(stripe,0,width,0,y,width,1);
		
		return ret;
	}
	
	public static Bitmap CreateScaledCopy(Bitmap source,int width,int height)
	{	
		int original_width = source.getWidth();
	    int original_height = source.getHeight();
	    int bound_width = width;
	    int bound_height = height;
	    int newWidth = original_width;
	    int newHeight = original_height;
	    
	    // first check if we need to scale width
	    if (original_width > bound_width) {
	        //scale width to fit
	        newWidth = bound_width;
	        //scale height to maintain aspect ratio
	        newHeight = (newWidth * original_height) / original_width;
	    }

	    // then check if we need to scale even with the new height
	    if (newHeight > bound_height) {
	        //scale height to fit instead
	        newHeight = bound_height;
	        //scale width to maintain aspect ratio
	        newWidth = (newHeight * original_width) / original_height;
	    }
		
		Bitmap dest = new Bitmap(newWidth, newHeight);
		dest.createAlpha(Bitmap.ALPHA_BITDEPTH_8BPP);
		
		int[] stripe=new int[width];
		for (int n=width-1;n>=0;n--) 
			stripe[n]=0;
		try{
		for (int y=height-1;y>=0;y--) 
			dest.setARGB(stripe,0,width,0,y,width,1);
		}catch(Exception e)
		{
			e.printStackTrace();
		}
		source.scaleInto(dest, Bitmap.FILTER_BILINEAR);
		
		return dest;
	}
	
	public static Bitmap CreateScaledCopyKeepAspectRatio(Bitmap source,int width,int height){
		
		int original_width = source.getWidth();
	    int original_height = source.getHeight();
	    int bound_width = width;
	    int bound_height = height;
	    int newWidth = original_width;
	    int newHeight = original_height;
	    
	    // first check if we need to scale width
	    if (original_width > bound_width) {
	        //scale width to fit
	        newWidth = bound_width;
	        //scale height to maintain aspect ratio
	        newHeight = (newWidth * original_height) / original_width;
	    }

	    // then check if we need to scale even with the new height
	    if (newHeight > bound_height) {
	        //scale height to fit instead
	        newHeight = bound_height;
	        //scale width to maintain aspect ratio
	        newWidth = (newHeight * original_width) / original_height;
	    }
		
		Bitmap dest = new Bitmap(newWidth, newHeight);
		dest.createAlpha(Bitmap.ALPHA_BITDEPTH_8BPP);
		
		int[] stripe=new int[width];
		for (int n=width-1;n>=0;n--) 
			stripe[n]=0;
		try{
		for (int y=height-1;y>=0;y--) 
			dest.setARGB(stripe,0,width,0,y,width,1);
		}catch(Exception e)
		{
			e.printStackTrace();
		}
		source.scaleInto(dest, Bitmap.FILTER_BILINEAR);
		
		return dest;
	}
	
	public static Bitmap CreateScaledCopy(Bitmap source)
	{
		int width = source.getWidth();
		int height = source.getHeight();
		
		Bitmap dest= new Bitmap((int)(width*GetWidthScale()),(int)( height*GetHeightScale()));
		dest.createAlpha(Bitmap.ALPHA_BITDEPTH_8BPP);
		
		int[] stripe=new int[width];
		for (int n=width-1;n>=0;n--) 
			stripe[n]=0;
		try{
		for (int y=height-1;y>=0;y--) 
			dest.setARGB(stripe,0,width,0,y,width,1);
		}catch(Exception e)
		{
			e.printStackTrace();
		}
		source.scaleInto(dest, Bitmap.FILTER_BILINEAR);
		
		return dest;
	}
	public static Bitmap CreateScaledEmptyTransparentBitmap(int width,int height)
	{
		return CreateEmptyTransparentBitmap((int)(width*GetWidthScale()),(int)( height*GetHeightScale()));
	}
}

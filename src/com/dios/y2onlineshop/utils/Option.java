package com.dios.y2onlineshop.utils;


import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.microedition.io.Connector;
import javax.microedition.io.HttpConnection;
import javax.microedition.io.file.FileConnection;

import net.rim.device.api.io.IOUtilities;
import net.rim.device.api.math.Fixed32;
import net.rim.device.api.system.Bitmap;
import net.rim.device.api.system.CoverageInfo;
import net.rim.device.api.system.Display;
import net.rim.device.api.system.EncodedImage;
import net.rim.device.api.system.PNGEncodedImage;
import net.rim.device.api.system.RadioInfo;


public class Option {

	 public static EncodedImage resizeImage(EncodedImage image, int newWidth, int newHeight) {
	        int scaleFactorX = Fixed32.div(Fixed32.toFP(image.getWidth()), Fixed32.toFP(newWidth));
	        int scaleFactorY = Fixed32.div(Fixed32.toFP(image.getHeight()), Fixed32.toFP(newHeight));
	        return image.scaleImage32(scaleFactorX, scaleFactorY);
	 }
	 
	
	 
	 public static EncodedImage getImageScaled(EncodedImage image, double scale){
		double logoWidth, logoHeight;
		EncodedImage encodedImage = null;
		if(image!=null){
			logoWidth = (double) (scale * image.getWidth() * (Display.getWidth()/320));
			logoHeight = (double) (scale * image.getHeight() * (Display.getWidth()/320)); 

			encodedImage = Option.resizeImage(image, (int)logoWidth, (int)logoHeight);
		}
		return encodedImage;
		
	 }
	 
	 public static EncodedImage getImageScaledWH(EncodedImage image, double scaleW, double scaleH){
			double logoWidth, logoHeight;
			EncodedImage encodedImage = null;
			if(image!=null){
				logoWidth = (double) (scaleW * image.getWidth() * (Display.getWidth()/320));
				logoHeight = (double) (scaleH * image.getHeight() * (Display.getWidth()/320)); 

				encodedImage = Option.resizeImage(image, (int)logoWidth, (int)logoHeight);
			}
			return encodedImage;
			
		 }
	
	 public static Bitmap getLiveImage(String url,int width,int height) throws IOException
	    {   
	        Bitmap bitmap = null;
	        HttpConnection httpconnection = null;
	        InputStream inputStream = null;
	        EncodedImage encodeImageBitmap;
	        try  
	        {  
		
	             byte[] responseData = new byte[10000];  
	               int length = 0;  
	               StringBuffer rawResponse = new StringBuffer();  
	            httpconnection = (HttpConnection) Connector.open(url, Connector.READ, true);  
	            String location=httpconnection.getHeaderField("location");

	                if(location!=null){
	                     httpconnection = (HttpConnection) Connector.open(location, Connector.READ, true);  

	                }else{
	                     httpconnection = (HttpConnection) Connector.open(url, Connector.READ, true);  
	                }


	                inputStream = httpconnection.openInputStream();  


	                    while (-1 != (length = inputStream.read(responseData)))  
	                    {  
	                     rawResponse.append(new String(responseData, 0, length));  
	                    }  
	                    int responseCode = httpconnection.getResponseCode();


	                if (responseCode != HttpConnection.HTTP_OK)  
	                {  
	                    throw new IOException("HTTP response code: "  
	                            + responseCode);  
	                }  
	                final String result = rawResponse.toString();

	                 byte[] dataArray = result.getBytes();  
	                 encodeImageBitmap = EncodedImage.createEncodedImage(dataArray, 0, dataArray.length); 
	                 System.out.println(encodeImageBitmap);

	            }  
	            catch (final Exception ex)  
	            {  
	             System.out.print("Exception (" + ex.getClass() + "): " + ex.getMessage());   
	            }  
	            finally  
	            {  
	                try  
	                {  
	                    inputStream.close();  
	                    inputStream = null;  
	                    httpconnection.close();  
	                    httpconnection = null;  
	                }  
	                catch(Exception e){}  
	            }


	        return bitmap;
	    }
	 
	 public static String getResFileContent() {
		 	String filename = "profile.json";
	        InputStream is = null;
	        FileConnection fc = null;
	        try {
	        	fc = (FileConnection) Connector.open(Utils.localPath + filename, Connector.READ_WRITE);
	        	if(!fc.exists()){
	        		return null;
	        	}else{
	        		is = fc.openInputStream();
		        	
		            byte[] bytes = new byte[Byte.MAX_VALUE];
		           
		            int len = 0;
		            StringBuffer buf = new StringBuffer();
		            while ((len = is.read(bytes)) != -1) {
		                buf.append(new String(bytes, 0, len));
		            }
		            System.out.println(buf.toString());
		            return buf.toString();
	        	}
	        	
	        } catch(Exception ex) {
	            System.out.println("Error: " + ex.toString());
	            return null;
	        } finally {
	            try {
	                if (is != null) {
	                	is.close();
	                }
	            } catch (Exception e) {
	                System.out.println("Ex: "+e.getMessage());
	            }
	        }
	    }
	 
	 public static void writeData(String data) {
	    	String pFileName = "profile.json";
	        FileConnection fc = null;
	        OutputStream lStream = null;
	        InputStream is = null;
	        
	        if (pFileName != null) 
	        {
	            try {  
	            		fc = (FileConnection) Connector.open(Utils.localPath + pFileName, Connector.READ_WRITE);
	            
			            if (!fc.exists()) {
			            	FileConnection afile = (FileConnection)Connector.open(Utils.localPath);
			            	if(!afile.exists()){
			            		afile.mkdir();
			            	}else{
			            		afile.close();
			            		afile=null;
			            	}
			            	fc =(FileConnection)Connector.open(Utils.localPath + pFileName, Connector.READ_WRITE);
			        		fc.create();
			            }
			            
			            lStream = fc.openOutputStream(fc.fileSize());
			            System.out.println("============================================data.getBytes() : "+data.getBytes());
			            lStream.write(data.getBytes());
		
			            is = fc.openInputStream();
			            byte bytes[] = new byte[1024];
			           
			            int len = 0;
			            StringBuffer buf = new StringBuffer();
			            while ((len = is.read(bytes)) != -1) {
			            	System.out.println("LOOP KE "+len);
			                buf.append(new String(bytes, 0, len));
			            }
		            
	            } catch (Exception ioex) {
	            	ioex.printStackTrace();
	            	System.out.println("--------------------exception pas nulis : "+ioex);
	            } finally {
		            if (lStream != null) {
		                try {
		                lStream.close();
		                lStream = null;
		                } catch (Exception ioex) {
		                }
		            }
		            if (fc != null) {
		                try {
		                fc.close();
		                fc = null;
		                } catch (Exception ioex) {
		                }
		            }
		            if (is != null) {
		                try {
		                is.close();
		                is = null;
		                } catch (Exception ioex) {
		                }
		            }
	            }
	        }
	  }
	 
	 public static void writeFileData(String pFileName, String fileLocation, String data){		 
	        FileConnection fc = null;
	        OutputStream lStream = null;
	        InputStream is = null;
	        
	        if (pFileName != null) {
	            try {
		            fc = (FileConnection) Connector.open(fileLocation + pFileName, Connector.READ_WRITE);   
		            
		            if (!fc.exists()) {
		            	FileConnection afile = (FileConnection)Connector.open(fileLocation);
		            	if(!afile.exists()){
		            		afile.mkdir();
		            	}else{
		            		afile.close();
		            		afile=null;
		            	}
		            	fc =(FileConnection)Connector.open(fileLocation + pFileName, Connector.READ_WRITE);
		        		fc.create();
		            }
		            lStream = fc.openOutputStream(fc.fileSize());
		            System.out.println("============================================data.getBytes() : "+data.getBytes());
		            lStream.write(data.getBytes());
	
		            is = fc.openInputStream();
		            byte bytes[] = new byte[1024];
		           
		            int len = 0;
		            StringBuffer buf = new StringBuffer();
		            while ((len = is.read(bytes)) != -1) {
		            	System.out.println("LOOP KE "+len);
		                buf.append(new String(bytes, 0, len));
		            }	            
	            } catch (Exception ioex) {
	            	ioex.printStackTrace();
	            	System.out.println("--------------------exception pas nulis : "+ioex);
	            } finally {
		            if (lStream != null) {
		                try {
		                lStream.close();
		                lStream = null;
		                } catch (Exception ioex) {
		                }
		            }
		            if (fc != null) {
		                try {
		                fc.close();
		                fc = null;
		                } catch (Exception ioex) {
		                }
		            }
		            if (is != null) {
		                try {
		                is.close();
		                is = null;
		                } catch (Exception ioex) {
		                }
		            }
	            }
	        }
	 }
	 
	 public static String readDataFile(String fileName, String fileLocation){
		    InputStream is = null;
	        FileConnection fc = null;
	        try {
	        	fc = (FileConnection) Connector.open(fileLocation + fileName, Connector.READ_WRITE);
	        	if(!fc.exists()){
	        		return null;
	        	}else{
	        		is = fc.openInputStream();
		        	
		            byte[] bytes = new byte[Byte.MAX_VALUE];
		           
		            int len = 0;
		            StringBuffer buf = new StringBuffer();
		            while ((len = is.read(bytes)) != -1) {
		                buf.append(new String(bytes, 0, len));
		            }
		            System.out.println(buf.toString());
		            return buf.toString();
	        	}
	        	
	        } catch(Exception ex) {
	            System.out.println("Error: " + ex.toString());
	            return null;
	        } finally {
	            try {
	                if (is != null) {
	                	is.close();
	                }
	            } catch (Exception e) {
	                System.out.println("Ex: "+e.getMessage());
	            }
	        }
	 }
	 
	 public static void deleteFile(String fileName) {
	    	String pFileName = fileName;
	        FileConnection fc = null;	        
	        if (pFileName != null) {
	            try {
					fc = (FileConnection) Connector.open(System.getProperty("fileconn.dir.music") + pFileName, Connector.READ_WRITE);
					if(fc.exists())fc.delete();
	            } catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}   
            }
	  }
	 
	 public static boolean checkInternetConnection(){
		 return checkSignal() || checkWifi();
	 }
	 
	 public static void createDirectory(String directory){		
	        if (directory != null) {            	          
	        	 // First step make sure temp directory is there
	            FileConnection fconn = null;
	            try {
	            	System.out.println("File connection open: " + directory);
	                fconn = (FileConnection) Connector.open(directory, Connector.READ_WRITE);
	                if ( !fconn.exists() ) {
	                    fconn.mkdir();  // create the folder/file if it doesn't exist
	                }
	                fconn.close();
	                fconn = null;
	            } catch (Exception e) {
	                String errorMessage = "Error creating directory: " + directory + '\n' + e.toString();
	                System.out.println(errorMessage);
	            } finally {
	                try {
	                    if ( fconn != null ) {
	                        fconn.close();
	                        fconn = null;
	                    }
	                } catch (Exception e) {
	                }
	            }         	            
	        }
		 }
	 
	 public static void createAppDirectory(){
		 createDirectory(Utils.localPath);
	 }
	 
	 public static void saveBitmap(final String path, final Bitmap bmp) {
		 new Thread(){
			 public void run() {
				 try {
					 	System.out.println("path : " + path);
			            FileConnection fconn = (FileConnection) Connector.open(path, Connector.READ_WRITE);

			            if (!fconn.exists())

			                fconn.create();

			            OutputStream out = fconn.openOutputStream();

			            PNGEncodedImage encodedImage = PNGEncodedImage.encode(bmp);

			            byte[] imageBytes = encodedImage.getData();

			            out.write(imageBytes);

			            out.close();

			            fconn.close();

			        } catch (Exception e) {

			            System.out.println(" Exception while saving Bitmap:: "+ e.toString());

			            e.getMessage();
			        }
			      
			        System.out.println("File has been saved succesfully in " + path );
			 };
		 }.start();
	        
	    }
	 
	 public static Bitmap getBitmapFromFile(String filePath) 
	 {
		 System.out.println("filepath: " + filePath);
	     Bitmap bitmap=null,scaleBitmap=null;
	     InputStream inputStream=null;
	     FileConnection fileConnection=null;     
	     try
	     {
	         fileConnection=(FileConnection) Connector.open(filePath);
	         if(fileConnection.exists())
	         {
	             inputStream=fileConnection.openInputStream();           
	             byte[] data=new byte[(int)fileConnection.fileSize()];           
	             data=IOUtilities.streamToBytes(inputStream);
	             inputStream.close();
	             fileConnection.close();
	             bitmap=Bitmap.createBitmapFromBytes(data,0,data.length,1);
	             
	             //You can return this bitmap otherwise, after this you can scale it according to your requirement; like...

	             scaleBitmap = bitmap;
	         }
	         else
	         {
	             scaleBitmap=Bitmap.getBitmapResource("default_photo.png");//Otherwise, Give a Dialog here;
	         }
	     }
	     catch (Exception e) 
	     {
	         try 
	         {
	             if(inputStream!=null)
	             {
	                 inputStream.close();                
	             }
	             if(fileConnection!=null)
	             {
	                 fileConnection.close();
	             }
	         } 
	         catch (Exception exp) 
	         {

	         }
	         scaleBitmap=Bitmap.getBitmapResource("default_photo.png");//Your known Image;     
	     }
	     return scaleBitmap;//return the scale Bitmap not the original bitmap;
	   } 
	 
	 private static boolean checkSignal() {
		    return RadioInfo.getState() != RadioInfo.STATE_OFF && RadioInfo.getSignalLevel() != 
		            RadioInfo.LEVEL_NO_COVERAGE;
		}
			
		private static boolean checkWifi() {
		    if ((RadioInfo.getActiveWAFs() & RadioInfo.WAF_WLAN ) != 0 ) {
		        return CoverageInfo.isCoverageSufficient(CoverageInfo.COVERAGE_DIRECT, RadioInfo.WAF_WLAN, true);
		    }
			    
		    return false;
		}
	 
		public static boolean checkExist(String fullPath){
			boolean exist = false;
			String path = fullPath;
			FileConnection file = null;
			try {
				file = (FileConnection)Connector.open(path);
				if (!file.exists()) {
					System.out.println("file " + fullPath +  " tidak ada");
		        	exist = false;
		        } else{
		        	System.out.println("file " + fullPath + " sudah ada");
		        	exist = true;
		        }
				file.close();
				file = null;			
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	        	
			return exist;
		}
}

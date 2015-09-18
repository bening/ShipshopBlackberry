package com.dios.y2onlineshop.screen;

import impl.rim.com.twitterapime.xauth.ui.BrowserFieldOAuthDialogWrapper;

import java.io.IOException;

import net.rim.device.api.browser.field2.BrowserField;
import net.rim.device.api.ui.UiApplication;
import net.rim.device.api.ui.component.Dialog;
import net.rim.device.api.ui.container.MainScreen;

import com.dios.y2onlineshop.interfaces.ColorList;
import com.dios.y2onlineshop.utils.AlertDialog;
import com.dios.y2onlineshop.utils.Utils;
import com.twitterapime.rest.Credential;
import com.twitterapime.rest.TweetER;
import com.twitterapime.rest.UserAccountManager;
import com.twitterapime.search.LimitExceededException;
import com.twitterapime.search.Tweet;
import com.twitterapime.xauth.Token;
import com.twitterapime.xauth.ui.OAuthDialogListener;
import com.twitterapime.xauth.ui.OAuthDialogWrapper;

public class TweetScreen extends MainScreen implements OAuthDialogListener, ColorList {
	private String tweetContent;	
		
	public TweetScreen(String tweetContent) {
		this.tweetContent = tweetContent;
				
		BrowserField myBrowserField = new BrowserField();
		add(myBrowserField);
		
		OAuthDialogWrapper loginWrapper =
			new BrowserFieldOAuthDialogWrapper(
				myBrowserField,
				Utils.CONSUMER_KEY,
				Utils.CONSUMER_SECRET,
				Utils.TWITTER_CALLBACK_URL,
				this);
		
		loginWrapper.login();
	}

	public void onAuthorize(Token token) {
		System.out.println("onAuthorize: " + token);
		
		Credential c = new Credential(Utils.CONSUMER_KEY, Utils.CONSUMER_SECRET, token);
		UserAccountManager m = UserAccountManager.getInstance(c);
		
		try {
			if (m.verifyCredential()) {
				//user authorized!
				System.out.println("User authorized");
				Tweet tweet = new Tweet(tweetContent);
				tweet = TweetER.getInstance(m).post(tweet);		
				System.out.println("tweet " + tweet.toString());
				UiApplication.getUiApplication().invokeLater(new Runnable() {
					
					public void run() {
						// TODO Auto-generated method stub
						Dialog.inform("Share via twitter berhasil");
						UiApplication.getUiApplication().popScreen(getScreen());
					}
				});
			} else{
				UiApplication.getUiApplication().invokeLater(new Runnable() {
					
					public void run() {
						// TODO Auto-generated method stub
						UiApplication.getUiApplication().popScreen(getScreen());
					}
				});
			}
						
		} catch (IOException e) {
			UiApplication.getUiApplication().invokeLater(new Runnable() {
				
				public void run() {
					// TODO Auto-generated method stub
					UiApplication.getUiApplication().popScreen(getScreen());
				}
			});			
		} catch (LimitExceededException e) {
			UiApplication.getUiApplication().invokeLater(new Runnable() {
				
				public void run() {
					// TODO Auto-generated method stub
					UiApplication.getUiApplication().popScreen(getScreen());
				}
			});			
		}		
	}

	public void onAccessDenied(String message) {
		System.out.println("access_denied: " + message);
		UiApplication.getUiApplication().invokeLater(new Runnable() {
			
			public void run() {
				// TODO Auto-generated method stub
				UiApplication.getUiApplication().popScreen(getScreen());
			}
		});	
	}

	public void onFail(String error, String message) {
		System.out.println("error: " + error + " message: " + message);
		UiApplication.getUiApplication().invokeLater(new Runnable() {
			
			public void run() {
				// TODO Auto-generated method stub
				AlertDialog.showAlertMessage("Share via twitter gagal");
				UiApplication.getUiApplication().popScreen(getScreen());
			}
		});	
	}
}

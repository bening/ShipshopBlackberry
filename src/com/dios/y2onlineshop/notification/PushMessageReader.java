package com.dios.y2onlineshop.notification;

import javax.microedition.io.Connection;

import net.rim.device.api.io.http.HttpServerConnection;
import net.rim.device.api.io.http.PushInputStream;
import net.rim.device.api.notification.NotificationsManager;
import net.rim.device.api.system.Application;
import net.rim.device.api.ui.Manager;
import net.rim.device.api.ui.Ui;
import net.rim.device.api.ui.UiEngine;
import net.rim.device.api.ui.component.Dialog;
import net.rim.device.api.util.Arrays;

public class PushMessageReader {
	// HTTP header property that carries unique push message ID
    private static final String MESSAGE_ID_HEADER = "Push-Message-ID";
    // content type constant for text messages
    private static final String MESSAGE_TYPE_TEXT = "text";
    private static final int MESSAGE_ID_HISTORY_LENGTH = 10;
    private static String[] messageIdHistory = new String[MESSAGE_ID_HISTORY_LENGTH];
    private static byte historyIndex;

    private static byte[] buffer = new byte[15 * 1024];
    public static void process(PushInputStream pis, Connection conn) {
        System.out.println("Reading incoming push message ...");

        try {

            HttpServerConnection httpConn;
            if (conn instanceof HttpServerConnection) {
                httpConn = (HttpServerConnection) conn;
            } else {
                throw new IllegalArgumentException("Can not process non-http pushes, expected HttpServerConnection but have "
                        + conn.getClass().getName());
            }

            String msgId = httpConn.getHeaderField(MESSAGE_ID_HEADER);
            String msgType = httpConn.getType();
            String encoding = httpConn.getEncoding();

            System.out.println("Message props: ID=" + msgId + ", Type=" + msgType + ", Encoding=" + encoding);

           
            boolean accept = true;
            if (!alreadyReceived(msgId)) {
                byte[] binaryData;

                if (msgId == null) {
                    msgId = String.valueOf(System.currentTimeMillis());
                }

                if (msgType == null) {
                    System.out.println("Message content type is NULL");
                    accept = false;
                } 
                else if (msgType.indexOf(MESSAGE_TYPE_TEXT) >= 0) 
                {
                    // a string
                    int size = pis.read(buffer);
                    binaryData = new byte[size];
                    System.arraycopy(buffer, 0, binaryData, 0, size);   
                   
                    PushMessage message = new PushMessage(msgId, System.currentTimeMillis(), binaryData, true, true );
                    String text = new String( message.getData(), "UTF-8" );
                     try{
                            final Dialog screen = new Dialog(Dialog.D_OK_CANCEL, " "+text,
                                    Dialog.OK,
                                    //mImageGreen.getBitmap(),
                                    null, Manager.VERTICAL_SCROLL);
                            final UiEngine ui = Ui.getUiEngine();
                            Application.getApplication().invokeAndWait(new Runnable() {
                                public void run() {
                                    NotificationsManager.triggerImmediateEvent(0x749cb23a76c66e2dL, 0, null, null);
                                    ui.pushGlobalScreen(screen, 0, UiEngine.GLOBAL_QUEUE);
                                   
                                }
                            });
                            screen.setDialogClosedListener(new MyDialogClosedListener());
                            }
                            catch (Exception e) {
                                // TODO: handle exception
                            }
                           
                   
                    // TODO report message
                }  else {
                    System.out.println("Unknown message type " + msgType);
                    accept = false;
                }
            } else {
                System.out.println("Received duplicate message with ID " + msgId);
            }
            pis.accept();
        } catch (Exception e) {
            System.out.println("Failed to process push message: " + e);
        }
    }
    
    private static boolean alreadyReceived(String id) {
        if (id == null) {
            return false;
        }

        if (Arrays.contains(messageIdHistory, id)) {
            return true;
        }

        // new ID, append to the history (oldest element will be eliminated)
        messageIdHistory[historyIndex++] = id;
        if (historyIndex >= MESSAGE_ID_HISTORY_LENGTH) {
            historyIndex = 0;
        }
        return false;
    }
}

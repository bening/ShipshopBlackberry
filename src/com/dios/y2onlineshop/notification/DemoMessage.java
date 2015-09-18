package com.dios.y2onlineshop.notification;
 
import java.util.Date;
import java.util.Vector;

import com.dios.y2onlineshop.app.Y2OnlineShopApp;
 
import net.rim.blackberry.api.messagelist.ApplicationMessage;
import net.rim.device.api.collection.ReadableList;
import net.rim.device.api.system.*;
 
 
/**
 * This class provides a sample implementation of the ApplicationMessage
 * interface. It demonstrates how an application can define its own message
 * formats for use with the message store.
 */
public final class DemoMessage implements ApplicationMessage
{
    static final int DEMO_MESSAGE_TYPE = 0x01;
 
    private String _sender;
    private String _subject;
    private String _message;
    private long _receivedTime;
    private boolean _isNew;
    private boolean _deleted;
    private String _replyMessage;
    private long _replyTime;
    private EncodedImage _previewPicture;
 
 
    /**
     * Creates a new DemoMesage object
     */
    public DemoMessage()
    {
        _isNew = true;
    }
 
 
    /**
     * Constructs a DemoMessage object with specified properties
     *
     * @param sender The name of the sender
     * @param subject The subject of the message
     * @param message The body of the message
     * @param receivedTime The time stamp for when the message was received
     */
   public DemoMessage(String sender, String subject, String message, long receivedTime)
    {
        _sender = sender;
        _subject = subject;
        _message = message;
        _receivedTime = receivedTime;
        _isNew = true;
    }
 
 
    /**
     * Stores the reply message and sets the reply time
     *
     * @param message The reply message
     */
    void reply(String message)
    {
        markRead();
        _replyMessage = message;
        _replyTime = System.currentTimeMillis();
    }
 
 
    /**
     * Marks this message as deleted
     */
    void messageDeleted()
    {
        _isNew = false;
        _deleted = true;
    }
 
 
    /**
     * Marks this message as new
     */
    void markAsNew()
    {
        _isNew = true;
        _replyMessage = null;
    }
 
 
    /**
     * Marks this message as read
     */
    void markRead()
    {
        _isNew = false;
    }
 
 
    /**
     * Indicates whether this message is new or not
     *
     * @return True if the message is new, false otherwise
     */
    boolean isNew()
    {
        return _isNew;
    }
 
 
    /**
     * Indicates whether this message has been replied to or not
     *
     * @return True if the message has been replied to, false otherwise
     */
    boolean hasReplied()
    {
        return _replyMessage != null;
    }
 
 
    /**
     * Sets the name of the sender who sent this message
     *
     * @param sender The name of the sender
     */
    void setSender(String sender)
    {
        _sender = sender;
    }
 
 
    /**
     * Sets the subject of this message
     *
     * @param subject The subject of this message
     */
    void setSubject(String subject)
    {
        _subject = subject;
    }
 
 
    /**
     * Sets the time at which this message was received
     *
     * @param receivedTime The time at which this message was received
     */
    void setReceivedTime(long receivedTime)
    {
        _receivedTime = receivedTime;
    }
 
 
    /**
     * Sets the message body
     *
     * @param message The message body
     */
    void setMessage(String message)
    {
        _message = message;
    }
 
 
    /**
     * Retrieves the message body
     *
     * @return The message body
     */
    String getMessage()
    {
        return _message;
    }
 
 
    /**
     * Sets the preview picture for this message
     *
     * @param image The desired preview picture of this message
     */
    void setPreviewPicture(EncodedImage image)
    {
        _previewPicture = image;
    }
 
 
    // Implementation of ApplicationMessage ------------------------------------
    /**
     * @see net.rim.blackberry.api.messagelist.ApplicationMessage#getContact()
     */
    public String getContact()
    {
        return _sender;
    }
 
 
    /**
     * @see net.rim.blackberry.api.messagelist.ApplicationMessage#getStatus()
     */
    public int getStatus()
    {  // Form message list status based on current message state
        if(_isNew)
        {
            return Y2OnlineShopApp.STATUS_NEW;
        }
        if(_deleted)
        {
            return Y2OnlineShopApp.STATUS_DELETED;
        }
        if(_replyMessage != null)
        {
            return Y2OnlineShopApp.STATUS_REPLIED;
        }
        return Y2OnlineShopApp.STATUS_OPENED;
    }
 
 
    /**
     *
     * @see net.rim.blackberry.api.messagelist.ApplicationMessage#getSubject()
     */
    public String getSubject()
    {
        if(_replyMessage != null)
        {
            return "Re: " + _subject;
        }
        else
        {
            return _subject;
        }
    }
 
 
    /**
     * @see net.rim.blackberry.api.messagelist.ApplicationMessage#getTimestamp()
     */
    public long getTimestamp()
    {
        return _receivedTime;
    }
 
 
    /**
     * @see net.rim.blackberry.api.messagelist.ApplicationMessage#getType()
     */
    public int getType()
    {
        // All messages have the same type
        return DEMO_MESSAGE_TYPE;
    }
 
 
    /**
     * @see net.rim.blackberry.api.messagelist.ApplicationMessage#getPreviewText()
     */
    public String getPreviewText()
    {
        if(_message == null)
        {
            return null;
        }
 
        StringBuffer buffer = new StringBuffer(_message);
 
        if(_replyMessage != null)
        {
            buffer.append(". You replied on ").append(new Date(_replyTime)).append(": ").append(_replyMessage);
        }
 
        return buffer.length() > 100 ? buffer.toString().substring(0, 100) + " ..." : buffer.toString();
    }
 
 
    /**
     *@see net.rim.blackberry.api.messagelist.ApplicationMessage#getCookie(int)
     */
    public Object getCookie(int cookieId)
    {
        return null;
    }
 
 
    /**
     *
     * @see net.rim.blackberry.api.messagelist.ApplicationMessage#getPreviewPicture()
     */
    public Object getPreviewPicture()
    {
        return _previewPicture;
    }
    
    /**
     * This is an implementation of the ReadableList interface which stores the
     * list of messages using a Vector.
     */
    public static class ReadableListImpl implements ReadableList
    {
        private Vector messages;
 
        /**
         * Creates a empty instance of ReadableListImpl
         */
        public ReadableListImpl()
        {
            messages = new Vector();
        }
 
 
        /**
         * @see net.rim.device.api.collection.ReadableList#getAt(int)
         */
        public Object getAt(int index)
        {
            return messages.elementAt(index);
        }
 
 
        /**
         * @see net.rim.device.api.collection.ReadableList#getAt(int, int, Object[], int)
         */
        public int getAt(int index, int count, Object[] elements, int destIndex)
        {
            return 0;
        }
 
 
        /**
         * @see net.rim.device.api.collection.ReadableList#getIndex(Object)
         */
        public int getIndex(Object element)
        {
            return messages.indexOf(element);
        }
 
 
        /**
         * @see net.rim.device.api.collection.ReadableList#size()
         */
        public int size()
        {
            return messages.size();
        }
 
 
        /**
         * Add a message to this list
         *
         * @param message The message to add to this list
         */
        void addMessage(DemoMessage message)
        {
            messages.addElement(message);
        }
         
 
        /**
         * Removes a message from this list
         *
         * @param message The message to remove from this list
         */
        void removeMessage(DemoMessage message)
        {
            messages.removeElement(message);
        }
    }
}
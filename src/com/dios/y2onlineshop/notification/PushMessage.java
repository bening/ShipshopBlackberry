package com.dios.y2onlineshop.notification;

public class PushMessage {

	private String id;
    private long timestamp;
    private byte[] data;
    private boolean textMesasge;
    private boolean unread;

    public PushMessage( String id, long timestamp, byte[] data, boolean textMesasge, boolean unread ) {
        super();
        this.id = id;
        this.timestamp = timestamp;
        this.data = data;
        this.textMesasge = textMesasge;
        this.unread = unread;
    }

    public String getId() {
        return id;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public byte[] getData() {
        return data;
    }

    public boolean isTextMesasge() {
        return textMesasge;
    }

    public boolean isUnread() {
        return unread;
    }

    public void setUnread( boolean unread ) {
        this.unread = unread;
    }
}

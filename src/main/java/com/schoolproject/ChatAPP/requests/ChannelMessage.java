// New ChannelMessage class
package com.schoolproject.ChatAPP.requests;

import org.springframework.stereotype.Component;

@Component
public class ChannelMessage {
    private String sender;
    private String sendeName;
    private String channelId;
    private String content;
    private String messageType;
    private String fileUrl;

    // Getters and Setters
    public String getSender() {
        return sender;
    }

    public void setSenderId(String sender) {
        this.sender = sender;
    }

    public String getSendeName() {
        return sendeName;
    }

    public void setSendeName(String sendeName) {
        this.sendeName = sendeName;
    }

    public String getChannelId() {
        return channelId;
    }

    public void setChannelId(String channelId) {
        this.channelId = channelId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getMessageType() {
        return messageType;
    }

    public void setMessageType(String messageType) {
        this.messageType = messageType;
    }

    public String getFileUrl() {
        return fileUrl;
    }

    public void setFileUrl(String fileUrl) {
        this.fileUrl = fileUrl;
    }

    @Override
    public String toString() {
        return "ChannelMessage{" +
                "senderId='" + sender + '\'' +
                ", channelId='" + channelId + '\'' +
                ", content='" + content + '\'' +
                ", messageType='" + messageType + '\'' +
                ", fileUrl='" + fileUrl + '\'' +
                '}';
    }
}

package model.message;

import java.time.LocalDateTime;

public class Message {
    private int id;
    private String msgText;
    private LocalDateTime timeOfSending;
    private MessageType messageType;

    public Message(int id, String msgText, LocalDateTime timeOfSending, MessageType messageType) {
        this.id = id;
        this.msgText = msgText;
        this.timeOfSending = timeOfSending;
        this.messageType = messageType;
    }

}

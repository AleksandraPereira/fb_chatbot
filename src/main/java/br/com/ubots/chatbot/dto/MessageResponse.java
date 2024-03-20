package br.com.ubots.chatbot.dto;

import org.json.JSONObject;

public class MessageResponse {
    private Recipient recipient;
    private String messaging_type;
    private Message message;

    public String responseBody() {
        JSONObject json = new JSONObject(this);
        return json.toString();
    }

    public static class Recipient {
        private String id;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }
    }

    public static class Message {
        private String text;

        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }
    }

    public Recipient getRecipient() {
        return recipient;
    }

    public void setRecipient(Recipient recipient) {
        this.recipient = recipient;
    }

    public String getMessaging_type() {
        return messaging_type;
    }

    public void setMessaging_type(String messaging_type) {
        this.messaging_type = messaging_type;
    }

    public Message getMessage() {
        return message;
    }

    public void setMessage(Message message) {
        this.message = message;
    }
}


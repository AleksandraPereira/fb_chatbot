package br.com.ubots.chatbot;



public class MessageItem {
        private Messages message;
        private String senderId;

        public Messages message() {
            return message;
        }

        public void setMessage(Messages message) {
            this.message = message;
        }

        public String senderId() {
            return senderId;
        }

        public void setSenderId(String senderId) {
            this.senderId = senderId;
        }

}

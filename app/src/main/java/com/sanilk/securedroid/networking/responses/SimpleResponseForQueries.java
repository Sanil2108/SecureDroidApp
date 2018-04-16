package com.sanilk.securedroid.networking.responses;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Date;

/**
 * Created by root on 13/4/18.
 */
public class SimpleResponseForQueries extends Response {
    public static final String RESPONSE_TYPE ="SIMPLE_RESPONSE_FOR_QUERIES";

    public final static String ACTIONS_KEY="actions";

    private Action[] actions;

    public SimpleResponseForQueries(){
        responseType= RESPONSE_TYPE;
    }

    public Action[] getActions() {
        return actions;
    }

    public void setActions(Action[] actions) {
        this.actions = actions;
    }

    public static class Action{
        public static final String ACTION_TYPE_KEY="action_type";

        protected String actionType;
    }

    public static class MessageAction extends Action{
        public static final String ACTION_TYPE="SHOW_MESSAGE";
        public static final String DURATION_KEY="duration";
        public static final String MESSAGE_TEXT_KEY="message_text";

        private int duration;
        private Date time;
        private String messageText;

        public Date getTime() {
            return time;
        }

        public void setTime(Date time) {
            this.time = time;
        }

        public MessageAction(){
            actionType=ACTION_TYPE;
        }

        public String getMessageText() {
            return messageText;
        }

        public void setMessageText(String messageText) {
            this.messageText = messageText;
        }

        public int getDuration() {
            return duration;
        }

        public void setDuration(int duration) {
            this.duration = duration;
        }

        public JSONObject getJSONObject(){
            JSONObject jsonObject=new JSONObject();
            try {
                jsonObject.put(ACTION_TYPE_KEY, ACTION_TYPE);
                jsonObject.put(DURATION_KEY, duration);
                jsonObject.put(MESSAGE_TEXT_KEY, messageText);
            }catch (Exception e){
                e.printStackTrace();
            }

            return jsonObject;
        }
    }

    public static class StartAlarmAction extends Action{
        public static final String ACTION_TYPE="START_ALARM";
        public static final String DURATION_KEY="duration";

        private int duration;
        private Date time;

        public Date getTime() {
            return time;
        }

        public void setTime(Date time) {
            this.time = time;
        }

        public StartAlarmAction(){
            actionType=ACTION_TYPE;
        }

        public int getDuration() {
            return duration;
        }

        public void setDuration(int duration) {
            this.duration = duration;
        }

        public JSONObject getJSONObject(){
            JSONObject jsonObject=new JSONObject();
            try {
                jsonObject.put(ACTION_TYPE_KEY, ACTION_TYPE);
                jsonObject.put(DURATION_KEY, duration);
            }catch (Exception e){
                e.printStackTrace();
            }

            return jsonObject;
        }
    }
}
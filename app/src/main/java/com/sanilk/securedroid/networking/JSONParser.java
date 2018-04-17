package com.sanilk.securedroid.networking;

import com.sanilk.securedroid.networking.responses.CreateUserResponse;
import com.sanilk.securedroid.networking.responses.IsClientAuthenticResponse;
import com.sanilk.securedroid.networking.responses.IsEmailTakenResponse;
import com.sanilk.securedroid.networking.responses.Response;
import com.sanilk.securedroid.networking.responses.SimpleResponseForQueries;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by sanil on 13/4/18.
 */

public class JSONParser {
    public Response parse(String s){
        try {
            JSONObject jsonObject = new JSONObject(s);
            switch (jsonObject.getString(Response.RESPONSE_TYPE_KEY)){
                case SimpleResponseForQueries.RESPONSE_TYPE:
                    return parseSimpleResponseForQueries(jsonObject);
                case IsEmailTakenResponse.RESPONSE_TYPE:
                    return parseIsEmailTakenResponse(jsonObject);
                case CreateUserResponse.RESPONSE_TYPE:
                    return parseCreateUserResponse(jsonObject);
                case IsClientAuthenticResponse.RESPONSE_TYPE:
                    return parseIsClientAuthenticResponse(jsonObject);
                default:
                    return null;
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    private IsClientAuthenticResponse parseIsClientAuthenticResponse(JSONObject jsonObject) throws Exception{
        IsClientAuthenticResponse isClientAuthenticResponse=new IsClientAuthenticResponse();
        isClientAuthenticResponse.setSuccessful(jsonObject.getBoolean(IsClientAuthenticResponse.IS_SUCCESSFUL_KEY));
        return isClientAuthenticResponse;
    }

    private CreateUserResponse parseCreateUserResponse(JSONObject jsonObject) throws Exception{
        CreateUserResponse createUserResponse=new CreateUserResponse();
        createUserResponse.setSuccessful(jsonObject.getBoolean(CreateUserResponse.IS_SUCCESSFUL_KEY));
        return createUserResponse;
    }

    private IsEmailTakenResponse parseIsEmailTakenResponse(JSONObject jsonObject) throws Exception{
        IsEmailTakenResponse isEmailTakenResponse=new IsEmailTakenResponse();
        isEmailTakenResponse.setEmail(jsonObject.getString(IsEmailTakenResponse.EMAIL_KEY));
        isEmailTakenResponse.setEmailAvailable(jsonObject.getBoolean(IsEmailTakenResponse.EMAIL_AVAILABLE_KEY));
        return isEmailTakenResponse;
    }

    public SimpleResponseForQueries parseSimpleResponseForQueries(JSONObject jsonObject) throws Exception{
        SimpleResponseForQueries simpleResponseForQueries=new SimpleResponseForQueries();
        JSONArray jsonArray=jsonObject.getJSONArray(SimpleResponseForQueries.ACTIONS_KEY);
        SimpleResponseForQueries.Action[] actions=new SimpleResponseForQueries.Action[jsonArray.length()];
        for(int i=0;i<jsonArray.length();i++){
            JSONObject actionJSONObject=jsonArray.getJSONObject(i);
            actions[i]=parseActionJSONObject(actionJSONObject);
        }
        simpleResponseForQueries.setActions(actions);
        return simpleResponseForQueries;
    }

    public SimpleResponseForQueries.Action parseActionJSONObject(JSONObject jsonObject) throws Exception{
        switch (jsonObject.getString(SimpleResponseForQueries.Action.ACTION_TYPE_KEY)){
            case SimpleResponseForQueries.StartAlarmAction.ACTION_TYPE:
                return parseStartAlarmAction(jsonObject);
            case SimpleResponseForQueries.MessageAction.ACTION_TYPE:
                return parseMessageAction(jsonObject);
            default:
                return null;
        }
    }

    private SimpleResponseForQueries.StartAlarmAction parseStartAlarmAction(JSONObject jsonObject) throws Exception{
        SimpleResponseForQueries.StartAlarmAction startAlarmAction=new SimpleResponseForQueries.StartAlarmAction();
        startAlarmAction.setDuration(jsonObject.getInt(SimpleResponseForQueries.StartAlarmAction.DURATION_KEY));
        return startAlarmAction;
    }

    private SimpleResponseForQueries.MessageAction parseMessageAction(JSONObject jsonObject) throws Exception{
        SimpleResponseForQueries.MessageAction messageAction=new SimpleResponseForQueries.MessageAction();
        messageAction.setDuration(jsonObject.getInt(SimpleResponseForQueries.MessageAction.DURATION_KEY));
        messageAction.setMessageText(jsonObject.getString(SimpleResponseForQueries.MessageAction.MESSAGE_TEXT_KEY));
        return messageAction;
    }
}

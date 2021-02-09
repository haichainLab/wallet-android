package io.samos.im;

import android.util.Log;

import com.zhangke.websocket.ErrorResponse;
import com.zhangke.websocket.IResponseDispatcher;
import com.zhangke.websocket.Response;
import com.zhangke.websocket.ResponseDelivery;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.TypeReference;



public class AppResponseDispatcher implements IResponseDispatcher {
    private static final String LOGTAG = "SamosIMResponse";

    public void onConnected(ResponseDelivery delivery) {
        delivery.onConnected();
    }

    public void onConnectError(Throwable cause, ResponseDelivery delivery) {
        delivery.onConnectError(cause);
    }

    public void onDisconnected(ResponseDelivery delivery) {
        delivery.onDisconnected();
    }

    /**
     * 统一处理响应的数据
     */
    public void onMessageResponse(Response message, ResponseDelivery delivery) {
        try {
            CommonResponseEntity responseEntity = JSON.parseObject(message.getResponseText(), new TypeReference<CommonResponseEntity>() {
            });
            CommonResponse commonResponse = new CommonResponse(message.getResponseText(), responseEntity);
            if (commonResponse.getResponseEntity().getCode() > 0) {
                delivery.onMessageResponse(commonResponse);
            } else {
                ErrorResponse errorResponse = new ErrorResponse();
                errorResponse.setErrorCode(commonResponse.getResponseEntity().getCode());
                errorResponse.setDescription(commonResponse.getResponseEntity().getErrmsg());
                errorResponse.setResponseText(message.getResponseText());
                //将已经解析好的 CommonResponseEntity 独享保存起来以便后面使用
                errorResponse.setReserved(responseEntity);
                onSendMessageError(errorResponse, delivery);
            }
        } catch (JSONException e) {
            ErrorResponse errorResponse = new ErrorResponse();
            errorResponse.setResponseText(message.getResponseText());
            errorResponse.setErrorCode(11);
            errorResponse.setCause(e);
            onSendMessageError(errorResponse, delivery);
        }
    }

    /**
     * 统一处理错误信息，
     * 界面上可使用 ErrorResponse#getDescription() 来当做提示语
     */
    public void onSendMessageError(ErrorResponse error, ResponseDelivery delivery) {
        switch (error.getErrorCode()) {
            case 1:
                error.setDescription("网络错误");
                break;
            case 2:
                error.setDescription("网络错误");
                break;
            case 3:
                error.setDescription("网络错误");
                break;
            case 11:
                error.setDescription("数据格式异常");
                Log.e(LOGTAG, "数据格式异常", error.getCause());
                break;
        }
        delivery.onSendMessageError(error);
    }
}

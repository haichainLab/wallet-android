package io.samos.im;

/**
 * 后台接口返回的数据格式
 */
public class CommonResponseEntity {

    private String cmd;
    private String body;
    private int code;
    private String errmsg;

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getErrmsg() {
        return errmsg;
    }

    public void setErrmsg(String errmsg) {
        this.errmsg = errmsg;
    }

    public String getCmd() {
        return cmd;

    }

    public void setCmd(String cmd) {
        this.cmd = cmd;
    }
}

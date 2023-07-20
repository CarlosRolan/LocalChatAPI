package com;

import java.io.Serializable;

public class Msg implements Serializable {
    private static final long serialVersionUID = 123456789L;

    public enum MsgType {
        REQUEST,
        MESSAGE,
        ERROR;
    }

    public final MsgType PACKAGE_TYPE;
    private String action = "NO_ACTION";
    private String[] headers = { "no_emisor", "no_receptor" };
    private String[] parameters = { "none" };
    private String body = "empty";

    public String getAction() {
        return action;
    }

    public String[] getHeaders() {
        return headers;
    }

    public String getEmisor() {
        return headers[0];
    }

    public String getReceptor() {
        return headers[1];
    }

    public String[] getParameters() {
        return parameters;
    }

    public String getParameter(int pos) {
        return parameters[pos];
    }

    public String getBody() {
        return body;
    }

    public String getText() {
        return getBody();
    }

    public void setAction(String action) {
        this.action = action;
    }

    public void setHeaders(String[] headers) {
        this.headers = headers;
    }

    public void setEmisor(String emisor) {
        headers[0] = emisor;
    }

    public void setReceptor(String receptor) {
        headers[1] = receptor;
    }

    public void setParameters(String[] parameters) {
        this.parameters = parameters;
    }

    public void setParameter(int pos, String parameter) {
        this.parameters[pos] = parameter;

    }

    public void setBody(String body) {
        this.body = body;
    }

    public Msg(MsgType TYPE) {
        PACKAGE_TYPE = TYPE;
    }

    public String showParameters() {
        String params = "";
        try {
            for (int i = 0; i < parameters.length; i++) {
                params += parameters[i] + ",";
            }
            return params;
        } catch (IndexOutOfBoundsException e) {
            return parameters[0];
        }

    }

    @Override
    public String toString() {
        return PACKAGE_TYPE + "[" + action + "]\n" + "Headers:[" + headers[0] + ", " + headers[1] + "]\n"
                + "Parameters:["
                + showParameters() + "]\n" + "Body:{" + body + "}";
    }
}

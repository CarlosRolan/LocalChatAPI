package com.comunication;
import java.io.Serializable;

/**
 * Description: TODO"
 * Program Name: LocalChatApi
 * Date: 2020-12-16
 * @author Carlos Rolán Díaz
 * @version 1.0
 */
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

    /* GETTERS */
    public String getAction() {
        return action;
    }

    /**
     * Description:Note that the HEADERs array has always only 2 positions (headers.lenght = 2)
     * 
     * @return EMISOR in the position[0] and RECEPTOR at the [1]
     */
    public String[] getHeaders() {
        return headers;
    }

    /**
     * 
     * @return the EMISOR who is always at the headers[0] position
     */
    public String getEmisor() {
        return headers[0];
    }

    /**
     * 
     * @return the RECEPTOR who is always at the headers[0] position
     */
    public String getReceptor() {
        return headers[1];
    }

    /**
     * 
     * @return the string array of PARAMETERS
     */
    public String[] getParameters() {
        return parameters;
    }

    /**
     * Note that the PARAMETERs array can have any lenght unlike the HEADERs array
     * which is always 2
     * 
     * @param position of the parameter
     * @return the parameter at the given position from the PARAMETERS array
     */
    public String getParameter(int position) {
        return parameters[position];
    }

    /**
     * 
     * @return Msg's body
     */
    public String getBody() {
        return body;
    }

    /**
     * Just to make a clearer use, when the MsgType = MESSAGE, use this method
     * instead of getBody()
     * 
     * @return Msg's body
     */
    public String getText() {
        return getBody();
    }

    /* SETTERS */
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

    /* CONSTRUCTORS */
    public Msg(MsgType TYPE) {
        PACKAGE_TYPE = TYPE;
    }

    /* PUBLIC METHODS */
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

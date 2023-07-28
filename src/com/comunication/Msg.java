package com.comunication;

import java.io.Serializable;

/**
 * Description:
 * Program Name: LocalChatApi
 * 
 * @author Carlos Rolán Díaz
 * @version beta
 */
public class Msg implements Serializable {
    private static final long serialVersionUID = 123456789L;

    public enum MsgType {
        REQUEST,
        MESSAGE,
        ERROR;
    }

    public final MsgType PACKAGE_TYPE;
    private String mAction = "NO_ACTION";
    private String[] mHeaders = { "no_emisor", "no_receptor" };
    private String[] mParameters = { "p1", "p2", "p3", "p4", "p5" };
    private String mBody = "empty";

    /* GETTERS */
    public String getAction() {
        return mAction;
    }

    /**
     * Description:Note that the HEADERs array has always only 2 positions
     * headers[0] = emisor
     * header[1] = receptor
     * (headers.lenght = 2)
     * 
     * @return EMISOR in the position[0] and RECEPTOR at the [1]
     */
    public String[] getHeaders() {
        return mHeaders;
    }

    /**
     * 
     * @return the EMISOR who is always at the headers[0] position
     */
    public String getEmisor() {
        return mHeaders[0];
    }

    /**
     * 
     * @return the RECEPTOR who is always at the headers[0] position
     */
    public String getReceptor() {
        return mHeaders[1];
    }

    /**
     * 
     * @return the string array of PARAMETERS
     */
    public String[] getParameters() {
        return mParameters;
    }

    /**
     * Note that the PARAMETERs array can have any lenght unlike the HEADERs array
     * which is always 2
     * 
     * @param position of the parameter
     * @return the parameter at the given position from the PARAMETERS array
     */
    public String getParameter(int position) {
        return mParameters[position];
    }

    /**
     * 
     * @return Msg's body
     */
    public String getBody() {
        return mBody;
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
        this.mAction = action;
    }

    public void setHeaders(String[] headers) {
        this.mHeaders = headers;
    }

    public void setEmisor(String emisor) {
        mHeaders[0] = emisor;
    }

    public void setReceptor(String receptor) {
        mHeaders[1] = receptor;
    }

    public void setParameters(String[] parameters) {
        mParameters = parameters;
    }

    public void setParameter(int pos, String parameter) {
        mParameters[pos] = parameter;
    }

    public void setBody(String body) {
        this.mBody = body;
    }

    /* CONSTRUCTORS */
    public Msg(MsgType TYPE) {
        PACKAGE_TYPE = TYPE;
    }

    /* PUBLIC METHODS */
    public String showParameters() {
        String toret = "";
        for (int i = 0; i < mParameters.length; i++) {
            if (i > 0) {
                toret += ",";
            }
            toret += mParameters[i];
        }
        return toret;
    }

    @Override
    public String toString() {
        return PACKAGE_TYPE + "[" + mAction + "]\n" + "Headers:[" + mHeaders[0] + ", " + mHeaders[1] + "]\n"
                + "Parameters:["
                + showParameters() + "]\n" + "Body:{" + mBody + "}";
    }
}

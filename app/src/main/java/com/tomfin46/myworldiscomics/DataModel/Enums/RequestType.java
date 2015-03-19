package com.tomfin46.myworldiscomics.DataModel.Enums;

/**
 * Created by Tom on 10/03/2015.
 */
public class RequestType
{
    public enum RequestTypeEnum
    {
        Quick,
        Basic,
        Filtered
    }

    public static String getRequestTerm(RequestTypeEnum requestType)
    {
        switch (requestType)
        {
            case Quick:
                return "quick";
            case Basic:
                return "basic";
            case Filtered:
                return "filtered";
            default:
                return "";
        }
    }
}

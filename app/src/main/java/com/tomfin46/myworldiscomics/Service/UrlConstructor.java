package com.tomfin46.myworldiscomics.Service;

import com.tomfin46.myworldiscomics.DataModel.Enums.RequestType;
import com.tomfin46.myworldiscomics.DataModel.Enums.ResourceTypes;

/**
 * Created by Tom on 10/03/2015.
 */
public class UrlConstructor {

    public static String constructCv(ResourceTypes.ResourcesEnum resourcesEnum, String query) {
        return contructCv(resourcesEnum, query, RequestType.RequestTypeEnum.Basic, "");
    }

    public static String constructCv(ResourceTypes.ResourcesEnum resourcesEnum, String query, RequestType.RequestTypeEnum requestType) {
        return contructCv(resourcesEnum, query, requestType, "");
    }

    public static String constructCv(ResourceTypes.ResourcesEnum resourcesEnum, String query, String filter) {
        return contructCv(resourcesEnum, query, RequestType.RequestTypeEnum.Filtered, filter);
    }

    public static String contructCv(ResourceTypes.ResourcesEnum resourcesEnum, String query, RequestType.RequestTypeEnum requestType, String filter)
    {
        String uri = ServiceConstants.ServiceBaseUrl + ServiceConstants.ComicVineController + ResourceTypes.getResourceTerm(resourcesEnum).replace("_", "") + "/";
        switch (resourcesEnum)
        {
            case Search:
                uri += "?query=" + query + "&";
                break;
            case Characters:
            case Concepts:
            case Issues:
            case Locations:
            case Objects:
            case People:
            case StoryArcs:
            case Teams:
            case Volumes:
                uri += "?";
                break;
            default:
                uri += query + "?";
                break;
        }
        uri += "requestType=" + RequestType.getRequestTerm(requestType) + "&filter=" + filter;
        return uri;
    }

    public static String ConstructDesc()
    {
        return ServiceConstants.ServiceBaseUrl + ServiceConstants.DescriptionController;
    }

    public static String ConstructDesc(String descHtml)
    {
        return ConstructDesc() + "mapHtml/";
    }
}

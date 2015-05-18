package com.tomfin46.myworldiscomics.DataModel.Enums;

import com.tomfin46.myworldiscomics.DataModel.Resources.BaseResource;
import com.tomfin46.myworldiscomics.DataModel.Resources.CharacterResource;
import com.tomfin46.myworldiscomics.DataModel.Resources.TeamResource;

/**
 * Created by Tom on 10/03/2015.
 */
public class ResourceTypes
{
    public enum ResourcesEnum
    {
        Character,
        Characters,
        Chat,
        Chats,
        Concept,
        Concepts,
        Episode,
        Episodes,
        Issue,
        Issues,
        Location,
        Locations,
        Movie,
        Movies,
        Object,
        Objects,
        Origin,
        Origins,
        Person,
        People,
        Power,
        Powers,
        Promo,
        Promos,
        Publisher,
        Publishers,
        Series,
        SeriesList,
        Search,
        StoryArc,
        StoryArcs,
        Team,
        Teams,
        Types,
        Video,
        Videos,
        VideoType,
        VideoTypes,
        Volume,
        Volumes,
        Error
    }

    public static String getResourceTerm(ResourcesEnum resourcesEnum)
    {
        switch (resourcesEnum)
        {
            case Character:
                return "character";
            case Characters:
                return "characters";
            case Search:
                return "search";
            case Team:
                return "team";
            case Teams:
                return "teams";
            case Issue:
                return "issue";
            case Issues:
                return "issues";
            case Person:
                return "person";
            case People:
                return "people";
            case Location:
                return "location";
            case Locations:
                return "locations";
            case Concept:
                return "concept";
            case Concepts:
                return "concepts";
            case Object:
                return "object";
            case Objects:
                return "objects";
            case StoryArc:
                return "story_arc";
            case StoryArcs:
                return "story_arcs";
            case Volume:
                return "volume";
            case Volumes:
                return "volumes";
            case Publisher:
                return "publisher";
            case Movie:
                return "movie";
            default:
                return "";
        }
    }

    public static Class GetResourceClass(ResourcesEnum resourcesEnum) {
        switch (resourcesEnum)
        {
            case Character:
                return CharacterResource.class;
            case Characters:
                break;
            case Chat:
                break;
            case Chats:
                break;
            case Concept:
                break;
            case Concepts:
                break;
            case Episode:
                break;
            case Episodes:
                break;
            case Issue:
                break;
            case Issues:
                break;
            case Location:
                break;
            case Locations:
                break;
            case Movie:
                break;
            case Movies:
                break;
            case Object:
                break;
            case Objects:
                break;
            case Origin:
                break;
            case Origins:
                break;
            case Person:
                break;
            case People:
                break;
            case Power:
                break;
            case Powers:
                break;
            case Promo:
                break;
            case Promos:
                break;
            case Publisher:
                break;
            case Publishers:
                break;
            case Series:
                break;
            case SeriesList:
                break;
            case Search:
                break;
            case StoryArc:
                break;
            case StoryArcs:
                break;
            case Team:
                return TeamResource.class;
            case Teams:
                break;
            case Types:
                break;
            case Video:
                break;
            case Videos:
                break;
            case VideoType:
                break;
            case VideoTypes:
                break;
            case Volume:
                break;
            case Volumes:
                break;
            case Error:
                break;
            default:
                return BaseResource.class;
        }

        return BaseResource.class;
    }

    /*public static ResourcesEnum GetResourcesEnum(Type resourceClassType) {
        return GetResourcesEnum(resourceClassType, false);
    }

    public static ResourcesEnum GetResourcesEnum(Type resourceClassType, boolean isCollection)

    {
        if (resourceClassType instanceof Character)
        {
            return isCollection ? Characters : Character;
        }
        else if (resourceClassType == typeof(Concept))
        {
            return isCollection ? Concepts : Concept;
        }
        else if (resourceClassType == typeof(Issue))
        {
            return isCollection ? Issues : Issue;
        }
        else if (resourceClassType == typeof(Location))
        {
            return isCollection ? Locations : Location;
        }
        else if (resourceClassType == typeof(Movie))
        {
            return isCollection ? Movies : Movie;
        }
        else if (resourceClassType == typeof(ObjectResource))
        {
            return isCollection ? Objects : Object;
        }
        else if (resourceClassType == typeof(Origin))
        {
            return isCollection ? Origins : Origin;
        }
        else if (resourceClassType == typeof(Person))
        {
            return isCollection ? People : Person;
        }
        else if (resourceClassType == typeof(Power))
        {
            return isCollection ? Powers : Power;
        }
        else if (resourceClassType == typeof(Publisher))
        {
            return isCollection ? Publishers : Publisher;
        }
        else if (resourceClassType == typeof(StoryArc))
        {
            return isCollection ? StoryArcs : StoryArc;
        }
        else if (resourceClassType == typeof(Team))
        {
            return isCollection ? teams : Team;
        }
        else if (resourceClassType == typeof(Volume))
        {
            return isCollection ? Volumes : Volume;
        }
        else
        {
            return Error;
        }
    }*/
}
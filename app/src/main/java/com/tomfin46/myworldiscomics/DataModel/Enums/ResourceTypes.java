package com.tomfin46.myworldiscomics.DataModel.Enums;

import com.tomfin46.myworldiscomics.Activities.CharacterActivity;
import com.tomfin46.myworldiscomics.Activities.GenericResourceActivity;
import com.tomfin46.myworldiscomics.Activities.IssueActivity;
import com.tomfin46.myworldiscomics.Activities.TeamActivity;
import com.tomfin46.myworldiscomics.DataModel.Resources.BaseResource;
import com.tomfin46.myworldiscomics.DataModel.Resources.CharacterResource;
import com.tomfin46.myworldiscomics.DataModel.Resources.ConceptResource;
import com.tomfin46.myworldiscomics.DataModel.Resources.IssueResource;
import com.tomfin46.myworldiscomics.DataModel.Resources.LocationResource;
import com.tomfin46.myworldiscomics.DataModel.Resources.ObjectResource;
import com.tomfin46.myworldiscomics.DataModel.Resources.PersonResource;
import com.tomfin46.myworldiscomics.DataModel.Resources.PublisherResource;
import com.tomfin46.myworldiscomics.DataModel.Resources.StoryArcResource;
import com.tomfin46.myworldiscomics.DataModel.Resources.TeamResource;
import com.tomfin46.myworldiscomics.DataModel.Resources.VolumeResource;

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
                return ConceptResource.class;
            case Concepts:
                break;
            case Episode:
                break;
            case Episodes:
                break;
            case Issue:
                return IssueResource.class;
            case Issues:
                break;
            case Location:
                return LocationResource.class;
            case Locations:
                break;
            case Movie:
                break;
            case Movies:
                break;
            case Object:
                return ObjectResource.class;
            case Objects:
                break;
            case Origin:
                break;
            case Origins:
                break;
            case Person:
                return PersonResource.class;
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
                return PublisherResource.class;
            case Publishers:
                break;
            case Series:
                break;
            case SeriesList:
                break;
            case Search:
                break;
            case StoryArc:
                return StoryArcResource.class;
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
                return VolumeResource.class;
            case Volumes:
                break;
            case Error:
                break;
            default:
                return BaseResource.class;
        }

        return BaseResource.class;
    }

    public static Class GetResourceActivityClass(ResourcesEnum resourcesEnum) {
        switch (resourcesEnum) {
            case Character:
                return CharacterActivity.class;
            case Issue:
                return IssueActivity.class;
            case Team:
                return TeamActivity.class;
            default:
                return GenericResourceActivity.class;
        }
    }
}

package com.tomfin46.myworldiscomics.DataModel.Resources;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Tom on 13/03/2015.
 */
public class CharacterResource extends BaseResource {
    public String RealNameFormattedString;
    public List<TeamResource> teams;

    public CharacterResource() {
        teams = new ArrayList<TeamResource>();
    }

    public String getRealNameFormattedString() {
        return RealNameFormattedString;
    }

    public void setRealNameFormattedString(String realNameFormattedString) {
        RealNameFormattedString = realNameFormattedString;
    }

    public List<TeamResource> getTeams() {
        return teams;
    }

    public void setTeams(List<TeamResource> teams) {
        this.teams = teams;
    }
}

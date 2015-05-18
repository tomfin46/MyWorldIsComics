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
}

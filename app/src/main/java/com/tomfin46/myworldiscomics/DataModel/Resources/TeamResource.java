package com.tomfin46.myworldiscomics.DataModel.Resources;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Tom on 18/05/2015.
 */
public class TeamResource extends BaseResource {
    public List<CharacterResource> character_enemies;
    public List<CharacterResource> characters_friends;
    public List<CharacterResource> characters;

    public TeamResource() {
        character_enemies = new ArrayList<>();
        characters_friends = new ArrayList<>();
        characters = new ArrayList<>();
    }

    public List<CharacterResource> getCharacter_enemies() {
        return character_enemies;
    }

    public void setCharacter_enemies(List<CharacterResource> character_enemies) {
        this.character_enemies = character_enemies;
    }

    public List<CharacterResource> getCharacters_friends() {
        return characters_friends;
    }

    public void setCharacters_friends(List<CharacterResource> characters_friends) {
        this.characters_friends = characters_friends;
    }

    public List<CharacterResource> getCharacters() {
        return characters;
    }

    public void setCharacters(List<CharacterResource> characters) {
        this.characters = characters;
    }
}

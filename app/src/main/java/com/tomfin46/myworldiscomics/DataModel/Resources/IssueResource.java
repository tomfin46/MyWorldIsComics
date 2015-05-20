package com.tomfin46.myworldiscomics.DataModel.Resources;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Tom on 19/05/2015.
 */
public class IssueResource extends BaseResource {
    public String CoverDateFormattedString;
    public String StoreDateFormattedString;
    public String IssueNumberFormattedString;
    public VolumeResource volume;

    public List<PersonResource> person_credits;
    public List<CharacterResource> character_credits;
    public List<TeamResource> team_credits;
    public List<LocationResource> location_credits;
    public List<ConceptResource> concept_credits;
    public List<ObjectResource> object_credits;
    public List<StoryArcResource> story_arc_credits;

    public IssueResource() {
        person_credits = new ArrayList<>();
        character_credits = new ArrayList<>();
        team_credits = new ArrayList<>();
        location_credits = new ArrayList<>();
        concept_credits = new ArrayList<>();
        object_credits = new ArrayList<>();
        story_arc_credits = new ArrayList<>();
    }

    public String getCoverDateFormattedString() {
        return CoverDateFormattedString;
    }

    public void setCoverDateFormattedString(String coverDateFormattedString) {
        CoverDateFormattedString = coverDateFormattedString;
    }

    public String getStoreDateFormattedString() {
        return StoreDateFormattedString;
    }

    public void setStoreDateFormattedString(String storeDateFormattedString) {
        StoreDateFormattedString = storeDateFormattedString;
    }

    public String getIssueNumberFormattedString() {
        return IssueNumberFormattedString;
    }

    public void setIssueNumberFormattedString(String issueNumberFormattedString) {
        IssueNumberFormattedString = issueNumberFormattedString;
    }

    public VolumeResource getVolume() {
        return volume;
    }

    public void setVolume(VolumeResource volume) {
        this.volume = volume;
    }

    public List<PersonResource> getPerson_credits() {
        return person_credits;
    }

    public void setPerson_credits(List<PersonResource> person_credits) {
        this.person_credits = person_credits;
    }

    public List<CharacterResource> getCharacter_credits() {
        return character_credits;
    }

    public void setCharacter_credits(List<CharacterResource> character_credits) {
        this.character_credits = character_credits;
    }

    public List<TeamResource> getTeam_credits() {
        return team_credits;
    }

    public void setTeam_credits(List<TeamResource> team_credits) {
        this.team_credits = team_credits;
    }

    public List<LocationResource> getLocation_credits() {
        return location_credits;
    }

    public void setLocation_credits(List<LocationResource> location_credits) {
        this.location_credits = location_credits;
    }

    public List<ConceptResource> getConcept_credits() {
        return concept_credits;
    }

    public void setConcept_credits(List<ConceptResource> concept_credits) {
        this.concept_credits = concept_credits;
    }

    public List<ObjectResource> getObject_credits() {
        return object_credits;
    }

    public void setObject_credits(List<ObjectResource> object_credits) {
        this.object_credits = object_credits;
    }

    public List<StoryArcResource> getStory_arc_credits() {
        return story_arc_credits;
    }

    public void setStory_arc_credits(List<StoryArcResource> story_arc_credits) {
        this.story_arc_credits = story_arc_credits;
    }
}

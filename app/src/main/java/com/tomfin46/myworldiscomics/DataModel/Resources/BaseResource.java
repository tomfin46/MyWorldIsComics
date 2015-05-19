package com.tomfin46.myworldiscomics.DataModel.Resources;

import org.json.JSONArray;

import java.io.Serializable;

/**
 * Created by Tom on 13/03/2015.
 */
public class BaseResource implements Serializable {
    public String AliasesOneLine;
    public int count_of_issue_appearances;
    public String deck;
    public String description;
    public JSONArray descriptionSections;
    public IssueResource first_appeared_in_issue;
    public int id;
    public ImageObj image;
    public String name;

    public BaseResource() {

    }

    public String getAliasesOneLine() {
        return AliasesOneLine;
    }

    public void setAliasesOneLine(String aliasesOneLine) {
        AliasesOneLine = aliasesOneLine;
    }

    public int getCount_of_issue_appearances() {
        return count_of_issue_appearances;
    }

    public void setCount_of_issue_appearances(int count_of_issue_appearances) {
        this.count_of_issue_appearances = count_of_issue_appearances;
    }

    public String getDeck() {
        return deck;
    }

    public void setDeck(String deck) {
        this.deck = deck;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public JSONArray getDescriptionSections() {
        return descriptionSections;
    }

    public void setDescriptionSections(JSONArray descriptionSections) {
        this.descriptionSections = descriptionSections;
    }

    public IssueResource getFirst_appeared_in_issue() {
        return first_appeared_in_issue;
    }

    public void setFirst_appeared_in_issue(IssueResource first_appeared_in_issue) {
        this.first_appeared_in_issue = first_appeared_in_issue;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public ImageObj getImage() {
        return image;
    }

    public void setImage(ImageObj image) {
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}


package com.tomfin46.myworldiscomics.DataModel.Resources;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Tom on 19/05/2015.
 */
public class VolumeResource extends BaseResource {

    public int count_of_issues;
    public List<IssueResource> issues;
    public String start_year;

    public VolumeResource() {
        issues = new ArrayList<>();
    }

    public int getCount_of_issues() {
        return count_of_issues;
    }

    public void setCount_of_issues(int count_of_issues) {
        this.count_of_issues = count_of_issues;
    }

    public List<IssueResource> getIssues() {
        return issues;
    }

    public void setIssues(List<IssueResource> issues) {
        this.issues = issues;
    }

    public String getStart_year() {
        return start_year;
    }

    public void setStart_year(String start_year) {
        this.start_year = start_year;
    }
}

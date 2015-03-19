package com.tomfin46.myworldiscomics.DataModel.Description;

import java.util.List;

/**
 * Created by Tom on 16/03/2015.
 */
public class Section {
    public String Title;
    public String Text;
    public String ImageSource;
    public List<Section> ContentQueue;
    public List<Link> Links;
    public String Type;

}

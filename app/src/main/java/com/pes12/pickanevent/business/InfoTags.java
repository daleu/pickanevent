package com.pes12.pickanevent.business;

import com.pes12.pickanevent.persistence.entity.Tag.TagEntity;

/**
 * Created by Clara on 25/11/2016.
 */
public class InfoTags {
    public String idTag;
    public String nombreTag;
    public Boolean checked;
    public TagEntity tagEnt;

    public InfoTags(String _nombreTag, Boolean _checked, String _idTag, TagEntity tagEnt) {
        super();
        this.nombreTag = _nombreTag;
        this.checked = _checked;
        this.idTag = _idTag;
        this.tagEnt = tagEnt;
    }

    public String getNombreTag() {
        return nombreTag;
    }

    public void setNombreTag(String _nombreTag) {
        this.nombreTag = _nombreTag;
    }

    public Boolean getChecked() {
        return checked;
    }

    public void setChecked(Boolean _checked) {
        this.checked = _checked;
    }

    public String getIdTag() {
        return idTag;
    }

    public TagEntity getEntity() {
        return tagEnt;
    }
}

package com.pes12.pickanevent.business;

/**
 * Created by Clara on 25/11/2016.
 */
public class InfoTags {
    public String idTag;
    public String nombreTag;
    public Boolean checked;

    public InfoTags(String _nombreTag, Boolean _checked, String _idTag) {
        super();
        this.nombreTag = _nombreTag;
        this.checked = _checked;
        this.idTag = _idTag;
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

}

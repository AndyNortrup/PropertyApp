package com.NortrupDevelopment.PropertyBook.model;

import java.util.AbstractList;

import io.realm.RealmList;
import io.realm.RealmObject;

public class PropertyBook extends RealmObject {
  private String description;
  private String uic;
  private String pbic;

  private RealmList<LineNumber> lineNumbers;

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public String getUic() {
    return uic;
  }

  public void setUic(String uic) {
    this.uic = uic;
  }

  public String getPbic() {
    return pbic;
  }

  public void setPbic(String pbic) {
    this.pbic = pbic;
  }

  public AbstractList<LineNumber> getLineNumbers() {
    return lineNumbers;
  }

  public void setLineNumbers(AbstractList<LineNumber> lineNumbers) {
    this.lineNumbers = lineNumbers;
  }
}

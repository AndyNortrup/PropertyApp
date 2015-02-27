package com.NortrupDevelopment.PropertyBook.model;

import java.util.AbstractList;

public class PropertyBookImpl implements PropertyBook {
  private String description;
  private String uic;
  private String pbic;
  private AbstractList<LineNumber> lineNumbers;

  public PropertyBookImpl(String description,
                          String uic,
                          String pbic,
                          AbstractList<LineNumber> lineNumbers) {
    this.description = description;
    this.uic = uic;
    this.pbic = pbic;
    this.lineNumbers = lineNumbers;
  }

  @Override
  public String getDescription() {
    return description;
  }

  @Override
  public void setDescription(String description) {
    this.description = description;
  }

  @Override
  public String getUic() {
    return uic;
  }

  @Override
  public void setUic(String uic) {
    this.uic = uic;
  }

  @Override
  public String getPbic() {
    return pbic;
  }

  @Override
  public void setPbic(String pbic) {
    this.pbic = pbic;
  }

  @Override
  public AbstractList<LineNumber> getLineNumbers() {
    return lineNumbers;
  }

  @Override
  public void setLineNumbers(AbstractList<LineNumber> lineNumbers) {
    this.lineNumbers = lineNumbers;
  }
}

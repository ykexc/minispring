package com.test.mvc;

import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import com.minis.beans.PropertyEditor;
import com.minis.util.NumberUtils;
import com.minis.util.StringUtils;

public class CustomDateEditor implements PropertyEditor {     
    private Class<Date> dateClass;
    private DateTimeFormatter datetimeFormatter;
    private boolean allowEmpty;
    private Date value;
  public CustomDateEditor() throws IllegalArgumentException {
    this(Date.class, "yyyy-MM-dd", true);
  }    
  public CustomDateEditor(Class<Date> dateClass) throws IllegalArgumentException {
    this(dateClass, "yyyy-MM-dd", true);
  }    
  public CustomDateEditor(Class<Date> dateClass,
          boolean allowEmpty) throws IllegalArgumentException {
    this(dateClass, "yyyy-MM-dd", allowEmpty);
  }  
  public CustomDateEditor(Class<Date> dateClass,
        String pattern, boolean allowEmpty) throws IllegalArgumentException {
    this.dateClass = dateClass;
    this.datetimeFormatter = DateTimeFormatter.ofPattern(pattern);
    this.allowEmpty = allowEmpty;
  }
    public void setAsText(String text) {
    if (this.allowEmpty && !StringUtils.hasText(text)) {
      setValue(null);
    }      
    else {
      LocalDate localdate = LocalDate.parse(text, datetimeFormatter);
      setValue(Date.from(localdate.atStartOfDay(ZoneId.systemDefault()).toInstant()));
    }
    }
    public void setValue(Object value) {         
            this.value = (Date) value;
    }
    public String getAsText() {
        Date value = this.value;
    if (value == null) {
      return "";
    }
    else {
      LocalDate localDate = value.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
      return localDate.format(datetimeFormatter);
    }
    }
    public Object getValue() {         
        return this.value;
    }
}
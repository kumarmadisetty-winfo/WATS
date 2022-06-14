package com.winfo.constants;

public enum ActionEnum {
	
	CLICK_ACTION("clickAction"),
	TYPE_ACTION("typeAction"),
	TAB("tab"),
	PASTE("paste"),
	DOUBLE_CLICK("doubleClick"),
	PAGE_UP("pageUp"),
	ENTER("enter")
	;


//    private final String key;
    private final String value;

    ActionEnum(String  value) {
//        this.key = key;
        this.value = value;
    }

	 public String getValue() {
        return value;
    }

}


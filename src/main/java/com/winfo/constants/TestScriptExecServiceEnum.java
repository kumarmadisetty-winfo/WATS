package com.winfo.constants;

public enum TestScriptExecServiceEnum {
    BACK_SLASH("\\\\"),
    TOPIC("test-script-run"),
    PY_EXTN(".py"),
    JPG_EXTENSION(".jpg"),
    PASSED("Passed"),
    FAILED("Failed"),
    FAIL("Fail"),
    PDF_EXTENSION(".pdf"),
    SCREENSHOT("Screenshot"),
    FORWARD_SLASH("/"),
    PNG_EXTENSION(".png");

    private final String value;

    TestScriptExecServiceEnum(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}

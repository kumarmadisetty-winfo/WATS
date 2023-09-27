package com.winfo.exception;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;

@Data
public class ErrorResponse {
    private Integer code = null;
    private List<String> message = new ArrayList<>();
    
    public ErrorResponse(Integer code, String message) {
       this.code = code;
       this.message.add(message);
    }
    
    public ErrorResponse(Integer code, List<String> messages) {
       this.code = code;
       this.message= messages;
    }
    
    public void setMessage(String message) {
       this.message.add(message);
    }
    public void setMessage(List<String> messages) {
       this.message = messages;
    }
    
    public void setCode(Integer code) {
       this.code = code;
    }
}
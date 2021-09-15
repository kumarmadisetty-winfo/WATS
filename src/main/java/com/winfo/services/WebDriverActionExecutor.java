package com.winfo.services;

import org.openqa.selenium.WebDriverException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.winfo.interface1.IWebDriverAction;

public class WebDriverActionExecutor {
	 private final Logger logger = LoggerFactory.getLogger(WebDriverActionExecutor.class);
	    private int max_retry_times;

	    public WebDriverActionExecutor() {
	        this(3);
	    }

	    public WebDriverActionExecutor(int max_retry_times) {
	        this.max_retry_times = max_retry_times;
	    }

	    public void execute(IWebDriverAction action) {
	        execute(action, WebDriverException.class);
	    }

	    /**
	     * @param action
	     * @param exceptionType any exception such as UnreachableBrowserException, TimeoutException, WebDriverException
	     * @param <T>
	     */
	    public <T extends Exception> void execute(IWebDriverAction action, Class<T> exceptionType) {
	        execute(action, 0, exceptionType);
	    }

	    private <T extends Exception> void execute(IWebDriverAction action, int retries, Class<T> exceptionType) {
	        if (retries > max_retry_times) {
	            throw new RuntimeException("exceed retry times");
	        } else {
	            if (retries > 0) {
	            	logger.warn("{} occurred, retry {}", exceptionType.getName(), retries);
	            }
	            try {
	                action.execute();
	            } catch (Exception e) {
	                if (exceptionType.isInstance(e)) {
	                    execute(action, ++retries, exceptionType);
	                } else {
	                    throw new RuntimeException(e);
	                }
	            }
	        }
	    }
}

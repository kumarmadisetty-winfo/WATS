package com.winfo.config;

import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public final class MessageUtil {

	private static final String MESSAGE_FILE = "messages.properties";
	private static final Map<String, String> messageMap = new HashMap<>();

	static {
		reloadMessages();
		URL resource = MessageUtil.class.getClassLoader().getResource(MESSAGE_FILE);
		Path path = null;
		try {
			path = Paths.get(resource.toURI()).toAbsolutePath().getParent();
		} catch (URISyntaxException e1) {
			e1.printStackTrace();
		}

		try {
			WatchService watchService = FileSystems.getDefault().newWatchService();
			path.register(watchService, StandardWatchEventKinds.ENTRY_MODIFY);
			Thread watchThread = new Thread(() -> {
				while (true) {
					try {
						WatchKey key = watchService.take();
						for (WatchEvent<?> event : key.pollEvents()) {
							if (event.context().toString().equals(MESSAGE_FILE)) {
								reloadMessages();
							}
						}
						key.reset();
					} catch (InterruptedException e) {
						e.printStackTrace();
						Thread.currentThread().interrupt();
					}
				}
			});
			watchThread.setDaemon(true);
			watchThread.start();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static void reloadMessages() {
		try (InputStream input = MessageUtil.class.getClassLoader().getResourceAsStream(MESSAGE_FILE)) {
			Properties properties = new Properties();
			properties.load(input);
			for (String key : properties.stringPropertyNames()) {
				String value = properties.getProperty(key);
				messageMap.put(key, value);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static String getMessage(String code, Object... args) {
		String message = messageMap.get(code);
		if (message == null) {
			return code;
		}
		return MessageFormat.format(message, args);
	}

	private MessageUtil() {
		throw new IllegalStateException("Utility class");
	}
}

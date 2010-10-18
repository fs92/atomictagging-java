package org.atomictagging.img.metadataextractor;

public class KeyValue {
	
	public KeyValue(String key, String value) {
		this.key = key;
		this.value = value;
	}
	
	public String key;
	public String value;
	
	@Override
	public String toString() {
		return "key: " + key + "   value:" + value;
	}
	
}

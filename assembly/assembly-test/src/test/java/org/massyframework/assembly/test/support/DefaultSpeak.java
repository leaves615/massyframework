package org.massyframework.assembly.test.support;

public class DefaultSpeak implements Speak {

	public DefaultSpeak() {
		
	}

	@Override
	public void say(String text) {
		System.out.println("read \r\n\t" + text);
	}

}

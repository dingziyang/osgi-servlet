package com.dcm.osgi.remotetranslateasslt.impls;

import java.util.concurrent.ConcurrentHashMap;

import com.dcm.osgi.translate.service.TranslateService;

public class TranslateServiceRemoteImpl implements TranslateService {

	private static final ConcurrentHashMap<String, String> dictonary = new ConcurrentHashMap<String, String>();
	
	static {
		dictonary.put("cat", "小猫");
		dictonary.put("dog", "小狗");
	}

	@Override
	public String translate(String word) {
		System.out.println("RemoteTranslateAsslt Service---------->");
		String result = dictonary.get(word);
		if (null == result) {
			result = "remotetranslateasslt:未找到您所查单词意义，请检查单词是否正确!";
		}
		return result;
	}
	
}

package com.dit.chatapp.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.swing.plaf.basic.BasicFormattedTextFieldUI;

import com.mysql.cj.protocol.Message;

public interface Encryption {
	public static String passwordEncrpt(String plainPassword) throws NoSuchAlgorithmException {
		String encryptedPassword=null;
		MessageDigest messageDigest=MessageDigest.getInstance("MD5");
		messageDigest.update(plainPassword.getBytes());
		byte [] encrypt=messageDigest.digest();
		StringBuffer sb=new StringBuffer();
		for(byte b: encrypt ) {
			sb.append(b);
		}
		encryptedPassword=sb.toString();
		return encryptedPassword;
	}
	

}

package org.opencommunity.util;

import java.util.Random;

public class AutomaticPassword 
{
static private String vocali ="aeiou";
static private String consonanti = "bbccddffgghllmmnnppqrrssttvvz";
static private Random rnd = new Random();
	
public static String newPassword(int size)
	{
	String code = "";
	for(int i=0;i<size/2;i++)
		code+=grab(consonanti)+grab(vocali);
	return code;
	}
public static String newPassword() 
	{	
	return (grab(consonanti)+grab(vocali)+grab(consonanti)+grab(vocali)+grab(consonanti)+grab(vocali)).toUpperCase();
	}
private static String grab(String value)			
	{
	int pos = rnd.nextInt(value.length());
	return value.substring(pos,pos+1);
	}

}
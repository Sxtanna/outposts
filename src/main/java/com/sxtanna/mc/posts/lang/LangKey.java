package com.sxtanna.mc.posts.lang;

public enum LangKey
{

	OUTPOST_ENTER("You have entered {outpost_name}!"),
	OUTPOST_LEAVE("You have left {outpost_name}!");


	private final String defaultMessage;


	LangKey(final String defaultMessage)
	{
		this.defaultMessage = defaultMessage;
	}


	public String getDefaultMessage()
	{
		return defaultMessage;
	}

}

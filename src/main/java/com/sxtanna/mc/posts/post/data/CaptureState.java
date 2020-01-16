package com.sxtanna.mc.posts.post.data;

public enum CaptureState
{

	//<editor-fold desc="finalized states">
	/**
	 * No one is in the outpost
	 */
	NEUTRAL,
	/**
	 * The outpost is claimed by a faction
	 */
	CLAIMED,
	//</editor-fold>


	//<editor-fold desc="progressing states">
	/**
	 * The outpost is being captured by a faction
	 */
	CAPTURING,
	/**
	 * The outpost's capture progress is going down (capturing cut short)
	 */
	DWINDLING,
	/**
	 * The outpost's claim is being unset by a different faction
	 */
	UNSEATING,
	//</editor-fold>


	//<editor-fold desc="contesting states">
	/**
	 * The capturing is being contested by another faction
	 */
	CONTESTED_CAPTURING,
	/**
	 * The unseating is being contested by the currently claimed faction
	 */
	CONTESTED_UNSEATING,
	//</editor-fold>

}
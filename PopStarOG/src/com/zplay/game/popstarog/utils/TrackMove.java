package com.zplay.game.popstarog.utils;

import java.util.List;

import com.zplay.game.popstarog.sprite.Position;

/**
 * 每次操作的记录，用于rollBack道具回溯之前的操作
 * 
 * @author glzlaohuai
 * @version 2014-8-23
 */
public class TrackMove {
	// 动作类型：[消除、使用转换道具]（之后如果有新加道具的话，可能会有更多的类型）
	public enum TrackMoveType {
		EXPLOADE, SWITCH, HAMMER
	};

	private TrackMoveType trackMoveType;

	private List<Position> originalSpriteList;
	private List<Position> randomSpriteList;

	private List<ExploadeTrackInform> exploadeSpriteList;
	private List<Integer> emptyColumnList;

	public TrackMove(TrackMoveType trackMoveType,
			List<Position> originalSpriteList, List<Position> randomSpriteList,
			List<ExploadeTrackInform> exploadeSpriteList,
			List<Integer> emptyColumnList) {
		this.trackMoveType = trackMoveType;
		this.originalSpriteList = originalSpriteList;
		this.randomSpriteList = randomSpriteList;
		this.exploadeSpriteList = exploadeSpriteList;
		this.emptyColumnList = emptyColumnList;
	}

	public TrackMoveType getTrackMoveType() {
		return trackMoveType;
	}

	public List<Position> getOriginalSpriteList() {
		return originalSpriteList;
	}

	public List<Position> getRandomSpriteList() {
		return randomSpriteList;
	}

	public List<ExploadeTrackInform> getExploadeSpriteList() {
		return exploadeSpriteList;
	}

	public List<Integer> getEmptyColumnList() {
		return emptyColumnList;
	}

}

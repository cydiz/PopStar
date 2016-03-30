#pragma once

#include "cocos2d.h"
#include "Constant.h"

USING_NS_CC;

class Block : public Sprite
{
public:
	Block();
	~Block();

	static			CCSize	s_scale;

	void			blockColor(BlockColor val){ _blockColor = val; }

	static			Block* createWithFile(String fileName, bool useBatchNode);
	Block*			initWithFile(String filename, bool useBatchNode);

private:
	Sprite*			_blockOutLine;
	BlockColor		_blockColor;

};


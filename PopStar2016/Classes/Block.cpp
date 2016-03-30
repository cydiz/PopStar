#include "Block.h"
#include "MainGameScene.h"

CCSize	Block::s_scale(1.f, 1.f);

Block::Block()
{
}

Block::~Block()
{
}

Block* Block::createWithFile(String fileName, bool useBatchNode)
{
	Block* pBlock = new Block();
	pBlock->initWithFile(fileName, useBatchNode);
	pBlock->autorelease();
	return pBlock;
}

Block* Block::initWithFile(String filename, bool useBatchNode)
{
	bool flag = false;
	CCString* finalName = CCString::create(filename.getCString());

	flag = Sprite::initWithFile(finalName->getCString());
	if (!flag)
	{
		return 0;
	}

	_blockOutLine = Sprite::create("block_select.png");
	_blockOutLine->setVisible(false);
	addChild(_blockOutLine, 1);

	return this;
}
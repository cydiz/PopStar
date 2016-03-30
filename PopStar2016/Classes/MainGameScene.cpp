#include "MainGameScene.h"
#include "cocostudio/CocoStudio.h"
#include "ui/CocosGUI.h"
#include "Constant.h"
#include "Block.h"

USING_NS_CC;

using namespace cocostudio::timeline;
using namespace ui;

int MainGame::_bannerOffset = 0;

Scene* MainGame::createScene()
{
    auto scene = Scene::create();
	auto layer = MainGame::create();
    scene->addChild(layer);
    return scene;
}

bool MainGame::init()
{
    if ( !Layer::init() )
    {
        return false;
    }

	_showPopup = false;
    
    auto rootNode = CSLoader::createNode("MainGameScene.csb");
	auto spriteBg = rootNode->getChildByName("sprite_bg");
    addChild(rootNode);

	//游戏暂停按钮
	auto btnPause = dynamic_cast<Button*>(spriteBg->getChildByName("btn_pause"));
	if (!showPopup())
		btnPause->addClickEventListener(CC_CALLBACK_1(MainGame::showPauseDialog, this));

	//创建10 * 10星星
	for (int i = 0; i < 10; i++)
	{
		for (int j = 0; j < 10; j++)
		{
			Block* block = createBlockAtRow(i, j, (BlockColor)(rand() % kMaxBlockColor));
		}
	}

    return true;
}

//创建单个的格子
Block* MainGame::createBlockAtRow(int row, int col, BlockColor color)
{
	Block* block = NULL;
	switch (color)
	{
	case kRed:
		block = Block::createWithFile("block_red.png", true);
		break;
	case kBlue:
		block = Block::createWithFile("block_blue.png", true);
		break;
	case kGreen:
		block = Block::createWithFile("block_green.png", true);
		break;
	case kYellow:
		block = Block::createWithFile("block_yellow.png", true);
		break;
	case kPurple:
		block = Block::createWithFile("block_purple.png", true);
		break;
	case kMaxBlockColor:
		block = NULL; break;
	};
	block->blockColor(color);
	addChild(block);
	block->setPosition(ccp(kBlockWidth * (col + 0.5f), _bannerOffset + kBlockHeight * (row + 0.5f)));
	return block;
}

void MainGame::showPauseDialog(Ref* pSender)
{
	setShowPopup(true);
	auto settingLayer = CSLoader::createNode("SettingLayer.csb");
	auto settingBg = settingLayer->getChildByName("setting_bg");
	addChild(settingLayer);

	auto btnClose = dynamic_cast<Button*>(settingBg->getChildByName("btn_close"));
	btnClose->addClickEventListener([=](Ref* sender){
		settingLayer->removeFromParent();
		setShowPopup(false);
	});
}

void MainGame::closeSettingDialog(Ref* pSender)
{

}
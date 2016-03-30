#include "MainMenuScene.h"
#include "cocostudio/CocoStudio.h"
#include "ui/CocosGUI.h"
#include "MainGameScene.h"
#include "ShopScene.h"

USING_NS_CC;

using namespace cocostudio::timeline;
using namespace ui;

Scene* MainMenu::createScene()
{
    auto scene = Scene::create();
	auto layer = MainMenu::create();
    scene->addChild(layer);
    return scene;
}

bool MainMenu::init()
{
    if ( !Layer::init() )
    {
        return false;
    }
    
    auto rootNode = CSLoader::createNode("MainMenuScene.csb");
	auto spriteBg = rootNode->getChildByName("sprite_bg");
    addChild(rootNode);

	auto btnStartGame = dynamic_cast<Button*>(spriteBg->getChildByName("btn_start_game"));
	btnStartGame->addClickEventListener(CC_CALLBACK_1(MainMenu::startGame, this));

	auto btnIntoShop = dynamic_cast<Button*>(spriteBg->getChildByName("btn_into_shop"));
	btnIntoShop->addClickEventListener(CC_CALLBACK_1(MainMenu::intoShop, this));

    return true;
}

void MainMenu::startGame(Ref *pSender)
{
	Director::sharedDirector()->replaceScene(MainGame::createScene());
}

void MainMenu::intoShop(Ref *pSender)
{
	Director::sharedDirector()->replaceScene(Shop::createScene());
}

Node* MainMenu::seekFromRootByName(Node* root, std::string& name)
{
	if (!root)
		return nullptr;
	if (root->getName() == name)
		return root;

	const auto& arrayNode = root->getChildren();
	for (auto& child : arrayNode)
	{
		Node* pNode = dynamic_cast<Node*>(child);
		if (pNode)
		{
			Node* res = seekFromRootByName(pNode, name);
			if (res)
				return res;
		}
	}
	return nullptr;
}

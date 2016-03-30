#include "ShopScene.h"
#include "cocostudio/CocoStudio.h"
#include "ui/CocosGUI.h"

USING_NS_CC;

using namespace cocostudio::timeline;

Scene* Shop::createScene()
{
    auto scene = Scene::create();
	auto layer = Shop::create();
    scene->addChild(layer);
    return scene;
}

bool Shop::init()
{
    if ( !Layer::init() )
    {
        return false;
    }
    
    auto rootNode = CSLoader::createNode("ShopScene.csb");
	auto spriteBg = rootNode->getChildByName("sprite_bg");
    addChild(rootNode);

    return true;
}
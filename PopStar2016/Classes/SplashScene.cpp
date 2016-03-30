#include "SplashScene.h"
#include "cocostudio/CocoStudio.h"
#include "ui/CocosGUI.h"
#include "MainMenuScene.h"

USING_NS_CC;

using namespace cocostudio::timeline;

Scene* Splash::createScene()
{
    auto scene = Scene::create();
	auto layer = Splash::create();
    scene->addChild(layer);
    return scene;
}

bool Splash::init()
{
    if ( !Layer::init() )
    {
        return false;
    }
    
    auto rootNode = CSLoader::createNode("SplashScene.csb");
    addChild(rootNode);
	schedule(schedule_selector(Splash::splashChange), 2);

    return true;
}

void Splash::splashChange(float dt)
{
	CCDirector::sharedDirector()->replaceScene(MainMenu::createScene());
}

#ifndef __MAINMENU_SCENE_H__
#define __MAINMENU_SCENE_H__

#include "cocos2d.h"

class MainMenu : public cocos2d::Layer
{
public:
    static cocos2d::Scene* createScene();

    virtual bool init();

	CREATE_FUNC(MainMenu);

private:
	Node* seekFromRootByName(Node* root, std::string& name);

	void startGame(Ref* pSender);
	void intoShop(Ref* pSender);

};

#endif // __MAINMENU_SCENE_H__

#ifndef __MAIN_GAME_SCENE_H__
#define __MAIN_GAME_SCENE_H__

#include "cocos2d.h"
#include "Constant.h"
#include "Block.h"

class MainGame : public cocos2d::Layer
{
public:
    static cocos2d::Scene* createScene();

    virtual bool init();

	CREATE_FUNC(MainGame);

private:
	static int		_bannerOffset;
	bool            _showPopup;


	bool	showPopup(){ return _showPopup; }
	void	setShowPopup(bool val){ _showPopup = val; }


	Block*	createBlockAtRow(int row, int col, BlockColor color);

	void	showPauseDialog(Ref* pSender);
	void	closeSettingDialog(Ref* pSender);

};

#endif // __MAIN_GAME_SCENE_H__

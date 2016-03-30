#ifndef PopStar2016_Const_h
#define PopStar2016_Const_h

#define kScreenWidth		720
#define kScreenHeight		1280
#define MAX_COLUMN			10
#define kPlateWidth			(kBlockWidth * MAX_COLUMN)
#define LEFT_MARGIN			(int)((kScreenWidth - kPlateWidth) / 2)
#define BOTTOM_MARGIN		2

#define kBlockWidth			(76.8f * Block::s_scale.width)
#define kBlockHeight		(76.8f * Block::s_scale.height)

typedef enum
{
	kRed,
	kGreen,
	kBlue,
	kYellow,
	kPurple,
	kMaxBlockColor,
} BlockColor;

#endif

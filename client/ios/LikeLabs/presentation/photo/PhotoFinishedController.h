#import <UIKit/UIKit.h>
#import "ChildController.h"

@interface PhotoFinishedController : UIViewController <ChildController>
@property (retain, nonatomic) IBOutlet UIButton *button;
@property (retain, nonatomic) IBOutlet UILabel *lbMessageSent;
@property (retain, nonatomic) IBOutlet UIImageView *thankYouImg;
@property (retain, nonatomic) IBOutlet UIView *instructionsBackground;
- (IBAction)goHome:(id)sender;

@end

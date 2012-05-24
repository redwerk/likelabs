#import <UIKit/UIKit.h>
#import "ChildController.h"

@interface PhotoFinishedController : UIViewController <ChildController>
@property (retain, nonatomic) IBOutlet UIButton *button;
@property (retain, nonatomic) IBOutlet UILabel *lbMessageSent;
- (IBAction)goHome:(id)sender;

@end

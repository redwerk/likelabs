#import <UIKit/UIKit.h>
#import "ChildController.h"

@interface PhotoFinishedController : UIViewController <ChildController>
@property (retain, nonatomic) IBOutlet UIButton *button;
- (IBAction)goHome:(id)sender;

@end

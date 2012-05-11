#import <UIKit/UIKit.h>
#import "ChildController.h"

@interface WelcomeScreenController : UIViewController<ChildController>
- (IBAction)showTextScreen:(id)sender;
- (IBAction)showPhotoScreen:(id)sender;

@end

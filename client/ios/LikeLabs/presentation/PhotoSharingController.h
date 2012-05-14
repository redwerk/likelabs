#import <UIKit/UIKit.h>
#import "ChildController.h"
#import "CustomizableSegmentedControl.h"

@interface PhotoSharingController : UIViewController <ChildController, CustomizableSegmentedControlDelegate>
- (IBAction)goHome:(id)sender;
@property (retain, nonatomic) IBOutlet UISegmentedControl *segmentedControl;
@property (retain, nonatomic) IBOutlet UIView *headerView;

@end

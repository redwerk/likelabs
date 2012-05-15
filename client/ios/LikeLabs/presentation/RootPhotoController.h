#import <UIKit/UIKit.h>
#import "ChildController.h"
#import "CustomizableSegmentedControl.h"
#import "ContainerController.h"
#import "RootController.h"

@interface RootPhotoController : UIViewController <ChildController, CustomizableSegmentedControlDelegate, ContainerController>
@property (retain, nonatomic) RootController* rootController;
@property (retain, nonatomic) IBOutlet UISegmentedControl *segmentedControl;
@property (retain, nonatomic) IBOutlet UIView *headerView;

- (IBAction)goHome:(id)sender;
- (void)step;

@end

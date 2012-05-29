#import <UIKit/UIKit.h>
#import "ChildController.h"
#import "CustomizableSegmentedControl.h"
#import "ContainerController.h"
#import "RootController.h"

@interface RootMessageController : UIViewController <ChildController, CustomizableSegmentedControlDelegate, ContainerController>
@property (retain, nonatomic) RootController* rootController;
@property (retain, nonatomic) IBOutlet UIView *headerView;
@property (retain, nonatomic) IBOutlet UIImageView *navigationBackground;

- (IBAction)goHome:(id)sender;
- (void)step;
- (NSMutableDictionary *) getDividers;
- (NSMutableArray*) getButtons;

@end

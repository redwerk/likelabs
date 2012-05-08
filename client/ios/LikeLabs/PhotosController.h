#import <UIKit/UIKit.h>
#import "ChildController.h"
#import "RootControllerProtocol.h"

@interface PhotosController : UIViewController<ChildController, RootControllerProtocol>

@property (retain, nonatomic) IBOutlet UIView *navBar;
@property (retain, nonatomic) IBOutlet UIView *contentView;
@property (retain, nonatomic) IBOutlet UIImageView *navigationBackground;
@property (retain, nonatomic) IBOutlet UISegmentedControl *segmentedControl;
@property (retain, nonatomic) IBOutlet UIView *headerView;
- (IBAction)goHome:(id)sender;

@end

#import <UIKit/UIKit.h>
#import "ChildController.h"

@interface PhotosController : UIViewController<ChildController>
@property (retain, nonatomic) IBOutlet UIView *navBar;
@property (retain, nonatomic) IBOutlet UIView *contentView;
@property (retain, nonatomic) IBOutlet UIImageView *navigationBackground;
@property (retain, nonatomic) IBOutlet UISegmentedControl *segmentedControl;
- (IBAction)goHome:(id)sender;

@end

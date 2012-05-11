#import <UIKit/UIKit.h>
#import "ChildController.h"

@interface PhotoSelectionController : UIViewController <ChildController>
- (IBAction)goHome:(id)sender;
@property (retain, nonatomic) IBOutlet UISegmentedControl *segmentedControl;
@property (retain, nonatomic) IBOutlet UIImageView *navigationBackground;
@property (retain, nonatomic) IBOutlet UIImageView *imageView;
@property (retain, nonatomic) IBOutlet UIView *thumbNailsView;
- (IBAction)shareThisPhoto:(id)sender;
- (IBAction)navigationChanged:(UISegmentedControl *)sender;

@end

#import <UIKit/UIKit.h>
#import "ChildController.h"
#import "CustomizableSegmentedControl.h"

@interface PhotosController : UIViewController<ChildController, CustomizableSegmentedControlDelegate>

@property (retain, nonatomic) IBOutlet UIView *navBar;
@property (retain, nonatomic) IBOutlet UIView *contentView;
@property (retain, nonatomic) IBOutlet UIImageView *navigationBackground;
@property (retain, nonatomic) IBOutlet UISegmentedControl *segmentedControl;
@property (retain, nonatomic) IBOutlet UIView *instructionalView;
@property (retain, nonatomic) IBOutlet UIButton *button;
@property (retain, nonatomic) IBOutlet UILabel *whenYouAreReadyLabel;
@property (retain, nonatomic) IBOutlet UILabel *photosWillBeTakenLabel;
@property (retain, nonatomic) IBOutlet UILabel *getReadyLabel;
- (IBAction)takePhotos:(id)sender;
- (IBAction)goHome:(id)sender;

@end

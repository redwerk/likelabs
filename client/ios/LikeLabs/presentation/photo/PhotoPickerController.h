#import <UIKit/UIKit.h>
#import "ChildController.h"
#import "ContainerController.h"

@interface PhotoPickerController : UIViewController <ChildController, ContainerController>

@property (retain, nonatomic) IBOutlet UILabel *label;
@property (retain, nonatomic) IBOutlet UIView *messageView;
@property (retain, nonatomic) IBOutlet UILabel *messageLabel;
@property (retain, nonatomic) IBOutlet UIButton *startButton;

- (IBAction)start:(id)sender;
@end

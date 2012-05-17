#import <UIKit/UIKit.h>
#import "ChildController.h"

@interface PhotoMessageController : UIViewController <ChildController, UITextViewDelegate>
@property (retain, nonatomic) IBOutlet UISegmentedControl *segmentedControl;
@property (retain, nonatomic) IBOutlet UIImageView *navigationBackground;
@property (retain, nonatomic) IBOutlet UITextView *textView;
@property (retain, nonatomic) IBOutlet UIImageView *imageView;
- (IBAction)next:(id)sender;

@end

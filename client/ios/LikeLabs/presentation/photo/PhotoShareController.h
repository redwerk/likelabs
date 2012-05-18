#import <UIKit/UIKit.h>
#import "ChildController.h"
#import "UIImageWithReview.h"

@interface PhotoShareController : UIViewController <ChildController, UITextFieldDelegate>
@property (retain, nonatomic) UIImageWithReview *imageView;
@property (retain, nonatomic) IBOutlet UITextField *phoneField;
@property (retain, nonatomic) IBOutlet UIView *addRecipientsView;
@property (retain, nonatomic) IBOutlet UIButton *submitBtn;
@property (retain, nonatomic) IBOutlet UIButton *mailButton;
@property (retain, nonatomic) IBOutlet UIButton *phoneButton;
- (IBAction)fieldTouched:(id)sender;
- (IBAction)addMail:(id)sender;
- (IBAction)addPhone:(id)sender;
- (IBAction)submit:(id)sender;

@end

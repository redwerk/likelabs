#import <UIKit/UIKit.h>
#import "ChildController.h"

@interface PhotoShareController : UIViewController <ChildController, UITextFieldDelegate>
@property (retain, nonatomic) IBOutlet UIImageView *imageView;
@property (retain, nonatomic) IBOutlet UITextField *phoneField;
@property (retain, nonatomic) IBOutlet UIView *addRecipientsView;
@property (retain, nonatomic) IBOutlet UIButton *submitBtn;
@property (retain, nonatomic) IBOutlet UIImageView *reviewTextBgView;
@property (retain, nonatomic) IBOutlet UIButton *mailButton;
@property (retain, nonatomic) IBOutlet UITextView *textView;
@property (retain, nonatomic) IBOutlet UIButton *phoneButton;
- (IBAction)fieldTouched:(id)sender;
- (IBAction)addMail:(id)sender;
- (IBAction)addPhone:(id)sender;

@end

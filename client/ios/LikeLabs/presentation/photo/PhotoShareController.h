#import <UIKit/UIKit.h>
#import "ChildController.h"
#import "UIImageWithReview.h"

@interface PhotoShareController : UIViewController <ChildController, UITextFieldDelegate>
@property (retain, nonatomic) UIImageWithReview *imageView;
@property (retain, nonatomic) UIImageView *messageView;
@property (retain, nonatomic) IBOutlet UITextField *phoneField;
@property (retain, nonatomic) IBOutlet UIView *addRecipientsView;
@property (retain, nonatomic) IBOutlet UIButton *submitBtn;
@property (retain, nonatomic) IBOutlet UIButton *mailButton;
@property (retain, nonatomic) IBOutlet UIButton *phoneButton;
@property (retain, nonatomic) IBOutlet UILabel *lbTitle;
@property (retain, nonatomic) IBOutlet UIView *instructionsBackground;
@property (retain, nonatomic) IBOutlet UILabel *thirdStepLabel;
@property (retain, nonatomic) IBOutlet UIImageView *requiredLabel;
@property (retain, nonatomic) IBOutlet UILabel *recipientsCountLabel;
- (IBAction)fieldTouched:(id)sender;
- (IBAction)addMail:(id)sender;
- (IBAction)addPhone:(id)sender;
- (IBAction)submit:(id)sender;

@end

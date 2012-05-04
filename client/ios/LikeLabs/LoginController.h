#import <UIKit/UIKit.h>
#import "ChildController.h"

@interface LoginController : UIViewController <UITextFieldDelegate, ChildController>

@property (retain, nonatomic) IBOutlet UITextField *inputCode;
@property (retain, nonatomic) IBOutlet UITextField *inputPassword;

@property (retain, nonatomic) IBOutlet UIButton *submitButton;
- (IBAction)formSubmit:(id)sender;

@end

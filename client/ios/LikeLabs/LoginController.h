#import <UIKit/UIKit.h>

@interface LoginController : UIViewController

@property (strong, nonatomic) IBOutlet UITextField *inputCode;
@property (strong, nonatomic) IBOutlet UITextField *inputPassword;

@property (strong, nonatomic) IBOutlet UIButton *submitButton;
- (IBAction)formSubmit:(id)sender;

@end

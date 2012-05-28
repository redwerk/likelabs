#import <UIKit/UIKit.h>
#import "ChildController.h"

typedef enum {ControllerModeLogin, ControllerModeLogout} ControllerMode;

@interface LoginController : UIViewController <UITextFieldDelegate, ChildController, UIAlertViewDelegate>

extern NSString *const kLogoutViewDidDismiss;

@property (retain, nonatomic) IBOutlet UITextField *inputCode;
@property (retain, nonatomic) IBOutlet UITextField *inputPassword;
@property (assign, nonatomic) ControllerMode mode;

@property (retain, nonatomic) IBOutlet UIButton *submitButton;
- (IBAction)formSubmit:(id)sender;
- (IBAction)logout:(id)sender;

- (void)setSubmitButtonName:(NSString*)name;

@end

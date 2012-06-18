#import <UIKit/UIKit.h>

@interface PhotoOverlayController : UIViewController <UITextFieldDelegate>
extern NSString *const kPrimaryPhoneDidCancel;
extern NSString *const kPrimaryPhoneDone;

@property (retain, nonatomic) IBOutlet UITextField *textField;
@property (retain, nonatomic) IBOutlet UIView *buttonsView;
@property (nonatomic, retain) NSString* phone;
@property (nonatomic, retain) NSString* phonePrefix;
- (id) initWithPhone:(NSString*) phone andPhonePrefix: (NSString*) phonePrefix;
- (IBAction)cancel:(id)sender;
- (IBAction)done:(id)sender;

@end

#import <UIKit/UIKit.h>
#import "Contact.h"

@interface PhotoRecipientsOverlayController : UIViewController <UITextFieldDelegate>
extern NSString *const kRecipientsDidCancel;
extern NSString *const kRecipientsDone;

@property (retain, nonatomic) NSMutableArray* recipients;
@property (retain, nonatomic) IBOutlet UIView *recipientsView;
@property (retain, nonatomic) IBOutlet UITextField *recipientContactField;
- (IBAction)cancel:(id)sender;
- (IBAction)done:(id)sender;
- (id) initWithContactType:(ContactType)contactType andRecipients:(NSMutableArray*) recipients;

@end

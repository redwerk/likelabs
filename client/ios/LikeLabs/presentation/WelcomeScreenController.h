#import <UIKit/UIKit.h>
#import "ChildController.h"

@interface WelcomeScreenController : UIViewController<ChildController>
@property (retain, nonatomic) IBOutlet UIButton *btnExit;
@property (retain, nonatomic) IBOutlet UIButton *btnShowText;
@property (retain, nonatomic) IBOutlet UIButton *btnShowPhoto;
@property (retain, nonatomic) IBOutlet UIButton *btnFacebook;
@property (retain, nonatomic) IBOutlet UIButton *btnVkcom;
@property (retain, nonatomic) IBOutlet UIButton *btnTweeter;
@property (retain, nonatomic) IBOutlet UIButton *btnEmail;
@property (retain, nonatomic) IBOutlet UIImageView *imageCompany;
@property (retain, nonatomic) IBOutlet UIButton *btnHome;
@property (retain, nonatomic) IBOutlet UILabel *textLabel;

- (IBAction)showTextScreen:(id)sender;
- (IBAction)showPhotoScreen:(id)sender;
- (IBAction)exitApp:(id)sender;
- (IBAction)shareFacebook:(id)sender;
- (IBAction)shareVkontakte:(id)sender;
- (IBAction)shareTweeter:(id)sender;
- (IBAction)shareEmail:(id)sender;
- (IBAction)showHome:(id)sender;

@end

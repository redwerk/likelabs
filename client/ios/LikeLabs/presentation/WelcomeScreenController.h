#import <UIKit/UIKit.h>
#import "ChildController.h"

@interface WelcomeScreenController : UIViewController<ChildController>
@property (retain, nonatomic) IBOutlet UIButton *btnExit;
@property (retain, nonatomic) IBOutlet UIImageView *imageCompany;
@property (retain, nonatomic) IBOutlet UIButton *btnHome;
@property (retain, nonatomic) IBOutlet UILabel *textLabel;
@property (retain, nonatomic) IBOutlet UIImageView *poweredBy;
@property (retain, nonatomic) IBOutlet UIView *textBtnView;
@property (retain, nonatomic) IBOutlet UIView *photoBtnView;
@property (retain, nonatomic) IBOutlet UIView *logoView;
@property (retain, nonatomic) IBOutlet UIView *socialsView;

- (IBAction)showTextScreen:(id)sender;
- (IBAction)showPhotoScreen:(id)sender;
- (IBAction)exitApp:(id)sender;
- (IBAction)showHome:(id)sender;

@end

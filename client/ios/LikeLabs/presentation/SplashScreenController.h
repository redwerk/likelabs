#import <UIKit/UIKit.h>
#import "ChildController.h"

@interface SplashScreenController : UIViewController <ChildController>
@property (retain, nonatomic) IBOutlet UIButton *startBtn;
@property (retain, nonatomic) IBOutlet UIView *socialButtonsView;
@property (retain, nonatomic) IBOutlet UIImageView *shareYourSmileImg;
@property (retain, nonatomic) IBOutlet UIImageView *companyLogo;
- (IBAction)showWelcomeScreen:(id)sender;

@end

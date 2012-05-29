#import <UIKit/UIKit.h>
#import "Review.h"
#import "ContainerController.h"
#import "ReviewService.h"
#import "LoginService.h"

@interface RootController : UIViewController <ContainerController>

extern NSString *const NAVIGTION_BG_PORTRAIT;
extern NSString *const NAVIGTION_BG_LANDSCAPE;

@property (nonatomic, retain) Review *review;
@property (nonatomic, retain) ReviewService *reviewService;
@property (nonatomic, retain) LoginService *loginService;

+ (void) switchBackToController:(NSString *)controllerName rootController:(UIViewController<ContainerController> *)root;
+ (void) switchToController:(NSString *)controllerName rootController:(UIViewController <ContainerController> *) root;
+ (UIViewController *) viewControllerByName:(NSString *)controllerName rootController:(UIViewController <ContainerController> *) root;
@end

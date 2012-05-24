#import <UIKit/UIKit.h>
#import "Review.h"
#import "ContainerController.h"
#import "ReviewService.h"

@interface RootController : UIViewController <ContainerController>

extern NSString *const NAVIGTION_BG_PORTRAIT;
extern NSString *const NAVIGTION_BG_LANDSCAPE;

@property (nonatomic, retain) Review *review;
@property (nonatomic, retain) ReviewService *reviewService;

- (UIViewController *)viewControllerByName:(NSString *)controllerName;

@end

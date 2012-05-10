#import <UIKit/UIKit.h>
#import "Review.h"

@interface RootController : UIViewController

@property (nonatomic, retain) Review *review;
- (void) switchToController:(NSString *) controllerName;

@end

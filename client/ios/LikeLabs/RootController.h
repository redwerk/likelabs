#import <UIKit/UIKit.h>
#import "RootControllerProtocol.h"

@interface RootController : UIViewController <RootControllerProtocol>

- (void) switchToController:(NSString *) controllerName;

@end

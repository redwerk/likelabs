#import <Foundation/Foundation.h>
#import "RootController.h"
#import "RootControllerProtocol.h"

@protocol ChildController <NSObject>

- (id) initWithRootController:(UIViewController<RootControllerProtocol> * ) rootController;

@end

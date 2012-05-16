#import <Foundation/Foundation.h>
#import "ContainerController.h"

@protocol ChildController <NSObject>

- (id) initWithRootController:(UIViewController<ContainerController> *) rootController;

@end

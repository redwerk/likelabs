#import <Foundation/Foundation.h>
#import "RootController.h"

@protocol ChildController <NSObject>

- (id) initWithRootController:(RootController *) rootController;

@end

#import <UIKit/UIKit.h>
#import "Review.h"
#import "ContainerController.h"

@interface RootController : UIViewController <ContainerController>

@property (nonatomic, retain) Review *review;

@end

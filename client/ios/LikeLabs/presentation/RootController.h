#import <UIKit/UIKit.h>
#import "Review.h"
#import "ContainerController.h"
#import "ReviewService.h"

@interface RootController : UIViewController <ContainerController>

@property (nonatomic, retain) Review *review;
@property (nonatomic, retain) ReviewService *reviewService;

@end

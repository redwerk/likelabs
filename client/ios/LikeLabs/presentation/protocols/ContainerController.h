#import <Foundation/Foundation.h>
#import "ReviewService.h"

@protocol ContainerController <NSObject>

- (UIViewController *) getCurrentController;
- (void) setCurrentController:(UIViewController *)controller;

@optional
- (void) step;
- (Review *) getReview;
- (ReviewService *) getReviewService;
- (void) bringHeaderViewToFront;

@end

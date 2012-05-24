#import <Foundation/Foundation.h>
#import "ReviewService.h"

@protocol ContainerController <NSObject>

- (void)switchToController:(NSString *)controllerName;

@optional
- (void) step;
- (Review *) getReview;
- (ReviewService *) getReviewService;

@end

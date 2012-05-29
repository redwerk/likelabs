#import <Foundation/Foundation.h>
#import "ReviewService.h"

@protocol ContainerController <NSObject>

- (void)switchToController:(NSString *)controllerName;
- (void)switchToController:(NSString *)controllerName rootController:(UIViewController <ContainerController> *) root;
- (void)switchBackToController:(NSString *)controllerName rootController:(UIViewController <ContainerController> *) root;
- (UIViewController *) getCurrentController;
- (void) setCurrentController:(UIViewController *)controller;

@optional
- (void) step;
- (Review *) getReview;
- (ReviewService *) getReviewService;
- (void) bringHeaderViewToFront;

@end

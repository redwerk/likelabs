#import <Foundation/Foundation.h>
#import "ASIHTTPRequest.h"

@class Review;
@class User;

@interface ReviewService : NSObject <ASIHTTPRequestDelegate>

- (void) postReview:(Review *)review;

@end

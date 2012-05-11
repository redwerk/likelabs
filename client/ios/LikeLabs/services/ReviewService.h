#import <Foundation/Foundation.h>

@class Review;
@class User;

@interface ReviewService : NSObject

void postReview(Review *review, User *user, NSArray *contactsToShareTo);

@end

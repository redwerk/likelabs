//
//  ReviewService.h
//  LikeLabs
//
//  Created by Dmitry Mishchenko on 4/18/12.
//  Copyright (c) 2012 Redwerk. All rights reserved.
//

#import <Foundation/Foundation.h>
@class Review;
@class User;

@interface ReviewService : NSObject

void postReview(Review *review, User *user, NSArray *contactsToShareTo);

@end

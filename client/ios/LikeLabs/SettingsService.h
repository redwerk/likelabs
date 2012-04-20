//
//  SettingsService.h
//  LikeLabs
//
//  Created by Dmitry Mishchenko on 4/18/12.
//  Copyright (c) 2012 Redwerk. All rights reserved.
//

#import <Foundation/Foundation.h>
@class Tablet;
@class Review;


@interface SettingsService : NSObject

NSString* getCompanyName();
id getLogo();
Tablet* getTablet();
NSArray* getWelcomeReviews();
NSArray* getSocialReviews();

@end

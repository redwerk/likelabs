//
//  SettingsDao.h
//  LikeLabs
//
//  Created by Dmitry Mishchenko on 4/18/12.
//  Copyright (c) 2012 Redwerk. All rights reserved.
//

#import <Foundation/Foundation.h>

@class Tablet;

@interface SettingsDao : NSObject


NSString* getCompanyName();
void setCompanyTame(NSString* name);

id getLogo();
void setLogo(id logo);

Tablet* getTablet();
void setTablet(Tablet* tablet);

NSArray* getWelcomeReviews();
void setWelcomeReviews(NSArray* reviews);


@end

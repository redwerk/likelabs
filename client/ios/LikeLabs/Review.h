//
//  Review.h
//  LikeLabs
//
//  Created by Dmitry Mishchenko on 4/18/12.
//  Copyright (c) 2012 Redwerk. All rights reserved.
//

#import <Foundation/Foundation.h>

typedef enum {
    Text,
    Photo
} ReviewType;

@interface Review : NSObject

@property int reviewPhotoIndex;
@property(strong) NSArray *photos;
@property(strong) NSString *text;
@property ReviewType reviewType;

@end

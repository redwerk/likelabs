//
//  Contact.h
//  LikeLabs
//
//  Created by Dmitry Mishchenko on 4/18/12.
//  Copyright (c) 2012 Redwerk. All rights reserved.
//

#import <Foundation/Foundation.h>

typedef enum {
    Phone,
    Email
} ContactType;

@interface Contact : NSObject

@property ContactType contactType;
@property(strong) NSString *contactString;

@end

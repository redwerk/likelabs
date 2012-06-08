#import <Foundation/Foundation.h>

@class Tablet;

@interface SettingsDao : NSObject

extern NSString* const PHONE_FORMAT;
extern NSString* const PHONE_DIGIT_MASK;

@property (nonatomic, retain) NSDate* lastUpdate;
@property (nonatomic, retain) UIImage* logo;
@property (nonatomic, retain) NSString* companyName;
@property (nonatomic, assign) NSUInteger tabletId;
@property (nonatomic, retain) NSString* apiKey;
@property (nonatomic, retain) NSArray* promoReviews;
@property (nonatomic, readonly) NSArray* textReviews;
@property (nonatomic, readonly) NSString* phonePrefix;

- (void) setUserDefaults;
@end

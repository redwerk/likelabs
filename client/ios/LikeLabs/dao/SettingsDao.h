#import <Foundation/Foundation.h>

@class Tablet;

@interface SettingsDao : NSObject

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

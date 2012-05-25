#import <Foundation/Foundation.h>

@class Tablet;

@interface SettingsDao : NSObject
@property (nonatomic, retain) UIImage* logo;
@property (nonatomic, retain) NSString* companyName;
@property (nonatomic, assign) NSUInteger tabletId;
@property (nonatomic, retain) NSString* apiKey;
@property (nonatomic, retain) NSArray* promoReviews;
@property (nonatomic, readonly) NSArray* textReviews;

+ (void) setUserDefaults;
@end

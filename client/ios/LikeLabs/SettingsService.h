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

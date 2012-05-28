#import <Foundation/Foundation.h>

@interface URLUtil : NSObject

extern NSString* const kServerUrlPreference;
extern NSString* const CONTENT_TYPE_KEY;
extern NSString* const CONTENT_TYPE_VALUE;
extern NSString* const TABLET_API_KEY;

+ (NSURL*) getReviewUrlForTablet: (NSUInteger) tabletId;
+ (NSURL*) getSettingsUrlForTablet: (NSUInteger) tabletId;
+ (NSURL*) getLoginUrl;
+ (NSURL*) getLogoutUrlForTablet: (NSUInteger) tabletId;

@end

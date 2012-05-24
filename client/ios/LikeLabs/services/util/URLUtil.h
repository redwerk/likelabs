#import <Foundation/Foundation.h>

@interface URLUtil : NSObject

extern NSString* const kServerUrlPreference;
extern NSString* const REST_PATH;
extern NSString* const REVIEW_TEMPLATE_PATH;
extern NSString* const SETTINGS_TEMPLATE_PATH;

+ (NSURL*) getReviewUrlForTablet: (NSUInteger) tabletId;
+ (NSURL *)getSettingsUrlForTablet: (NSUInteger) tabletId;

@end

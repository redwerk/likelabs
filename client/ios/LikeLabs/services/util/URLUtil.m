#import "URLUtil.h"

@interface URLUtil()

+ (NSMutableString*) getRestUrl;

@end

@implementation URLUtil

NSString* const kServerUrlPreference = @"server_url_preference";
NSString* const REST_PATH = @"/restapi/1.0";
NSString* const REVIEW_TEMPLATE_PATH = @"/tablets/%d/reviews";
NSString* const SETTINGS_TEMPLATE_PATH = @"/tablets/%d/settings";

+ (NSURL *)getReviewUrlForTablet:(NSUInteger)tabletId {
    NSMutableString* postUrl = [self getRestUrl];
    [postUrl appendFormat:REVIEW_TEMPLATE_PATH, tabletId];
    NSLog(@"Post URL: %@", postUrl);
    return [NSURL URLWithString:postUrl];
}

+ (NSURL *)getSettingsUrlForTablet:(NSUInteger)tabletId {
    NSMutableString* settingsUrl = [self getRestUrl];
    [settingsUrl appendFormat:SETTINGS_TEMPLATE_PATH, tabletId];
    NSLog(@"Settings URL: %@", settingsUrl);
    return [NSURL URLWithString:settingsUrl];
}

+ (NSMutableString *)getRestUrl {
    NSUserDefaults* defaults = [NSUserDefaults standardUserDefaults];    
    NSMutableString *postUrl = [NSMutableString stringWithString:[defaults objectForKey:kServerUrlPreference]];
    [postUrl appendString:REST_PATH];
    return postUrl;
}


@end

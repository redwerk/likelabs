#import "URLUtil.h"

@interface URLUtil()

+ (NSMutableString*) getRestUrl;

@end

@implementation URLUtil

NSString* const kServerUrlPreference = @"server_url_preference";
NSString* const CONTENT_TYPE_KEY = @"Content-Type";
NSString* const CONTENT_TYPE_VALUE = @"application/xml";
NSString* const TABLET_API_KEY = @"tablet-api-key";

static NSString* const REST_PATH = @"/restapi/1.0";
static NSString* const REVIEW_TEMPLATE_PATH = @"/tablets/%d/reviews";
static NSString* const SETTINGS_TEMPLATE_PATH = @"/tablets/%d/settings";
static NSString* const LOGIN_PATH = @"/tablets/login";
static NSString* const LOGOUT_TEMPLATE_PATH = @"/tablets/%d/logout";

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

+ (NSURL *)getLoginUrl {
    NSMutableString* loginUrl = [self getRestUrl];
    [loginUrl appendString:LOGIN_PATH];
    NSLog(@"Login URL: %@", loginUrl);
    return [NSURL URLWithString:loginUrl];
}

+ (NSURL *)getLogoutUrlForTablet:(NSUInteger)tabletId {
    NSMutableString* logoutUrl = [self getRestUrl];
    [logoutUrl appendFormat:LOGOUT_TEMPLATE_PATH, tabletId];
    NSLog(@"Logout URL: %@", logoutUrl);
    return [NSURL URLWithString:logoutUrl];    
}

+ (NSMutableString *)getRestUrl {
    NSUserDefaults* defaults = [NSUserDefaults standardUserDefaults];    
    NSMutableString *postUrl = [NSMutableString stringWithString:[defaults objectForKey:kServerUrlPreference]];
    [postUrl appendString:REST_PATH];
    return postUrl;
}


@end

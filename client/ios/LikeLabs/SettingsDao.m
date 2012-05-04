#import "SettingsDao.h"
#import "SettingsService.h"

static NSString *const SOCIAL_COMMENTS = @"TextReviews";

@implementation SettingsDao

-(void)setTextReviews:(NSArray *)comments {
    /*NSUserDefaults *settings = [NSUserDefaults standardUserDefaults];
    [settings setObject:comments forKey:SOCIAL_COMMENTS];
    [settings synchronize];*/
}

-(NSArray *)getTextReviews { 
    /*NSUserDefaults *settings = [NSUserDefaults standardUserDefaults];
    NSArray* comments = [settings arrayForKey:SOCIAL_COMMENTS];
    if (comments == nil) {*/
        SettingsService* service = [[SettingsService alloc] init];
        NSArray* comments = [service getTextReviews];
        [self setTextReviews:comments];
        [service release];
    //}
    return comments;
}

@end

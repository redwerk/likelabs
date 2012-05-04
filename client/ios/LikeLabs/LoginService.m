#import "LoginService.h"

@implementation LoginService

- (BOOL)checkLogin:(NSString*)code andPassword:(NSString*)password {
    return [code isEqualToString: @""] && [password isEqualToString: @""];
}

@end

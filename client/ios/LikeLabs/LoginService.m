#import "LoginService.h"

@implementation LoginService

- (BOOL)checkLogin:(NSString*)code andPassword:(NSString*)password {
    return [code isEqualToString: @"admin"] && [password isEqualToString: @"admin"];
}

@end

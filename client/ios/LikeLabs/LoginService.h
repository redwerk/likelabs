#import <Foundation/Foundation.h>

@interface LoginService : NSObject

- (BOOL)checkLogin:(NSString*)code andPassword:(NSString*)password;

@end

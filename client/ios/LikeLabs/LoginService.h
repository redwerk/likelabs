#import <Foundation/Foundation.h>

@interface LoginService : NSObject

- (BOOL)ccheckLogin:(NSString*)code andPassword:(NSString*)password;

@end
